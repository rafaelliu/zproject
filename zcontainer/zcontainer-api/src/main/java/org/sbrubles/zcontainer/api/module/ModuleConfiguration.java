package org.sbrubles.zcontainer.api.module;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleConfiguration {
	
	private String name;
	private String moduleListenerClass;
	private String containerListenerClass;
	
	@XmlElementWrapper(name="dependencies")
	@XmlElement(name="dependency")
	private List<String> dependencies = new ArrayList<String>();
	
	public String getModuleListenerClass() {
		return moduleListenerClass;
	}
	public void setModuleListenerClass(String moduleListenerClass) {
		this.moduleListenerClass = moduleListenerClass;
	}
	public String getContainerListenerClass() {
		return containerListenerClass;
	}
	public void setContainerListenerClass(String containerListenerClass) {
		this.containerListenerClass = containerListenerClass;
	}
	public List<String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
