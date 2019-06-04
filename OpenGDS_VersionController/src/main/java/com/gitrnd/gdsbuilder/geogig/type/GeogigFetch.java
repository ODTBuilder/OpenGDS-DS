package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Fetch Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigFetch {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * Remote 목록
	 */
	private List<Remote> remotes;

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

	@XmlElementWrapper(name = "Fetch")
	@XmlElement(name = "Remote")
	public List<Remote> getRemotes() {
		return remotes;
	}

	public void setRemotes(List<Remote> remotes) {
		this.remotes = remotes;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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
		 * Remote Repository Branch Updeate 목록
		 */
		private List<Branch> branchs;

		@XmlElement(name = "remoteURL")
		public String getRemoteURL() {
			return remoteURL;
		}

		public void setRemoteURL(String remoteURL) {
			this.remoteURL = remoteURL;
		}

		@XmlElement(name = "Branch")
		public List<Branch> getBranchs() {
			return branchs;
		}

		public void setBranchs(List<Branch> branchs) {
			this.branchs = branchs;
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
			 * change type(ADDED_REF, MODIFYED_REF, REMOVED_REF)
			 */
			private String changeType;

			/**
			 * Branch명
			 */
			private String name;

			/**
			 * 최신 버전의 Branch Object ID
			 */
			private String newValue;

			/**
			 * 이전 버전의 Branch Object ID
			 */
			private String oldValue;

			@XmlElement(name = "changeType")
			public String getChangeType() {
				return changeType;
			}

			public void setChangeType(String changeType) {
				this.changeType = changeType;
			}

			@XmlElement(name = "name")
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			@XmlElement(name = "newValue")
			public String getNewValue() {
				return newValue;
			}

			public void setNewValue(String newValue) {
				this.newValue = newValue;
			}

			@XmlElement(name = "oldValue")
			public String getOldValue() {
				return oldValue;
			}

			public void setOldValue(String oldValue) {
				this.oldValue = oldValue;
			}

		}
	}

}
