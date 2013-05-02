package org.sbrubles.zcontainer.arquillian.container;


import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

public class ZContainerEmbeddedConfiguration implements ContainerConfiguration
{
   private String bindAddress = "localhost";

   private int bindHttpPort = 9090;

   private boolean jettyPlus = true;

   private String configurationClasses;

   /* (non-Javadoc)
    * @see org.jboss.arquillian.spi.client.container.ContainerConfiguration#validate()
    */
   @Override
   public void validate() throws ConfigurationException
   {
   }
   
   public int getBindHttpPort()
   {
      return bindHttpPort;
   }

   public void setBindHttpPort(int bindHttpPort)
   {
      this.bindHttpPort = bindHttpPort;
   }

   public String getBindAddress()
   {
      return bindAddress;
   }

   public void setBindAddress(String bindAddress)
   {
      this.bindAddress = bindAddress;
   }

   public boolean isJettyPlus()
   {
      return jettyPlus;
   }

   public void setJettyPlus(boolean jettyPlus)
   {
      this.jettyPlus = jettyPlus;
   }

   public String getConfigurationClasses()
   {
      return configurationClasses;
   }

   /**
    * @param configurationClasses A comma separated list of fully qualified configuration classes
    */
   public void setConfigurationClasses(String configurationClasses)
   {
      this.configurationClasses = configurationClasses;
   }
}
