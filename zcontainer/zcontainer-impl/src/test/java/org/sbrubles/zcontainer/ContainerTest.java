package org.sbrubles.zcontainer;

import java.net.MalformedURLException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;
import org.sbrubles.zcontainer.util.ModuleArchive;

import test.ClassA;
import test.ClassB;
import test.Listener;

public class ContainerTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";

	private ModuleArchive archiveA;
	private ModuleArchive archiveB;
	private ModuleConfiguration descriptorA;
	private ModuleConfiguration descriptorB;
	
	@Before
	public void init() {
		archiveA = ModuleArchive.newInstance(MODULE_A + ".jar")
				.addClass(Listener.class)
				.addClass(ClassA.class);

		archiveB = ModuleArchive.newInstance(MODULE_B + ".jar")
				.addClass(Listener.class)
				.addClass(ClassB.class);
		
		/*descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_A)
				.addDependency(MODULE_B)
				.addModuleListener(Listener.class.getName())
				.build();

		descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_B)
				.addModuleListener(Listener.class.getName())
				.build();*/
		
		descriptorA = new ModuleConfiguration();
		descriptorA.setName(MODULE_A);
		descriptorA.setDependencies(Arrays.asList(MODULE_B));
		descriptorA.setModuleListenerClass(Listener.class.getName());
		
		descriptorB = new ModuleConfiguration();
		descriptorB.setName(MODULE_B);
		descriptorB.setModuleListenerClass(Listener.class.getName());

	}
	
	@Test
	public void lifeCycleClassloaderTest() throws Exception {
		System.out.println(System.getProperty("java.class.path"));
		Container container = Container.create();

		Module moduleA = container.install(archiveA.getInputStream(), descriptorA );
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));

		Module moduleB = container.install(archiveB.getInputStream(), descriptorB );
		assertThat(moduleB.getStatus(), is(ModuleStatus.ACTIVE));
		assertThat(moduleA.getStatus(), is(ModuleStatus.ACTIVE));

		container.uninstall(MODULE_B);
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));
	}
	
	@Test
	public void customBeanManagerClassloaderTest() throws Exception {
		System.out.println(System.getProperty("java.class.path"));
		Container container = Container.create();

		Module moduleA = container.install(archiveA.getInputStream(), descriptorA );
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));

		Module moduleB = container.install(archiveB.getInputStream(), descriptorB );
		assertThat(moduleB.getStatus(), is(ModuleStatus.ACTIVE));
		assertThat(moduleA.getStatus(), is(ModuleStatus.ACTIVE));

		container.uninstall(MODULE_B);
		assertThat(moduleA.getStatus(), is(ModuleStatus.INSTALLED));
	}

}
