package org.vaadin.virkki.cdiutils.addressbook;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.sbrubles.zcontainer.api.module.BeanManagerFactory;
import org.sbrubles.zcontainer.api.module.Module;

public class EmbeddedBeanManagerFactory implements BeanManagerFactory {

	private static BeanManager beanManager;

	@Inject
	private BeanManager bm;
	
	// TODO: qualifier @Initialized não funciona
	public void lister(@Observes ServletContext context) {
		beanManager = bm;
	}

	@Override
	public BeanManager createBeanManager(Module module) {
		return beanManager;
	}

}
