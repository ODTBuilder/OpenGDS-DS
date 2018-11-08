/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Push Command 응답 xml 객체.
 * 
 * @author GIT
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

	private String error;

	/**
	 * @return the success
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * @return the push
	 */
	@XmlElement(name = "Push")
	public String getPush() {
		return push;
	}

	/**
	 * @return the dataPushed
	 */
	@XmlElement(name = "dataPushed")
	public String getDataPushed() {
		return dataPushed;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param push the push to set
	 */
	public void setPush(String push) {
		this.push = push;
	}

	/**
	 * @param dataPushed the dataPushed to set
	 */
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
