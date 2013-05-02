package org.sbrubles.zcontainer.arquillian.container;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class ZContainerExtension implements LoadableExtension
{
   @Override
   public void register(ExtensionBuilder builder)
   {
      builder.service(DeployableContainer.class, ZContainerEmbeddedContainer.class);
      builder.service(TestEnricher.class, ZContainerEnricher.class);

   }

}
