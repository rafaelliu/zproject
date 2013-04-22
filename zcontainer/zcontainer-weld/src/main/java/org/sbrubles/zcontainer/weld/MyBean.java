package org.sbrubles.zcontainer.weld;

import java.util.Map;

import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.jboss.weld.environment.se.events.ContainerInitialized;

@Named
public class MyBean {
	
	public void init(@Observes ContainerInitialized param) {
		System.out.println("aha!");
	}

}
