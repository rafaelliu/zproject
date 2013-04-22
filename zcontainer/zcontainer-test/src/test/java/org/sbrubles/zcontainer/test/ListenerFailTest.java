package org.sbrubles.zcontainer.test;

import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.test.ZContainerRunner;
import org.sbrubles.zcontainer.test.annotations.ContainerInstance;
import org.sbrubles.zcontainer.test.annotations.TestClassloader;
import org.sbrubles.zcontainer.test.util.ModuleArchive;
import org.sbrubles.zcontainer.test.util.ModuleDescriptorBuilder;

import test.ClassA;
import test.ClassB;
import test.Listener;

@RunWith(ZContainerRunner.class)
@TestClassloader(ListenerFailTest.MODULE_B)
public class ListenerFailTest {

	public static final String MODULE_A = "aModule";
	public static final String MODULE_B = "bModule";
	
	@ContainerInstance
	public static Container getContainer() throws Exception {
		Container container = Container.create();

		ModuleArchive archiveA = ModuleArchive.newInstance(MODULE_A + ".jar")
				.addClass(Listener.class)
				.addClass(ClassA.class);
		ModuleDescriptor descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_A)
				.addContainerListener(Listener.class.getName())
				.build();
		container.install(archiveA.getInputStream(), descriptorA );

		ModuleArchive archiveB = ModuleArchive.newInstance(MODULE_B + ".jar")
				.addClass(ClassB.class);
		ModuleDescriptor descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_B)
				.build();
		container.install(archiveB.getInputStream(), descriptorB );
		
		return container;
	}
	
	@Test
	public void loadClassFromAnotherModuleNokTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertTrue(ClassA.class.getClassLoader() instanceof ModuleClassLoader);
		Assert.assertTrue(ClassB.class.getClassLoader() instanceof ModuleClassLoader);
		Assert.assertTrue(ClassA.class.getClassLoader() != ClassB.class.getClassLoader());
	}
	
}
