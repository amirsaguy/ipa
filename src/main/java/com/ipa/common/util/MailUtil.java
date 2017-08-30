package com.ipa.common.util;

import jangada.ReplyToAnnotator;

public class MailUtil {

	public static String removeReplyLines(String message) {
		ReplyToAnnotator repto = new ReplyToAnnotator();
		try {
			String onelist3 = repto.getMsgReplyLines(message);
			String cleanMessage = repto.deleteReplyLinesFromMsg(message);
			return cleanMessage;
			
		} catch (Exception e) {
			e.printStackTrace();
			return message;
		}
	}

}
