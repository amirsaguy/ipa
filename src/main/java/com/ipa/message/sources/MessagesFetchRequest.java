package com.ipa.message.sources;

import com.ipa.common.accounts.AccountUser;
import com.ipa.common.messagingaccounts.MessagingAccount;
import com.ipa.common.messagingaccounts.MessagingAccountInfo;

public class MessagesFetchRequest {
	
	protected AccountUser user;
	protected MessagingAccount account;
	protected long minTime = -1;
	protected int maxNumberOfMessages = -1;
	
	public void updateFromMessagingAccount(MessagingAccount messagingAccount) {
	}
	
	public void updateFromMessagingAccountInfo(MessagingAccountInfo info) {
	}
	
	public AccountUser getUser() {
		return user;
	}
	
	public void setUser(AccountUser user) {
		this.user = user;
	}
	
	public MessagingAccount getAccount() {
		return account;
	}
	
	public void setAccount(MessagingAccount account) {
		this.account = account;
	}
	
	public long getMinTime() {
		return minTime;
	}
	
	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}
	
	public int getMaxNumberOfMessages() {
		return maxNumberOfMessages;
	}
	
	public void setMaxNumberOfMessages(int maxNumberOfMessages) {
		this.maxNumberOfMessages = maxNumberOfMessages;
	}

}
