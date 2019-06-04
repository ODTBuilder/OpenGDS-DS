/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.gitrnd.gdsbuilder.geogig.type.GeogigMerge.Merge;

/**
 * Geogig Pull Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigPull {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * Pull 객체
	 */
	private Pull pull;
	/**
	 * Merge 객체
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

	@XmlElement(name = "Pull")
	public Pull getPull() {
		return pull;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	@XmlElement(name = "Merge")
	public Merge getMerge() {
		return merge;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setPull(Pull pull) {
		this.pull = pull;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setMerge(Merge merge) {
		this.merge = merge;
	}

	/**
	 * Pull 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Pull")
	public static class Pull {

		/**
		 * Fetch 정보
		 */
		private Fetch fetch;

		/**
		 * Remote Repository 위치
		 */
		private String remote;

		/**
		 * Remote Repository Branch명
		 */
		private String ref;

		/**
		 * 새로 추가된 객체 수
		 */
		private String added;

		/**
		 * 수정된 객체 수
		 */
		private String modified;

		/**
		 * 삭제된 객체 수
		 */
		private String removed;

		/**
		 * 병합 결과
		 */
		private Merge merge;

		@XmlElement(name = "Fetch")
		public Fetch getFetch() {
			return fetch;
		}

		@XmlElement(name = "Remote")
		public String getRemote() {
			return remote;
		}

		@XmlElement(name = "Ref")
		public String getRef() {
			return ref;
		}

		@XmlElement(name = "Added")
		public String getAdded() {
			return added;
		}

		@XmlElement(name = "Modified")
		public String getModified() {
			return modified;
		}

		@XmlElement(name = "Removed")
		public String getRemoved() {
			return removed;
		}

		@XmlElement(name = "Merge")
		public Merge getMerge() {
			return merge;
		}

		public void setMerge(Merge merge) {
			this.merge = merge;
		}

		public void setFetch(Fetch fetch) {
			this.fetch = fetch;
		}

		public void setRemote(String remote) {
			this.remote = remote;
		}

		public void setRef(String ref) {
			this.ref = ref;
		}

		public void setAdded(String added) {
			this.added = added;
		}

		public void setModified(String modified) {
			this.modified = modified;
		}

		public void setRemoved(String removed) {
			this.removed = removed;
		}

	}

	/**
	 * Fetch 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Fetch")
	public static class Fetch {

		/**
		 * Remote Repository Fetch 결과
		 */
		private Remote remote;

		@XmlElement(name = "Remote")
		public Remote getRemote() {
			return remote;
		}

		public void setRemote(Remote remote) {
			this.remote = remote;
		}

	}

	/**
	 * Remote 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Remote")
	public static class Remote {

		/**
		 * Remote Repository url
		 */
		private String remoteURL;

		/**
		 * Remote Repository의 Branch Fetch 목록
		 */
		private List<Branch> branchs;

		@XmlElement(name = "remoteURL")
		public String getRemoteURL() {
			return remoteURL;
		}

		@XmlElement(name = "Branch")
		public List<Branch> getBranchs() {
			return branchs;
		}

		public void setRemoteURL(String remoteURL) {
			this.remoteURL = remoteURL;
		}

		public void setBranchs(List<Branch> branchs) {
			this.branchs = branchs;
		}

	}

	/**
	 * Branch 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Branch")
	public static class Branch {

		/**
		 * changeType (CHANGED_REF)
		 */
		private String changeType;

		/**
		 * Branch 명
		 */
		private String name;

		/**
		 * 최신 버전의 Commit Object ID
		 */
		private String newValue;

		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "newValue")
		public String getNewValue() {
			return newValue;
		}

		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}

	}

}
