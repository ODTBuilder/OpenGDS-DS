package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Transcation Command 응답 xml 객체.
 * BeginTransaction/EndTransaction/CancleTranscation
 * 
 * @author GIT
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

	/**
	 * Geogig Command 응답 성공 여부 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * Geogig Transaction 객체 반환
	 * 
	 * @return Transaction
	 */
	@XmlElement(name = "Transaction")
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @return the error
	 */
	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * Geogig Transaction 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "Transaction")
	public static class Transaction {

		/**
		 * Transaction Id
		 */
		private String id;

		/**
		 * Transaction Id 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "ID")
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}
}
