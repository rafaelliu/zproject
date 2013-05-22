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

public class ZContainerModuleExtension implements Extension {

    void registerCDIOSGiBeans(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        event.addAnnotatedType(manager.createAnnotatedType(ServiceRegistry.class));
        event.addAnnotatedType(manager.createAnnotatedType(EventManager.class));
    }
    
    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
    	// TODO: I can seem to call ZContainerExtension (only on embedded) for creating
    	// the Container...
    	
        abd.addBean(new BeanFactory<Container>(bm, Container.class, "container", BeanManagerLocator.container));

    	bm.fireEvent(new ZContainerContext(BeanManagerLocator.container)); 
    	
    }

}
