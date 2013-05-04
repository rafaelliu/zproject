package org.sbrubles.zcontainer.impl.weld;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class ZContainerExtension implements Extension {

    void registerCDIOSGiBeans(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        event.addAnnotatedType(manager.createAnnotatedType(ServiceRegistry.class));
        event.addAnnotatedType(manager.createAnnotatedType(EventManager.class));
    }

}
