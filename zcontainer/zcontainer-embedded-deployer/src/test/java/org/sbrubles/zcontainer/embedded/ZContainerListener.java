package org.sbrubles.zcontainer.embedded;

import javax.enterprise.event.Observes;

import org.sbrubles.zcontainer.impl.weld.beans.ZContainerContext;

public class ZContainerListener {

	   public void printHello(@Observes ZContainerContext event) {

//	       System.out.println("Hello " + event.getContainer().getModules());

	   }

}
