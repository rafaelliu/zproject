package org.sbrubles.zcontainer.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.deployers.Deployer;
import org.sbrubles.zcontainer.api.listener.ModuleListener;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.impl.module.ModuleImpl;

public class ContainerImpl extends Container {
	
	public Map<String, Module> modules = new HashMap<String, Module>();
	
	private List<Deployer> deployers = new ArrayList<Deployer>();
	
	public ContainerImpl() {
		ServiceLoader<Deployer> deployerServices = ServiceLoader.load(Deployer.class);
		Iterator<Deployer> iterator = deployerServices.iterator();
		while (iterator.hasNext()) {
			Deployer d = iterator.next();
			try {
				d.init(this);
				deployers.add(d);
			} catch (Exception e) {
				System.err.println("Erro ao iniciar deployer " + d);
			}
		}
	}
	
	public Collection<Module> getModules() {
		return Collections.unmodifiableCollection(modules.values());
	}
	
	public Module install(InputStream in, ModuleDescriptor descriptor) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		File file = storeFile(in, descriptor.getName());
		return install(file, descriptor);
	}

	@Override
	public Module install(File file, ModuleDescriptor descriptor)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		
		ModuleImpl module = new ModuleImpl(file, descriptor);
		modules.put(descriptor.getName(), module);
		notify(module, ModuleStatus.INSTALLED);

		for (Module m : modules.values()) {
			if (m.getStatus() == ModuleStatus.INSTALLED && resolve(m)) {
				m.setStatus(ModuleStatus.ACTIVE);
				notify(m, ModuleStatus.ACTIVE);
			}
		}
		
		return module;
	}

	public void notify(Module module, ModuleStatus status) {
		for (Module m : modules.values()) {
			ModuleListener listener = null;
			if (m.equals(module)) {
				listener = m.getModuleListener();
			} else {
				listener = m.getContainerListener();
			}
			
			if (listener == null) continue;
			
			if (status == ModuleStatus.INSTALLED) {
				listener.onInstall(module);
			} else if (status == ModuleStatus.ACTIVE) {
				listener.onActivate(module);
			} else if (status == ModuleStatus.UNINSTALLED) {
				listener.onUninstall(module);
			}
		}
	}
	
	public Module uninstall(String name) throws MalformedURLException {
		Module module = modules.remove(name);
		
		for (Module m : modules.values()) {
			if (m.getStatus() == ModuleStatus.ACTIVE && !resolve(m)) {
				m.setStatus(ModuleStatus.INSTALLED);
				notify(m, ModuleStatus.INSTALLED);
			}
		}
		
		return module;
	}
	
	public boolean resolve(Module module) throws MalformedURLException {
		List<String> dependencies = module.getDescriptor().getDependencies();
		List<ModuleClassLoader> classloaders = new ArrayList<ModuleClassLoader>();
		
		for (String d : dependencies) {
			Module m = modules.get(d);
			if (m == null) {
				return false;
			}
			
			classloaders.add(m.getClassloader());
		}
		ModuleClassLoader classloader = module.getClassloader();
		classloader.setModuleClassloders(classloaders);
		
		return true;
	}

	private File storeFile(InputStream in, String name) {
		try {
			File tempFile = File.createTempFile("zcontainer_" + name + "_", null);
			FileOutputStream out = new FileOutputStream(tempFile);
			
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
			    out.write(buffer, 0, len);
			}
			
			return tempFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
