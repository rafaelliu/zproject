package org.sbrubles.zcontainer.impl.weld;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.Activator;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;


public class ManagerRegistry implements ModuleListener {

	private static Map<Module, WeldContainer> weldContainers = new HashMap<Module, WeldContainer>();
	
	public static Map<Module, WeldContainer> getManagers() {
		return Collections.unmodifiableMap(weldContainers);
	}

	public void onInstall(Module module) {
	}

	public void onActivate(Module module) {
	
		Thread thread = Thread.currentThread();
		
		ClassLoader oldCCL = thread.getContextClassLoader();
		ModuleClassLoader moduleClassloader = module.getClassloader();

		thread.setContextClassLoader(moduleClassloader);
		
		// TODO: throw an exception when a bean is ignored!
		// Weld logs an info:  WELD-000119 Not generating any bean definitions from test.ClassA because of underlying class loading error
		WeldContainer initialize = new Weld().initialize();

		thread.setContextClassLoader(oldCCL);

		((ModuleImpl) module).setWeld(initialize);

		// TODO: is put() thread-safe? 
		synchronized (weldContainers) {
			weldContainers.put(module, initialize);
		}
	}

	public void onUninstall(Module module) {
		weldContainers.remove(module);
	}

}
