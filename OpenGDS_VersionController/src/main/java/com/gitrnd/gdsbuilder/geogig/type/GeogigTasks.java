package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Tasks Command 응답 xml 객체. tasks/[task id].xml
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "task")
public class GeogigTasks {

	/**
	 * tasks Id
	 */
	private String id;
	/**
	 * tasks status
	 */
	private String status;
	/**
	 * transactionId
	 */
	private String transactionId;
	/**
	 * description
	 */
	private String description;
	/**
	 * result
	 */
	private Result result;
	/**
	 * error message
	 */
	private String error;

	@XmlElement(name = "id")
	public String getId() {
		return id;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	@XmlElement(name = "transactionId")
	public String getTransactionId() {
		return transactionId;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}

	@XmlElement(name = "result")
	public Result getResult() {
		return result;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Result 객체
	 * 
	 * @author DY.Oh
	 *
	 */
	public static class Result {
		/**
		 * RevTree
		 */
		private RevTree revTree;

		@XmlElement(name = "RevTree")
		public RevTree getRevTree() {
			return revTree;
		}

		public void setRevTree(RevTree revTree) {
			this.revTree = revTree;
		}

	}

	/**
	 * RevTree 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "RevTree")
	public static class RevTree {

		/**
		 * treeId
		 */
		private String treeId;

		@XmlElement(name = "treeId")
		public String getTreeId() {
			return treeId;
		}

		public void setTreeId(String treeId) {
			this.treeId = treeId;
		}

	}
}
