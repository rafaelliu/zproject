package org.sbrubles.zcontainer.api;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;

public abstract class Container {

	public abstract Collection<Module> getModules();

	public abstract Module install(InputStream in)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException;

	public abstract Module install(File file)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException;

	public abstract Module install(InputStream in, ModuleConfiguration configuration)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException;

	public abstract Module install(File file, ModuleConfiguration configuration)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException;

	public abstract Module uninstall(String name) throws MalformedURLException;

	public static Container create() {
		ServiceLoader<Container> service = ServiceLoader.load(Container.class);
		Iterator<Container> it = service.iterator();
		return it.hasNext() ? it.next() : null;
	}

}