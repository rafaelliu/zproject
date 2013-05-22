package org.sbrubles.zcontainer.impl.weld.beans;

import org.sbrubles.zcontainer.api.Container;

public class ZContainerContext {

	private Container container;

	public ZContainerContext() {
	}

	public ZContainerContext(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}

}
