package com.ipa.common.errors;

public class AppException extends Exception{
	
	private static final long serialVersionUID = -3343138852034696184L;

	public static final int GEBERAL_ERROR_CODE = 400;
	
	protected int errorCode = GEBERAL_ERROR_CODE;
	
	public AppException() {
		super();
	}
	
	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AppException(String message) {
		super(message);
	}
	
	public AppException(Throwable cause) {
		super(cause);
	}
	
	public AppException(int errorCode,Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public AppException(int errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
