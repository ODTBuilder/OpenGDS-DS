package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Merge Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigMerge {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * merge 결과
	 */
	private Merge merge;

	/**
	 * error message
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "Merge")
	public Merge getMerge() {
		return merge;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setMerge(Merge merge) {
		this.merge = merge;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Merge 객체
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Merge")
	public static class Merge {

		/**
		 * 현재 Checkout 중인 Branch의 Commit Object ID
		 */
		private String ours;
		/**
		 * 병합하고자 하는 Branch의 Commit Object ID
		 */
		private String theirs;
		/**
		 * 현재 Checkout 중인 Branch의 상위 버전 Commit Object ID
		 */
		private String ancestor;
		/**
		 * 병합 완료 후 새로 생성된 버전의 Commit Object ID
		 */
		private String mergedCommit;
		/**
		 * 병합 시 충돌 발생 객체 수
		 */
		private String conflicts;
		/**
		 * 병합 시 충돌 발생 객체 목록
		 */
		private List<Feature> features;

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

		@XmlElement(name = "mergedCommit")
		public String getMergedCommit() {
			return mergedCommit;
		}

		@XmlElement(name = "conflicts")
		public String getConflicts() {
			return conflicts;
		}

		@XmlElement(name = "Feature")
		public List<Feature> getFeatures() {
			return features;
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

		public void setMergedCommit(String mergedCommit) {
			this.mergedCommit = mergedCommit;
		}

		public void setConflicts(String conflicts) {
			this.conflicts = conflicts;
		}

		public void setFeatures(List<Feature> features) {
			this.features = features;
		}

	}

	/**
	 * Feature 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Feature")
	public static class Feature {

		/**
		 * 병합 결과(ADDED, MODIFIED, REMOVED, CONFLICT)
		 */
		private String change;
		/**
		 * 객체 경로
		 */
		private String id;
		/**
		 * Checkout 중인 Branch의 객체 Object ID
		 */
		private String ourvalue;
		/**
		 * 병합하고자 하는 Branch의 객체 Object ID
		 */
		private String theirvalue;
		/**
		 * Geometry (ex.POINT (-61.42671331549701 -16.869396995266598))
		 */
		private String geometry;
		/**
		 * 좌표계 (ex.EPSG:4326)
		 */
		private String crs;

		@XmlElement(name = "change")
		public String getChange() {
			return change;
		}

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "ourvalue")
		public String getOurvalue() {
			return ourvalue;
		}

		@XmlElement(name = "theirvalue")
		public String getTheirvalue() {
			return theirvalue;
		}

		@XmlElement(name = "geometry")
		public String getGeometry() {
			return geometry;
		}

		@XmlElement(name = "crs")
		public String getCrs() {
			return crs;
		}

		public void setChange(String change) {
			this.change = change;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setOurvalue(String ourvalue) {
			this.ourvalue = ourvalue;
		}

		public void setTheirvalue(String theirvalue) {
			this.theirvalue = theirvalue;
		}

		public void setGeometry(String geometry) {
			this.geometry = geometry;
		}

		public void setCrs(String crs) {
			this.crs = crs;
		}

	}

}
