package org.sbrubles.zcontainer.impl.weld;

import java.util.HashMap;
import java.util.Map;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.Activator;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;


public class WeldListener implements ModuleListener {

	public Map<Module, WeldContainer> weldContainers = new HashMap<Module, WeldContainer>();

	public void onInstall(Module module) {
	}

	public void onActivate(Module module) {
		
		synchronized (weldContainers) {
			ModuleClassLoader classloader = module.getClassloader();
			ClassLoader oldCCL = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(classloader);
			
			WeldContainer initialize = new Weld().initialize();

			Thread.currentThread().setContextClassLoader(oldCCL);

			((ModuleImpl) module).setWeld(initialize);
			
			weldContainers.put(module, initialize);
		}
	}

	public void onUninstall(Module module) {
	}

}
