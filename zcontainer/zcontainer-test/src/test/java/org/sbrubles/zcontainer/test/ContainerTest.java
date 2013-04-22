package org.sbrubles.zcontainer.test;

import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.test.annotations.ContainerInstance;
import org.sbrubles.zcontainer.test.annotations.TestClassloader;
import org.sbrubles.zcontainer.test.util.ModuleArchive;
import org.sbrubles.zcontainer.test.util.ModuleDescriptorBuilder;

import test.ClassA;
import test.ClassB;

@RunWith(ZContainerRunner.class)
@TestClassloader(ContainerTest.MODULE_A)
public class ContainerTest {

	public static final String MODULE_A = "aModule";
	public static final String MODULE_B = "bModule";
	
	private Container container;
	
	@ContainerInstance
	public static Container getContainer() throws Exception {
		ModuleArchive archiveA = ModuleArchive.newInstance(MODULE_A + ".jar")
				.addClass(ClassA.class);

		ModuleArchive archiveB = ModuleArchive.newInstance(MODULE_B + ".jar")
				.addClass(ClassB.class);
		
		ModuleDescriptor descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_A)
				.addDependency(MODULE_B)
				.build();

		ModuleDescriptor descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_B)
				.build();

		Container container = Container.create();
		container.install(archiveA.getInputStream(), descriptorA );
		container.install(archiveB.getInputStream(), descriptorB );
		
		return container;
	}
	
	@Test
	public void classloadingTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ClassLoader classLoaderA = ClassA.class.getClassLoader();
		ClassLoader classLoaderB = ClassB.class.getClassLoader();
		Assert.assertTrue(classLoaderA instanceof ModuleClassLoader);
		Assert.assertTrue(classLoaderB instanceof ModuleClassLoader);
		Assert.assertFalse(classLoaderB == classLoaderA);
	}
	
	@Test
	public void injectionTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertTrue(container != null);
	}

}
