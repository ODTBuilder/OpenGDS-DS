package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Merge Command 응답 xml 객체.
 * 
 * @author GIT
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
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "Merge")
	public static class Merge {

		private String ours;
		private String theirs;
		private String ancestor;
		private String mergedCommit;
		private String conflicts;
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
	 * Feature 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "Feature")
	public static class Feature {

		private String change;
		private String id;
		private String ourvalue;
		private String theirvalue;
		private String geometry;
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
