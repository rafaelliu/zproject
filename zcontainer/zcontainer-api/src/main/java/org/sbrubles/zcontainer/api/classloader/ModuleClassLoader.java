package org.sbrubles.zcontainer.api.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import org.sbrubles.zcontainer.ContainerPackage;

/**
 * We should try to avoid using ModuleClassLoader and use only the
 * ClassLoader abstraction
 * @author blah
 *
 */
public abstract class ModuleClassLoader extends URLClassLoader {

	public static final List<String> DEFAULT_BOOTLOADER_PACKAGES = Arrays.asList(
			"org.sbrubles.zcontainer.impl",
			"org.sbrubles.zcontainer.api",
			"java", "javax", "org.xml", "org.jboss.weld", "sun");

	public static final String BOOTLOADER_PKGS_PARAMETER = "bootloader.packages";
	
	// ========================

	public ModuleClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ModuleClassLoader(URL[] urls) {
		super(urls, null);
	}

	// ========================

	public abstract ClassLoader getMainClassloder();

	public abstract List<ModuleClassLoader> getModuleClassloders();

	public abstract void setModuleClassloders(List<ModuleClassLoader> moduleClassloders);

	@Override
	public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}
	
	public String printTree(int ident) {
		StringBuilder spaces = new StringBuilder();
		for (int i = 0; i < ident; i++) spaces.append("   ");
		StringBuilder sb = new StringBuilder();
		sb.append(spaces.toString() + "ModuleClassloader [moduleClassloders=");
		if (getModuleClassloders().isEmpty()) {
		sb.append("[],\n");
		} else {
			sb.append("\n");
			for (ModuleClassLoader mcl : getModuleClassloders()) {
				sb.append(mcl.printTree(++ident));
			}
		}
		sb.append(spaces.toString() + "getURLs()=" + Arrays.toString(getURLs()) + ",\n");
		sb.append(spaces.toString() + "getParent()=" + getParent() + "\n");
		return sb.toString();
	}

}