package org.sbrubles.zcontainer.impl.classloader;

import java.util.ArrayList;
import java.util.List;

public class FilteredClassloader extends ClassLoader {

	private List<String> bootloaderPackages;

	public FilteredClassloader(List<String> bootloaderPackages) {
		// this is to make sure the list passed is not immutable
		this.bootloaderPackages = new ArrayList<String>(bootloaderPackages);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		for (String p : bootloaderPackages) {
			if (name.startsWith(p.concat("."))) { // "." makes sure there's a package match
				return super.loadClass(name, resolve);
			}
		}
		throw new ClassNotFoundException("Class not found: " + name);
	}
	
	public List<String> getBootloaderPackages() {
		return bootloaderPackages;
	}
	
	@Override
	public String toString() {
		return "FilteredClassloader [bootloaderPackages=" + bootloaderPackages
				+ "]";
	}

}
