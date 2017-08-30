package com.ipa.common.message.mail;

public class MailMessageWrapper extends MailMessage{
	
	protected String mailFolder;
	protected long messageUID;
	protected MailMessage mailMessage;
	
	public String getMailFolder() {
		return mailFolder;
	}
	
	public void setMailFolder(String mailFolder) {
		this.mailFolder = mailFolder;
	}
	
	public long getMessageUID() {
		return messageUID;
	}
	
	public void setMessageUID(long messageUID) {
		this.messageUID = messageUID;
	}

	public MailMessage getMailMessage() {
		return mailMessage;
	}

	public void setMailMessage(MailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

}
