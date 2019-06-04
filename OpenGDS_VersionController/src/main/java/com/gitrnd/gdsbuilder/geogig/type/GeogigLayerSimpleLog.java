package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Log Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigLayerSimpleLog {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * error message
	 */
	private String error;

	/**
	 * SimpleCommit 목록
	 */
	private List<SimpleCommit> simpleCommits;

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public List<SimpleCommit> getSimpleCommits() {
		return simpleCommits;
	}

	public void setSimpleCommits(List<SimpleCommit> simpleCommits) {
		this.simpleCommits = simpleCommits;
	}

	/**
	 * SimpleCommit 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	public static class SimpleCommit {

		/**
		 * commitId
		 */
		private String commitId;

		/**
		 * 사용자명
		 */
		private String authorName;

		/**
		 * 날짜
		 */
		private String date;

		/**
		 * commit message
		 */
		private String message;

		/**
		 * 생성된 feature 개수
		 */
		private int adds;
		/**
		 * 변경된 feature 개수
		 */
		private int modifies;
		/**
		 * 삭제된 feature 개수
		 */
		private int removes;

		public String getCommitId() {
			return commitId;
		}

		public void setCommitId(String commitId) {
			this.commitId = commitId;
		}

		public String getAuthorName() {
			return authorName;
		}

		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getAdds() {
			return adds;
		}

		public void setAdds(int adds) {
			this.adds = adds;
		}

		public int getModifies() {
			return modifies;
		}

		public void setModifies(int modifies) {
			this.modifies = modifies;
		}

		public int getRemoves() {
			return removes;
		}

		public void setRemoves(int removes) {
			this.removes = removes;
		}
	}
}
