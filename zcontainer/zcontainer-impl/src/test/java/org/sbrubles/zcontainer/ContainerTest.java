package org.sbrubles.zcontainer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.Before;
import org.junit.Test;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.BootloaderFilter.FilterType;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

import test.ClassA;
import test.ClassB;

public class ContainerTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";

	private ZArchive archiveA;
	private ZArchive archiveB;
	
	@Before
	public void init() {
		archiveA = ShrinkWrap.create(ZArchive.class)
				.addClass(ClassA.class)
				.addAsConfiguration(
						ModuleConfigurationBuilder
							.newInstance(MODULE_A)
							.addDependency(MODULE_B)
							.setBootloaderType(FilterType.BLACKLIST)
							.build()
						);

		archiveB = ShrinkWrap.create(ZArchive.class)
				.addClass(ClassB.class)
				.addAsConfiguration(
						ModuleConfigurationBuilder
							.newInstance(MODULE_B)
							.build()
						);

	}
	
	@Test
	public void lifeCycleClassloaderTest() throws Exception {
		System.out.println(System.getProperty("java.class.path"));
		Container container = Container.create();

		Module moduleA = container.install(asInputStream(archiveA));
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));

		Module moduleB = container.install(asInputStream(archiveB));
		assertThat(moduleB.getStatus(), is(ModuleStatus.ACTIVE));
		assertThat(moduleA.getStatus(), is(ModuleStatus.ACTIVE));

		container.uninstall(MODULE_B);
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));
	}
	
	@Test
	public void customBeanManagerClassloaderTest() throws Exception {
		System.out.println(System.getProperty("java.class.path"));
		Container container = Container.create();

		Module moduleA = container.install(asInputStream(archiveA));
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));

		Module moduleB = container.install(asInputStream(archiveB));
		assertThat(moduleB.getStatus(), is(ModuleStatus.ACTIVE));
		assertThat(moduleA.getStatus(), is(ModuleStatus.ACTIVE));

		container.uninstall(MODULE_B);
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));
	}
	
	private InputStream asInputStream(ZArchive archive) {
		return archive.as(ZipExporter.class).exportAsInputStream();
	}

}
