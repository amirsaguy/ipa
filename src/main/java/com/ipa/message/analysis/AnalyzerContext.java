package com.ipa.message.analysis;

import java.util.HashMap;

import com.ipa.common.accounts.AccountUser;
import com.ipa.common.thread.MessageThread;

public class AnalyzerContext {
	
	protected AccountUser user;
	protected HashMap<String, MessageThread> userThreads;
	
	public AccountUser getUser() {
		return user;
	}
	
	public void setUser(AccountUser user) {
		this.user = user;
	}
	
	public HashMap<String, MessageThread> getUserThreads() {
		return userThreads;
	}
	
	public void setUserThreads(HashMap<String, MessageThread> userThreads) {
		this.userThreads = userThreads;
	}
	
	

}
