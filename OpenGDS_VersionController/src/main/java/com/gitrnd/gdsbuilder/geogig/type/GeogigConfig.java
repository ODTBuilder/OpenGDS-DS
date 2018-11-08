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
 * @author GIT
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

	private String error;

	/**
	 * @return the success
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * @return the configs
	 */
	@XmlElement(name = "config")
	public List<Config> getConfigs() {
		return configs;
	}

	/**
	 * @return the value
	 */
	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param configs the configs to set
	 */
	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	/**
	 * @param value the value to set
	 */
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

		/**
		 * @return the name
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the value
		 */
		@XmlElement(name = "value")
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

	}

}
