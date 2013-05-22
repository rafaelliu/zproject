package org.sbrubles.zcontainer.embedded;

import java.io.File;
import java.io.InputStream;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.BootloaderFilter.FilterType;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.config.Configuration;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

import test.ClassB;
import test.ModuleClass;
import test.EmbeddedClass;

@RunWith(Arquillian.class)
public class EmbeddedDeployerTest {

	private static final String WEB_APP = "webApp";
	private static final String MODULE_A = "aModule";

	@Deployment
	public static JavaArchive embedded_module() {
		ModuleConfiguration embeddedConfig = ModuleConfigurationBuilder
				.newInstance(WEB_APP)
				.setBootloaderType(FilterType.BLACKLIST)
				.build();
		
		File configFile = Configuration.writeModuleConfiguration(embeddedConfig);
		
		return ShrinkWrap.create(JavaArchive.class)
				.addClass(EmbeddedClass.class)
				.addAsResource(configFile, Configuration.CONFIG_PATH)
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Inject
	private Container container;
	
	@Inject
	private EmbeddedClass serverClass;
	
	@Before
	public void init() throws Exception {
		ModuleConfiguration moduleConfig = ModuleConfigurationBuilder
				.newInstance(MODULE_A)
				.addDependency(WEB_APP)
				.build();
		
		ZArchive moduleA = ShrinkWrap.create(ZArchive.class)
				.addClass(ModuleClass.class)
				.addAsConfiguration(moduleConfig)
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
		
		InputStream stream = moduleA.as(ZipExporter.class).exportAsInputStream();
		
		container.install(stream);
	}
	
	@Test
	public void lifeCycleClassloaderTest() throws Exception {
		serverClass.fireEvent("rafaelliu");
		Assert.assertThat(serverClass.getValue(), CoreMatchers.is("rafaelliu"));
	}
	
}
