package org.sbrubles.zcontainer.impl.weld;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;


public class ManagerRegistry implements ModuleListener {

	private static Map<Module, BeanManager> weldContainers = new HashMap<Module, BeanManager>();
	
	public static Map<Module, BeanManager> getManagers() {
		return Collections.unmodifiableMap(weldContainers);
	}

	@Override
	public void onInstall(Module module) {
	}

	@Override
	public void onActivate(Module module) {
	
		Thread thread = Thread.currentThread();
		
		ClassLoader oldCCL = thread.getContextClassLoader();
		ModuleClassLoader moduleClassloader = module.getClassloader();

		thread.setContextClassLoader(moduleClassloader);
		
		// TODO: throw an exception when a bean is ignored!
		// Weld logs an info:  WELD-000119 Not generating any bean definitions from test.ClassA because of underlying class loading error
		WeldContainer initialize = new Weld().initialize();

		thread.setContextClassLoader(oldCCL);

		BeanManager bm = initialize.getBeanManager();
		
		// TODO: is put() thread-safe? 
		synchronized (weldContainers) {
			weldContainers.put(module, bm);
		}
	}

	@Override
	public void onUninstall(Module module) {
		// TODO Auto-generated method stub
		
	}

}
