package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * WFSOutputFormat 타입
 * @author SG.Lee
 * @Date 2017. 6. 5. 오후 5:45:47
 * */
public enum EnFeatureTypeList {
	CONFIGURED("CONFIGURED"), 
	AVAILABLE("AVAILABLE"), 
	ALL("ALL"),
	UNKNOWN(null);
	
	String type;
	
	private EnFeatureTypeList(String type) {
		this.type = type;
	}
	
	public static EnFeatureTypeList getFromType(String type) {
		for (EnFeatureTypeList format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.type.equals(type.toUpperCase()))
				return format;
		}
		return UNKNOWN;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
