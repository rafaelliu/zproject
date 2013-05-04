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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.util.ModuleConfigurationBuilder;

import test.ClassA;
import test.ClassB;
import test.ClientClass;
import test.MyEvent;
import test.ServerClass;
import test.ServerParameter;

@RunWith(Arquillian.class)
public class ModuleIntegrationTest {

	private static final String MODULE_CLIENT = "clientModule";
	private static final String MODULE_SERVER = "serverModule";
	
	@Deployment(name = MODULE_CLIENT, order = 1)
	public static ZArchive moduleA() {
		return ShrinkWrap.create(ZArchive.class , MODULE_CLIENT)
				.addClass(ClientClass.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_CLIENT)
						.addDependency(MODULE_SERVER)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Deployment(name = MODULE_SERVER, order = 2)
	public static ZArchive moduleB() {
		return ShrinkWrap.create(ZArchive.class , MODULE_SERVER)
				.addClass(ServerClass.class)
				.addClass(ServerParameter.class)
				.addClass(MyEvent.class)
				.addAsConfiguration(
					ModuleConfigurationBuilder
						.newInstance(MODULE_SERVER)
						.build())
				.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
	}
	
	@Inject
	ClientClass clientClass;
	
	@Inject @OperateOnDeployment(MODULE_SERVER)
	ServerClass serverClass;
	
	@Test @OperateOnDeployment(MODULE_CLIENT)
	public void lookupServiceTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ServerParameter callServer = clientClass.callServer("rafaelliu");
		Assert.assertThat(callServer.getGreating(), CoreMatchers.is("Hello rafaelliu"));
	}
	
	@Test @OperateOnDeployment(MODULE_CLIENT)
	public void eventTest() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		serverClass.fireEvent("Hello rafaelliu");
		Assert.assertThat(clientClass.getValue(), CoreMatchers.is("Hello rafaelliu"));
	}

}