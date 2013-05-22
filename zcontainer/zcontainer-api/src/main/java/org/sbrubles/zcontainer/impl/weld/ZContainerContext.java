package org.sbrubles.zcontainer.impl.weld;

import org.sbrubles.zcontainer.api.Container;

public class ZContainerContext {

	private Container container;

	public ZContainerContext(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}

}
