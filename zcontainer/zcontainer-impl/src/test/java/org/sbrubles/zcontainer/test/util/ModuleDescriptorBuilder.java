package org.sbrubles.zcontainer.test.util;

import org.sbrubles.zcontainer.api.module.ModuleDescriptor;

public class ModuleDescriptorBuilder {
	private ModuleDescriptor descriptor;
	
	public static ModuleDescriptorBuilder newInstance(String name) {
		return new ModuleDescriptorBuilder(name);
	}
	private ModuleDescriptorBuilder(String name) {
		descriptor = new ModuleDescriptor(name);
	}
	public ModuleDescriptorBuilder addDependency(String name) {
		descriptor.getDependencies().add(name);
		return this;
	}
	public ModuleDescriptorBuilder addModuleListener(String name) {
		descriptor.setModuleListenerClass(name);
		return this;
	}
	public ModuleDescriptorBuilder addContainerListener(String name) {
		descriptor.setContainerListenerClass(name);
		return this;
	}
	public ModuleDescriptor build() {
		return descriptor;
	}
}
