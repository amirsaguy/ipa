package com.ipa.message.sources.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import com.ipa.common.message.mail.MailBodyPart;
import com.ipa.common.message.mail.MailMessage;
import com.ipa.common.message.mail.MailMessageWrapper;
import com.ipa.common.message.mail.MailMultiPart;
import com.ipa.common.messagingaccounts.MessagingAccount;
import com.ipa.common.messagingaccounts.mail.MailAccount;
import com.ipa.common.util.IpaStringUtil;
import com.ipa.common.util.MailUtil;
import com.ipa.common.util.MessageUtil;
import com.ipa.message.sources.MessageFetcher;
import com.ipa.message.sources.MessagesFetchRequest;
import com.ipa.message.sources.MessagesFetchResponse;
import com.ipa.message.sources.mail.MailFetchRequest.MessageUidRange;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.util.BASE64DecoderStream;

public class MailFetcher extends MessageFetcher{

	public static String INBOX_FOLDER = "INBOX";
	public static String SENT_ITEMS_INBOX = "[Gmail]/Sent Mail";

	protected MailAccount account = null;
	protected MailFetchRequest mailFetchRequest;
	protected boolean initialized = false;
	
	protected MessagesFetchResponse response = null;


	protected boolean doPrint = false;
	
	protected static final Logger log = Logger.getLogger(MailFetcher.class);

	public MailFetcher(MessagesFetchRequest request){
		super(request);
	}

	public boolean init(){
		if (request != null){
			if (request instanceof MailFetchRequest){
				this.mailFetchRequest = (MailFetchRequest)request; 
			}
			MessagingAccount acc = request.getAccount();
			if (acc instanceof MailAccount){
				this.account = (MailAccount)acc;
			}
			this.initialized = true;
			if (response == null) {
				response = new MessagesFetchResponse();
			}
		}
		return true;
	}

