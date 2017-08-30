package com.ipa.message.analysis;

import java.util.HashMap;

import com.ipa.common.message.Message;
import com.ipa.common.messagingaccounts.MessagingAccount;
import com.ipa.common.thread.MessageThread;
import com.ipa.message.sources.MessagesFetchRequest;

public class MessageAnalyzer {
	
	protected MessageAnalysisRequest request;
	protected MessagesFetchRequest fetchRequest;
	protected MessagingAccount messagingAccount;
	protected AnalyzerContext context;
	
	protected HashMap<String, MessageThread> messageThreads = null;
	
	public MessageAnalyzer(){
	}
	
	public MessageAnalyzer(MessageAnalysisRequest request){
		this.request = request;
		if (request != null){
			this.context = request.getContext();
			this.fetchRequest = request.getFetchRequest();
			if (this.fetchRequest != null){
				this.messagingAccount = fetchRequest.getAccount();
			}
		}
	}
	
	public boolean init(){
		if (request != null){
			if (fetchRequest == null || messagingAccount == null){
				//TODO return fail status and write to audit
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param Message
	 * Remove irrelevant data (previous responses etc.)
	 * @return
	 */
	public Message contextualize(Message message){
		return null;
	}
	
	/**
	 * 
	 * @param Message
	 * @return
	 */
	public MessageAnalysisResponse analyze(Message message){
		return null;
	}

	public MessageAnalysisRequest getRequest() {
		return request;
	}

	public void setRequest(MessageAnalysisRequest request) {
		this.request = request;
	}

	public AnalyzerContext getContext() {
		return context;
	}

	public void setContext(AnalyzerContext context) {
		this.context = context;
	}
	
	

}
