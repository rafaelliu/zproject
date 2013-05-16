package org.sbrubles.zcontainer.api.module;


import java.io.File;

import javax.enterprise.inject.spi.BeanManager;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.Activator;
import org.sbrubles.zcontainer.api.listener.ModuleListener;

public interface Module {

	public abstract Container getConstainer();

	public abstract BeanManager getBeanManager();

	public abstract ModuleListener getModuleListener();

	public abstract Activator getContainerListener();

	public abstract File getFile();

	public abstract ModuleConfiguration getDescriptor();

	public abstract ModuleClassLoader getClassloader();

	public abstract void setClassloader(ModuleClassLoader classloader);

	public abstract ModuleStatus getStatus();

	public abstract void setStatus(ModuleStatus status);

}