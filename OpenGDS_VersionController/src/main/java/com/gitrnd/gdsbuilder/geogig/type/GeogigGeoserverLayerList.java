package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "list")
public class GeogigGeoserverLayerList {

	private String success;

	private String error;

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
