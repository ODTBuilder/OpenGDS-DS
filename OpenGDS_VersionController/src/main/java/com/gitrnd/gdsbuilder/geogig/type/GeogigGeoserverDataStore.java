package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.gitrnd.gdsbuilder.geogig.type.GeogigGeoserverWorkSpace.Workspace;

@XmlRootElement(name = "dataStore")
public class GeogigGeoserverDataStore {

	private String success;

	private String error;

	private String name;

	private String type;

	private String enabled;

	private Workspace workspace;

	private String _default;

	private ConnectionParameters connetParams;

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
