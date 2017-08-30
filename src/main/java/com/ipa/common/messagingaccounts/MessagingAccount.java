package com.ipa.common.messagingaccounts;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipa.common.messagingaccounts.mail.MailAccountInfo;

public abstract class MessagingAccount {
	
	protected static final Logger log = Logger.getLogger(MessagingAccount.class);
			
	protected String username;
	protected String id;
	protected String name;
	protected String description;
	protected boolean editable = true;
	protected boolean systemAccount = false;
	protected boolean enabled = true;
	protected String adminMail; //use for notifications (connection errors etc.)
	
	protected MailAccountInfo accountInfo;
	
	public abstract String getType();
	
	public String toJsonString(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public MailAccountInfo getMailAccountInfo(){
		if (accountInfo instanceof MailAccountInfo){
			return (MailAccountInfo)accountInfo;
		}
		return null;
	}

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
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isSystemAccount() {
		return systemAccount;
	}
	
	public void setSystemAccount(boolean systemAccount) {
		this.systemAccount = systemAccount;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getAdminMail() {
		return adminMail;
	}
	
	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}
	
	public MailAccountInfo getAccountInfo() {
		return accountInfo;
	}
	
	public void setAccountInfo(MailAccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	
	@Override
	public String toString(){
		return "MessagingAccount {type=" + getType() + ", name=" + name + "}";
	}
	
}
