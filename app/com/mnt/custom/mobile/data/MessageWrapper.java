package com.mnt.custom.mobile.data;

public class MessageWrapper {

	public enum Code{
		MERCHANTCODE_NOT_MATCH(201),
		ERROR(300),
		SUCCESS(200), STAMP_EXPIRED(202), CANNOT_STAMP_VISIT(203), CANNOT_MARK_POINT(204), MERCHANT_CREDENTIAL_NEEDED(205),
		MERCHANT_BAD_PASSWORD(206),BAD_PASSWORD_ALGO(207);
		private int code;
		Code(int code){
			this.code = code;
		}
	}
	
	public int code;
	public String message;
	
	public MessageWrapper(Code code,String message) {
		this.code = code.code;
		this.message = message;
	}
}
