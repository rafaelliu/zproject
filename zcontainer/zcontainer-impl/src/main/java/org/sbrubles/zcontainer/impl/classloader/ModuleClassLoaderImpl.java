package org.sbrubles.zcontainer.impl.classloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.impl.ContainerImpl;

/**
 * Modules can load classes from it's classpath or from other modules.
 * Using classpath would cause the same classloader to load classes,
 * loading other modules uses the other module's classloader 
 * 
 * @author blah
 *
 */
public class ModuleClassLoaderImpl extends ModuleClassLoader {
	
	private List<ModuleClassLoader> moduleClassloders = new ArrayList<ModuleClassLoader>();

	/**
	 * 
	 * @param urls Classpath for this module
	 * @param moduleClassloders Dependencies modules
	 */
	public ModuleClassLoaderImpl(URL[] urls, List<ModuleClassLoader> moduleClassloders, List<String> bootloaderPackages) {
		super(urls, new FilteredClassloader(bootloaderPackages, null));
		this.moduleClassloders = moduleClassloders;
	}
	
	public ModuleClassLoaderImpl(URL[] urls, List<String> bootloaderPackages) {
		this(urls, new ArrayList<ModuleClassLoader>(), bootloaderPackages);
	}
	
	public ModuleClassLoaderImpl(URL[] urls) {
		this(urls, new ArrayList<ModuleClassLoader>(), DEFAULT_BOOTLOADER_PACKAGES);
	}
	
	public void addClasspath(URL url) {
		super.addURL(url);
	}
	
	public void  addBootloaderPackage(String name) {
		((FilteredClassloader) getParent()).getBootloaderPackages().add(name);
	}

	public void  addBootloaderClass(String name) {
		((FilteredClassloader) getParent()).getBootloaderClasses().add(name);
	}

	public List<ModuleClassLoader> getModuleClassloders() {
		return moduleClassloders;
	}

	public void setModuleClassloders(List<ModuleClassLoader> moduleClassloders) {
		this.moduleClassloders = moduleClassloders;
	}
	
	@Override
	public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			return super.loadClass(name, resolve);
		} catch (ClassNotFoundException e) {}
		
		for (ModuleClassLoader mcl : moduleClassloders) {
			try {
				return mcl.loadClass(name, resolve);
			} catch (ClassNotFoundException e) {}
		}
		
		throw new ClassNotFoundException("Could not find " + name);
	}

	@Override
	public String toString() {
		return printTree(0);
	}

	@Override
	public ClassLoader getMainClassloder() {
		return getParent();
	}
	
}
