package org.sbrubles.zcontainer.api.module;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.sbrubles.zcontainer.api.module.BootloaderFilter.FilterType;

@XmlRootElement(name="module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleConfiguration {
	
	private String name;
	private String moduleListenerClass;
	private String containerListenerClass;
	
	@XmlElementWrapper(name="dependencies")
	@XmlElement(name="dependency")
	private List<String> dependencies = new ArrayList<String>();
	
	@XmlElement(name="bootloader-filter")
	private BootloaderFilter bootloaderFilter = new BootloaderFilter();

	/*
	 * 
	 */
	
	public BootloaderFilter getBootloaderFilter() {
		return bootloaderFilter;
	}
	public void setBootloaderFilter(BootloaderFilter bootloaderPackages) {
		this.bootloaderFilter = bootloaderPackages;
	}
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
	
	@Override
	public String toString() {
		return "ModuleConfiguration [name=" + name + ", moduleListenerClass="
				+ moduleListenerClass + ", containerListenerClass="
				+ containerListenerClass + ", dependencies=" + dependencies
				+ ", bootloaderFilter=" + bootloaderFilter + "]";
	}
	
	
	
	public static void main(String[] args) throws Exception {
		ModuleConfiguration obj = new ModuleConfiguration();
//		BootloaderFilter listWrapper = new BootloaderFilter();
//		listWrapper.setFilterType(FilterType.WHITELIST);
//		listWrapper.addClass("classsss");
//		obj.setBootloaderFilter(listWrapper);
		
        JAXBContext jc = JAXBContext.newInstance(ModuleConfiguration.class, BootloaderFilter.class);
        
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, System.out);
	}
	
}