	public MessagesFetchResponse fetchData() {
		Properties properties = new Properties();
		properties.put("mail.pop3.host", account.getHost());
		properties.put("mail.pop3.port", account.getPort());
		properties.put("mail.pop3.starttls.enable", account.isUseSSL());
		Session emailSession = null;
		emailSession = Session.getDefaultInstance(properties);
		Store store = null;
		try {
			store = emailSession.getStore(account.getProtocol());
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (store != null){
			try{
				store.connect(account.getHost(), account.getUser(), account.getPassword());
				ArrayList<String> folders = account.getFolders();
				if (folders == null || folders.size() == 0){
					folders = new ArrayList<String>();
					folders.add(INBOX_FOLDER);
					folders.add(SENT_ITEMS_INBOX);
				}
				/*Folder[] foldersArr = store.getFolder("[Gmail]").list();
			for (int i = 0; foldersArr != null && i < foldersArr.length; i++) {
				System.out.println("Available folder " + foldersArr[i].getFullName());
			}*/

				for (String folder : folders) {
					fetchFolderMail(store, folder);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					store.close();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	protected void fetchFolderMail(Store store, String folder){
		try {
			Folder emailFolder = store.getFolder(folder);
			emailFolder.open(Folder.READ_ONLY);
			UIDFolder uidFolder = null;

			if (emailFolder instanceof UIDFolder){
				uidFolder = (UIDFolder)emailFolder;
			}

			System.out.println("*** " + folder + " is " + (uidFolder == null ? "not " : "") + "a UIDFolder ***");

			int numberOfUnreadMessages = emailFolder.getUnreadMessageCount();
			System.out.println("Number of unread messages: " + numberOfUnreadMessages);

			SearchTerm searchTerm = mailFetchRequest.getSearchTerm();
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length: " + messages.length);

			int maxNumberOfMessages = request.getMaxNumberOfMessages();
			int len = messages.length;
			int startMessageIndex = 0;
			if (maxNumberOfMessages > 0 && maxNumberOfMessages < len){
				startMessageIndex = len - maxNumberOfMessages;
			}

			for (int i = startMessageIndex; i < len ; i++) {
				Message message = messages[i];
				long messageUID = uidFolder != null ? uidFolder.getUID(message) : -1;
				if (messageUID != -1){
					HashMap<String, MessageUidRange> folderUidRanges = mailFetchRequest.getFolderUidRanges();
					if (folderUidRanges != null){
						MessageUidRange folderUidRange = folderUidRanges.get(folder);
						if (folderUidRange != null){
							if (folderUidRange.getStartMessageUid() >= 0 && messageUID < folderUidRange.getStartMessageUid()){
								continue;
							}
							if (folderUidRange.getEndMessageUid() >= 0 && messageUID > folderUidRange.getEndMessageUid()){
								break;
							}
						}
					}
				}
				if (searchTerm != null && !searchTerm.match(message)){
					continue;
				}

				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1) + " (UID: " + messageUID + ")");
				System.out.println("Subject: " + message.getSubject());
				System.out.println("Date: " + message.getReceivedDate());
				System.out.println("From: " + message.getFrom()[0]);
				
				MailMessage mailMessage = createMailMessage(folder, message, messageUID);
				if (mailMessage != null) {
					response.addMessageToResponse(mailMessage);
				}

				Object content = message.getContent();
				if (content instanceof Multipart){
					Multipart multiPart = (Multipart)content;
					printMultiPart(multiPart, 1);
				}else{
					System.out.println("Content: \n" + content);

				}


			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected MailMessageWrapper createMailMessage(String folder, Message message, long messageUID) throws Exception{
		String messageID = null;
		if (message instanceof IMAPMessage) {
			messageID = ((IMAPMessage)message).getMessageID();
		}
		MailMessageWrapper mailMessageWrapper = new MailMessageWrapper();
		mailMessageWrapper.setMailFolder(folder);
		mailMessageWrapper.setMessageUID(messageUID);
		
		MailMessage mailMessage = new MailMessage();
		mailMessageWrapper.setMailMessage(mailMessage);
		mailMessageWrapper.setMessageId(messageID);
		String subject = message.getSubject();
		//subject
		mailMessage.setSubject(subject);
		//timestamp
		mailMessage.setTimestamp(message.getReceivedDate().getTime());
		Object content = message.getContent();
		ParticipantsInfo participantsInfo = getParticipantsList(message);
		String fromAddress = participantsInfo != null ? participantsInfo.fromAddress : null;
		//sender
		mailMessage.setSender(fromAddress);
		ArrayList<String> recipientsList = participantsInfo != null ? participantsInfo.recipients : null;
		String recipients = participantsInfo != null ? String.join(",", recipientsList) : null;
		//recipient
		mailMessage.setRecipient(recipients);
		ArrayList<String> ccsList = participantsInfo != null ? participantsInfo.ccs : null;
		String ccs = participantsInfo != null ? String.join(",", ccsList) : null;
		//css
		mailMessage.setCcs(ccs);
		ArrayList<String> bccsList = participantsInfo != null ? participantsInfo.bccs : null;
		String bccs = participantsInfo != null ? String.join(",", bccsList) : null;
		//bccs
		mailMessage.setBccs(bccs);
		if (content instanceof Multipart){
			Multipart multiPart = (Multipart)content;
			MailMultiPart mailMultiPart = new MailMultiPart(multiPart.getContentType());
			updateMultiPart(subject, mailMultiPart, multiPart);
			mailMessage.setContent(mailMultiPart);
		}else if (content instanceof BodyPart){
			BodyPart bodyPart = (BodyPart)content;
			MailBodyPart mailBodyPart = createMailBodyPart(subject, bodyPart);
			mailMessage.setContent(mailBodyPart);
			if (mailBodyPart.isMessageText()) {
				Object bodyPartContent = mailBodyPart.getContent();
				if (bodyPartContent instanceof String) {
					mailMessage.setContent((String)bodyPartContent);
				}
			}
		}
		mailMessage.updateMessage();
		return mailMessageWrapper;
	}
	
	protected ParticipantsInfo getParticipantsList(Message message){
		ArrayList<String> tos = new ArrayList<String>();
		ArrayList<String> ccs = new ArrayList<String>();
		ArrayList<String> bccs = new ArrayList<String>();
		String fromAddress = null;
		try {
			HashSet<String> participantsSet =new HashSet<String>();
			Address[] fromArr = message.getFrom();
			//String fromName = null;
			if (fromArr != null && fromArr.length > 0) {
				Address from = fromArr[0];
				if (from instanceof InternetAddress) {
					InternetAddress internetAddress = (InternetAddress)from;
					fromAddress = internetAddress.getAddress();
					//fromName = internetAddress.getPersonal();
				}
			}
			Address[] toArr = message.getRecipients(RecipientType.TO);
			Address[] ccArr = message.getRecipients(RecipientType.CC);
			Address[] bccArr = message.getRecipients(RecipientType.BCC);
			if (toArr != null && toArr.length > 0) {
				for (InternetAddress address : (InternetAddress[])toArr) {
					String emailAddress = address.getAddress();
					if (!participantsSet.contains(fromAddress)) {
						participantsSet.add(emailAddress);
						tos.add(emailAddress);
					}
				}
			}
			if (ccArr != null && ccArr.length > 0) {
				for (InternetAddress address : (InternetAddress[])ccArr) {
					String emailAddress = address.getAddress();
					if (!participantsSet.contains(fromAddress)) {
						participantsSet.add(emailAddress);
						ccs.add(emailAddress);
					}
				}
			}
			if (bccArr != null && bccArr.length > 0) {
				for (InternetAddress address : (InternetAddress[])bccArr) {
					String emailAddress = address.getAddress();
					if (!participantsSet.contains(fromAddress)) {
						participantsSet.add(emailAddress);
						bccs.add(emailAddress);
					}
				}
			}
		}catch(Exception e) {
			try {
				log.error("Failed to get participants list from email " + message.getSubject(), e);
			} catch (MessagingException e1) {
				log.error("Failed to get participants list from email", e1);
			}
		}
		tos.stream().sorted((String s1, String s2) -> s1.compareTo(s2));
		ccs.stream().sorted((String s1, String s2) -> s1.compareTo(s2));
		bccs.stream().sorted((String s1, String s2) -> s1.compareTo(s2));
		return new ParticipantsInfo(fromAddress, tos, ccs, bccs);
	}
	
	public static class ParticipantsInfo {
		protected String fromAddress;
		protected ArrayList<String> recipients;
		protected ArrayList<String> ccs;
		protected ArrayList<String> bccs;
		public ParticipantsInfo(String fromAddress, ArrayList<String> recipients,
				ArrayList<String> ccs, ArrayList<String> bccs) {
			super();
			this.fromAddress = fromAddress;
			this.recipients = recipients;
			this.ccs = ccs;
			this.bccs = bccs;
		}
		
	}
	
	public static class MessageTextWrapper {
		protected String messageText;
		protected boolean isHtml = false;
		public MessageTextWrapper(String messageText, boolean isHtml) {
			super();
			this.messageText = messageText;
			this.isHtml = isHtml;
		}
		
	}
	
	protected void updateMultiPart(String subject, MailMultiPart mailMultiPart, Multipart multiPart) {
		try {
			for (int mi = 0; mi < multiPart.getCount(); mi++){
				BodyPart bodyPart = multiPart.getBodyPart(mi);
				Object content = bodyPart.getContent();
				String bodyPartMimeType = bodyPart.getContentType();
				if (content instanceof Multipart){
					Multipart mp = (Multipart)content;
					MailMultiPart mmp = new MailMultiPart(bodyPartMimeType);
					updateMultiPart(subject, mmp, mp);
					mailMultiPart.addMailBodyPart(mmp);
				}else {
					MailBodyPart mailBodyPart = createMailBodyPart(subject, bodyPart);
					mailMultiPart.addMailBodyPart(mailBodyPart);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to update mail multipart " + multiPart.getContentType(), e);
		}
	}
	
	protected MailBodyPart createMailBodyPart(String subject, BodyPart bodyPart){
		MailBodyPart mailBodyPart = new MailBodyPart();
		String bodyPartMimeType = null;
		try {
			bodyPartMimeType = bodyPart.getContentType();
			mailBodyPart.setContentType(bodyPartMimeType);
		} catch (MessagingException e) {
			log.error("Failed to get body part mime type of message " + subject, e);
			return null;
		}
		Object content = null;
		try {
			content = bodyPart.getContent();
		} catch (IOException | MessagingException e) {
			log.error("Failed to get body part content of message " + subject, e);
			return null;
		}
		if (content instanceof String) {
			if (content instanceof String && MessageUtil.isHtmlMimeType(bodyPartMimeType) && 
					mailFetchRequest.isStripHtml()) {
				String text = (String)content;
				text = IpaStringUtil.stripHtml(text, false);
				text = MailUtil.removeReplyLines(text);
				content = text;
			}
			mailBodyPart.setMessageText(true);
		}else if (content instanceof BASE64DecoderStream) {
			BASE64DecoderStream stream = null;
			try {
				stream = (BASE64DecoderStream)content;
				ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	      int c; 
	      while((c = stream.read()) != -1) { 
	       out.write(c); 
	      }
	      content = out.toByteArray();
	      mailBodyPart.setAttachment(true);
	      //Files.write(Paths.get("word.doc"), out.toByteArray());
			} catch (Exception e) {
				log.error("Failed to read " + bodyPartMimeType + " body part stream of message " + subject, e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						log.error("Failed to close " + bodyPartMimeType + " body part stream of message " + subject, e);
					}
				}
			}
		}
		mailBodyPart.setContent(content);
		return mailBodyPart;
	}

	protected void printMultiPart(Multipart multiPart, int level) throws Exception{
		for (int mi = 0; mi < multiPart.getCount(); mi++){
			BodyPart bodyPart = multiPart.getBodyPart(mi);
			Object content = bodyPart.getContent();
			String bodyPartMimeType = bodyPart.getContentType();
			if (content instanceof Multipart){
				Multipart mp = (Multipart)content;
				printMultiPart(mp, level+1);
			}else if (content instanceof String){
				System.out.println("\nBody part is of Mime type: " + bodyPartMimeType);
				String text = (String)content;
				if (mailFetchRequest.isStripHtml()){
					text = IpaStringUtil.stripHtml(text, false);
				}
				System.out.println("Content: \n" + text);
			}else{
				System.out.println("\nBody part is of Mime type: " + bodyPartMimeType);
			}
		}
	}
	
	 

}
