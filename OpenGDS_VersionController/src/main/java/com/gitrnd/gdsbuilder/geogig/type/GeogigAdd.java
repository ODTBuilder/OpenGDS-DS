/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Add Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigAdd {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * Geogig Add Command 성공 여부
	 */
	private String add;

	/**
	 * 오류 메세지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@XmlElement(name = "Add")
	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
