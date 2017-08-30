package com.ipa.message;

import java.util.HashMap;

import com.ipa.common.messagingaccounts.mail.MailAccount;
import com.ipa.common.util.ClassResourceManager;
import com.ipa.message.analysis.MessageAnalysisRequest;
import com.ipa.message.analysis.MessageAnalyzer;
import com.ipa.message.sources.MessageFetcher;
import com.ipa.message.sources.MessagesFetchRequest;

public class IPAFactory {
	
	private static IPAFactory current = null;
	
	private static final HashMap<String, String> messageFetchers = new HashMap<String, String>();
	private static final HashMap<String, String> messageAnalyzers = new HashMap<String, String>();
	private static final HashMap<String, String> messagesFetchRequests = new HashMap<String, String>();

	static {
		//set message fetchers classes
		messageFetchers.put(MailAccount.ACCOUNT_TYPE_MAIL, "com.ipa.message.sources.mail.MailFetcher");
		//set message analyzers classes
		messageAnalyzers.put(MailAccount.ACCOUNT_TYPE_MAIL, "com.ipa.message.analysis.mail.MailAnalyzer");
		//set fetch requests classes
		messagesFetchRequests.put(MailAccount.ACCOUNT_TYPE_MAIL, "com.ipa.message.sources.mail.MailFetchRequest");
	}
	
	public static IPAFactory getInstance() {
		if (current == null) {
			synchronized (IPAFactory.class) {
				if (current == null) {
					current = new IPAFactory();
				}
			}
		}
		return current;
	}
	
	private IPAFactory() {
	}
	
	public MessageFetcher createMessageFetcherByAccountType(String accountType){
		MessageFetcher messageFetcher = null;
		String className = messageFetchers.get(accountType);
		if (className != null) {
			messageFetcher = (MessageFetcher)ClassResourceManager.createObject(className);
		}
		return messageFetcher;
	}

	public MessageAnalyzer createMessageAnalyzerByAccountType(String accountType){
		MessageAnalyzer messageAnalyzer = null;
		String className = messageAnalyzers.get(accountType);
		if (className != null) {
			messageAnalyzer = (MessageAnalyzer)ClassResourceManager.createObject(className);
		}
		return messageAnalyzer;
	}
	
	public MessageAnalysisRequest createMessageAnalysisRequest(String accountType) {
		MessageAnalysisRequest messageAnalysisRequest = new MessageAnalysisRequest();
		return messageAnalysisRequest;
	}
	
	public MessagesFetchRequest createMessagesFetchRequest(String accountType) {
		MessagesFetchRequest fetchRequest = null;
		String className = messagesFetchRequests.get(accountType);
		if (className != null) {
			fetchRequest = (MessagesFetchRequest)ClassResourceManager.createObject(className);
		}
		return fetchRequest;
		
	}



}
