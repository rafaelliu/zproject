package org.sbrubles.zcontainer.api.module;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModuleDescriptor {
	
	public final static ModuleDescriptor EMPTY_DESCRIPTOR = new ModuleDescriptor(null);
	
	private String name;
	private URL[] urls = new URL[]{};
	private String moduleListenerClass;
	private String containerListenerClass;
	private List<String> dependencies = new ArrayList<String>();

	public ModuleDescriptor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public URL[] getUrls() {
		return urls;
	}

	public void setUrls(URL... urls) {
		this.urls = urls;
	}

	public String getModuleListenerClass() {
		return moduleListenerClass;
	}

	public void setModuleListenerClass(String moduleListenerClass) {
		this.moduleListenerClass = moduleListenerClass;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleDescriptor other = (ModuleDescriptor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getContainerListenerClass() {
		return containerListenerClass;
	}

	public void setContainerListenerClass(String containerListenerClass) {
		this.containerListenerClass = containerListenerClass;
	}

}
