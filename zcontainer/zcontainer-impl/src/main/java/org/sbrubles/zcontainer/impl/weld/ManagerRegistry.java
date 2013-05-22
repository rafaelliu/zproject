package org.sbrubles.zcontainer.impl.weld;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.WeldSEBeanRegistrant;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;
import org.sbrubles.zcontainer.impl.weld.extension.ZContainerModuleExtension;
import org.sbrubles.zcontainer.impl.weld.extension.ZContainerExtension;


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
		
		// give the module a change to define its own bm.
		BeanManager bm = module.getBeanManager();
		
		if (bm == null) {
	
			// TODO: throw an exception when a bean is ignored!
			// Weld logs an info:  WELD-000119 Not generating any bean definitions from test.ClassA because of underlying class loading error
			ClassLoader cl = module.getClassloader();
			Weld weld = new Weld();
			weld.addExtension(new ZContainerModuleExtension());
			weld.addExtension(new WeldSEBeanRegistrant());
			WeldContainer initialize = weld.initialize(cl);
	
			bm = initialize.getBeanManager();
			
		}
		
		((ModuleImpl) module).setBeanManager(bm);
		
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
