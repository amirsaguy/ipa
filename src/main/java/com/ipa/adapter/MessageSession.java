package com.ipa.adapter;

import java.util.Hashtable;


public class MessageSession {
	
	protected Hashtable hash = null;
	protected long creationTime;
	protected long lastAccessTime;
	protected String Id;
	protected long timeout = -1;

	public MessageSession() {
		hash = new Hashtable();
		setCreationTime(System.currentTimeMillis());
		setLastAccessTime();
	}
	
	public boolean registerObject(String key, Object val){
		if(key != null) {
			hash.put(key,val);
			return true;
		}
		return false;
	}
	
	public boolean unregisterObject(String key){
		if(key != null && hash.get(key) != null) {
			hash.remove(key);
			return true;
		}
		return false;
	}
	
	public Object getObject(String key){
		return hash.get(key);
	}

	public long getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public long getLastAccessTime() {
		return lastAccessTime;
	}
	
	public void setLastAccessTime() {
		lastAccessTime = System.currentTimeMillis();
	}
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public void dispose(){
		
	}

}

