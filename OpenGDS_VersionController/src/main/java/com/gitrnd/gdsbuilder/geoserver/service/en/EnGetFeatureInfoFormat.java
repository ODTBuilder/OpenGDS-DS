package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * WMS GetFeatureInfo Export 포맷
 * EnGetFeatureInfo 타입
 * @author SG.Lee
 * @since 2017. 6. 5. 오후 5:45:47
 * */
public enum EnGetFeatureInfoFormat {
	GML2("GML2", "application/vnd.ogc.gml"), 
	GML3("GML3", "application/vnd.ogc.gml/3.1.1"), 
	TEXT("TEXT", "text/plain"),
	HTML("HTML", "text/html"),
	JSON("JSON", "application/json"),
	JSONP("JSONP", "text/javascript"),
	UNKNOWN(null,null);
	
	
	String type;
	String typeName;
	
	private EnGetFeatureInfoFormat(String type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}
	
	/**
	 * type명으로 부터 {@link EnGetFeatureInfoFormat} 조회
	 * @author SG.LEE
	 * @param type type명
	 * @return {@link EnGetFeatureInfoFormat}
	 */
	public static EnGetFeatureInfoFormat getFromType(String type) {
		for (EnGetFeatureInfoFormat format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.type.equals(type.toUpperCase()))
				return format;
		}
		return UNKNOWN;
	}
	
	/**
	 * typename명으로 부터 {@link EnGetFeatureInfoFormat} 조회
	 * @author SG.LEE
	 * @param typeName typeName명
	 * @return {@link EnGetFeatureInfoFormat}
	 */
	public static EnGetFeatureInfoFormat getFromTypeName(String typeName) {
		for (EnGetFeatureInfoFormat format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.typeName.equals(typeName.toUpperCase()))
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
