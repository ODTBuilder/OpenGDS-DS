/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Status Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigStatus {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * 현재 Checktout 중인 Branch
	 */
	private Header header;
	/**
	 * Staged 상태의 객체 목록
	 */
	private List<Staged> staged;
	/**
	 * Unstaged 상태의 객체 목록
	 */
	private List<Unstaged> unstaged;
	/**
	 * Unmerged 상태의 객체 목록
	 */
	private List<Unmerged> unmerged;
	/**
	 * 오류 메세지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "header")
	public Header getHeader() {
		return header;
	}

	@XmlElement(name = "staged")
	public List<Staged> getStaged() {
		return staged;
	}

	@XmlElement(name = "unstaged")
	public List<Unstaged> getUnstaged() {
		return unstaged;
	}

	@XmlElement(name = "unmerged")
	public List<Unmerged> getUnmerged() {
		return unmerged;
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

	public void setHeader(Header header) {
		this.header = header;
	}

	public void setStaged(List<Staged> staged) {
		this.staged = staged;
	}

	public void setUnstaged(List<Unstaged> unstaged) {
		this.unstaged = unstaged;
	}

	public void setUnmerged(List<Unmerged> unmerged) {
		this.unmerged = unmerged;
	}

	/**
	 * Header 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "header")
	public static class Header {

		/**
		 * Branch 명
		 */
		private String branch;

		@XmlElement(name = "branch")
		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

	}

	/**
	 * Unstaged 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "unstaged")
	public static class Unstaged {

		/**
		 * changeType(ADDED,MODIFIED,REMOVED)
		 */
		private String changeType;
		/**
		 * 최신 경로(changeType이 ADDED,MODIFIED일 경우만 해당)
		 */
		private String newPath;
		/**
		 * 최신 ObjectID(changeType이 ADDED,MODIFIED일 경우만 해당)
		 */
		private String newObjectId;
		/**
		 * 이전 경로(changeType이 MODIFIED, REMOVED일 경우만 해당)
		 */
		private String path;
		/**
		 * 이전 ObjectID(changeType이 MODIFIED, REMOVED일 경우만 해당)
		 */
		private String oldObjectId;

		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		@XmlElement(name = "newPath")
		public String getNewPath() {
			return newPath;
		}

		@XmlElement(name = "newObjectId")
		public String getNewObjectId() {
			return newObjectId;
		}

		@XmlElement(name = "path")
		public String getPath() {
			return path;
		}

		@XmlElement(name = "oldObjectId")
		public String getOldObjectId() {
			return oldObjectId;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		public void setNewPath(String newPath) {
			this.newPath = newPath;
		}

		public void setNewObjectId(String newObjectId) {
			this.newObjectId = newObjectId;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public void setOldObjectId(String oldObjectId) {
			this.oldObjectId = oldObjectId;
		}

	}

	/**
	 * Staged 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "staged")
	public static class Staged {

		/**
		 * changeType(ADDED,MODIFIED,REMOVED)
		 */
		private String changeType;
		/**
		 * 최신 경로(changeType이 ADDED,MODIFIED일 경우만 해당)
		 */
		private String newPath;
		/**
		 * 최신 ObjectID(changeType이 ADDED,MODIFIED일 경우만 해당)
		 */
		private String newObjectId;
		/**
		 * 이전 경로(changeType이 MODIFIED, REMOVED일 경우만 해당)
		 */
		private String path;
		/**
		 * 이전 ObjectID(changeType이 MODIFIED, REMOVED일 경우만 해당)
		 */
		private String oldObjectId;

		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		@XmlElement(name = "newPath")
		public String getNewPath() {
			return newPath;
		}

		@XmlElement(name = "newObjectId")
		public String getNewObjectId() {
			return newObjectId;
		}

		@XmlElement(name = "path")
		public String getPath() {
			return path;
		}

		@XmlElement(name = "oldObjectId")
		public String getOldObjectId() {
			return oldObjectId;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		public void setNewPath(String newPath) {
			this.newPath = newPath;
		}

		public void setNewObjectId(String newObjectId) {
			this.newObjectId = newObjectId;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public void setOldObjectId(String oldObjectId) {
			this.oldObjectId = oldObjectId;
		}

	}

	/**
	 * Unmerged 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "unmerged")
	public static class Unmerged {

		/**
		 * changeType(CONFLICT)
		 */
		private String changeType;
		/**
		 * 충돌 객체 경로
		 */
		private String path;
		/**
		 * 충돌 객체 이전 버전 Commit ID
		 */
		private String ours;
		/**
		 * 충돌 객체 최신 버전 Commit ID
		 */
		private String theirs;
		/**
		 * 상위 버전 Commit ID
		 */
		private String ancestor;

		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		@XmlElement(name = "path")
		public String getPath() {
			return path;
		}

		@XmlElement(name = "ours")
		public String getOurs() {
			return ours;
		}

		@XmlElement(name = "theirs")
		public String getTheirs() {
			return theirs;
		}

		@XmlElement(name = "ancestor")
		public String getAncestor() {
			return ancestor;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public void setOurs(String ours) {
			this.ours = ours;
		}

		public void setTheirs(String theirs) {
			this.theirs = theirs;
		}

		public void setAncestor(String ancestor) {
			this.ancestor = ancestor;
		}

	}

}
