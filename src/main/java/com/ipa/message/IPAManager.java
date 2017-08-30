package com.ipa.message;

public class IPAManager {
	
	private static IPAManager current = null;
	
	public static IPAManager getInstance() {
		if (current == null) {
			synchronized (IPAManager.class) {
				if (current == null) {
					current = new IPAManager();
				}
			}
		}
		return current;
	}
	
	private IPAManager() {
	}

}
