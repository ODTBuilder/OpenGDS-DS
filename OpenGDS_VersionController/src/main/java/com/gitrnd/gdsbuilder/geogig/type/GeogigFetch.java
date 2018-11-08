package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GeogigFetch {

	private String success;

	private List<Remote> remotes;

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

	@XmlRootElement(name = "Remote")
	public static class Remote {

		private String remoteURL;

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

		@XmlRootElement(name = "Branch")
		public static class Branch {

			private String changeType;

			private String name;

			private String newValue;

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
