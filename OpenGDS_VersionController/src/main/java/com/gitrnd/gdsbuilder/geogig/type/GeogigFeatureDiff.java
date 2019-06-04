package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig FeatureDiff Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigFeatureDiff {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * Diff 목록
	 */
	private List<Diff> diffs;

	/**
	 * error message
	 */
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

	/**
	 * Diff 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "diff")
	public static class Diff {

		/**
		 * 속성명
		 */
		private String attributename;

		/**
		 * 이전 버전의 Feature와의 변경 상태(MODIFIED, REMOVED)
		 */
		private String changetype;

		/**
		 * 이전 버전의 속성 값
		 */
		private String oldvalue;

		/**
		 * 최신 버전의 속성 값
		 */
		private String newvalue;

		/**
		 * 속성이 Geometry인 경우 "true"
		 */
		private String geometry;

		/**
		 * 좌표계
		 */
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
