package org.sbrubles.zcontainer.weld;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.sbrubles.zcontainer.api.listener.ContainerListener;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;


public class WeldListener implements ContainerListener {
//public class WeldListener implements ModuleListener {

	public void onInstall(Module module) {
	}

	public void onActivate(Module module) {
		WeldContainer initialize = new Weld().initialize();
//		System.out.println(module.getDescriptor().getName());
//		System.out.println(module.getClassloader());
//		System.out.println("WeldListener");
//		System.out.println(this.getClass().getClassLoader());
	}

	public void onUninstall(Module module) {
	}

}
