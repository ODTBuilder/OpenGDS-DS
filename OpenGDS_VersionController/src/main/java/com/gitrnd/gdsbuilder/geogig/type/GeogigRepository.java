/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Repository Command 응답 xml 객체
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "repos")
public class GeogigRepository {

	/**
	 * Repo 목록
	 */
	List<Repo> repos;

	@XmlElement(name = "repo")
	public List<Repo> getRepos() {
		return repos;
	}

	public void setRepos(List<Repo> repos) {
		this.repos = repos;
	}

	/**
	 * repo 객체
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "repo")
	public static class Repo {

		/**
		 * repo id
		 */
		private String id;
		/**
		 * repo name
		 */
		private String name;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
