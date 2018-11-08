package com.gitrnd.gdsbuilder.geoserver.service.wfs;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnGeoserverService;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;

public class WFSGetFeature {
	private final static String SERVICE = EnGeoserverService.WFS.getState();
	private final static String REQUEST = "GetFeature";
	
	private String serverURL ="";
	private String version="1.1.1";
	private String typeName="";
	private EnWFSOutputFormat outputformat=null;
	private int maxFeatures = 0; 
	private String bbox="";
	private String format_options="";
	private String featureID = "";
	private String sortBy = "";
	private String propertyName = "";
	private String srsName="";
	
	public WFSGetFeature(){};
	
	public WFSGetFeature(String serverURL, String version, String typeName, EnWFSOutputFormat outputformat, int maxFeatures, String bbox,
			String format_options, String featureID, String sortBy, String propertyName, String srsName) {
		super();
		if(!serverURL.trim().equals("")){
			this.serverURL = serverURL;
		}
		if (!version.trim().equals("")) {
			this.version = version;
		}
		if (!typeName.trim().equals("")) {
			this.typeName = typeName;
		}
		if (outputformat!=null) {
			this.outputformat = outputformat;
		}
		if (maxFeatures!=0) {
			this.maxFeatures = maxFeatures;
		}
		if (!bbox.trim().equals("")) {
			this.bbox = bbox;
		}
		if (!format_options.trim().equals("")) {
			this.format_options = format_options;
		}
		if (!featureID.trim().equals("")) {
			this.featureID = featureID;
		}
		if (!sortBy.trim().equals("")) {
			this.sortBy = sortBy;
		}
		if (!propertyName.trim().equals("")) {
			this.propertyName = propertyName;
		}
		if (!srsName.trim().equals("")) {
			this.srsName = srsName;
		}
	}
	
	public String getSrsName() {
		return srsName;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	public WFSGetFeature(String serverURL, String version, String typeName) {
		super();
		if(!serverURL.trim().equals("")){
			this.serverURL = serverURL;
		}
		if (!version.trim().equals("")) {
			this.version = version;
		}
		if (!typeName.trim().equals("")) {
			this.typeName = typeName;
		}
	}
	
	public String getServerURL() {
		return serverURL;
	}
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public EnWFSOutputFormat getOutputformat() {
		return outputformat;
	}
	public void setOutputformat(EnWFSOutputFormat outputformat) {
		this.outputformat = outputformat;
	}
	public int getMaxFeatures() {
		return maxFeatures;
	}
	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}
	public String getBbox() {
		return bbox;
	}
	public void setBbox(String bbox) {
		this.bbox = bbox;
	}
	public String getFormat_options() {
		return format_options;
	}
	public void setFormat_options(String format_options) {
		this.format_options = format_options;
	}
	public static String getService() {
		return SERVICE;
	}
	public static String getRequest() {
		return REQUEST;
	}
	public String getFeatureID() {
		return featureID;
	}
	public void setFeatureID(String featureID) {
		this.featureID = featureID;
	}
	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String getWFSGetFeatureURL(){
		StringBuffer urlBuffer = new StringBuffer();
		if(!this.serverURL.trim().equals("")){
			
			if(serverURL.equals("")||version.equals("")||typeName.equals("")){
				throw new NullPointerException("필수값을 입력하지 않았습니다.");
			}
			
			
			urlBuffer.append(serverURL);
			urlBuffer.append("?");
			urlBuffer.append("request="+REQUEST);
			urlBuffer.append("&");
			urlBuffer.append("service="+SERVICE);
			if(!this.version.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("version="+version);
			}
			if(!this.typeName.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("typeNames="+typeName);
			}
			if(this.outputformat!=null){
				urlBuffer.append("&");
				urlBuffer.append("outputformat="+outputformat.getTypeName());
			}
			if(this.maxFeatures!=0){
				urlBuffer.append("&");
				urlBuffer.append("maxFeatures="+String.valueOf(maxFeatures));
			}
			if(!this.bbox.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("bbox="+bbox);
			}
			if(!this.format_options.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("format_options="+format_options);
			}
			if(!this.featureID.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("featureID="+featureID);
			}
			if(!this.sortBy.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("sortBy="+sortBy);
			}
			if(!this.propertyName.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("propertyName="+propertyName);
			}if(!this.srsName.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("srsName="+srsName);
			}
		}
		else
			return "";
		return urlBuffer.toString();
	}
}
