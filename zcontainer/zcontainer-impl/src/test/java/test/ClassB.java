package test;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.events.ContainerInitialized;

@Named
@Singleton
public class ClassB {

	@PostConstruct
	public void init() {
		System.out.println("uhu! ");
	}
	
	public ClassLoader getActualClassloader() {
		return this.getClass().getClassLoader();
	}

	public String receive(ClassParameter string) {
		return string.value;
	}

	private int i = 0;
	public int inc() {
		return i ++;
	}

}
