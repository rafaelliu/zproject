package org.sbrubles.zcontainer.weld;


import java.lang.reflect.Proxy;
import java.net.MalformedURLException;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.weld.Container;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.manager.BeanManagers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

import test.ClassA;
import test.ClassB;
import test.ClassC;
import test.ClassParameter;
import test.Listener;

@RunWith(Arquillian.class)
public class WeldTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";
	private static final String MODULE_C = "cModule";
	
	@Deployment(name = MODULE_A, order = 2)
	public static ZArchive moduleA() {
		return ShrinkWrap.create(ZArchive.class , MODULE_A)
				.addClass(ClassA.class)
				.addClass(ClassB.class)
				.addClass(ClassParameter.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_A)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Deployment(name = MODULE_B, order = 3)
	public static ZArchive moduleB() {
		return ShrinkWrap.create(ZArchive.class , MODULE_B)
				.addClass(ClassB.class)
				.addClass(ClassParameter.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_B)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Deployment(name = MODULE_C, order = 4)
	public static ZArchive moduleC() {
		return ShrinkWrap.create(ZArchive.class , MODULE_C)
				.addClass(ClassC.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_C)
						.addDependency(MODULE_B)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Inject
	ClassA classA1;
	
	@Inject
	ClassA classA2;
	
	@Inject
	ClassB classB_fromA;
	
	@Inject @OperateOnDeployment(MODULE_B)
	ClassB classB_fromB;
	
	@Inject @OperateOnDeployment(MODULE_C)
	ClassB classB_fromC;
	
	@Test @OperateOnDeployment(MODULE_A)
	public void lifeCycleClassloaderTest( ) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("Entrou ");
		
		String result = classB_fromA.receive(new ClassParameter("teste"));
		Assert.assertThat(result, CoreMatchers.equalTo("teste"));

		System.out.println(classA1.getActualClassloader());
		System.out.println(classA2.getActualClassloader());
		Assert.assertThat(classA1.inc(), CoreMatchers.is(0));
		Assert.assertThat(classA2.inc(), CoreMatchers.is(1));

		System.out.println(classB_fromA.getActualClassloader());
		System.out.println(classB_fromB.getActualClassloader());
		Assert.assertThat(classB_fromA.inc(), CoreMatchers.is(0));
		Assert.assertThat(classB_fromB.inc(), CoreMatchers.is(0));
		Assert.assertThat(classB_fromC, CoreMatchers.is(CoreMatchers.nullValue()));
		
	}

}
