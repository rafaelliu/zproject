package org.vaadin.virkki.cdiutils.addressbook;

import java.net.MalformedURLException;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.impl.ContainerImpl;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

@Singleton
public class PluginContainer {
	
	private Container container = Container.create();
	
	@PostConstruct
	public void init() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ContainerImpl c = (ContainerImpl) container;
		
		ModuleConfiguration descriptor = ModuleConfigurationBuilder
				.newInstance("zcenter")
				.addBootloaderPackage("org.vaadin")
				.build();
		
		Module module = new ModuleImpl(container, null, descriptor);
		c.addModule(module, descriptor);
	}

}
