package org.sbrubles.zcontainer.impl.util;

import java.io.File;

import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.impl.config.Configuration;

public class ModuleConfigurationBuilder {
	private ModuleConfiguration descriptor;
	
	public static ModuleConfigurationBuilder newInstance(String name) {
		return new ModuleConfigurationBuilder(name);
	}
	private ModuleConfigurationBuilder(String name) {
		descriptor = new ModuleConfiguration();
		descriptor.setName(name);
	}
	public ModuleConfigurationBuilder addDependency(String name) {
		descriptor.getDependencies().add(name);
		return this;
	}
	public ModuleConfigurationBuilder addBootloaderPackage(String name) {
		descriptor.getBootloaderPackages().add(name);
		return this;
	}
	public ModuleConfigurationBuilder addModuleListener(String name) {
		descriptor.setModuleListenerClass(name);
		return this;
	}
	public ModuleConfigurationBuilder addContainerListener(String name) {
		descriptor.setContainerListenerClass(name);
		return this;
	}
	public ModuleConfiguration build() {
		return descriptor;
	}
	public File asFile() {
		return Configuration.writeModuleConfiguration(descriptor);
	}
}
