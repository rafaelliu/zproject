package org.sbrubles.zcontainer.impl.config;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.sbrubles.zcontainer.api.module.ModuleConfiguration;

public class Configuration {
	
	private static Logger logger = Logger.getLogger(Configuration.class.getName());
	
	public static String TEMP_DIR;
	
	public static final String CONFIG_FILE = "META-INF/config.xml";

	static {
		TEMP_DIR = System.getProperty("tempDir");
		if (TEMP_DIR == null) {
			TEMP_DIR = System.getProperty("java.io.tmpdir");
		}
	}
	
	public static ModuleConfiguration readModuleConfiguration(File file) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ModuleConfiguration.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			return (ModuleConfiguration) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			logger.log(Level.FINE, "Could not read configuration file: " + file, e);
			return null;
		}
	}
	
	public static File writeModuleConfiguration(ModuleConfiguration configuration) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ModuleConfiguration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			File file = File.createTempFile("zcontainer_", configuration.getName());
			jaxbMarshaller.marshal(configuration, file);
			return file;
		} catch (Exception e) {
			logger.log(Level.FINE, "Could not write configuration: " + configuration, e);
			return null;
		}
	}

}
