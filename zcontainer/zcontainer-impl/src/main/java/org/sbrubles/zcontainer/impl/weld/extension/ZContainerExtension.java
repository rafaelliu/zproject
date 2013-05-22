package org.sbrubles.zcontainer.impl.weld.extension;


import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.impl.weld.beans.EventManager;
import org.sbrubles.zcontainer.impl.weld.beans.ServiceRegistry;
import org.sbrubles.zcontainer.impl.weld.beans.ZContainerContext;

public class ZContainerExtension implements Extension {

    void registerCDIOSGiBeans(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        event.addAnnotatedType(manager.createAnnotatedType(ServiceRegistry.class));
        event.addAnnotatedType(manager.createAnnotatedType(EventManager.class));
    }
    
	// TODO: think what to do if Container is not initialized in a CDI environment
	// and what it embedded-deploy is not active?
    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
    	// TODO: I can seem to call ZContainerExtension (only on embedded) for creating
    	// the Container...
    	
    	
    	// we can save it in a static field that way.
    	// embedded-deployer pass-through all class loading to bootloader
    	// so we will get this reference in all modules that adds emdedded's
    	// module as dependency
		BeanManagerLocator.bm = bm;
    	BeanManagerLocator.container = Container.create();
    	
        abd.addBean(new BeanFactory<Container>(bm, Container.class, "container", BeanManagerLocator.container));

    	bm.fireEvent(new ZContainerContext(BeanManagerLocator.container)); 
    	
    }

}
