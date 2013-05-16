package org.sbrubles.zcontainer.arquillian.container;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.module.Module;

public class ZContainerEmbeddedContainer implements DeployableContainer<ZContainerEmbeddedConfiguration>
{
	public static final String HTTP_PROTOCOL = "http";

	private static final Logger log = Logger.getLogger(ZContainerEmbeddedContainer.class.getName());

	private Container container;

	private String[] configurationClasses = null;

	private ZContainerEmbeddedConfiguration containerConfig;

	public ZContainerEmbeddedContainer()
	{
	}

	/* (non-Javadoc)
	 * @see org.jboss.arquillian.spi.client.container.DeployableContainer#getConfigurationClass()
	 */
	public Class<ZContainerEmbeddedConfiguration> getConfigurationClass()
	{
		return ZContainerEmbeddedConfiguration.class;
	}

	/* (non-Javadoc)
	 * @see org.jboss.arquillian.spi.client.container.DeployableContainer#getDefaultProtocol()
	 */
	public ProtocolDescription getDefaultProtocol()
	{
		return new ProtocolDescription("Local");
	}

	public void setup(ZContainerEmbeddedConfiguration containerConfig)
	{
		this.containerConfig = containerConfig;
	}

	public void start() throws LifecycleException
	{
		container = Container.create();
	}

	public void stop() throws LifecycleException
	{
	}

	/* (non-Javadoc)
	 * @see org.jboss.arquillian.spi.client.container.DeployableContainer#deploy(org.jboss.shrinkwrap.descriptor.api.Descriptor)
	 */
	public void deploy(Descriptor descriptor) throws DeploymentException
	{
		throw new UnsupportedOperationException("Not implemented");      
	}

	/* (non-Javadoc)
	 * @see org.jboss.arquillian.spi.client.container.DeployableContainer#undeploy(org.jboss.shrinkwrap.descriptor.api.Descriptor)
	 */
	public void undeploy(Descriptor descriptor) throws DeploymentException
	{
		throw new UnsupportedOperationException("Not implemented");      
	}

	public ProtocolMetaData deploy(final Archive<?> archive) throws DeploymentException
	{
		try {
			InputStream exportAsInputStream = archive.as(ZipExporter.class).exportAsInputStream();
			Module module = container.install(new NoCloseInputstream(exportAsInputStream));
			
			return new ProtocolMetaData()
					.addContext(module);
		} catch (Exception e) {
			throw new DeploymentException("erro", e);
		}
	}

	public void undeploy(Archive<?> archive) throws DeploymentException
	{
		try {
			container.uninstall(archive.getName());
		} catch (MalformedURLException e) {
			throw new DeploymentException("erro", e);
		}
	}
	
	// seems that IOUtil.closeOnComplete already close it for us
	//
	// TODO: only delegating read() and let InputStream abstraction take
	// care of the rest is enough? Got to see whether ShrinkWrap override
	// some method.
	private static class NoCloseInputstream extends InputStream {
		
		public InputStream delegate;

		public NoCloseInputstream(InputStream delegate) {
			this.delegate = delegate;
		}

		@Override
		public int read() throws IOException {
			return delegate.read();
		}
		
	}

}
