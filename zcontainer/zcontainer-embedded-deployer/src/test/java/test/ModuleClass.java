package test;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.sbrubles.zcontainer.impl.weld.beans.ZEvent;
import org.sbrubles.zcontainer.impl.weld.beans.ZService;

@Named
public class ModuleClass {
	
	@Inject
	private ZService<EmbeddedClass> server;

	public void observesEvent(@Observes MyEvent event) {
		EmbeddedClass serverClass = server.get();
		serverClass.setValue(event.getObj());
	}

}
