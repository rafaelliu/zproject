package org.sbrubles.zcontainer.test.util;
import java.io.File;
import java.io.InputStream;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.impl.config.Configuration;

public class ModuleArchive {
	private JavaArchive bundle;
	
	public static ModuleArchive newInstance(String name) {
		return new ModuleArchive(name);
	}
	private ModuleArchive(String name) {
		bundle = ShrinkWrap.create(JavaArchive.class, name);
		bundle.addAsResource(EmptyAsset.INSTANCE, "/META-INF/beans.xml");
	}
	public ModuleArchive addClass(Class<?>... classes) {
		bundle.addClasses(classes);
		return this;
	}
	public ModuleArchive addConfiguration(ModuleConfiguration configuration) {
		File file = Configuration.writeModuleConfiguration(configuration);
		bundle.addAsResource(file, Configuration.CONFIG_FILE);
		return this;
	}
	public String getName() {
		return bundle.getName();
	}
	public InputStream getInputStream() {
		return bundle.as(ZipExporter.class).exportAsInputStream();
	}
	public void exportFile(File file) {
		bundle.as(ZipExporter.class).exportTo(file, true);
	}
}
