package com.ipa.common.message.mail;

import java.util.Comparator;

import com.ipa.common.util.MessageUtil;

public class BodyPartComparator implements Comparator<MailBodyPart>{

	@Override
	public int compare(MailBodyPart o1, MailBodyPart o2) {
		if (o1 == o2) {
			return 0;
		}else if (o1 == null) {
			return -1;
		}else if (o2 == null) {
			return 1;
		}else {
			String t1 = o1.getContentType();
			String t2 = o2.getContentType();
			if (t1 == t2) {
				return 0;
			}
			if (MessageUtil.isTextMimeType(t1)) {
				return -1;
			}else if (MessageUtil.isTextMimeType(t2)) {
				return 1;
			}else {
				return 0;
			}
		}
	}

}
