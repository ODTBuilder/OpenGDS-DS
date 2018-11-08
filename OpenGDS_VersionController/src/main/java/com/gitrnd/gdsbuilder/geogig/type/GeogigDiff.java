package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GeogigDiff {

	private String success;

	private String error;

	private List<Diff> diffs;

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

	@XmlRootElement(name = "diff")
	public static class Diff {

		private String changeType;

		private String newPath;

		private String newObjectId;

		private String path;

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
