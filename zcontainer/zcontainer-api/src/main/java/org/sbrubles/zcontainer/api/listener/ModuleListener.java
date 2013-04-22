package org.sbrubles.zcontainer.api.listener;


import org.sbrubles.zcontainer.api.module.Module;

public interface ModuleListener {
	
	/**
	 * Be aware that at this point we still didn't solve our dependencies. That means
	 * that trying to load any of it's classes will throw a CNF Exception. If you want
	 * to change any thing in the module, onActivate is probably best as we do a lot of
	 * work after install, which means we might overwrite or changes or you may change
	 * a not yet ready module
	 * @param module
	 */
	public void onInstall(Module module);
	
	/**
	 * Here we are good to go.
	 * @param module
	 */
	public void onActivate(Module module);
	
	/**
	 * This is a special event in the sense that the module marked as UNINSTALLED is
	 * immediately removed soon after it's state change. 
	 * @param module
	 */
	public void onUninstall(Module module);

}
