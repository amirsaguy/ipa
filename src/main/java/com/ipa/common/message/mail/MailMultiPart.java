package com.ipa.common.message.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ipa.common.util.MessageUtil;

public class MailMultiPart extends MailBodyPart {
	
	protected ArrayList<MailBodyPart> parts;
	
	public MailMultiPart() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailMultiPart(String contentType) {
		super(contentType);
		// TODO Auto-generated constructor stub
	}

	public void addMailBodyPart(MailBodyPart bodyPart){
		if (parts == null){
			parts = new ArrayList<MailBodyPart>();
		}
		parts.add(bodyPart);
	}
	
	public void addMailBodyPart(int index, MailBodyPart bodyPart){
		if (parts == null){
			parts = new ArrayList<MailBodyPart>();
		}
		if (index <= parts.size()) {
			parts.add(index, bodyPart);
		}
	}
	
	public MailBodyPart getMailBodyPart(int index){
		if (parts == null || index >= parts.size()){
			return null;
		}
		return parts.get(index);
	}
	
	public boolean remove(MailBodyPart bodyPart){
		if (parts != null && bodyPart != null){
			return parts.remove(bodyPart);
		}else{
			return false;
		}
	}
	
	public void remove(int index){
		if (parts != null && index < parts.size()){
			parts.remove(index);
		}
	}
	
	public int getCount(){
		if (parts != null){
			return parts.size();
		}else{
			return 0;
		}
	}
	
	public String getMessageText() {
		MessageTextWrapper messageTextWrapper = getMessageTextWrapper();
		if (messageTextWrapper != null) {
			MailMultiPart mailMultiPart = messageTextWrapper.multiPart;
			MailBodyPart mailBodyPart = messageTextWrapper.bodyPart;
			if (mailMultiPart != null && mailBodyPart != null) {
				mailMultiPart.remove(mailBodyPart);
			}
		}
		return messageTextWrapper != null ? messageTextWrapper.messageText : null;
	}
	
	public MessageTextWrapper getMessageTextWrapper() {
		MessageTextWrapper messageTextWrapper = null;
		boolean needToUpdateMessageText = true;
		List<MailBodyPart> bps = parts.stream().sorted(new BodyPartComparator()).collect(Collectors.toList());
		for (MailBodyPart mailBodyPart : bps) {
			needToUpdateMessageText = messageTextWrapper == null || messageTextWrapper.needToUpdateMessageText();
			if (!needToUpdateMessageText) {
				break;
			}
			if (mailBodyPart instanceof MailMultiPart) {
				MailMultiPart mmp = (MailMultiPart)mailBodyPart;
				MessageTextWrapper mmpMessageTextWrapper = mmp.getMessageTextWrapper();
				if (mmpMessageTextWrapper != null) {
					if (mmpMessageTextWrapper.messageTextIsHtml) {
						return mmpMessageTextWrapper;
					}else if (messageTextWrapper == null){
						messageTextWrapper = mmpMessageTextWrapper;
					}
				}
			}else if (mailBodyPart.isMessageText() && mailBodyPart.getContent() instanceof String){
				MessageTextWrapper bodyPartMessageTextWrapper = mailBodyPart.getMessageTextWrapper();
				if (bodyPartMessageTextWrapper != null) {
					bodyPartMessageTextWrapper.multiPart = this;
					if (messageTextWrapper == null || bodyPartMessageTextWrapper.messageTextIsHtml) {
						messageTextWrapper = bodyPartMessageTextWrapper;
					}
				}
			}
		}
		return messageTextWrapper;
	}
	
	protected void normalize(boolean removePlainTextParts) {
		List<MailBodyPart> bps = parts.stream().collect(Collectors.toList());
		for (int i=0; i<bps.size(); i++) {
			MailBodyPart mailBodyPart = bps.get(i);
			String contentType = mailBodyPart.getContentType();
			if (mailBodyPart instanceof MailMultiPart) {
				MailMultiPart mailMultiPart = (MailMultiPart)mailBodyPart;
				mailMultiPart.normalize(removePlainTextParts);
				if (mailMultiPart.getCount() <= 1) {
					parts.remove(i);
					if (mailMultiPart.getCount() == 1) {
						parts.add(i, mailMultiPart.getMailBodyPart(0));
					}
				}
			}else if (removePlainTextParts && MessageUtil.isPlainTextMimeType(contentType)) {
				parts.remove(i);
			}
		}
	}

}
