package test;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.sbrubles.zcontainer.impl.weld.ZService;

@Named
public class ClientClass {
	
	@Inject
	private ZService<ServerClass> server;
	
	private static String value;
	
	public ServerParameter callServer(String s) {
		ServerClass serverClass = server.get();
		return serverClass.instantiate(s);
	}
	
	public void observesEvent(@Observes MyEvent event) {
		value = event.getValue();
	}

	public String getValue() {
		return value;
	}

}
