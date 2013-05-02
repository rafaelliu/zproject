package org.sbrubles.jaxb;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;

public class JaxbTests {

	@Test
	public void testMarshall() {
		ModuleConfiguration customer = new ModuleConfiguration();
		List<String> dependencies = Arrays.asList("moduleA", "moduleB", "moduleC");
		customer.setDependencies(dependencies );
		customer.setName("moduleD");

		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(ModuleConfiguration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(customer, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}


}
