package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Init Command 응답 xml 객체.
 * 
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigRepositoryInit {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * 생성된 Repository 정보
	 */
	private Repo repo;

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

	/**
	 * Repo 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "repo")
	public static class Repo {

		/**
		 * Repository 이름
		 */
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
