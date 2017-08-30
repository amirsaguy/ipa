package com.ipa.message.store.mail;

import com.ipa.common.message.mail.MailMessage;

public interface MailMessageDAO {
	
	public void create(MailMessage mailMessage);
	public MailMessage readById(String id);
	public void update(MailMessage mailMessage);
	public int deleteById(String id);

}
