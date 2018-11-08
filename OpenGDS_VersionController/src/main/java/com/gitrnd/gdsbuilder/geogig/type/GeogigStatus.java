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
 * @author GIT
 *
 */
@XmlRootElement(name = "response")
public class GeogigStatus {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	private Header header;

	private List<Staged> staged;

	private List<Unstaged> unstaged;

	private List<Unmerged> unmerged;

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

	@XmlRootElement(name = "header")
	public static class Header {

		private String branch;

		@XmlElement(name = "branch")
		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

	}

	@XmlRootElement(name = "unstaged")
	public static class Unstaged {

		private String changeType;
		private String newPath;
		private String newObjectId;
		private String path;
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

	@XmlRootElement(name = "staged")
	public static class Staged {
		private String changeType;
		private String newPath;
		private String newObjectId;
		private String path;
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

	@XmlRootElement(name = "unmerged")
	public static class Unmerged {
		private String changeType;
		private String path;
		private String ours;
		private String theirs;
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
