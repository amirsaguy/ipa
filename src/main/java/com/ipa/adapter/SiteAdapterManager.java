package com.ipa.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class SiteAdapterManager {
	
	private static SiteAdapterManager current;
	
	protected SiteAdapter siteAdapter;
	protected UpdateThread checkForUpdate = null;
	
	public static SiteAdapterManager getInstance(){
		if (current == null){
			synchronized (SiteAdapterManager.class){
				if (current == null){
					SiteAdapterManager temp = new SiteAdapterManager();
					temp.init();
					if (temp.siteAdapter != null){
						current = temp;
					}
				}
			}
		}
		return current;
	}
	
	private SiteAdapterManager(){
		
	}
	
	protected void init(){
		File f = new File("siteInit.prop");
		String configPath = System.getProperty("ipa.site.config.path");
		if (configPath != null){
			f = new File(configPath).getAbsoluteFile();			
		}
		if (!f.exists()){
			System.err.println("Failed to init config path " + f +  " with system attribute " + configPath);
			return;
		}
		FileInputStream in = null;
		try {
			Properties prop = new Properties();
			in = new FileInputStream(f);
			prop.load(in);
			String type = prop.getProperty("adapterType");
			if (type != null && type.trim().length() > 0){
				type = type.trim();				
				SiteAdapter adapter = null;
				if (type.equalsIgnoreCase("file")){
					adapter = new SiteFileAdapter();
				}else if (type.equalsIgnoreCase("db")){
					adapter = new SiteDBAdapter();
				} 
				if (adapter != null){
					adapter.init(prop);
				}else {
					throw new Exception("No adapter was created");
				}
				if (adapter != null){
					siteAdapter = adapter;
				}
			}
		}catch (Exception e){
			System.err.println("Failed to init config path " + f +  " with system attribute " + configPath);
			e.printStackTrace();
		}finally {
			try {
				in.close();
			}catch (Exception e){}
		}
		checkForUpdate = new UpdateThread();
		checkForUpdate.start();
		
	}
	
	public static void dispose() throws Exception {
		synchronized(SiteAdapterManager.class){
			if (current != null && current.siteAdapter != null){
				if (current.checkForUpdate != null){
					current.checkForUpdate.setThreadCanceled(true);
				}
				current.siteAdapter.dispose();
				
			}
		}
	}
	
	private class UpdateThread extends Thread{
		
		protected volatile boolean isThreadCanceled = false;
		
		public UpdateThread(){}
		

		public boolean isThreadCanceled() {
			return isThreadCanceled;
		}

		public void setThreadCanceled(boolean isThreadCanceled) {
			this.isThreadCanceled = isThreadCanceled;
		}


		public void run() {
			while (!isThreadCanceled()){
				try {
					Thread.sleep(60000);
					SiteAdapter adapter = current == null ? null : current.siteAdapter;
					if (adapter != null){
						adapter.checkForUpdate();
					}
				}catch (Throwable e){
					
				}
			}
		}
		
		
	}

	public SiteAdapter getSiteAdapter() {
		return siteAdapter;
	}

	public void setSiteAdapter(SiteAdapter siteAdapter) {
		this.siteAdapter = siteAdapter;
	}


}
