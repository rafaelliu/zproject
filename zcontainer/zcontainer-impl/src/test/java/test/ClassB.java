package test;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassB {
	
	public ClassLoader getActualClassloader() {
		return this.getClass().getClassLoader();
	}

	private int i = 0;
	public int inc() {
		return i ++;
	}

}
