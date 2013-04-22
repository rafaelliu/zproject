

import java.net.MalformedURLException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.api.module.ModuleDescriptor;
import org.sbrubles.zcontainer.api.module.ModuleStatus;
import org.sbrubles.zcontainer.test.util.ModuleArchive;
import org.sbrubles.zcontainer.test.util.ModuleDescriptorBuilder;
import org.sbrubles.zcontainer.weld.MyBean;
import org.sbrubles.zcontainer.weld.WeldListener;

import test.ClassA;
import test.ClassB;
import test.Listener;

public class ContainerTest {

	private static final String MODULE_WELD = "zcontainer-weld";
	private static final String MODULE_ZAR = "zar";

	private ModuleArchive archiveA;
	private ModuleArchive archiveB;
	private ModuleDescriptor descriptorA;
	private ModuleDescriptor descriptorB;
	
	@Before
	public void init() {
		archiveA = ModuleArchive.newInstance(MODULE_WELD + ".jar")
				.addClass(WeldListener.class);

		archiveB = ModuleArchive.newInstance(MODULE_ZAR + ".jar")
				.addClass(MyBean.class);
		
		descriptorA = ModuleDescriptorBuilder.newInstance(MODULE_WELD)
//				.addModuleListener(WeldListener.class.getName())
				.addContainerListener(WeldListener.class.getName())
				.build();

		descriptorB = ModuleDescriptorBuilder.newInstance(MODULE_ZAR)
				.addDependency(MODULE_WELD)
				.build();

	}
	
	@Test
	public void lifeCycleClassloaderTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
//		Container container = Container.create();
//
//		Module moduleA = container.install(archiveA.getInputStream(), descriptorA );
//		Assert.assertTrue(moduleA.getStatus() == ModuleStatus.ACTIVE);
//		System.out.println("* Module " + MODULE_WELD + " installed and activated");
//
//		Module moduleB = container.install(archiveB.getInputStream(), descriptorB );
//		Assert.assertTrue(moduleB.getStatus() == ModuleStatus.ACTIVE);
//		System.out.println("* Module " + MODULE_ZAR + " installed and activated");
		for (Map.Entry<String, String> env : System.getenv().entrySet()) {
			System.out.println(env.getKey() + "=" + env.getValue());
		}
		for (Map.Entry<Object, Object> p : System.getProperties().entrySet()) {
			System.out.println(p.getKey() + "=" + p.getValue());
		}

	}

}
