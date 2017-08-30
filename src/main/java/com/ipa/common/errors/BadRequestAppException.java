package com.ipa.common.errors;

public class BadRequestAppException extends AppException{

	private static final long serialVersionUID = -9156958350653603362L;

	public BadRequestAppException() {
		super(400,"Bad Request");
	}

	public BadRequestAppException(String message) {
		super(400,message);
	}
	
	

}
