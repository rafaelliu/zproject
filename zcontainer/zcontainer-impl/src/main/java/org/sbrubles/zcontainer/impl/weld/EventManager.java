package org.sbrubles.zcontainer.impl.weld;

import java.util.Collection;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.module.Module;

// TODO: we are raising the event with @ZEvent qualifier but observing without
// the @ZEvent qualifier. This is not consistent. We are doing this to avoid
// infinite loop. Give it a little more tought!
public class EventManager {
	
	@Inject
	private BeanManager thisBm;
	
	public void observeZEvent(@Observes @ZEvent Object event) {
		
		Map<Module, WeldContainer> weldContainers =  ManagerRegistry.getManagers();
		
		Collection<WeldContainer> values = weldContainers.values();
		
		for (WeldContainer c : values) {
			BeanManager bm = c.getBeanManager();
			
			// don't retrow this event in this module
			if (thisBm == bm) continue;
			
			bm.fireEvent(event);
		}
		
	}

}
