package org.sbrubles.zcontainer.arquillian.archive;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.container.ServiceProviderContainer;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;


public interface ZArchive extends Archive<ZArchive>, LibraryContainer<ZArchive>, ResourceContainer<ZArchive>, ServiceProviderContainer<ZArchive> {
	
	public ZArchive addAsConfiguration(ModuleConfiguration config);
	
}
