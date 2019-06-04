package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * FeatureTypeList 타입
 * @author SG.LEE
 *
 */
public enum EnFeatureTypeList {
	CONFIGURED("CONFIGURED"), 
	AVAILABLE("AVAILABLE"), 
	ALL("ALL"),
	UNKNOWN(null);
	
	String type;
	
	private EnFeatureTypeList(String type) {
		this.type = type;
	}
	
	/**
	 * type명으로 부터 {@link EnFeatureTypeList} 조회
	 * @author SG.LEE
	 * @param type type명
	 * @return {@link EnFeatureTypeList}
	 */
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
