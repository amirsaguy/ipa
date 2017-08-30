package com.ipa.common.thread;

import java.util.Set;

import com.ipa.common.Tag;

public class MessageThreadInfo {
	
	protected String id;
	protected String name;
	protected String description;
	protected String key;
	protected String messagingAccount;
	protected Set<Tag> tags;
	protected String parentThread;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getMessagingAccount() {
		return messagingAccount;
	}
	
	public void setMessagingAccount(String messagingAccount) {
		this.messagingAccount = messagingAccount;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public String getParentThread() {
		return parentThread;
	}
	
	public void setParentThread(String parentThread) {
		this.parentThread = parentThread;
	}
	
}
