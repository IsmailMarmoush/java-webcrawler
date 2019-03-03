package com.marmoush.communicator.validation;

import java.net.HttpURLConnection;
import java.net.URL;

public class HeadRequestResponse {
	private int code;
	private String message;

	public HeadRequestResponse(int timeout, String url) {
		HttpURLConnection httpUrlConn;
		try {
			httpUrlConn = (HttpURLConnection) new URL(url).openConnection();
			httpUrlConn.setRequestMethod("HEAD");
			// Set timeouts in milliseconds
			httpUrlConn.setConnectTimeout(timeout);
			httpUrlConn.setReadTimeout(timeout);
			this.code = httpUrlConn.getResponseCode();
			this.message = httpUrlConn.getResponseMessage();
		} catch (Exception e) {
			this.code = -1;
			this.message = e.getMessage();
		}
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return (this.code == -1) ? true : false;
	}

	public boolean isHttpOk() {
		return (this.code == HttpURLConnection.HTTP_OK) ? true : false;
	}
}
