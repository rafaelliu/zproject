package org.sbrubles.zcontainer.api.deployers;

import org.sbrubles.zcontainer.api.Container;

public abstract class Deployer {
	
	public abstract void init(Container container) throws Exception;

}
