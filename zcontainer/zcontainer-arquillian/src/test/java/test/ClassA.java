package test;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.jboss.weld.manager.BeanManagerImpl;


@Named
public class ClassA {
	
	public void init(@Observes ContainerInitialized param) {
		System.out.println("aha! ");
	}
	
	public void test(ClassB clazz) {
		System.out.println(clazz);
	}

	@Override
	public String toString() {
		return "ClassA [toString()=" + super.toString() + "]";
	}

}
