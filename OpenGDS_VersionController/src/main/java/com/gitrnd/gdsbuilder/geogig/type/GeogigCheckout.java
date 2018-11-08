/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Checkout Command 응답 xml 객체.
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "response")
public class GeogigCheckout {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	private String transactionId;

	private String oldTarget;

	private String newTarget;

	private String path;

	private String strategy;

	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "OldTarget")
	public String getOldTarget() {
		return oldTarget;
	}

	@XmlElement(name = "NewTarget")
	public String getNewTarget() {
		return newTarget;
	}

	@XmlElement(name = "Path")
	public String getPath() {
		return path;
	}

	@XmlElement(name = "Strategy")
	public String getStrategy() {
		return strategy;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setOldTarget(String oldTarget) {
		this.oldTarget = oldTarget;
	}

	public void setNewTarget(String newTarget) {
		this.newTarget = newTarget;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
