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
 * @author GIT
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
	 * Geogig Command 실패 여부
	 */
	private String error;

	/**
	 * @return the success
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * @return the pull
	 */
	@XmlElement(name = "Pull")
	public Pull getPull() {
		return pull;
	}

	/**
	 * @return the error
	 */
	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	/**
	 * @return the merge
	 */
	@XmlElement(name = "Merge")
	public Merge getMerge() {
		return merge;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param pull
	 *            the pull to set
	 */
	public void setPull(Pull pull) {
		this.pull = pull;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @param merge
	 *            the merge to set
	 */
	public void setMerge(Merge merge) {
		this.merge = merge;
	}

	@XmlRootElement(name = "Pull")
	public static class Pull {

		private Fetch fetch;

		private String remote;

		private String ref;

		private String added;

		private String modified;

		private String removed;

		private Merge merge;

		/**
		 * @return the fetch
		 */
		@XmlElement(name = "Fetch")
		public Fetch getFetch() {
			return fetch;
		}

		/**
		 * @return the remote
		 */
		@XmlElement(name = "Remote")
		public String getRemote() {
			return remote;
		}

		/**
		 * @return the ref
		 */
		@XmlElement(name = "Ref")
		public String getRef() {
			return ref;
		}

		/**
		 * @return the added
		 */
		@XmlElement(name = "Added")
		public String getAdded() {
			return added;
		}

		/**
		 * @return the modified
		 */
		@XmlElement(name = "Modified")
		public String getModified() {
			return modified;
		}

		/**
		 * @return the removed
		 */
		@XmlElement(name = "Removed")
		public String getRemoved() {
			return removed;
		}

		/**
		 * @return the merge
		 */
		@XmlElement(name = "Merge")
		public Merge getMerge() {
			return merge;
		}

		/**
		 * @param merge
		 *            the merge to set
		 */
		public void setMerge(Merge merge) {
			this.merge = merge;
		}

		/**
		 * @param fetch
		 *            the fetch to set
		 */
		public void setFetch(Fetch fetch) {
			this.fetch = fetch;
		}

		/**
		 * @param remote
		 *            the remote to set
		 */
		public void setRemote(String remote) {
			this.remote = remote;
		}

		/**
		 * @param ref
		 *            the ref to set
		 */
		public void setRef(String ref) {
			this.ref = ref;
		}

		/**
		 * @param added
		 *            the added to set
		 */
		public void setAdded(String added) {
			this.added = added;
		}

		/**
		 * @param modified
		 *            the modified to set
		 */
		public void setModified(String modified) {
			this.modified = modified;
		}

		/**
		 * @param removed
		 *            the removed to set
		 */
		public void setRemoved(String removed) {
			this.removed = removed;
		}

	}

	@XmlRootElement(name = "Fetch")
	public static class Fetch {

		private Remote remote;

		/**
		 * @return the remote
		 */
		@XmlElement(name = "Remote")
		public Remote getRemote() {
			return remote;
		}

		/**
		 * @param remote
		 *            the remote to set
		 */
		public void setRemote(Remote remote) {
			this.remote = remote;
		}

	}

	@XmlRootElement(name = "Remote")
	public static class Remote {

		private String remoteURL;

		private List<Branch> branchs;

		/**
		 * @return the remoteURL
		 */
		@XmlElement(name = "remoteURL")
		public String getRemoteURL() {
			return remoteURL;
		}

		/**
		 * @return the branchs
		 */
		@XmlElement(name = "Branch")
		public List<Branch> getBranchs() {
			return branchs;
		}

		/**
		 * @param remoteURL
		 *            the remoteURL to set
		 */
		public void setRemoteURL(String remoteURL) {
			this.remoteURL = remoteURL;
		}

		/**
		 * @param branchs
		 *            the branchs to set
		 */
		public void setBranchs(List<Branch> branchs) {
			this.branchs = branchs;
		}

	}

	@XmlRootElement(name = "Branch")
	public static class Branch {

		private String changeType;

		private String name;

		private String newValue;

		/**
		 * @return the changeType
		 */
		@XmlElement(name = "changeType")
		public String getChangeType() {
			return changeType;
		}

		/**
		 * @return the name
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * @return the newValue
		 */
		@XmlElement(name = "newValue")
		public String getNewValue() {
			return newValue;
		}

		/**
		 * @param changeType
		 *            the changeType to set
		 */
		public void setChangeType(String changeType) {
			this.changeType = changeType;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param newValue
		 *            the newValue to set
		 */
		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}

	}

}
