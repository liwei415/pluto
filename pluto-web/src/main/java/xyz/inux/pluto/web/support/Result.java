package xyz.inux.pluto.web.support;

import java.io.Serializable;

public class Result<T> implements Serializable {
	private static final long serialVersionUID = -8379100651362895882L;

	private int status;
	private String message;
	private T data;

	public Result(int status) {
		this(status, null);
	}

	public Result(int status, String message) {
		this(status, message, null);
	}

	public Result(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}


}
