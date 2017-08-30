package com.ipa.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipa.common.accounts.AccountUser;
import com.ipa.common.accounts.IpaAccount;
import com.ipa.common.accounts.IpaAccountInfo;
import com.ipa.common.comm.IpaClient;
import com.ipa.common.errors.UnauthorizedAppException;
import com.ipa.common.util.MailTransporter;

public class SiteAdapter {
	
	protected Logger log = LogManager.getLogger(this.getClass());
	protected Logger auditLog = LogManager.getLogger("audit");
	
	protected Properties initProp = new Properties();
	protected Properties configProp = new Properties();
	protected File configPath = null;
	protected HashMap<String, IpaAccount> permissionsMap = new HashMap<String, IpaAccount>();
	protected HashMap<String, AccountUser> users = new HashMap<String, AccountUser>();
	protected HashMap<String, IpaAccount> accounts = new HashMap<String, IpaAccount>();
	protected SessionManager sessionMgr = null;
	protected Object adminLock = new Object();
	//protected XpoLogLicense defaultLicense;
	protected SimpleDateFormat licenseExpirationFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected SimpleDateFormat licenseCreatedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected MessageDigest digest;
	protected MailTransporter mailTransporter = null;
	
	public ExecutorService generalCacheThreadPool = Executors.newCachedThreadPool();
	public ExecutorService admimistrationCacheThreadPool = Executors.newFixedThreadPool(2);
	
	protected Random RANDOM = new SecureRandom();
	public int PASSWORD_LENGTH = 8;
	
	protected long lastUpdate = -1;
	
	public void init(Properties prop) throws Exception {
		this.initProp = prop;
		String configPathStr = prop.getProperty("config.path");
		if (configPathStr != null && configPathStr.length() > 0){
			File f = new File(configPathStr).getAbsoluteFile();
			if (f.exists()){
				configPath = f;				
				File config = new File(f,"general/adapter.defaults.prop");
				if (config.exists()){
					FileInputStream in = null;
					try {
						in = new FileInputStream(config);
						configProp.load(in);
					}catch (Exception e){
						log.error("Failed to load config properties " + config,e);
					}finally {
						try {
							if (in != null){
								in.close();
							}
						}catch (Exception e){}
					}					
				}
				config = new File(f,"general/adapter.prop");
				if (config.exists()){
					FileInputStream in = null;
					try {
						in = new FileInputStream(config);
						configProp.load(in);
					}catch (Exception e){
						log.error("Failed to load config properties " + config,e);
					}finally {
						try {
							if (in != null){
								in.close();
							}
						}catch (Exception e){}
					}					
				}

			}

		}	
		initBasic();
		initSessionManager();
		
	}

	public String getBuild(){
		return configPath == null ? "" : configProp.getProperty("build","");
	}

