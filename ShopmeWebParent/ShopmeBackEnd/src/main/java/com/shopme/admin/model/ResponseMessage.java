package com.shopme.admin.model;

public class ResponseMessage {

	int status;	
	String code;
	String message;
	
	public ResponseMessage() {
		// TODO Auto-generated constructor stub
	}

	public ResponseMessage(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public ResponseMessage(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ResponseMessage(int status, String code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
