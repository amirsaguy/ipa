package com.ipa.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipa.common.accounts.AccountUser;
import com.ipa.common.accounts.IpaAccount;

public class SiteFileAdapter extends SiteAdapter {
	
	protected File rootPath = null;

	public SiteFileAdapter(){
	}
	
	public void init(Properties prop) throws Exception {
		super.init(prop);
		
		String rootPathStr = prop.getProperty("root.path");
		if (rootPathStr != null && rootPathStr.length() > 0){
			File f = new File(rootPathStr).getAbsoluteFile();
			if (f.exists()){
				rootPath = f;
			}else {
				throw new Exception("Invalid root.path " + f);
			}
			
		}
		if (rootPath == null){
			throw new Exception("Missing valid root.path " + rootPathStr);
		}
		initPath();
		initFinal();
	}
	
	protected void initPath() throws Exception{
		File usersDir = new File(rootPath,"users").getAbsoluteFile();
		if (usersDir.exists()){
			File [] files = usersDir.listFiles();
			if (files != null){
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					readUsers(file, null);
				}
			}
		}
		
		File accountsDir = new File(rootPath,"accounts").getAbsoluteFile();
		if (accountsDir.exists()){
			File [] files = accountsDir.listFiles();
			if (files != null){
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory()){
						readAccount(file);
					}
				}
			}
		}	
		
	}
	
	protected IpaAccount readAccount(File dir) throws Exception{
		File f= new File(dir,"account.json");
		IpaAccount result = null;
		ObjectMapper  mapper = createObjectMapper();
		try {
			IpaAccount account = mapper.readValue(f, IpaAccount.class);

			if (account != null && account.getName() != null && account.getName().length() > 0){
				result = account;
				updateAccountBasicInfo(account);
				addAccount(account);				
				readUsers(new File(dir,"users.json"), account);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	protected void readUsers(File file,IpaAccount account) throws Exception{
		readUsers(file, account, null);
	}
	
	protected void readUsers(File file, IpaAccount account, String userName) throws Exception{
		if (file == null || !file.exists()) return;
		ObjectMapper mapper = createObjectMapper();
		try {
			
			ArrayList<AccountUser> users = mapper.readValue(file, ArrayList.class);
			if (users != null && users.size() > 0){
				for (int i = 0; i < users.size(); i++) {
					AccountUser accountUser = (AccountUser) users.get(i);
					if (userName != null && !userName.equals(accountUser.getUsername())){
						continue;
					}
					if (account != null){
						accountUser.setAccount(account.getName());
						account.addUser(accountUser);
					}
					addUser(accountUser);
				}
			}	
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
