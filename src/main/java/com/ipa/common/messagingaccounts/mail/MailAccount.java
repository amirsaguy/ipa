package com.ipa.common.messagingaccounts.mail;

import java.util.ArrayList;

import com.ipa.common.messagingaccounts.MessagingAccount;

public class MailAccount extends MessagingAccount {
	
	public static final String ACCOUNT_TYPE_MAIL = "mail";
	
	protected String protocol;
	protected String host;
	protected String port;
	protected boolean useSSL = false;
	protected String user = null;
	protected String password = null;
	protected ArrayList<String> folders = null;
	
	public String getType(){
		return ACCOUNT_TYPE_MAIL;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getFolders() {
		return folders;
	}

	public void setFolders(ArrayList<String> folders) {
		this.folders = folders;
	}

}
