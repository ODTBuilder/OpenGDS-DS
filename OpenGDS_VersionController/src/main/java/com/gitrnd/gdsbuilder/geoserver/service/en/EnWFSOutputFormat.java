package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * Geoserver WFS Output 포맷타입
 * @author SG.Lee
 * @since 2017. 6. 5. 오후 5:45:47
 * */
public enum EnWFSOutputFormat {
	GML2("GML2", "gml2"), 
	GML3("GML3", "gml3"), 
	SHP("SHP", "shape-zip"),
	JSON("JSON", "application/json"),
	JSONP("JSONP", "text/javascript"),
	CSV("CSV", "csv"),
	UNKNOWN(null,null);
	
	
	String type;
	String typeName;
	
	private EnWFSOutputFormat(String type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}
	
	/**
	 * type명으로 부터 {@link EnWFSOutputFormat} 조회
	 * @author SG.LEE
	 * @param type명
	 * @return
	 */
	public static EnWFSOutputFormat getFromType(String type) {
		for (EnWFSOutputFormat format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.type.equals(type.toUpperCase()))
				return format;
		}
		return UNKNOWN;
	}
	
	/**
	 * typename으로 부터 {@link EnWFSOutputFormat} 조회
	 * @author SG.LEE
	 * @param typeName명
	 * @return
	 */
	public static EnWFSOutputFormat getFromTypeName(String typeName) {
		for (EnWFSOutputFormat format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.typeName.equals(typeName.toLowerCase()))
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
