/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Config Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigConfig {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * config list
	 */
	private List<Config> configs;
	/**
	 * config value
	 */
	private String value;

	/**
	 * 오류 메세지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "config")
	public List<Config> getConfigs() {
		return configs;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Config 정보.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "config")
	public static class Config {

		/**
		 * config name
		 */
		private String name;
		/**
		 * config value
		 */
		private String value;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlElement(name = "value")
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
