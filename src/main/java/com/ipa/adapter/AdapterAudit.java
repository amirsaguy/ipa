package com.ipa.adapter;

import com.ipa.common.accounts.AccountUser;
import com.ipa.common.accounts.IpaAccount;

public class AdapterAudit {
	
	public static final int GENERAL_ACTION_TYPE = 1;
	public static final int ADD_ACTION_TYPE = 2;
	public static final int DELETE_ACTION_TYPE = 3;
	public static final int UPDATE_ACTION_TYPE = 4;
	public static final int LOGIN_ACTION_TYPE = 5;
	public static final int LOGOUT_ACTION_TYPE = 6;
	public static final int GET_ACTION_TYPE = 7;
	
		

	protected AccountUser user = null;
	protected IpaAccount account = null;
	protected Object actionSource = null;
	protected int actionType = 0;

	public AdapterAudit() {
	}
	
	public AdapterAudit(AccountUser user, IpaAccount account, int actionType) {
		super();
		this.user = user;
		this.account = account;
		this.actionType = actionType;
	}
	
	public AdapterAudit(AccountUser user, IpaAccount account, int actionType,Object actionSource) {
		super();
		this.user = user;
		this.account = account;
		this.actionSource = actionSource;
		this.actionType = actionType;
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

	public Object getActionSource() {
		return actionSource;
	}

	public void setActionSource(Object actionSource) {
		this.actionSource = actionSource;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	
	public String getActionString(){
		switch(getActionType()){
		case 0:
			return "-";
		case GENERAL_ACTION_TYPE:
			return "GENERAL";
		case ADD_ACTION_TYPE:
			return "ADD";
		case DELETE_ACTION_TYPE:
			return "DELETE";
		case UPDATE_ACTION_TYPE:
			return "UPDATE";
		case LOGIN_ACTION_TYPE:
			return "LOGIN";
		case LOGOUT_ACTION_TYPE:
			return "LOGOUT";
		case GET_ACTION_TYPE:
			return "GET";
		}
		return "UNKNOWN";
	}

}
