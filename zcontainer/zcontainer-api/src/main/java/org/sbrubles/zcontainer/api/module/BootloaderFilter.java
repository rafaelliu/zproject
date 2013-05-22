package org.sbrubles.zcontainer.api.module;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class BootloaderFilter {
	
	@XmlAttribute(name="policy", required=false)
	private FilterType filterType = FilterType.WHITELIST;

	@XmlElement(name="package")
	private List<String> filteredPackages = new ArrayList<String>(ModuleClassLoader.DEFAULT_BOOTLOADER_PACKAGES);
	
	@XmlElement(name="class")
	private List<String> filteredClasses = new ArrayList<String>();
	
	public void addClass(String name) {
		filteredClasses.add(name);
	}
	
	public void addPackage(String name) {
		filteredPackages.add(name);
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	@XmlType
	public static enum FilterType {
		@XmlEnumValue("whitelist") WHITELIST,
		@XmlEnumValue("blacklist") BLACKLIST;
	}

	public List<String> getPackages() {
		return filteredPackages;
	}

	public List<String> getClasses() {
		return filteredClasses;
	}

}
