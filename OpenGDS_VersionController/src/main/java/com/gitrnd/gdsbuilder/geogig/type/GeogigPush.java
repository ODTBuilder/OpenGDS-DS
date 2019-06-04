/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Push Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigPush {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * Push Command 응답 성공 여부
	 */
	private String push;
	/**
	 * Data Push 성공 여부
	 */
	private String dataPushed;

	/**
	 * error message
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "Push")
	public String getPush() {
		return push;
	}

	@XmlElement(name = "dataPushed")
	public String getDataPushed() {
		return dataPushed;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setPush(String push) {
		this.push = push;
	}

	public void setDataPushed(String dataPushed) {
		this.dataPushed = dataPushed;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
