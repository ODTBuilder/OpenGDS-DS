/**
 * 
 */
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
public class GeogigRepositoryLog {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * commit 이력
	 */
	private List<Commit> commits;
	/**
	 * commit message
	 */
	private String message;

	/**
	 * error message
	 */
	private String error;

	/**
	 * 30개 이상의 commit 이력이 있는 경우 "true"
	 */
	private String nextPage;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "commit")
	public List<Commit> getCommits() {
		return commits;
	}

	public String getMessage() {
		return message;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "nextPage")
	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * Commit 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "commit")
	public static class Commit {

		/**
		 * Commit 변경 타입.
		 * 
		 * @author DY.Oh
		 *
		 */
		public enum ChangeType {
		/**
		 * 생성
		 */
		ADDED,
		/**
		 * 수정
		 */
		MODIFIED,
		/**
		 * 삭제
		 */
		REMOVED;
		}

		/**
		 * commitId
		 */
		private String commitId;
		/**
		 * tree id
		 */
		private String tree;
		/**
		 * parent commit id
		 */
		private Parents parents;
		/**
		 * GeogigCommit
		 */
		private GeogigCommitter committer;
		/**
		 * GeogigAuthor
		 */
		private GeogigAuthor author;

		/**
		 * message
		 */
		private String message;

		/**
		 * 생성된 feature 개수
		 */
		private String adds;
		/**
		 * 변경된 feature 개수
		 */
		private String modifies;
		/**
		 * 삭제된 feature 개수
		 */
		private String removes;

		@XmlElement(name = "id")
		public String getCommitId() {
			return commitId;
		}

		@XmlElement(name = "tree")
		public String getTree() {
			return tree;
		}

		@XmlElement(name = "parents")
		public Parents getParents() {
			return parents;
		}

		@XmlElement(name = "committer")
		public GeogigCommitter getCommitter() {
			return committer;
		}

		@XmlElement(name = "author")
		public GeogigAuthor getAuthor() {
			return author;
		}

		@XmlElement(name = "adds")
		public String getAdds() {
			return adds;
		}

		@XmlElement(name = "modifies")
		public String getModifies() {
			return modifies;
		}

		@XmlElement(name = "removes")
		public String getRemoves() {
			return removes;
		}

		@XmlElement(name = "message")
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setCommitId(String commitId) {
			this.commitId = commitId;
		}

		public void setTree(String tree) {
			this.tree = tree;
		}

		public void setParents(Parents parents) {
			this.parents = parents;
		}

		public void setCommitter(GeogigCommitter committer) {
			this.committer = committer;
		}

		public void setAuthor(GeogigAuthor author) {
			this.author = author;
		}

		public void setAdds(String adds) {
			this.adds = adds;
		}

		public void setModifies(String modifies) {
			this.modifies = modifies;
		}

		public void setRemoves(String removes) {
			this.removes = removes;
		}

	}

	/**
	 * Parents 객체
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "parents")
	public static class Parents {

		/**
		 * parent commit id
		 */
		private String id;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

}
