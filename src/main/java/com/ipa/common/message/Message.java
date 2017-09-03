package com.ipa.common.message;

import java.util.Set;

import org.springframework.data.annotation.Id;

import com.ipa.common.Tag;
import com.ipa.common.thread.MessageThread;

public class Message {
	
	protected String type; //email, slack, whatsapp, voice
	protected String id;
	protected String sourceId; //id of user account
	protected long timestamp;
	protected String sender;
	protected String recipient;
	protected String message;
	protected byte direction; //incoming, outgoing
	protected String priority;
	protected String status; //sent, read, rejected
	protected String threadId;
	protected Set<Tag> tags;
	
	public double matchThread(MessageThread messageThread) {
		return -1;
	}
	
	public String generateMessageKey() {
		return null;
	}
	
	public MessageThread createMessageThread() {
		return null;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Id
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSourceId() {
		return sourceId;
	}
	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public byte getDirection() {
		return direction;
	}
	
	public void setDirection(byte direction) {
		this.direction = direction;
	}
	
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getThreadId() {
		return threadId;
	}
	
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
}
