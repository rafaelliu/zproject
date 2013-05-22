package test;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.sbrubles.zcontainer.impl.weld.beans.ZEvent;

@Named
public class ServerClass {
	
	@Inject @ZEvent
	private Event<MyEvent> event;
	
	public ServerParameter instantiate(String s) {
		return new ServerParameter(s);
	}
	
	public void fireEvent(String value) {
		event.fire(new MyEvent(value));
	}

}
