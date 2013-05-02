package test;


import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.listener.Activator;
import org.sbrubles.zcontainer.api.module.Module;

public class Listener implements Activator {
	
	public void onInstall(Module module) {
	}

	public void onActivate(Module module) {
		module.getClassloader().getModuleClassloders().add((ModuleClassLoader) this.getClass().getClassLoader());
	}

	public void onUninstall(Module module) {
	}
}