package test;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.jboss.weld.manager.BeanManagerImpl;


@Named
@Singleton

public class ClassA {
	
//	private ClassLoader cl;

	@PostConstruct
	public void init() {
		System.out.println("aha! ");
//		cl = getActualClassloader();
	}

	public ClassLoader getActualClassloader() {
		return this.getClass().getClassLoader();
	}

	private int i = 0;
	public int inc() {
//		System.out.println(cl);
		return i ++;
	}

}
