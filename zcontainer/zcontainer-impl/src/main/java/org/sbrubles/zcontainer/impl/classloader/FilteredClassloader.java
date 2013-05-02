package org.sbrubles.zcontainer.impl.classloader;

import java.util.ArrayList;
import java.util.List;

public class FilteredClassloader extends ClassLoader {

	private List<String> bootloaderPackages = new ArrayList<String>();
	private List<String> bootloaderClasses = new ArrayList<String>();

	public FilteredClassloader(List<String> bootloaderPackages, List<String> bootloaderClasses) {
		// this is to make sure the list passed is not immutable
		if (bootloaderPackages != null) {
			this.bootloaderPackages.addAll(bootloaderPackages);
		}
		if (bootloaderClasses != null) {
			this.bootloaderClasses.addAll(bootloaderClasses);
		}
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		for (String p : bootloaderPackages) {
			if (name.startsWith(p.concat("."))) { // "." makes sure there's a package match
				return super.loadClass(name, resolve);
			}
		}
		for (String c : bootloaderClasses) {
			if (name.equals(c)) {
				return super.loadClass(name, resolve);
			}
		}
		throw new ClassNotFoundException("Class not found: " + name);
	}
	
	public List<String> getBootloaderPackages() {
		return bootloaderPackages;
	}
	
	public List<String> getBootloaderClasses() {
		return bootloaderClasses;
	}
	
	@Override
	public String toString() {
		return "FilteredClassloader [bootloaderPackages=" + bootloaderPackages
				+ "]";
	}

}
