package org.sbrubles.zcontainer.impl.weld;

public class ZService<T> {
	
	private T obj;
	
	public ZService(T obj) {
		this.obj = obj;
	}

	public T get() {
		return obj;
	}

}
