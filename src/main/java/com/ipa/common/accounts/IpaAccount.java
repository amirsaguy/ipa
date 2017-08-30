package com.ipa.common.accounts;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IpaAccount implements Cloneable{
	
	protected String accountType; //private, enterprise
	protected String name;
	protected String displayName;
	protected String description;
	protected long lastUpdateTime = -1;
	protected ArrayList<AccountUser> users = new ArrayList<AccountUser>();
	protected IpaAccountInfo info;
	
	public IpaAccount(){
		super();
	}
	
	public IpaAccount clone(){
		IpaAccount result = null;
		try {
			result = (IpaAccount)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	public void merge(IpaAccount account){
		if (account != null && account.getInfo() != null){
			if (info == null){
				info = new IpaAccountInfo();
			}
			info.merge(account.getInfo());
		}
	}
	
	public AccountUser getUser(String username){
		if (username == null || username.length() == 0) return null;
		if (users != null){
			for (int i = 0; i < users.size(); i++) {
				AccountUser user = users.get(i);
				if (user.getUsername() != null && username.equals(user.getUsername())){
					return user;
				}				
			}
		}
		return null;
	}
	
	public void addUser(AccountUser user){
		if (users == null){
			users = new ArrayList<AccountUser>();
		}else {
			for (int i = 0; i < users.size(); i++) {
				AccountUser u = users.get(i);
				if (u.getUsername() != null && user.getUsername().equals(u.getUsername())){
					users.set(i, user);
					return;
				}				
			}
		}
		
		users.add(user);
	}
	
	public void removeUser(String username){
		AccountUser user = getUser(username);
		if (user != null){
			users.remove(user);
		}
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@JsonIgnore
	public ArrayList<AccountUser> getUsers() {
		return users;
	}
	
	public void setUsers(ArrayList<AccountUser> users) {
		this.users = users;
	}

	public IpaAccountInfo getInfo() {
		return info;
	}

	public void setInfo(IpaAccountInfo info) {
		this.info = info;
	}
	
	@Override
	public String toString(){
		return "IpaAccount {name=" + name + "}";
	}
}
