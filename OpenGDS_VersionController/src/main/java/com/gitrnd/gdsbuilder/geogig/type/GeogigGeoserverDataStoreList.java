package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataStores")
public class GeogigGeoserverDataStoreList {

	private String success;

	private String error;

	private List<DataStore> dataStores;

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

	@XmlElement(name = "dataStore")
	public List<DataStore> getDataStores() {
		return dataStores;
	}

	public void setDataStores(List<DataStore> dataStores) {
		this.dataStores = dataStores;
	}

	@XmlRootElement(name = "dataStore")
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
}
