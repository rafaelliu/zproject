package org.sbrubles.zcontainer.arquillian.container;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.Translator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.imageio.plugins.bmp.BMPImageWriteParam;

import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.testenricher.cdi.CDIInjectionEnricher;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.module.Module;

import com.googlecode.transloader.DefaultTransloader;
import com.googlecode.transloader.ObjectWrapper;
import com.googlecode.transloader.Transloader;

import test.ClassA;

public class ZContainerEnricher extends CDIInjectionEnricher {
	
   private static final Logger log = Logger.getLogger(ZContainerEnricher.class.getName());

	@Inject
	private Instance<ProtocolMetaData> protocolMetadata;

	@Override
	public BeanManager getBeanManager() {
		Module first = getFirst(protocolMetadata.get(), Module.class);
		return ( first != null ? first.getBeanManager() : null );
	}

	public Module getModule() {
		return getFirst(protocolMetadata.get(), Module.class);
	}

	public ModuleClassLoader getClassloader() {
		Module first = getFirst(protocolMetadata.get(), Module.class);
		return ( first != null ? first.getClassloader() : null );
	}

	public Container getContainer() {
		Module first = getFirst(protocolMetadata.get(), Module.class);
		return ( first != null ? first.getConstainer() : null );
	}

	private <T> T getFirst(ProtocolMetaData metaData, Class<T> clazz) {
		List<Object> contexts = metaData.getContexts();

		for (Object o : contexts) {
			if (clazz.isAssignableFrom(o.getClass())) {
				return (T) o;
			}
		}
		return null;
	}

	protected void injectNonContextualInstance(BeanManager manager, Object instance)
	{
		Transloader transloader = new DefaultTransloader(com.googlecode.transloader.configure.CloningStrategy.MAXIMAL);

		Container container = getContainer();

		for (Field f : instance.getClass().getDeclaredFields()) {

			// skip not injectable
			if (!f.isAnnotationPresent(javax.inject.Inject.class)) continue;
			
			log.fine("Resolving injection for " + f);

			// TODO: optimize: store localy
			BeanManager bm = null;
			ModuleClassLoader classloader = null;

			if (f.isAnnotationPresent(OperateOnDeployment.class)) {
				String value = f.getAnnotation(OperateOnDeployment.class).value();
				
				for (Module m : container.getModules()) {
					if (m.getDescriptor().getName().equals(value)) {
						bm = m.getBeanManager();
						classloader = m.getClassloader();

						log.fine("Using context of " + m);
						break;
					}
				}
				
				if (bm == null || classloader == null) {
					throw new RuntimeException("There's no such module deployed: " + value);
				}
			} else {
				bm = getBeanManager();
				classloader = getClassloader();

				log.fine("Using context of " + getModule());
			}
			CreationalContext<Object> cc = bm.createCreationalContext(null);

			try {
				// load from module's classloader
				String name = f.getType().getName();
				Class<?> clazz = classloader.loadClass(name);

				// get reference to object
				// TODO: use qualifiers
				Set<Bean<?>> beans = bm.getBeans(clazz);
				log.fine("Found beans:");
				for (Bean b : beans) {
					log.fine(" - " + b);
				}
				if (beans.isEmpty()) {
					log.warning("No CDI bean for " + clazz);
					break;
				}
				
				Bean<?> next = beans.iterator().next();
				Object reference = bm.getReference(next, clazz, cc);

				// clone it with transloader
				ObjectWrapper wrap = transloader.wrap(reference);
				Object proxy = wrap.makeCastableTo(f.getType());
				
				// set it
				f.setAccessible(true);
				f.set(instance, proxy);
				
			} catch (Exception e) {
				// Weld couldn't resolve it. skip
				log.log(Level.FINE, "Couldn't inject " + f + ". This is not necessarily an error. Check previous logs.", e);
			}
		}


	}

} 