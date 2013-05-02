package org.sbrubles.zcontainer.impl.module;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.Activator;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.impl.classloader.ModuleClassLoaderImpl;

public class ModuleImpl implements Module {

	private File file;
	private ModuleDescriptor descriptor;
	private ModuleClassLoader classloader;
	private ModuleStatus status;
	private ModuleListener moduleListener;
	private Activator containerListener;
	private WeldContainer weld;
	private Container container;

	public ModuleImpl(Container container, File file, ModuleDescriptor descriptor) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.container = container;
		this.file = file;
		this.descriptor = descriptor;
		this.status = ModuleStatus.INSTALLED;
		
		List<URL> urls = new ArrayList<URL>();
		urls.add( file.toURL() );
		
		File libDir = new File(file, "lib");
		if (libDir.exists()) {
			
			for (File f : libDir.listFiles()) {
				urls.add( f.toURL() );
			}
			
		}
		
		List<String> bootloaderPackages = ModuleClassLoaderImpl.DEFAULT_BOOTLOADER_PACKAGES;
		
		String property = System.getProperty(ModuleClassLoader.BOOTLOADER_PKGS_PARAMETER);
//		if (property != null) {
//			String[] packages = property.split(",");
//			
//			bootloaderPackages = new ArrayList<String>(bootloaderPackages);
//			bootloaderPackages.addAll(Arrays.asList(packages));
//		}
		
		classloader = new ModuleClassLoaderImpl( urls.toArray(new URL[0]), bootloaderPackages  );
		
		String moduleListenerClass = descriptor.getModuleListenerClass();
		if (moduleListenerClass != null) {
			Class<?> listenerClass = classloader.loadClass(moduleListenerClass);
			
			try {
				Constructor<?> constructor = listenerClass.getConstructor(Module.class);
				moduleListener = (ModuleListener) constructor.newInstance(this);
			} catch (Exception e) {
				moduleListener = (ModuleListener) listenerClass.newInstance();
			}
		}
		
		String containerListenerClass = descriptor.getContainerListenerClass();
		if (containerListenerClass != null) {
			Class<?> listenerClass = classloader.loadClass(containerListenerClass);
			
			containerListener = (Activator) listenerClass.newInstance();
		}
	}

	private <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public ModuleClassLoader getClassloader() {
		return classloader;
	}

	public void setClassloader(ModuleClassLoader classloader) {
		this.classloader = classloader;
	}

	public ModuleStatus getStatus() {
		return status;
	}

	public void setStatus(ModuleStatus status) {
		this.status = status;
	}
	public ModuleListener getModuleListener() {
		return moduleListener;
	}

	public File getFile() {
		return file;
	}

	public ModuleDescriptor getDescriptor() {
		return descriptor;
	}


	@Override
	public String toString() {
		return "Module [file=" + file + ", descriptor=" + descriptor
				+ ", status=" + status + "]";
	}

	public Activator getContainerListener() {
		return containerListener;
	}

	public BeanManager getBeanManager() {
		return (weld != null ? weld.getBeanManager() : null);
	}
	
	public void setWeld(WeldContainer weld) {
		this.weld = weld;
	}

	public Container getConstainer() {
		return container;
	}

}
