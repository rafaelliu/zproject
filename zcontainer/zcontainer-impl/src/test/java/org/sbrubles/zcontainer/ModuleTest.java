package org.sbrubles.zcontainer;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.impl.ContainerImpl;
import org.sbrubles.zcontainer.test.util.ModuleArchive;
import org.sbrubles.zcontainer.test.util.ModuleDescriptorBuilder;

import test.ClassA;
import test.ClassB;
import test.ClassC;

public class ModuleTest {
	
	private static final String MODULE_C = "cModule";

	private static final String MODULE_B = "bModule";

	private static final String MODULE_A = "aModule";

	private Container container = ContainerImpl.create();

	private Module moduleA;
	private Module moduleB;
	private Module moduleC;

	@Before
	public void init() throws Exception {
		InputStream isA = ModuleArchive.newInstance(MODULE_A + ".jar")
			.addClass(ClassA.class)
			.getInputStream();

		InputStream isB = ModuleArchive.newInstance(MODULE_B + ".jar")
			.addClass(ClassB.class)
			.getInputStream();

		InputStream isC = ModuleArchive.newInstance(MODULE_C + ".jar")
			.addClass(ClassC.class)
			.getInputStream();
		
		moduleA = container.install(isA, ModuleDescriptorBuilder.newInstance(MODULE_A).build());
		moduleB = container.install(isB, ModuleDescriptorBuilder.newInstance(MODULE_B).addDependency(MODULE_A).build());
		moduleC = container.install(isC, ModuleDescriptorBuilder.newInstance(MODULE_C).addDependency(MODULE_B).build());
	}
	
	@Test(expected=ClassNotFoundException.class)
	public void isolationATest() throws ClassNotFoundException, IOException{
		moduleA.getClassloader().loadClass(ClassB.class.getName());
	}
	
	@Test(expected=ClassNotFoundException.class)
	public void isolationBTest() throws ClassNotFoundException, IOException{
		moduleB.getClassloader().loadClass(ClassC.class.getName());
	}
	
	@Test
	public void delegationTest() throws ClassNotFoundException, IOException{ 
		Class<?> class1 = moduleB.getClassloader().loadClass(ClassA.class.getName());
		Class<?> class2 = moduleC.getClassloader().loadClass(ClassA.class.getName());
		
		Assert.assertTrue(class1 == class2);
		Assert.assertTrue(class1.getClassLoader() != getClass().getClassLoader());
	}
	
}