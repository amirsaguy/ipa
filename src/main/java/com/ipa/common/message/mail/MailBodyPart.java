package com.ipa.common.message.mail;

public class MailBodyPart {
	protected String contentType = null;
	protected Object content = null;
	protected boolean isMessageText = false;
	protected boolean isAttachment = false;
	
	public MailBodyPart() {
		super();
	}

	public MailBodyPart(String contentType) {
		super();
		this.contentType = contentType;
	}

	public MailBodyPart(String contentType, String content) {
		super();
		this.contentType = contentType;
		this.content = content;
	}
	
	public String getMessageText() {
		MessageTextWrapper wrapper = getMessageTextWrapper();
		return wrapper != null ? wrapper.messageText : null;
	}
	
	public MessageTextWrapper getMessageTextWrapper() {
		if (isMessageText() && getContent() instanceof String){
			String text = (String)getContent();
			MessageTextWrapper messageTextWrapper = new MessageTextWrapper(text, isMessageText());
			messageTextWrapper.bodyPart = this;
			return messageTextWrapper;
		}else {
			return null;
		}
	}
	
	public static class MessageTextWrapper {
		MailMultiPart multiPart = null;
		MailBodyPart bodyPart = null;
		String messageText = null;
		boolean messageTextIsHtml = false;
		
		public MessageTextWrapper(String messageText, boolean messageTextIsHtml) {
			this.messageText = messageText;
			this.messageTextIsHtml = messageTextIsHtml;
		}
		
		public boolean needToUpdateMessageText() {
			return messageText == null || !messageTextIsHtml;
		}
		
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public Object getContent() {
		return content;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}

	public boolean isMessageText() {
		return isMessageText;
	}

	public void setMessageText(boolean isMessageText) {
		this.isMessageText = isMessageText;
	}

	public boolean isAttachment() {
		return isAttachment;
	}

	public void setAttachment(boolean isAttachment) {
		this.isAttachment = isAttachment;
	}
	
}
