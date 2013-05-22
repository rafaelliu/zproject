package org.sbrubles.zcontainer.embedded;

import java.io.InputStream;
import java.net.URL;

import javax.enterprise.inject.spi.BeanManager;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.deployers.Deployer;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.impl.ContainerImpl;
import org.sbrubles.zcontainer.impl.config.Configuration;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;
import org.sbrubles.zcontainer.impl.weld.extension.BeanManagerLocator;

public class EmbeddedDeployer extends Deployer {

	@Override
	public void init(Container container) throws Exception {

		// check for config file in classpath
		URL url = getClassloader().getResource(Configuration.CONFIG_PATH);
		if (url == null) return;
		
		ContainerImpl c = (ContainerImpl) container;
		
		InputStream stream = url.openStream();
		ModuleConfiguration configuration = Configuration.readModuleConfiguration(stream);
		
		ModuleImpl module = new ModuleImpl(c, null, configuration);
		
		// TODO: locate bm a smarter (safer) way
		BeanManager bm = BeanManagerLocator.bm;
		module.setBeanManager(bm);
		
		c.addModule(module);
		
	}

}
