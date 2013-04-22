package org.sbrubles.zcontainer.test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.classloader.ModuleClassLoader;
import org.sbrubles.zcontainer.api.module.Module;
import org.sbrubles.zcontainer.test.annotations.ContainerInstance;
import org.sbrubles.zcontainer.test.annotations.TestClassloader;

public class ZContainerRunner extends Runner {
	
	private BlockJUnit4ClassRunner blockJUnit4ClassRunner;

	public ZContainerRunner(Class<?> clazz) throws Throwable {
		final Map<String, Object> injectables = new HashMap<String, Object>();
		
		// create the container
		Container container = getContainer(clazz);
		injectables.put("container", container);

		// create the main module
		Module mainModule = getMainModule(clazz, container);
		injectables.put("module", mainModule);

		ModuleClassLoader mcl = mainModule.getClassloader();

		// we need JUnit itself. This we are delegating to bootloader so we don't
		// get any ClassCastException, since junit was run from this classloader
		// 
		// Otherwise we get weird behavior such as "No runnable methods" exception due
		// to @Test being loaded from different classloaders..
		mcl.addBootloaderPackage("junit");
		mcl.addBootloaderPackage("org.hamcrest");
		mcl.addBootloaderPackage("org.junit");

		// we also need to add zcontainer-test and the target JUnit class to classpath
		String[] split = System.getProperty("java.class.path").split(":");
		URL[] urls = new URL[split.length];
		for (int i = 0; i < split.length; i++) {
			urls[i] = new File(split[i]).toURI().toURL();
		}

		// our classloader we will have to add test folder to classpath, but we don't want
		// to add classes that are going to be packed in modules. this is a quick and dirty
		// way to do it: only classes from test class' package (and below) are loaded
		mcl = new ModuleClassLoaderAdapter(urls, mcl) {
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException {
				if (name.startsWith("test.")) {
					throw new ClassNotFoundException("Class not found: " + name);
				}
				return super.findClass(name);
			}
		};

		mainModule.setClassloader(mcl);
		// we will get the class definition from bundles's classloader (we added it to it's classpath previously)
		clazz = mcl.loadClass(clazz.getName());

		// now we create the default runner overriding it's createTest() method to give the test class
		// access to the Container instance we are talking about
		blockJUnit4ClassRunner = new BlockJUnit4ClassRunner(clazz) {;
			protected Object createTest() throws Exception {
				Object result = super.createTest();
				for (Map.Entry<String, Object> i : injectables.entrySet()) {
					try {
						Field containerField = getTestClass().getJavaClass().getDeclaredField(i.getKey());
						containerField.setAccessible(true);
						containerField.set(result, i.getValue());
					} catch (Exception e) {
						// we couldn't inject it. that's ok
					}
				}
				return result;
			}
		};
	
	}

	private Module getMainModule(Class<?> clazz, Container container) {
		TestClassloader annotation = clazz.getAnnotation(TestClassloader.class);
		String moduleName = annotation.value();

		Module mainModule = null;
		Collection<Module> modules = container.getModules();
		for (Module m : modules) {
			if (m.getDescriptor().getName().equals(moduleName)) {
				mainModule = m;
				break;
			}
		}
		
		if (mainModule == null) {
			throw new IllegalStateException("Either a module classpath wasn't defined using @ModuleClassloader or it doesn't exist");
		}
		return mainModule;
	}

	private Container getContainer(Class<?> clazz) throws IllegalAccessException, InvocationTargetException {
		Method containerIntanceMethod = null;
		for (Method m : clazz.getMethods()) {
			if (m.isAnnotationPresent(ContainerInstance.class)) {
				if (containerIntanceMethod == null) {
					containerIntanceMethod = m;
				} else {
					throw new IllegalStateException("Only one @ContainerInstance method allowed");
				}
			}
		}
		if (containerIntanceMethod == null) {
			throw new IllegalStateException("There's got to be a @ContainerInstance method");
		}
		if (!Modifier.isStatic(containerIntanceMethod.getModifiers())) {
			throw new IllegalStateException("Method @ContainerInstance must be static");
		}
		Container container = (Container) containerIntanceMethod.invoke(null);
		return container;
	}
	
	@Override
	public Description getDescription() {
		return blockJUnit4ClassRunner.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		blockJUnit4ClassRunner.run(notifier);
	}

}
