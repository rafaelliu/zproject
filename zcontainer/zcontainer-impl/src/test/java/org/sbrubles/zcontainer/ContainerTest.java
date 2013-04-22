package org.sbrubles.zcontainer;

import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.test.util.ModuleArchive;
import org.sbrubles.zcontainer.test.util.ModuleDescriptorBuilder;

import test.ClassA;
import test.ClassB;
import test.Listener;

public class ContainerTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";

	private ModuleArchive archiveA;
	private ModuleArchive archiveB;
	private ModuleDescriptor descriptorA;
	private ModuleDescriptor descriptorB;
	
	@Before
	public void init() {
		archiveA = ModuleArchive.newInstance(MODULE_A + ".jar")
				.addClass(Listener.class)
				.addClass(ClassA.class);

		archiveB = ModuleArchive.newInstance(MODULE_B + ".jar")
				.addClass(Listener.class)
				.addClass(ClassB.class);
		
		descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_A)
				.addDependency(MODULE_B)
				.addModuleListener(Listener.class.getName())
				.build();

		descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_B)
				.addModuleListener(Listener.class.getName())
				.build();

	}
	
	@Test
	public void lifeCycleClassloaderTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println(System.getProperty("java.class.path"));
		Container container = Container.create();

		Module moduleA = container.install(archiveA.getInputStream(), descriptorA );
		Assert.assertTrue(moduleA.getStatus() == ModuleStatus.INSTALLED);

		Module moduleB = container.install(archiveB.getInputStream(), descriptorB );
		Assert.assertTrue(moduleB.getStatus() == ModuleStatus.ACTIVE);
		Assert.assertTrue(moduleA.getStatus() == ModuleStatus.ACTIVE);

		container.uninstall(MODULE_B);
		Assert.assertTrue(moduleA.getStatus() == ModuleStatus.INSTALLED);
	}

}
