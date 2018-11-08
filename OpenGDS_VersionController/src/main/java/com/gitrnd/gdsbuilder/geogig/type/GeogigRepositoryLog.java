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
 * @author GIT
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

	private String error;

	private String nextPage;

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
	 * commit 이력 반환
	 * 
	 * @return List<Commit>
	 */
	@XmlElement(name = "commit")
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * commit message 반환
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Geogig Command 응답 성공 여부 설정
	 * 
	 * @param success Geogig Command 응답 성공 여부
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * commit 이력 설정
	 * 
	 * @param commits commit 이력
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * commit message 설정
	 * 
	 * @param message commit message
	 */
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

		public enum ChangeType {

			ADDS, MODIFIES, REMOVES;

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
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "parents")
	public static class Parents {

		/**
		 * parent commit id
		 */
		private String id;

		/**
		 * parent commit id 반환
		 * 
		 * @return
		 */
		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		/**
		 * parent commit id 설정
		 * 
		 * @param id parent commit id
		 */
		public void setId(String id) {
			this.id = id;
		}

	}

}
