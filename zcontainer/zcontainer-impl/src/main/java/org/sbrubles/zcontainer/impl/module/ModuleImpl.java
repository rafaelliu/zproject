package org.sbrubles.zcontainer.impl.module;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.ContainerListener;
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
	private ContainerListener containerListener;

	public ModuleImpl(File file, ModuleDescriptor descriptor) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.file = file;
		this.descriptor = descriptor;
		this.status = ModuleStatus.INSTALLED;
		
		URL[] moduleUrl = new URL[]{ file.toURI().toURL() };
		URL[] urls = concat(descriptor .getUrls(), moduleUrl);
		classloader = new ModuleClassLoaderImpl(urls);
		
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
			
			containerListener = (ContainerListener) listenerClass.newInstance();
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

	public ContainerListener getContainerListener() {
		return containerListener;
	}

}
