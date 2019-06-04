package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.gitrnd.gdsbuilder.geogig.type.GeogigGeoserverWorkSpace.Workspace;

/**
 * Geoserver DataStore 조회 결과 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "dataStore")
public class GeogigGeoserverDataStore {

	/**
	 * Command 응답 성공 여부
	 */
	private String success;

	/**
	 * error message
	 */
	private String error;

	/**
	 * Geoserver DataStore명
	 */
	private String name;

	/**
	 * Geoserver DataStore 타입
	 */
	private String type;

	/**
	 * Geoserver DataStore 상태
	 */
	private String enabled;

	/**
	 * Geoserver DataStore를 포함한 Workspace 명
	 */
	private Workspace workspace;

	private String _default;

	/**
	 * Geoserver DataStore 생성 파라미터
	 */
	ConnectionParameters connetParams;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "enabled")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@XmlElement(name = "workspace")
	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	@XmlElement(name = "__default")
	public String get_default() {
		return _default;
	}

	public void set_default(String _default) {
		this._default = _default;
	}

	@XmlElement(name = "connectionParameters")
	public ConnectionParameters getConnetParams() {
		return connetParams;
	}

	public void setConnetParams(ConnectionParameters connetParams) {
		this.connetParams = connetParams;
	}

	/**
	 * Geoserver DataStore 정보.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "workspace")
	public static class DataStore {

		private String name;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	/**
	 * Geoserver DataStore 생성 파라미터.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "connectionParameters")
	public static class ConnectionParameters {

		private List<Entry> entryList;

		@XmlElement(name = "entry")
		public List<Entry> getEntryList() {
			return entryList;
		}

		public void setEntryList(List<Entry> entryList) {
			this.entryList = entryList;
		}

	}

	/**
	 * Geoserver DataStore 접근 정보.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Entry {

		@XmlAttribute
		private String key;

		@XmlValue
		private String xmlValue;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getXmlValue() {
			return xmlValue;
		}

		public void setXmlValue(String xmlValue) {
			this.xmlValue = xmlValue;
		}

	}

}
