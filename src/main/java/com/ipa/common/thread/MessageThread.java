package com.ipa.common.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.ipa.common.Tag;

public class MessageThread {
	
	protected String id;
	protected String name;
	protected String description;
	protected String key;
	protected Set<String> participants;
	protected Set<String> messagingAccounts;
	protected Set<Tag> tags;
	protected String parentThreadId;
	protected ArrayList<MessageThread> subThreads;
	protected long firstMessage;
	protected long lastMessage;
	
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

	public Set<String> getParticipants() {
		return participants != null ? new HashSet<String>(participants) : null;
	}

	public void setParticipants(Set<String> participants) {
		this.participants = participants;
	}

	public Set<String> getMessagingAccounts() {
		return messagingAccounts != null ? new HashSet<String>(messagingAccounts) : null;
	}
	
	public void setMessagingAccounts(Set<String> messagingAccounts) {
		this.messagingAccounts = messagingAccounts;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public String getParentThreadId() {
		return parentThreadId;
	}

	public void setParentThreadId(String parentThreadId) {
		this.parentThreadId = parentThreadId;
	}

	public ArrayList<MessageThread> getSubThreads() {
		return subThreads;
	}
	
	public void setSubThreads(ArrayList<MessageThread> subThreads) {
		this.subThreads = subThreads;
	}

	public long getFirstMessage() {
		return firstMessage;
	}

	public void setFirstMessage(long firstMessage) {
		this.firstMessage = firstMessage;
	}

	public long getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(long lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	

}
