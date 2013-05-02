package test;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.weld.environment.se.events.ContainerInitialized;

public class ClassParameter {

	public ClassParameter(String value) {
		super();
		this.value = value;
	}

	public String value;

}
