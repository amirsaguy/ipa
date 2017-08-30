package com.ipa.common.messagingaccounts.mail;

import java.util.HashMap;

import com.ipa.common.messagingaccounts.MessagingAccountInfo;

public class MailAccountInfo extends MessagingAccountInfo{
	
	protected HashMap<String, Long> lastRetrievedUIDs;

	public HashMap<String, Long> getLastRetrievedUIDs() {
		return lastRetrievedUIDs;
	}

	public void setLastRetrievedUIDs(HashMap<String, Long> lastRetrievedUIDs) {
		this.lastRetrievedUIDs = lastRetrievedUIDs;
	}

}
