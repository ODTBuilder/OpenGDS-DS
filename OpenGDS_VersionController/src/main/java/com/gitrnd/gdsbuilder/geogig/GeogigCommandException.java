package com.gitrnd.gdsbuilder.geogig;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class GeogigCommandException extends IllegalArgumentException {

	private int rawStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

	private String responseBodyAsString;

	private boolean xml;

	public GeogigCommandException(String message) {
		super(message);
	}

	public GeogigCommandException(String message, String responseBodyAsString, int rawStatusCode) {
		super(message);
		this.responseBodyAsString = responseBodyAsString;
		this.xml = true;
		this.rawStatusCode = rawStatusCode;
	}

	public boolean isXml() {
		return xml;
	}

	public void setXml(boolean xml) {
		this.xml = xml;
	}

	public int getRawStatusCode() {
		return rawStatusCode;
	}

	public void setRawStatusCode(int rawStatusCode) {
		this.rawStatusCode = rawStatusCode;
	}

	public String getResponseBodyAsString() {
		return responseBodyAsString;
	}

	public void setResponseBodyAsString(String responseBodyAsString) {
		this.responseBodyAsString = responseBodyAsString;
	}

}
