package org.sbrubles.zcontainer.impl.classloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.sbrubles.zcontainer.api.classloader.FilteredClassloader;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;

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
	 * @param urls URL to be passed to URLClassLoader. Order is important.
	 * @param moduleClassloaders Classloader of dependency modules
	 * @param parentClassloader Parent classloader can be either WhitelistClassloader or BlacklistClassloader
	 */
	public ModuleClassLoaderImpl(URL[] urls, List<ModuleClassLoader> moduleClassloaders, FilteredClassloader parentClassloader) {
		super(urls, parentClassloader);
		this.moduleClassloders = moduleClassloaders;
	}

	/**
	 * 
	 * @param urls URL to be passed to URLClassLoader. Order is important.
	 * @param moduleClassloaders Classloader of dependency modules
	 * @param parentClassloader Parent classloader can be either WhitelistClassloader or BlacklistClassloader
	 */
	public ModuleClassLoaderImpl(URL[] urls, FilteredClassloader parentClassloader) {
		this(urls, new ArrayList<ModuleClassLoader>(), parentClassloader);
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
