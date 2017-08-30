package com.ipa.common.comm;

import java.io.Serializable;

import com.ipa.common.accounts.AccountUser;
import com.ipa.common.accounts.IpaAccount;

public class IpaClient implements Serializable{

	private static final long serialVersionUID = 3225988231213184838L;
	
	protected String token = null;
	protected AccountUser user = null;
	protected IpaAccount account = null;

	public IpaClient() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AccountUser getUser() {
		return user;
	}

	public void setUser(AccountUser user) {
		this.user = user;
	}

	public IpaAccount getAccount() {
		return account;
	}

	public void setAccount(IpaAccount account) {
		this.account = account;
	}
	
	

}
