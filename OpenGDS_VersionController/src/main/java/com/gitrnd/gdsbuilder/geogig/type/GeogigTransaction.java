package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Transcation Command 응답 xml 객체.
 * BeginTransaction/EndTransaction/CancleTranscation
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigTransaction {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * Geogig Transaction 객체
	 */
	private Transaction transaction;
	/**
	 * geogig error message
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "Transaction")
	public Transaction getTransaction() {
		return transaction;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * Geogig Transaction 객체
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Transaction")
	public static class Transaction {

		/**
		 * Transaction Id
		 */
		private String id;

		@XmlElement(name = "ID")
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
}
