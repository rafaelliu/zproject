package org.sbrubles.zcontainer.test;

import java.net.URL;
import java.util.List;

import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;

public class ModuleClassLoaderAdapter extends ModuleClassLoader {

	public ModuleClassLoaderAdapter(URL[] urls) {
		super(urls);
	}

	public ModuleClassLoaderAdapter(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public void addClasspath(URL url) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addBootloaderPackage(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ModuleClassLoader> getModuleClassloders() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setModuleClassloders(List<ModuleClassLoader> moduleClassloders) {
		throw new UnsupportedOperationException();
	}

}
