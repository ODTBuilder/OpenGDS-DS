package com.gitrnd.gdsbuilder.geoserver.service.wms;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;

/**
 * WMS GetLegendGraphic 정보를 받아 요청 URL을 생성해주는 클래스
 * <p> 
 * WMS 의 역량의 LegendURL 참조를 넘어서 범례 그래픽을 이미지로 생성하기
 * 위한 메커니즘을 제공합니다
 * @author SG.LEE
 *
 */
public class WMSGetLegendGraphic {

	/**
	 * 서비스명
	 */
	private final static String SERVICE = "WMS";
	/**
	 * 작업명
	 */
	private final static String REQUEST = "GetLegendGraphic";
	/**
	 * 서버 URL
	 */
	private String serverURL = "";
	/**
	 * Geoserver 버전
	 */
	private String version = "1.0.0";
	/**
	 * {@link EnWMSOutputFormat} PNG, PNG8, JPEG, GIF, TIFF, TIFF8, GeoTIFF, GeoTIFF8, SVG, PDF, GEORSS, KML, KMZ
	 */
	private EnWMSOutputFormat format = null;
	/**
	 * Map 너비
	 */
	private int width=0;
	/**
	 * Map 높이
	 */
	private int height=0;
	/**
	 * 레이어명
	 */
	private String layer = "";
	/**
	 * 범례 사이즈
	 */
	private int scale = 0;
	/**
	 * 상세 옵션
	 */
	private String legend_options = "";
	/**
	 * 스타일
	 */
	private String style = "";
	/**
	 * SLD 스타일 형식
	 */
	private String sld="";
	/**
	 * SLD 스타일 형식 body
	 */
	private String sld_body="";
	/**
	 * 예외 형식
	 */
	private String exceptions="";
	
	
	/**
	 * {@link WMSGetLegendGraphic} 생성자
	 * @author SG.LEE
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param format {@link EnWMSOutputFormat} PNG, PNG8, JPEG, GIF, TIFF, TIFF8, GeoTIFF, GeoTIFF8, SVG, PDF, GEORSS, KML, KMZ
	 * @param width Map 너비
	 * @param height Map 높이
	 * @param layer 레이어명
	 * @param scale 범례 사이즈
	 * @param legend_options 상세 옵션
	 * @param style 스타일
	 * @param sld SLD 스타일 형식
	 * @param sld_body SLD 스타일 형식 body
	 * @param exceptions 예외 형식
	 */
	public WMSGetLegendGraphic(String serverURL, String version, EnWMSOutputFormat format, int width, int height, String layer, int scale, String legend_options, String style, String sld, String sld_body, String exceptions){
		super();
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (!version.trim().equals("")) {
			this.version = version;
		}
		if (format!=null) {
			this.format = format;
		}
		if (width != 0) {
			this.width = width;
		}
		if (height != 0) {
			this.height = height;
		}
		if (scale != 0) {
			this.scale = scale;
		}
		if (!layer.trim().equals("")) {
			this.layer = layer;
		}
		if (!legend_options.trim().equals("")) {
			this.legend_options = legend_options;
		}
		if (!style.trim().equals("")) {
			this.style = style;
		}
		if (!sld.trim().equals("")) {
			this.sld = sld;
		}
		if (!sld_body.trim().equals("")) {
			this.sld_body = sld_body;
		}
		if (!exceptions.trim().equals("")) {
			this.exceptions = exceptions;
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
	public EnWMSOutputFormat getFormat() {
		return format;
	}
	public void setFormat(EnWMSOutputFormat format) {
		this.format = format;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public String getLegend_options() {
		return legend_options;
	}
	public void setLegend_options(String legend_options) {
		this.legend_options = legend_options;
	}
	public static String getService() {
		return SERVICE;
	}
	public static String getRequest() {
		return REQUEST;
	}
	
	/**
	 * WMS GetLegendGraphic 서비스 URL 생성
	 * @author SG.LEE
	 * @return WMS GetLegendGraphic URL
	 */
	public String getWMSGetLegendGraphicURL() {
		StringBuffer urlBuffer = new StringBuffer();
		if (!this.serverURL.equals("")) {
			urlBuffer.append(serverURL);
			urlBuffer.append("?");
			urlBuffer.append("request=" + REQUEST);
			urlBuffer.append("&");
			urlBuffer.append("service=" + SERVICE);
			if (!this.version.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("version=" + version);
			}
			if (!this.format.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("format=" + format.getTypeName());
			}
			if (!this.layer.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("layer=" + layer);
			}
			if (!this.legend_options.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("legend_options=" + legend_options);
			}
			if (!this.style.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("style=" + style);
				urlBuffer.append("&");
				urlBuffer.append("STRICT=false");
			}
			if (!this.sld.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("sld=" + sld);
			}
			if (!this.sld_body.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("sld_body=" + sld_body);
			}
			if (!this.exceptions.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("exceptions=" + exceptions);
			}
			urlBuffer.append("&");
			urlBuffer.append("width=" + String.valueOf(this.width));
			urlBuffer.append("&");
			urlBuffer.append("height=" + String.valueOf(this.height));
			urlBuffer.append("&");
			urlBuffer.append("scale=" + String.valueOf(this.scale));
		} else
			return "";
		return urlBuffer.toString();
	}
}
