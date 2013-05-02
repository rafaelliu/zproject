

import java.net.MalformedURLException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.ClassA;
import test.ClassB;
import test.Listener;

@RunWith(Arquillian.class)
public class ContainerTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";

	@Deployment(name = MODULE_A)
	public static JavaArchive moduleA() {
		return ShrinkWrap.create(JavaArchive.class , MODULE_A + ".jar")
				.addClass(Listener.class)
				.addClass(ClassA.class);
	}
	
	@Deployment(name = MODULE_B)
	public static JavaArchive moduleB() {
		return ShrinkWrap.create(JavaArchive.class , MODULE_B + ".jar")
				.addClass(ClassB.class);
	}
	
	@Test
	public void lifeCycleClassloaderTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Entrou");
	}

}
