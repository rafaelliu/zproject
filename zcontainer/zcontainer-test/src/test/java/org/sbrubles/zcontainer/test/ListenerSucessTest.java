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

@RunWith(ZContainerRunner.class)
@TestClassloader(ListenerSucessTest.MODULE_B)
public class ListenerSucessTest {

	public static final String MODULE_A = "aModule";
	public static final String MODULE_B = "bModule";
	
	@ContainerInstance
	public static Container getContainer() throws Exception {
		Container container = Container.create();

		ModuleArchive archiveB = ModuleArchive.newInstance(MODULE_B + ".jar")
				.addClass(ClassB.class);
		ModuleDescriptor descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_B)
				.build();
		container.install(archiveB.getInputStream(), descriptorB );

		ModuleArchive archiveA = ModuleArchive.newInstance(MODULE_A + ".jar")
				.addClass(ClassA.class);
		ModuleDescriptor descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_A)
				.build();
		container.install(archiveA.getInputStream(), descriptorA );
		
		return container;
	}
	
	@Test(expected=NoClassDefFoundError.class)
	public void loadClassFromAnotherModuleNokTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertTrue(ClassA.class.getClassLoader() instanceof ModuleClassLoader);
	}
	
}
