package org.sbrubles.zcontainer.api.classloader;

import java.util.ArrayList;
import java.util.List;

public abstract class FilteredClassloader extends ClassLoader {

	private List<String> filteredPackages = new ArrayList<String>();
	private List<String> filteredClasses = new ArrayList<String>();

	public FilteredClassloader(List<String> bootloaderPackages, List<String> bootloaderClasses) {
		// this is to make sure the list passed is not immutable
		if (bootloaderPackages != null) {
			this.filteredPackages.addAll(bootloaderPackages);
		}
		if (bootloaderClasses != null) {
			this.filteredClasses.addAll(bootloaderClasses);
		}
	}

	public List<String> getFilteredPackages() {
		return filteredPackages;
	}
	
	public List<String> getFilteredClasses() {
		return filteredClasses;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [filteredPackages=" + filteredPackages
				+ ", filteredClasses=" + filteredClasses + "]";
	}
	

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (shouldLoad(name)) {
			return super.loadClass(name, resolve);
		}

		throw new ClassNotFoundException("Class not found: " + name);
	}

	protected abstract boolean shouldLoad(String clazz);

//	// we won't pass through any resources
//	@Override
//	protected URL findResource(String name) {
//		System.out.println(">>> " + name);
//		return null;
//	}
//
//	// we won't pass through any resources
//	@Override
//	protected Enumeration<URL> findResources(String name) throws IOException {
//		System.out.println(">>> " + name);
//		return Collections.emptyEnumeration();
//	}
//
//	@Override
//	public URL getResource(String name) {
//		return findResource(name);
//	}
//
//	@Override
//	public Enumeration<URL> getResources(String name) throws IOException {
//		return findResources(name);
//	}
	
}
