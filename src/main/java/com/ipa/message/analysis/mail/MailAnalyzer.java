package com.ipa.message.analysis.mail;

import java.util.HashMap;
import java.util.Iterator;

import com.ipa.common.message.Message;
import com.ipa.common.message.mail.MailMessage;
import com.ipa.common.thread.MessageThread;
import com.ipa.message.analysis.MessageAnalysisRequest;
import com.ipa.message.analysis.MessageAnalysisResponse;
import com.ipa.message.analysis.MessageAnalyzer;

public class MailAnalyzer extends MessageAnalyzer{
	
	protected HashMap<String, MessageThread> messageThreads; //threads by keys
	protected HashMap<String, HashMap<String, MessageThread>> messageThreadsBySubjects; //threads maps by subjects
	
	public MailAnalyzer(MessageAnalysisRequest request) {
		super(request);
	}
	
	public boolean init(){
		boolean result = super.init();
		return result;
	}
	
	@Override
	public Message contextualize(Message message) {
		if (request.isContextualize()){
			
		}
		// TODO Auto-generated method stub
		return super.contextualize(message);
	}

	@Override
	public MessageAnalysisResponse analyze(Message message) {
		// TODO Auto-generated method stub
		return super.analyze(message);
	}
	
	protected Message getMessageThread(Message message, MessageAnalysisResponse response) {
		String messageKey = message.generateMessageKey();
		MessageThread messageThread = null;
		if (messageKey != null) {
			if (messageThreads != null) {
				messageThread = messageThreads.get(messageKey);
			}
			String cleanSubject = ((MailMessage)message).getCleanSubject();
			if (messageThread == null) {
				if (messageThreadsBySubjects != null) {
					HashMap<String, MessageThread> map = messageThreadsBySubjects.get(cleanSubject);
					if (map != null) {
						//Check if participants match
						double bestMatch = 0d;
						MessageThread bestThread = null; 
						Iterator<MessageThread> it = map.values().iterator();
						while (it.hasNext()) {
							MessageThread currThread = it.next();
							double currMatch = message.matchThread(currThread);
							if (currMatch > bestMatch) {
								bestThread = currThread;
								bestMatch = currMatch;
							}
						}
						if (bestThread != null) {
							messageThread = bestThread;
						}
					}
				}
			}
		}
		return null;
	}
	
	public MessageThread createMessageThread(Message message) {
		MessageThread mailThread = null;
		return null;
	}
	
	
	
	

}
