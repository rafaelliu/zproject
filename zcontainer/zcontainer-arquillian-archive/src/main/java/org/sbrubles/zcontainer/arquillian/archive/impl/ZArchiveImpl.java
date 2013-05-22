package org.sbrubles.zcontainer.arquillian.archive.impl;

import java.io.File;
import java.util.logging.Logger;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.impl.base.Validate;
import org.jboss.shrinkwrap.impl.base.asset.ServiceProviderAsset;
import org.jboss.shrinkwrap.impl.base.container.ContainerBase;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.jboss.shrinkwrap.impl.base.spec.WebArchiveImpl;
import org.sbrubles.zcontainer.api.module.ModuleConfiguration;
import org.sbrubles.zcontainer.arquillian.archive.ZArchive;
import org.sbrubles.zcontainer.impl.config.Configuration;

public class ZArchiveImpl extends ContainerBase<ZArchive> implements ZArchive {
    // -------------------------------------------------------------------------------------||
    // Class Members ----------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    // -------------------------------------------------------------------------------------||
    // Instance Members -------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    // -------------------------------------------------------------------------------------||
    // Constructor ------------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    public ZArchiveImpl(Archive<?> archive) {
        super(ZArchive.class, archive);
    }

    // -------------------------------------------------------------------------------------||
    // Class Members ----------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(WebArchiveImpl.class.getName());

    /**
     * Path to the resources inside of the Archive.
     */
    private static final ArchivePath PATH_RESOURCE = ArchivePaths.create("/");

    /**
     * Path to the libraries inside of the Archive.
     */
    private static final ArchivePath PATH_LIBRARY = ArchivePaths.create("lib");

    private static final ArchivePath PATH_CLASSES = ArchivePaths.create("/");

    /**
     * Path to the manifests inside of the Archive.
     */
    private static final ArchivePath PATH_MANIFEST = ArchivePaths.create("META-INF");

    /**
     * Path to web archive service providers.
     */
    private static final ArchivePath PATH_SERVICE_PROVIDERS = ArchivePaths.create(PATH_CLASSES, "META-INF/services");

    // -------------------------------------------------------------------------------------||
    // Instance Members -------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    // -------------------------------------------------------------------------------------||
    // Constructor ------------------------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    // -------------------------------------------------------------------------------------||
    // Required Implementations -----------------------------------------------------------||
    // -------------------------------------------------------------------------------------||

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.shrinkwrap.impl.base.container.ContainerBase#getManifestPath()
     */
    @Override
    protected ArchivePath getManifestPath() {
        return PATH_MANIFEST;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.shrinkwrap.impl.base.container.ContainerBase#getClassesPath()
     */
    @Override
    protected ArchivePath getClassesPath() {
        return PATH_CLASSES;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.shrinkwrap.impl.base.container.ContainerBase#getResourcePath()
     */
    @Override
    protected ArchivePath getResourcePath() {
        return PATH_RESOURCE;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.shrinkwrap.impl.base.container.ContainerBase#getLibraryPath()
     */
    @Override
    protected ArchivePath getLibraryPath() {
        return PATH_LIBRARY;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.shrinkwrap.impl.base.container.WebContainerBase#getWebInfPath()
     */
    protected ArchivePath getServiceProvidersPath() {
        return PATH_SERVICE_PROVIDERS;
    }
    // -------------------------------------------------------------------------------------||
    // Required Implementations - WebContainer --------------------------------------------||
    // -------------------------------------------------------------------------------------||

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.shrinkwrap.api.container.ManifestContainer#addServiceProvider(java.lang.Class,
     * java.lang.Class<?>[])
     */
    @Override
    public ZArchive addAsServiceProvider(Class<?> serviceInterface, Class<?>... serviceImpls) throws IllegalArgumentException {
        Validate.notNull(serviceInterface, "ServiceInterface must be specified");
        Validate.notNullAndNoNullValues(serviceImpls, "ServiceImpls must be specified and can not contain null values");

        Asset asset = new ServiceProviderAsset(serviceImpls);
        ArchivePath path = new BasicPath(getServiceProvidersPath(), serviceInterface.getName());
        return add(asset, path);
    }

    /* (non-Javadoc)
    * @see org.jboss.shrinkwrap.impl.base.container.ContainerBase#addAsServiceProvider(java.lang.String, java.lang.String[])
    */
    @Override
    public ZArchive addAsServiceProvider(String serviceInterface, String... serviceImpls) throws IllegalArgumentException
    {
        Validate.notNull(serviceInterface, "ServiceInterface must be specified");
        Validate.notNullAndNoNullValues(serviceImpls, "ServiceImpls must be specified and can not contain null values");

        Asset asset = new ServiceProviderAsset(serviceImpls);
        ArchivePath path = new BasicPath(getServiceProvidersPath(), serviceInterface);
        return add(asset, path);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.shrinkwrap.api.container.ServiceProviderContainer#addServiceProviderAndClasses(java.lang.Class,
     * java.lang.Class<?>[])
     */
    @Override
    public ZArchive addAsServiceProviderAndClasses(Class<?> serviceInterface, Class<?>... serviceImpls)
        throws IllegalArgumentException {
        Validate.notNull(serviceInterface, "ServiceInterface must be specified");
        Validate.notNullAndNoNullValues(serviceImpls, "ServiceImpls must be specified and can not contain null values");

        addAsServiceProvider(serviceInterface, serviceImpls);
        addClass(serviceInterface);
        return addClasses(serviceImpls);
    }

	@Override
	public ZArchive addAsConfiguration(ModuleConfiguration config) {
		File file = Configuration.writeModuleConfiguration(config);
        
        return addAsResource(file, Configuration.CONFIG_PATH);
	}
}
