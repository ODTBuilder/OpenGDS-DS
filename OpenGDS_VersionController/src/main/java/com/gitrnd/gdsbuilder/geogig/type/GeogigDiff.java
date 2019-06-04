package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Diff Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigDiff {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * 오류 메세지
	 */
	private String error;
	/**
	 * Diff 목록
	 */
	private List<Diff> diffs;
	/**
	 * nextPage 유무
	 */
	private String nextPage;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@XmlElement(name = "diff")
	public List<Diff> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<Diff> diffs) {
		this.diffs = diffs;
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
	 * Diff 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "diff")
	public static class Diff {

		/**
		 * changeType(ADDED 또는 MODIFIED 또는 REMOVED)
		 */
		private String changeType;

		/**
		 * 최신 Path. ADDED 또는 MODIFIED 객체만 해당
		 */
		private String newPath;

		/**
		 * 최신 ObjectId. ADDED 또는 MODIFIED 객체만 해당
		 */
		private String newObjectId;

		/**
		 * 이전 Path. MODIFIED 객체만 해당
		 */
		private String path;

		/**
		 * 이전 ObjectId. MODIFIED 객체만 해당
		 */
		private String oldObjectId;

		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		@XmlElement(name = "newPath")
		public String getNewPath() {
			return newPath;
		}

		public void setNewPath(String newPath) {
			this.newPath = newPath;
		}

		@XmlElement(name = "newObjectId")
		public String getNewObjectId() {
			return newObjectId;
		}

		public void setNewObjectId(String newObjectId) {
			this.newObjectId = newObjectId;
		}

		@XmlElement(name = "path")
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@XmlElement(name = "oldObjectId")
		public String getOldObjectId() {
			return oldObjectId;
		}

		public void setOldObjectId(String oldObjectId) {
			this.oldObjectId = oldObjectId;
		}

	}

}
