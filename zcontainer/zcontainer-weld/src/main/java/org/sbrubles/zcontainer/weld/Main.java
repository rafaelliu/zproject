package org.sbrubles.zcontainer.weld;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WeldContainer initialize = new Weld().initialize();
		
	}

}
