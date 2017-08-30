package com.ipa.adapter;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionManager{
	
	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	
	protected int TIME_OUT = 30*60*1000;
	protected int SLEEP_TIME_OUT = 60*1000;
	protected Hashtable hash = new Hashtable();
	protected boolean debug = false;
	protected SessionCheckerThread cleanThread = null;
	
	public SessionManager() {
		
		
	}
	
	public void init(Properties prop){
		TIME_OUT = Integer.parseInt(prop.getProperty("adapter.session.mgr.timeout","1800000"));
		SLEEP_TIME_OUT = Integer.parseInt(prop.getProperty("adapter.session.mgr.sleeptimeout","60000"));
		debug = prop.getProperty("adapter.session.mgr.debug","false").equalsIgnoreCase("true");
		cleanThread = new SessionCheckerThread();
		cleanThread.start();
	}
	

	public boolean unregisterSession(String key){
		if(key != null && hash.get(key) != null) {
			hash.remove(key);
			if (isDebug()){
				writeDebug("unregister session " + key + " , " + hash.size());
			}
			return true;
		}
		if (isDebug()){
			writeDebug("unregister no session " + key + " , " + hash.size());
		}
		return false;
	}
	
	public MessageSession getSession(String key){
		MessageSession ses = (MessageSession)hash.get(key);
		if(ses != null) {
			ses.setLastAccessTime();
		}
		return ses;
	}
	
	

	
	protected boolean isDebug(){
		return debug && log.isLoggable(Level.FINE);
	}
	
	protected void writeDebug(String message){
		if (log.isLoggable(Level.FINE)){
			log.log(Level.FINE,message);
		}
	}
	
	public void checkForUpdate(){
		if (TIME_OUT > 0){
			clearObjects();
		}
	}
	
	public void clearObjects() {		
		ArrayList<String> removingSessions = new ArrayList<String>();
		ArrayList<MessageSession> disposeSessions = new ArrayList<MessageSession>();
		synchronized (hash) {
			Enumeration enumeration = hash.keys();
			String el = null;
			MessageSession ses = null;
			long now = System.currentTimeMillis();
			while(enumeration.hasMoreElements()) {
				el = (String)enumeration.nextElement();
				ses = (MessageSession)hash.get(el);
				long timeout = TIME_OUT;
				MessageSession serverSes = null;
				if (ses instanceof MessageSession){
					serverSes = (MessageSession)ses;
					if (serverSes.getTimeout() > 0){
						timeout = serverSes.getTimeout();
					}
				}
				if(ses.getLastAccessTime() > 0 && (now - ses.getLastAccessTime()) > timeout) {
					removingSessions.add(el);
					if (serverSes != null){
						disposeSessions.add(serverSes);
					}
				}
			}
		}
		for (int i = 0; i < removingSessions.size(); i++) {
			hash.remove(removingSessions.get(i));			
		}
		for (int i = 0; i < disposeSessions.size(); i++) {
			disposeSessions.get(i).dispose();
		}
		if (isDebug()){
			writeDebug("clearObjects " + removingSessions.size() + " , " + hash.size());
		}
	}
	
	public String registerSession(MessageSession ses){
		String key = ses.getId() == null || ses.getId().length() == 0 ? new UID().toString() : ses.getId();
		if(key != null) {
			ses.setId(key);
			hash.put(key,ses);
			if (isDebug()){
				writeDebug("register session " + key + " , " + hash.size());
			}
			return key;
		}
		return null;
	}

	
	class SessionCheckerThread extends Thread {
		
		public SessionCheckerThread(){
			super("SessionServerChecker");
		}
		
		public void run(){
			while (true){
				try {
					try {
						Thread.sleep(SLEEP_TIME_OUT);
					} catch (InterruptedException e) {
						return;
					}
					checkForUpdate();
				}catch (Throwable e){
					log.log(Level.SEVERE,"Failed to clear sessions objects");
				}
				
			}
		}
	}
	
}

