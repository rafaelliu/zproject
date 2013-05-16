package org.sbrubles.zcontainer.impl.weld;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

// TODO: unregistry services
public class ServiceRegistry extends CacheLoader<Type, ZService<?>> {

	private static final Logger log = Logger.getLogger(ServiceRegistry.class.getName());

	private LoadingCache<Type, ZService<?>> services = CacheBuilder.newBuilder().build(this);
	
	@Produces
	@SuppressWarnings("rawtypes")
	public ZService produce(InjectionPoint injectionPoint) throws ExecutionException {
		Type genericType = injectionPoint.getType();
		if (genericType instanceof ParameterizedType) {
			Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];

			return services.get(type);
		} else {
			throw new RuntimeException();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ZService<?> load(Type key) {
		Map<Module, BeanManager> weldContainers =  ManagerRegistry.getManagers();

		Object reference = null;
		for (BeanManager bm : weldContainers.values()) {
			try {
				CreationalContext<Object> cc = bm.createCreationalContext(null);

				Set<Bean<?>> beans = bm.getBeans(key);
				Bean<?> next = beans.iterator().next();
				reference = bm.getReference(next, key, cc);
				
				return new ZService(reference);
			} catch (Exception e) {
			}
		}

		if (reference == null) {
			log.warning("No bean found for type " + key);
		}

		return null;
	}

}
