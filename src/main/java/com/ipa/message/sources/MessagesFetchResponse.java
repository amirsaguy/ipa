package com.ipa.message.sources;

import java.util.ArrayList;

import com.ipa.common.message.Message;

public class MessagesFetchResponse {
	
	protected String status;
	protected ArrayList<Message> messages;
	
	public void addMessageToResponse(Message message) {
		if (messages == null) {
			messages = new ArrayList<Message>();
		}
		messages.add(message);
	}
	
	public int size() {
		if (messages != null) {
			return messages.size();
		}else {
			return 0;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
	
	

}
