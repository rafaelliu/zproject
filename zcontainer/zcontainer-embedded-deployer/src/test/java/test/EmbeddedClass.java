package test;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sbrubles.zcontainer.impl.weld.beans.ZEvent;

@Singleton
public class EmbeddedClass {
	
	private String value;

	@Inject @ZEvent
	private Event<MyEvent> event;
	
	public String getValue() {
		return value;
	}

	public void fireEvent(String obj) {
		event.fire(new MyEvent(obj));
	}

	public void setValue(String value) {
		this.value = value;
	}

}
