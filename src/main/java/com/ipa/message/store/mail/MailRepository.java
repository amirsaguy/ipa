package com.ipa.message.store.mail;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ipa.common.message.Message;
import com.ipa.common.message.mail.MailMessage;

public class MailRepository {
	
	private static MailRepository current = null;
	
	private ClassPathXmlApplicationContext ctx = null;
	private MailMessageDAO mailMessageDAO = null;
	
	public static MailRepository getInstance() {
		if (current == null) {
			synchronized (MailRepository.class) {
				if (current == null) {
					current = new MailRepository();
				}
			}
		}
		return current;
	}
	
	private MailRepository() {
		ctx = new ClassPathXmlApplicationContext("spring.xml");
		mailMessageDAO = ctx.getBean("mailMessageDAO", MailMessageDAO.class);
	}
	
	public void storeMail(Message message){
		if (message instanceof MailMessage) {
			MailMessage mailMessage = (MailMessage)message;
			mailMessageDAO.create(mailMessage);
			System.out.println("Generated mail message id = " + message.getId());
		}
	}
	
	public List<MailMessage> readMessages(){
		return null;
	}

}
