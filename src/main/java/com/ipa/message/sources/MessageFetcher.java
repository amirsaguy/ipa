package com.ipa.message.sources;

public class MessageFetcher {
	
	protected MessagesFetchRequest request = null;
	
	public MessageFetcher(){
	}
	
	public MessageFetcher(MessagesFetchRequest request){
		this.request = request;
	}
	
	public boolean init(){
		return false;
	}
	
	public MessagesFetchResponse fetchData(){
		return null;
	}
	
	protected void dispose(){
		this.request = null;
	}

	public MessagesFetchRequest getRequest() {
		return request;
	}

	public void setRequest(MessagesFetchRequest request) {
		this.request = request;
	}

}
