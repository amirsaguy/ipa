package com.ipa.message.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ipa.adapter.SiteAdapter;
import com.ipa.adapter.SiteAdapterManager;
import com.ipa.common.accounts.AccountUser;
import com.ipa.common.message.Message;
import com.ipa.common.messagingaccounts.MessagingAccount;
import com.ipa.common.messagingaccounts.MessagingAccountInfo;
import com.ipa.common.messagingaccounts.mail.MailAccount;
import com.ipa.message.IPAFactory;
import com.ipa.message.analysis.MessageAnalyzer;
import com.ipa.message.sources.MessageFetcher;
import com.ipa.message.sources.MessagesFetchRequest;
import com.ipa.message.sources.MessagesFetchResponse;
import com.ipa.message.store.mail.MailRepository;

public class MessagesPlant {
	
	private static MessagesPlant current = null;
	
	public static MessagesPlant getInstance() {
		if (current == null) {
			synchronized (MessagesPlant.class) {
				if (current == null) {
					current = new MessagesPlant();
					current.init();
				}
			}
		}
		return current;
	}
	
	
	protected List<String> accountUsers;
	protected HashMap<String, MessageFetcher> messageFetchers = new HashMap<String, MessageFetcher>();
	protected HashMap<String, MessageAnalyzer> messageAnalyzers = new HashMap<String, MessageAnalyzer>();
	
	protected boolean initialized = false;
	
	public void init() {
		initMessagingAccounts();
		initMessageFetchers();
		initMessageAnalyzers();
		this.initialized = true;
	}
	
	protected boolean initMessagingAccounts() {
		SiteAdapter siteAdapter =  SiteAdapterManager.getInstance().getSiteAdapter();
		accountUsers = siteAdapter.getAllUsers();
		return true;
	}
	
	protected boolean initMessageFetchers() {
		MessageFetcher fetcher = IPAFactory.getInstance().createMessageFetcherByAccountType(MailAccount.ACCOUNT_TYPE_MAIL);
		if (fetcher != null) {
			messageFetchers.put(MailAccount.ACCOUNT_TYPE_MAIL, fetcher);
		}
		return true;
	}
	
	protected boolean initMessageAnalyzers() {
		MessageAnalyzer analyzer = IPAFactory.getInstance().createMessageAnalyzerByAccountType(MailAccount.ACCOUNT_TYPE_MAIL);
		if (analyzer != null) {
			messageAnalyzers.put(MailAccount.ACCOUNT_TYPE_MAIL, analyzer);
		}
		return true;
	}
	
	protected MessageFetcher getMessageFetcher(String messageType) {
		return messageFetchers.get(messageType);
	}
	
	protected MessageAnalyzer getMessageAnalyzer(String messageType) {
		return messageAnalyzers.get(messageType);
	}
	
	/**
	 * Currently run sequentially
	 * Later on, use pool of fetchers and analyzers and a queue of messages to do everything more efficiently
	 */
	public void run() {
		if (initialized && accountUsers != null) {
			SiteAdapter siteAdapter = SiteAdapterManager.getInstance().getSiteAdapter();
			for (String userMail : accountUsers) {
				AccountUser accountUser = siteAdapter.getUser(userMail);
				if (accountUser != null) {
					ArrayList<MessagingAccount> messagingAccounts = accountUser.getMessagingAccounts();
					if (messagingAccounts != null) {
						for (MessagingAccount messagingAccount : messagingAccounts) {
							runMessagingAccount(accountUser, messagingAccount);
						}
					}
				}
				
			}
		}
	}
	
	protected void runMessagingAccount(AccountUser accountUser, MessagingAccount messagingAccount) {
		MessageFetcher fetcher = getMessageFetcher(messagingAccount.getType());
		if (fetcher != null) {
			MessagesFetchRequest fetchRequest = getMessagesFetchRequest(accountUser, messagingAccount);
			fetcher.setRequest(fetchRequest);
			fetcher.init();
			MessagesFetchResponse fetchResponse = fetcher.fetchData();
			if (fetchResponse != null && fetchResponse.size() > 0) {
				MailRepository mailRepository = MailRepository.getInstance();
				ArrayList<Message> messages = fetchResponse.getMessages();
				for (Message message : messages) {
					mailRepository.storeMail(message);
				}
			}
		}
	}
	
	protected MessagesFetchRequest getMessagesFetchRequest(AccountUser accountUser, MessagingAccount messagingAccount) {
		MessagesFetchRequest fetchRequest = IPAFactory.getInstance().createMessagesFetchRequest(messagingAccount.getType());
		if (fetchRequest != null) {
			fetchRequest.setUser(accountUser);
			fetchRequest.setAccount(messagingAccount);
			MessagingAccountInfo messagingAccountInfo = loadMessagingAccountInfo(messagingAccount);
			if (messagingAccountInfo != null) {
				fetchRequest.updateFromMessagingAccountInfo(messagingAccountInfo);
			}
		}
		return fetchRequest;
	}
	
	protected MessagingAccountInfo loadMessagingAccountInfo(MessagingAccount messagingAccount) {
		return null;
	}

}
