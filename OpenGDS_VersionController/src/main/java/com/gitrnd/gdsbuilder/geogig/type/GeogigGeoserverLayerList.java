package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geoserver Layer 목록 조회 결과 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "list")
public class GeogigGeoserverLayerList {

	/**
	 * Command 응답 성공 여부
	 */
	private String success;

	/**
	 * error message
	 */
	private String error;

	/**
	 * Layer명 목록
	 */
	private List<String> featureTypeNames;

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

	@XmlElement(name = "featureTypeName")
	public List<String> getFeatureTypeNames() {
		return featureTypeNames;
	}

	public void setFeatureTypeNames(List<String> featureTypeNames) {
		this.featureTypeNames = featureTypeNames;
	}

}