	protected ObjectMapper createObjectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.enableDefaultTyping();
		return mapper;
	}
	
	protected void initFinal(){
		lastUpdate = System.currentTimeMillis();
	}
	
	protected void initBasic(){
		try {
			digest = MessageDigest.getInstance(configProp.getProperty("password.algorithm", "SHA-1"));
		}catch (NoSuchAlgorithmException e) {
			log.error("Failed to create digest ",e);
		} 
		mailTransporter = new MailTransporter(configProp);
		PASSWORD_LENGTH = Integer.parseInt(configProp.getProperty("password.temp.length", "8"));
	}

	protected void initSessionManager(){
		sessionMgr = new SessionManager();
		sessionMgr.init(configProp);
	}
	
	public List<String> getAllAccounts(){
		if (accounts != null){
			List<IpaAccount> list = new ArrayList<IpaAccount>(accounts.values());
			List<String> namesList = list.stream().map(account -> account.getName()).sorted((String s1, String s2) -> s1.compareTo(s2)).collect(Collectors.toList());
			return namesList;
		}
		return null;
	}
	
	public List<String> getAllUsers(){
		if (users != null){
			List<AccountUser> list = new ArrayList<AccountUser>(users.values());
			List<String> namesList = list.stream().map(user -> user.getUsername()).sorted((String s1, String s2) -> s1.compareTo(s2)).collect(Collectors.toList());
			return namesList;
		}
		return null;
	}
	
	public IpaAccount getAccount(AccountUser user, String name) throws Exception{
		if (!user.isAdmin()){
			String userAccountId = user.getAccount();
			boolean found = false;

			if (userAccountId != null && userAccountId.equals(name)){
				found = true;
			}
			if (!found){
				throw new UnauthorizedAppException();
			}
		}
		IpaAccount result =  accounts.get(name);
		try {
			if (result != null){
				result = result.clone();
				if (user != null){
					IpaAccount acc = permissionsMap.get("account");
					if (acc != null){
						result.merge(acc);
					}

					if (user.getPermission() != null){
						acc = permissionsMap.get(user.getPermission());
						if (acc != null){
							result.merge(acc);
						}
					}
				}
			}
		}catch (Exception e){
			log.error("Failed to clone account " + name,e);
			return null;
		}
		return result;
	}
	
	protected void addAccount(IpaAccount account){
		if (account.getName() != null && account.getName().length() > 0){
			accounts.put(account.getName(), account);					
		}
	}

	public void addAccount(IpaClient client,IpaAccount account) throws Exception {		
		if (!verifyAdminPermissions(client)){
			return;
		}
		synchronized (adminLock) {			
			audit(new AdapterAudit(client.getUser(), account, AdapterAudit.ADD_ACTION_TYPE,account), "add account");
			if (saveAccount(account)){
				loadAccount(account.getName());
			}
		}
	}
	
	protected boolean saveAccount(IpaAccount account) throws Exception {
		return true;
	}

	protected IpaAccount loadAccount(String name) throws Exception {
		return null;
	}

	protected boolean adapterAddAcount(IpaAccount account) throws Exception{
		return true;
	}

	protected void updateAccountBasicInfo(IpaAccount account){
		if (account.getDisplayName() != null && account.getDisplayName().length() > 0){
			IpaAccountInfo info = account.getInfo(); 
			if (account.getInfo() == null){
				info = new IpaAccountInfo();
				account.setInfo(info);
			}
			info.addValue("accountName", account.getDisplayName());			
		}
	}protected void addUser(AccountUser user){
		if (user.getUsername() != null && user.getUsername().length() > 0){
			users.put(user.getUsername(), user);
		}
	}

	
	
	public void removeAccount(IpaClient client,String accountName) throws Exception {		
		if (!verifyAdminPermissions(client)){
			return;
		}
		synchronized (adminLock) {
			audit(new AdapterAudit(client.getUser(), accounts.get(accountName), AdapterAudit.DELETE_ACTION_TYPE,accounts.get(accountName)), "remove account " + accountName);
			removeAccount(accountName);
		}
	}

	protected boolean removeAccount(String accountName) throws Exception {
		accounts.remove(accountName);
		return true;
	}
	
	

	public void audit(AdapterAudit audit,String message){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(audit.getUser() == null ? "-" : audit.getUser().getUsername()).append("]");
		buffer.append(" [").append(audit.getUser() == null ? "-" : audit.getUser().getDisplayName()).append("]");
		buffer.append(" [").append(audit.getAccount() == null ? "-" : audit.getAccount().getName()).append("]");
		buffer.append(" [").append(audit.getAccount() == null ? "-" : audit.getAccount().getDisplayName()).append("]");
		buffer.append(" [").append(audit.getActionString()).append("]");
		buffer.append(" [").append(getAuditSourceString(audit.getActionSource())).append("]");
		buffer.append(" ").append(message);
		auditLog.info(buffer.toString());
	}
	
	protected String getAuditSourceString(Object source){
		if (source instanceof AccountUser){
			ObjectMapper mapper = createObjectMapper();
			try {
				return mapper.writeValueAsString(source);
			} catch (Exception e) {
				log.error("Failed to write value as json " + source,e);
			}
		}
		return "-";
	}
	
	public void checkForUpdate(){
	}
	
	public void dispose(){
	}
	
	public AccountUser getUser(String username){
		AccountUser result =  users.get(username);
		try {
			if (result != null){
				result = result.clone();
			}
		}catch (Exception e){
			log.error("Failed to clone user " + username,e);
			return null;
		}
		return result;
	}

	protected boolean verifyAdminPermissions(IpaClient client) throws Exception {
		if (client == null || client.getUser() == null || !client.getUser().isAdmin()){
			throw new UnauthorizedAppException();
		}
		return true;
	}
	
	protected boolean verifyAccountAdminPermissions(IpaClient client) throws Exception {
		if (client == null || client.getUser() == null || !(client.getUser().isAccountAdmin() || client.getUser().isAdmin())){
			throw new UnauthorizedAppException();
		}
		return true;
	}
	
	protected HashSet getAccountInfoKeys(JSONObject data){
		HashSet<String> set = new HashSet<String>();
		if (data != null){
			JSONArray accountInfoArr = (JSONArray)data.get("uisettings");
			if (accountInfoArr != null){				
				for (int i = 0; i < accountInfoArr.size(); i++) {
					JSONObject arrObj = (JSONObject)accountInfoArr.get(i);
					String groupName = (String)arrObj.get("name");
					if (groupName != null){
						JSONArray groupInfoArr = (JSONArray)arrObj.get("data");

						for (int j = 0; j < groupInfoArr.size(); j++) {
							JSONObject groupArrObj = (JSONObject)groupInfoArr.get(i);
							String itemName = (String)groupArrObj.get("name");
							if (itemName != null){
								set.add(itemName);
							}
						}
					}
				}
			}
		}
		return set;
	}

	public IpaAccount updateAccountInfo(AccountUser user, String name, IpaAccountInfo info) throws Exception{
		audit(new AdapterAudit(user,accounts.get(name), AdapterAudit.UPDATE_ACTION_TYPE,info), "update account info");
		IpaAccount account = getAccount(user, name);
		if (account != null){
			synchronized (adminLock) {					
				IpaAccountInfo currentInfo = account.getInfo();
				if (currentInfo == null){
					currentInfo = new IpaAccountInfo();
					account.setInfo(currentInfo);
				}
				if (info != null){
					currentInfo.updateValues(info);
					String displayName = (String)currentInfo.getValue("accountName");
					if (displayName != null && displayName.length() > 0){
						account.setDisplayName(displayName);						
					}
					
					audit(new AdapterAudit(user, account, AdapterAudit.UPDATE_ACTION_TYPE,info), "update account info");
					if (saveAccount(account)){
						loadAccount(account.getName());
						account = getAccount(user,account.getName());
					}
				}					

			}
		}

		return account;
	}

}
