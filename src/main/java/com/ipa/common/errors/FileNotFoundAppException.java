package com.ipa.common.errors;

public class FileNotFoundAppException extends AppException{

	private static final long serialVersionUID = 6143871674152370049L;

	public FileNotFoundAppException() {
		super(404,"Missing resources");
	}
	
	public FileNotFoundAppException(String message) {
		super(404,message);
	}

}
