package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryLog.Commit.ChangeType;

/**
 * Geogig Log Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigFeatureSimpleLog {

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

		private int cIdx;

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
		 * ChangeType
		 */
		private ChangeType changeType;

		/**
		 * message
		 */
		private String message;

		public int getcIdx() {
			return cIdx;
		}

		public void setcIdx(int cIdx) {
			this.cIdx = cIdx;
		}

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

		public ChangeType getChangeType() {
			return changeType;
		}

		public void setChangeType(ChangeType changeType) {
			this.changeType = changeType;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
