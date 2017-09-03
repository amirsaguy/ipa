package com.ipa.common.message.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Transient;

import com.ipa.common.message.Message;
import com.ipa.common.thread.MessageThread;
import com.ipa.common.util.MessageConstants;

public class MailMessage extends Message {
	
	protected String messageId;
	protected String subject;
	@Transient
	protected Object content;
	protected String ccs;
	protected String bccs;
	protected List<MailAttachment> attachments;
	
	public void updateMessage() {
		String messageText = null;
		if (content instanceof MailMultiPart) {
			MailMultiPart mailMultiPart = (MailMultiPart)content;
			messageText = mailMultiPart.getMessageText();
			if (messageText != null) {
				setMessage(messageText);
			}
		}else if (content instanceof MailBodyPart) {
			MailBodyPart mailBodyPart = (MailBodyPart)content;
			messageText = mailBodyPart.getMessageText();
			if (messageText != null) {
				content = messageText;
			}
		}
		normalize(messageText != null);
	}
	
	public void normalize(boolean removeTextParts) {
		if (content instanceof MailMultiPart) {
			MailMultiPart mailMultiPart = (MailMultiPart)content;
			mailMultiPart.normalize(removeTextParts);
			if (mailMultiPart.getCount() == 0) {
				content = null;
			}else if (mailMultiPart.getCount() == 1){
				content = mailMultiPart.getMailBodyPart(0);
			}
		}
	}
	
	public String generateMessageKey() {
		StringBuilder sb = new StringBuilder();
		//Add subject
		String token = getCleanSubject();
		if (token == null || token.length() == 0) {
			token = "-";
		}
		sb.append(token);
		sb.append(MessageConstants.Common.KEY_SEP);
		List<String> list = getParticipantsList(true, true);
		
		String participants = String.join(",", list);
		sb.append(participants);
		
		return null;
	}
	
	public List<String> getParticipantsList(boolean includeSender, boolean sort){
		List<String> list = new ArrayList<String>();
		//Add participants
		HashSet<String> set = new HashSet<String>(); 
		if (includeSender) {
			String sender = getSender();
			if (sender != null) {
				list.add(sender);
				set.add(sender);
			}
		}
		if (recipient != null && recipient.length() > 0) {
			list.addAll(Arrays.stream(recipient.split(",")).filter(p -> !set.contains(p) && set.add(p)).collect(Collectors.toList()));
		}
		if (ccs != null && ccs.length() > 0) {
			list.addAll(Arrays.stream(ccs.split(",")).filter(p -> !set.contains(p) && set.add(p)).collect(Collectors.toList()));
		}
		if (bccs != null && bccs.length() > 0) {
			list.addAll(Arrays.stream(bccs.split(",")).filter(p -> !set.contains(p) && set.add(p)).collect(Collectors.toList()));
		}
		if (sort) {
			list.sort((a,b) -> a.compareTo(b));
		}
		return list;
	}
	
	public Set<String> getParticipantsSet(boolean includeSender){
		HashSet<String> set = new HashSet<String>(); 
		if (includeSender) {
			String sender = getSender();
			if (sender != null) {
				set.add(sender);
			}
		}
		if (recipient != null && recipient.length() > 0) {
			set.addAll(Arrays.stream(recipient.split(",")).collect(Collectors.toSet()));
		}
		if (ccs != null && ccs.length() > 0) {
			set.addAll(Arrays.stream(ccs.split(",")).collect(Collectors.toSet()));
		}
		if (bccs != null && bccs.length() > 0) {
			set.addAll(Arrays.stream(bccs.split(",")).collect(Collectors.toSet()));
		}
		return set;
	}
	
	public String getCleanSubject() {
		String result = getSubject();
		if (result != null && result.length() > 0) {
			result = result.replaceAll(MessageConstants.Mail.ReservedWords.RE_PATTERN, "");
		}
		return result;
	}
	
	public double matchThread(MessageThread messageThread) {
		return matchParticipants(messageThread);
	}
	
	protected double matchParticipants(MessageThread messageThread) {
		double result = 0.0d;
		if (messageThread != null) {
			Set<String> messageThreadParticipants = messageThread.getParticipants();
			if (messageThreadParticipants != null) {
				Set<String> allRecipients = getParticipantsSet(false);
				Set<String> participants = new HashSet<String>(messageThreadParticipants);
				String sender = getSender();
				if (participants.contains(sender)) {
					result = 0.5d;
					if (!allRecipients.contains(sender)) {
						participants.remove(sender);
					}
				}
				int recipientsSize = participants.size();
				long matchingRecipients = allRecipients.stream().filter(r -> participants.contains(r)).count();
				double recipientsMatch = 0.5d * ((double)matchingRecipients / (double)recipientsSize);
				result += recipientsMatch;
				return result;
			}
		}
		return 0;
	}
	
	public MessageThread createMessageThread() {
		MessageThread messageThread = new MessageThread();
		return messageThread;
	}
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getCcs() {
		return ccs;
	}

	public void setCcs(String ccs) {
		this.ccs = ccs;
	}

	public String getBccs() {
		return bccs;
	}

	public void setBccs(String bccs) {
		this.bccs = bccs;
	}

	public List<MailAttachment> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}
	
}
