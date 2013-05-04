package org.sbrubles.zcontainer.weld;


import java.net.MalformedURLException;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

import test.ClassA;
import test.ClassB;

@RunWith(Arquillian.class)
public class WeldTest {

	private static final String MODULE_A = "aModule";
	private static final String MODULE_B = "bModule";
	private static final String MODULE_C = "cModule";
	
	@Deployment(name = MODULE_A, order = 1)
	public static ZArchive moduleA() {
		return ShrinkWrap.create(ZArchive.class , MODULE_A)
				.addClass(ClassA.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_A)
						.addDependency(MODULE_B)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Deployment(name = MODULE_B, order = 2)
	public static ZArchive moduleB() {
		return ShrinkWrap.create(ZArchive.class , MODULE_B)
				.addClass(ClassB.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_B)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	
	@Deployment(name = MODULE_C, order = 3)
	public static ZArchive moduleC() {
		return ShrinkWrap.create(ZArchive.class , MODULE_C)
				.addClass(ClassB.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_C)
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
	public void ensureWeldDoesntScanDependenciesTest( ) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertThat(classB_fromA, CoreMatchers.is(CoreMatchers.nullValue()));
		Assert.assertThat(classB_fromB, CoreMatchers.is(CoreMatchers.notNullValue()));
	}

	@Test @OperateOnDeployment(MODULE_A)
	public void ensureModuleHasSameBeansTest( ) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertThat(classA1.inc(), CoreMatchers.is(0));
		Assert.assertThat(classA2.inc(), CoreMatchers.is(1));
		Assert.assertThat(classA2.inc(), CoreMatchers.is(2));
	}
	
	@Test @OperateOnDeployment(MODULE_A)
	public void ensureModulesHaveDiferentBeansTest( ) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Assert.assertThat(classB_fromB.inc(), CoreMatchers.is(0));
		Assert.assertThat(classB_fromB.inc(), CoreMatchers.is(1));
		Assert.assertThat(classB_fromC.inc(), CoreMatchers.is(0));
		Assert.assertThat(classB_fromC.inc(), CoreMatchers.is(1));
	}
	
}