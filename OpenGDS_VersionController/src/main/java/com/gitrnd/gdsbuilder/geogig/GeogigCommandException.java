package com.gitrnd.gdsbuilder.geogig;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class GeogigCommandException extends IllegalArgumentException {

	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	private boolean xml = true;

	public GeogigCommandException(String message) {
		super(message);
	}

	public GeogigCommandException(String message, boolean xml) {
		super(message);
		this.xml = xml;
	}

	public GeogigCommandException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public boolean isXml() {
		return xml;
	}

	public void setXml(boolean xml) {
		this.xml = xml;
	}

}
