package org.sbrubles.zcontainer.api.module;


import java.io.File;

import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.ContainerListener;
import org.sbrubles.zcontainer.api.listener.ModuleListener;

public interface Module {

	public abstract ModuleListener getModuleListener();

	public abstract ContainerListener getContainerListener();

	public abstract File getFile();

	public abstract ModuleDescriptor getDescriptor();

	public abstract ModuleClassLoader getClassloader();

	public abstract void setClassloader(ModuleClassLoader classloader);

	public abstract ModuleStatus getStatus();

	public abstract void setStatus(ModuleStatus status);

}