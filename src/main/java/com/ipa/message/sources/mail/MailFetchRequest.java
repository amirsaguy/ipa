package com.ipa.message.sources.mail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.mail.search.SearchTerm;

import com.ipa.common.messagingaccounts.MessagingAccountInfo;
import com.ipa.common.messagingaccounts.mail.MailAccountInfo;
import com.ipa.message.sources.MessagesFetchRequest;

public class MailFetchRequest extends MessagesFetchRequest{
	
	protected SearchTerm searchTerm = null;
	protected HashMap<String, MessageUidRange> folderUidRanges = null; 
	protected boolean stripHtml = false;

	public void addMessageUidRange(String folderName, long minMessageUid, long maxMessageUid){
		if (folderUidRanges == null){
			folderUidRanges = new HashMap<String, MessageUidRange>();
		}
		MessageUidRange range = new MessageUidRange(minMessageUid, maxMessageUid);
		folderUidRanges.put(folderName, range);
	}
	
	public void updateFromMessagingAccountInfo(MessagingAccountInfo info) {
		if (info instanceof MailAccountInfo) {
			MailAccountInfo mailAccountInfo = (MailAccountInfo)info;
			HashMap<String, Long> folderUids = mailAccountInfo.getLastRetrievedUIDs();
			if (folderUids != null && folderUids.size() > 0) {
				
			}
		}
	}
	

	
	public SearchTerm getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(SearchTerm searchTerm) {
		this.searchTerm = searchTerm;
	}

	public boolean isStripHtml() {
		return stripHtml;
	}

	public void setStripHtml(boolean stripHtml) {
		this.stripHtml = stripHtml;
	}
	
	public HashMap<String, MessageUidRange> getFolderUidRanges() {
		return folderUidRanges;
	}

	public void setFolderUidRanges(
			HashMap<String, MessageUidRange> folderUidRanges) {
		this.folderUidRanges = folderUidRanges;
	}

	public static class MessageUidRange {
		
		protected long startMessageUid = -1;
		protected long endMessageUid = -1;
		
		public MessageUidRange(long startMessageUid, long endMessageUid) {
			super();
			this.startMessageUid = startMessageUid;
			this.endMessageUid = endMessageUid;
		}

		public long getStartMessageUid() {
			return startMessageUid;
		}

		public void setStartMessageUid(long startMessageUid) {
			this.startMessageUid = startMessageUid;
		}

		public long getEndMessageUid() {
			return endMessageUid;
		}

		public void setEndMessageUid(long endMessageUid) {
			this.endMessageUid = endMessageUid;
		}

		}
	

}
