package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geoserver DataStore 목록 조회 결과 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "dataStores")
public class GeogigGeoserverDataStoreList {

	/**
	 * Command 응답 성공 여부
	 */
	private String success;

	/**
	 * error message
	 */
	private String error;

	/**
	 * DataStore 목록
	 */
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

	/**
	 * DataStore 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "dataStore")
	public static class DataStore {

		/**
		 * DataStore명
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
