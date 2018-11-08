package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GeogigFeatureDiff {

	private String success;

	private List<Diff> diffs;

	private String error;

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

	@XmlRootElement(name = "diff")
	public static class Diff {

		private String attributename;

		private String changetype;

		private String oldvalue;

		private String newvalue;

		private String geometry;

		private String crs;

		@XmlElement(name = "attributename")
		public String getAttributename() {
			return attributename;
		}

		public void setAttributename(String attributename) {
			this.attributename = attributename;
		}

		@XmlElement(name = "changetype")
		public String getChangetype() {
			return changetype;
		}

		public void setChangetype(String changetype) {
			this.changetype = changetype;
		}

		@XmlElement(name = "oldvalue")
		public String getOldvalue() {
			return oldvalue;
		}

		public void setOldvalue(String oldvalue) {
			this.oldvalue = oldvalue;
		}

		@XmlElement(name = "newvalue")
		public String getNewvalue() {
			return newvalue;
		}

		public void setNewvalue(String newvalue) {
			this.newvalue = newvalue;
		}

		@XmlElement(name = "geometry")
		public String getGeometry() {
			return geometry;
		}

		public void setGeometry(String geometry) {
			this.geometry = geometry;
		}

		@XmlElement(name = "crs")
		public String getCrs() {
			return crs;
		}

		public void setCrs(String crs) {
			this.crs = crs;
		}

	}
}
