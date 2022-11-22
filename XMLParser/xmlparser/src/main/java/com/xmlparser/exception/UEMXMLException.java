package com.xmlparser.exception;

public class UEMXMLException extends Exception {
	private static final long serialVersionUID = 1012231456101725103L;

	UEMXMLErrorCode errorCode;

	public UEMXMLException(UEMXMLErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public UEMXMLException(UEMXMLErrorCode errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public UEMXMLException(UEMXMLErrorCode errorCode, Exception cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public UEMXMLException(UEMXMLErrorCode errorCode, String msg, Exception cause) {
		super(msg, cause);
		this.errorCode = errorCode;
	}

	public UEMXMLErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("ErrorCode : ").append(errorCode).append("\t");
		builder.append(super.toString());

		return builder.toString();
	}


}
