package com.ipa.message.analysis;

import com.ipa.message.sources.MessagesFetchRequest;

public class MessageAnalysisRequest {

	protected AnalyzerContext context;
	protected MessagesFetchRequest fetchRequest;
	protected boolean contextualize = true; //Keep relevant data only

	public AnalyzerContext getContext() {
		return context;
	}

	public void setContext(AnalyzerContext context) {
		this.context = context;
	}

	public MessagesFetchRequest getFetchRequest() {
		return fetchRequest;
	}

	public void setFetchRequest(MessagesFetchRequest fetchRequest) {
		this.fetchRequest = fetchRequest;
	}

	public boolean isContextualize() {
		return contextualize;
	}

	public void setContextualize(boolean contextualize) {
		this.contextualize = contextualize;
	}
	
	
}
