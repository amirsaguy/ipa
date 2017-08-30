package com.ipa.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ipa.common.MailTemplate;


public class MailTransporter {
	
	protected Properties prop;

	public MailTransporter() {
	}

	public MailTransporter(Properties prop) {
		super();
		this.prop = prop;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	protected boolean allowSendMail(){
		return prop.getProperty("mail.enabled", "false").equalsIgnoreCase("true"); 
	}
	
	public void sendMail(MailTemplate mailTemplate,String from,String to) throws Exception {
		if (!allowSendMail()){
			return;
		}
		String type = mailTemplate.getMailType();
		if (type == null || type.length() == 0){
			type = prop.getProperty("mail.type","text/plain");
		}
		Message msg = prepareHeader(from, to, mailTemplate);
		msg.setContent(mailTemplate.getUpdateMessage(), type);
		Transport.send(msg);
	}

	protected Message prepareHeader(String from,String to,MailTemplate mailTemplate) throws Exception{
		
		Properties p = new Properties();
		p.setProperty("mail.smtp.host", prop.getProperty("mail.smtp.host"));
		p.setProperty("mail.smtp.port", prop.getProperty("mail.smtp.port"));
		Authenticator mailAuth = null;		
		if (prop.getProperty("mail.smtp.auth","false").equals("true")){
			p.setProperty("mail.smtp.auth", "true");
			mailAuth = new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prop.getProperty("mail.username"), prop.getProperty("mail.password"));
				}
			  };
		}
		
		Session session = Session.getInstance(p, mailAuth);
		Message msg = new MimeMessage(session);
		InternetAddress [] addr = getAddress(to);
		msg.setRecipients(Message.RecipientType.TO,addr);

		if (from == null){
			from = prop.getProperty("mail.defaultSender");
		}
		addr = getAddress(from);
		InternetAddress from_addr = addr != null && addr.length > 0 ? addr[0] : null;
		msg.setFrom(from_addr);
		msg.setSubject(mailTemplate.getUpdateSubject());
		return msg;
	}
	
	protected InternetAddress [] getAddress(String mails) throws Exception{
		List<InternetAddress> list = new ArrayList<InternetAddress>();
		String [] arr = mails.split(";");
		if (arr != null){
			for (int i = 0; i < arr.length; i++) {
				String string = arr[i];
				int index = string.indexOf("="); 
				if (index > 0){
					list.add(new InternetAddress(string.substring(0,index),string.substring(index + 1)));
				}else {
					list.add(new InternetAddress(string));
				}
			}
		}
		return (InternetAddress[]) list.toArray(new InternetAddress[list.size()]);
	}
	
}
