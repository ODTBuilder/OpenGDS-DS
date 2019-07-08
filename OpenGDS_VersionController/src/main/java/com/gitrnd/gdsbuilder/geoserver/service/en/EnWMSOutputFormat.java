package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * Geoserver WMS Output 포맷타입
 * @author SG.Lee
 * @since 2017. 6. 5. 오후 5:45:47
 * */
public enum EnWMSOutputFormat {
	PNG("PNG", "image/png"), 
	PNG8("PNG8", "image/png8"), 
	JPEG("JPEG", "image/jpeg"),
	GIF("GIF", "image/gif"),
	TIFF("TIFF", "image/tiff"),
	TIFF8("TIFF8", "image/tiff8"),
	GeoTIFF("GeoTIFF", "image/geotiff"),
	GeoTIFF8("GeoTIFF8", "image/geotiff8"),
	SVG("SVG", "image/svg"),
	PDF("PDF", "application/pdf"),
	GEORSS("GEORSS", "rss"),
	KML("KML", "kml"),
	KMZ("KMZ", "kmz"),
	UNKNOWN(null,null);
	
	String type;
	String typeName;
	
	private EnWMSOutputFormat(String type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}
	
	/**
	 * type명으로 부터 {@link EnWMSOutputFormat} 조회
	 * @author SG.LEE
	 * @param type type명
	 * @return {@link EnWMSOutputFormat}
	 */
	public static EnWMSOutputFormat getFromType(String type) {
		for (EnWMSOutputFormat format : values()) {
			if(format == UNKNOWN)
				continue;
			if(format.type.equals(type.toUpperCase()))
				return format;
		}
		return UNKNOWN;
	}
	
	/**
	 * typename으로 부터 {@link EnWMSOutputFormat} 조회 
	 * @author SG.LEE
	 * @param typeName명
	 * @return {@link EnWMSOutputFormat}
	 */
	public static EnWMSOutputFormat getFromTypeName(String typeName) {
		for (EnWMSOutputFormat format : values()) {
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
