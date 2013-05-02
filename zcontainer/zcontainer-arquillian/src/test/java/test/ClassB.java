package test;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.environment.se.events.ContainerInitialized;

@Named
public class ClassB {
	@Inject
	BeanManager bm;

	public void init(@Observes ContainerInitialized param) {
		System.out.println("uhu! " + bm);
	}

	@Override
	public String toString() {
		return "ClassB ARQ [bm=" + bm + "]";
	}

}
