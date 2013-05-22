package org.sbrubles.zcontainer.api.deployers;

import org.sbrubles.zcontainer.api.Container;

public abstract class Deployer {
	
	public abstract void init(Container container) throws Exception;
	
	protected ClassLoader getClassloader() {
		ClassLoader ccl = Thread.currentThread().getContextClassLoader();

		return ( ccl != null ? ccl : getClass().getClassLoader() );
	}

}
