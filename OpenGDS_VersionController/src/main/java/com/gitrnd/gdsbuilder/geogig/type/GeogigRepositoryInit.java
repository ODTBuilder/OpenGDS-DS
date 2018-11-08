package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GeogigRepositoryInit {

	private String success;

	private Repo repo;

	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@XmlElement(name = "repo")
	public Repo getRepo() {
		return repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlRootElement(name = "repo")
	public static class Repo {

		private String name;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
