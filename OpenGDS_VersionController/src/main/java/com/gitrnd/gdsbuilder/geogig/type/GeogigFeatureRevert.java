package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.gitrnd.gdsbuilder.geogig.type.GeogigMerge.Merge;

@XmlRootElement(name = "response")
public class GeogigFeatureRevert {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * merge 결과
	 */
	private Merge merge;

	private String error;

	private String transactionId;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "Merge")
	public Merge getMerge() {
		return merge;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setMerge(Merge merge) {
		this.merge = merge;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
