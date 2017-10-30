package com.app.sofyan.scheduler.util;

public class Message {
	
	private static final String SUCCESS = "success";
	private static final String ERROR =  "error";
	
	private String message;
	private String status;
	private Object data;
	
	private Message() {}
	
	private Message(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	private Message(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public static Message success(String message) {
		return new Message(SUCCESS, message);
	}
	
	public static Message success(String message, Object data) {
		return new Message(SUCCESS, message, data);
	}
	
	public static Message error(String message) {
		return new Message(ERROR, message);
	}
	
	public static Message error(String message, Object data) {
		return new Message(ERROR, message, data);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
