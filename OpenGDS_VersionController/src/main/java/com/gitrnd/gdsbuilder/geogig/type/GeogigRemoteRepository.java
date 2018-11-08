/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Remote Command 응답 xml 객체.
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "response")
public class GeogigRemoteRepository {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	private String name;

	private String error;

	private Ping ping;

	private List<Remote> remotes;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	@XmlElement(name = "ping")
	public Ping getPing() {
		return ping;
	}

	@XmlElement(name = "Remote")
	public List<Remote> getRemotes() {
		return remotes;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setPing(Ping ping) {
		this.ping = ping;
	}

	public void setRemotes(List<Remote> remotes) {
		this.remotes = remotes;
	}

	@XmlRootElement(name = "ping")
	public static class Ping {
		private String success;

		@XmlElement(name = "success")
		public String getSuccess() {
			return success;
		}

		public void setSuccess(String success) {
			this.success = success;
		}
	}

	@XmlRootElement(name = "Remote")
	public static class Remote {

		private String name;

		private String url;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "url")
		public String getUrl() {
			return url;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
