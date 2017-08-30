package com.ipa.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MailTemplate implements Cloneable{
	
	protected String id;
	protected String lang;
	protected String subject;
	protected String message;
	protected String messagePath;
	protected String mailType;
	protected String from;
	protected String to;
	protected  Map<String, Object> values;

	public MailTemplate() {
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	public void addValue(String key,String value){
		if (values == null){
			values = new HashMap<String, Object>();
		}
		values.put(key, value);
	}
	
	private String replaceTextWithValues(String text){
		if (text != null && values != null){
			for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String value = (String)values.get(key);
				if (value != null){
					text = text.replaceAll("\\[" + key + "\\]", value);
				}
			}
		}
		return text;
	}
	
	@JsonIgnore
	public String getUpdateSubject(){
		return replaceTextWithValues(getSubject());
	}
	
	@JsonIgnore
	public String getUpdateMessage(){
		return replaceTextWithValues(getMessage());
	}

	public String getMessagePath() {
		return messagePath;
	}

	public void setMessagePath(String messagePath) {
		this.messagePath = messagePath;
	}

}
