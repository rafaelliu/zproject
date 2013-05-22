package org.sbrubles.zcontainer.impl.classloader;

import java.util.List;

import org.sbrubles.zcontainer.api.classloader.FilteredClassloader;

public class BlacklistClassloader extends FilteredClassloader {

	public BlacklistClassloader(List<String> bootloaderPackages, List<String> bootloaderClasses) {
		super(bootloaderPackages, bootloaderClasses);
	}

	@Override
	protected boolean shouldLoad(String clazz) {
		for (String p : getFilteredPackages()) {
			if (clazz.startsWith(p.concat("."))) { // "." makes sure there's a package match
				return false;
			}
		}
		for (String c : getFilteredClasses()) {
			if (clazz.equals(c)) {
				return false;
			}
		}
		
		return true;
	}

}
