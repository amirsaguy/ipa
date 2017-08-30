package com.ipa.common.errors;

public class UnauthorizedAppException extends AppException{

	private static final long serialVersionUID = -8902587552733041686L;

	public UnauthorizedAppException() {
		super(401,"Unauthorized request");
	}

	public UnauthorizedAppException(String message) {
		super(401,message);
	}
	
	

}
