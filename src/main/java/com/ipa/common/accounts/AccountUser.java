package com.ipa.common.accounts;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipa.common.messagingaccounts.MessagingAccount;

public class AccountUser implements Serializable, Cloneable {

	private static final long serialVersionUID = -8382440210184695006L;
	
	public static final int UNKNOWN_STATE = 0;
	public static final int VALID_STATE = 1;
	public static final int RESET_PASSWORD_STATE = 2;
	
	public static final String ADMIN_PERMISSION = "admin";
	public static final String ACCOUNT_ADMIN_PERMISSION = "accountadmin";
	public static final String ACCOUNT_USER_PERMISSION = "accountuser";
	
	protected String username;
	protected String email;
	protected String displayName;
	protected String password;
	protected String permission;
	protected ArrayList<MessagingAccount> messagingAccounts;

	protected String account;

	public AccountUser clone(){
		AccountUser result = null;
		try {
			result = (AccountUser)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@JsonIgnore
	public boolean isAdmin(){
		String policy = getPermission();
		return policy != null && policy.equalsIgnoreCase(ADMIN_PERMISSION);
	}
	
	@JsonIgnore
	public boolean isAccountAdmin(){
		String policy = getPermission();
		return policy != null && policy.equalsIgnoreCase(ACCOUNT_ADMIN_PERMISSION);
	}
	
	public boolean verifyPermission(){
		String policy = getPermission();
		return policy != null && (policy.equalsIgnoreCase(ACCOUNT_ADMIN_PERMISSION) || policy.equalsIgnoreCase(ADMIN_PERMISSION) || policy.equalsIgnoreCase(ACCOUNT_USER_PERMISSION));
	}
	
	public void addMessagingAccount(MessagingAccount messagingAccount){
		if (messagingAccounts == null){
			messagingAccounts = new ArrayList<MessagingAccount>();
		}else{
			for (int i=0; i<messagingAccounts.size(); i++) {
				MessagingAccount acc = messagingAccounts.get(i);
				if (acc != null && acc.getId().equals(messagingAccount.getId())){
					messagingAccounts.set(i, acc);
					return;
				}
			}
		}
		messagingAccounts.add(messagingAccount);
	}
	
	public MessagingAccount getMessagingAccount(String id){
		if (messagingAccounts != null){
			for (MessagingAccount messagingAccount : messagingAccounts) {
				if (messagingAccount.getId().equals(id)){
					return messagingAccount;
				}
			}
		}
		return null;
	}
	
	public MessagingAccount getMessagingAccountByName(String name){
		if (messagingAccounts != null){
			for (MessagingAccount messagingAccount : messagingAccounts) {
				if (messagingAccount.getName().equals(name)){
					return messagingAccount;
				}
			}
		}
		return null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}

	public ArrayList<MessagingAccount> getMessagingAccounts() {
		return messagingAccounts;
	}

	public void setMessagingAccounts(ArrayList<MessagingAccount> messagingAccounts) {
		this.messagingAccounts = messagingAccounts;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Override
	public String toString(){
		return "AccountUser {username=" + username + ", account=" + account + "}";
	}
	
}
