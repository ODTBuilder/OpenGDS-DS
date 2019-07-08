package com.gitrnd.gdsbuilder.geoserver.service.wms;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnGetFeatureInfoFormat;

/**
 * WMS GetFeatureInfo 정보를 받아 요청 URL을 생성해주는 클래스
 * <p>
 * 맵 상의 특정 위치에 있는 피처에 대한 공간 및 속성 데이터를 요청 WFS 의GetFeature 작업과 유사하지만 , 입력 및 출력 면에서
 * 융통성이 부족 GeoServer 가 WFS 서비스를 제공하고 있으므로 가능한 GetFeatureInfo 대신 GetFeature 를 이용할
 * 것을 권장함
 * 
 * @author SG.LEE
 * @since 2018. 7. 17. 오후 1:26:26
 */
public class WMSGetFeatureInfo {
	/**
	 * 서비스명
	 */
	private final static String SERVICE = "WMS";
	/**
	 * 작업명
	 */
	private final static String REQUEST = "GetFeatureInfo";
	/**
	 * 서버 URL
	 */
	private String serverURL = "";
	/**
	 * Geoserver 버전
	 */
	private String version = "1.0.0";
	/**
	 * 레이어명
	 */
	private String layers = "";
	/**
	 * 스타일
	 */
	private String styles = "";
	/**
	 * 좌표계
	 */
	private String crs = ""; // 1.0.0 , 1.1.0 , 1.1.1 버전일경우
	/**
	 * 좌표계
	 */
	private String srs = ""; // 1.3.0 버전일경우
	/**
	 * Map 크기
	 */
	private String bbox = "";
	/**
	 * Map 너비
	 */
	private int width = 0;
	/**
	 * Map 높이
	 */
	private int height = 0;
	/**
	 * 쿼리할 하나 이상의 레이어를 쉼표로 구분
	 */
	private String query_layers = "";
	/**
	 * FeatureInfo Output Format {@link EnGetFeatureInfoFormat} GML2, GML3, TEXT,
	 * HTML, JSON, JSONP
	 */
	private EnGetFeatureInfoFormat info_format = null;
	/**
	 * 포맷 옵션
	 */
	private String format_options = "";
	/**
	 * 반환할 피처 최대 개수
	 */
	private int feature_count = 0;
	/**
	 * 지도상의 X 좌표의 픽셀값
	 */
	private Integer x = null;
	/**
	 * 지도상의 Y 좌표의 픽셀값
	 */
	private Integer y = null;
	/**
	 * 지도상의 X 좌표의 픽셀값 1.3.0 버전일경우 x->i
	 */
	private Integer i = null; //
	/**
	 * 지도상의 Y 좌표의 픽셀값 1.3.0 버전일경우 y->j
	 */
	private Integer j = null;
	/**
	 * 예외 보고 형식 기본값 application/vnd.ogc.se_xml
	 */
	private String exceptions = "";

	/**
	 * {@link WMSGetFeatureInfo} 생성자
	 * 
	 * @author SG.LEE
	 * @param serverURL      서버 URL
	 * @param version        Geoserver 버전
	 * @param layers         레이어명
	 * @param styles         스타일
	 * @param crs            좌표계 Geoserver 버전이 1.0.0 or 1.1.0 or 1.1.1 일 경우
	 * @param srs            좌표계 Geoserver 버전이 1.3.0
	 * @param bbox           Map 크기
	 * @param width          Map 너비
	 * @param height         Map 높이
	 * @param query_layers   쿼리할 하나 이상의 레이어를 쉼표로 구분
	 * @param info_format    {@link EnGetFeatureInfoFormat} GML2, GML3, TEXT, HTML,
	 *                       JSON, JSONP
	 * @param format_options 포맷 옵션
	 * @param feature_count  반환할 피처 최대 개수
	 * @param x              지도상의 X 좌표의 픽셀값
	 * @param y              지도상의 Y 좌표의 픽셀값
	 * @param i              지도상의 X 좌표의 픽셀값 1.3.0 버전일경우 x : i
	 * @param j              지도상의 Y 좌표의 픽셀값 1.3.0 버전일경우 y : j
	 * @param exceptions     예외 보고 형식 기본값 application/vnd.ogc.se_xml
	 */
	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String crs,
			String bbox, int width, int height, String query_layers, EnGetFeatureInfoFormat info_format,
			String format_options, int feature_count, Integer x, Integer y, Integer i, Integer j, String exceptions) {
		super();
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (!version.trim().equals("")) {
			this.version = version;
		}
		if (!layers.trim().equals("")) {
			this.layers = layers;
		}
		if (!styles.trim().equals("")) {
			this.styles = styles;
		}
		if (!srs.trim().equals("")) {
			this.srs = srs;
		}
		if (!crs.trim().equals("")) {
			this.crs = crs;
		}
		if (!bbox.trim().equals("")) {
			this.bbox = bbox;
		}
		if (width != 0) {
			this.width = width;
		}
		if (height != 0) {
			this.height = height;
		}
		if (!query_layers.trim().equals("")) {
			this.query_layers = query_layers;
		}
		if (info_format != null) {
			this.info_format = info_format;
		}
		if (!format_options.trim().equals("")) {
			this.format_options = format_options;
		}
		if (feature_count != 0) {
			this.feature_count = feature_count;
		}
		if (x != null) {
			this.x = x;
		}
		if (y != null) {
			this.y = y;
		}
		if (i != null) {
			this.i = i;
		}
		if (j != null) {
			this.j = j;
		}
		if (!exceptions.trim().equals("")) {
			this.exceptions = exceptions;
		}
	}

	/**
	 * {@link WMSGetFeatureInfo} 생성자
	 * 
	 * @author SG.LEE
	 * @param serverURL    서버 URL
	 * @param version      Geoserver 버전
	 * @param layers       레이어명
	 * @param styles       스타일
	 * @param crs          좌표계 Geoserver 버전이 1.0.0 or 1.1.0 or 1.1.1 일 경우
	 * @param srs          좌표계 Geoserver 버전이 1.3.0
	 * @param bbox         Map 크기
	 * @param width        Map 너비
	 * @param height       Map 높이
	 * @param query_layers 쿼리할 하나 이상의 레이어를 쉼표로 구분
	 * @param x            지도상의 X 좌표의 픽셀값
	 * @param y            지도상의 Y 좌표의 픽셀값
	 * @param i            지도상의 X 좌표의 픽셀값 1.3.0 버전일경우 x : i
	 * @param j            지도상의 Y 좌표의 픽셀값 1.3.0 버전일경우 y : j
	 */
	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String crs,
			String bbox, int width, int height, String query_layers, Integer x, Integer y, Integer i, Integer j) {
		super();
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (!version.trim().equals("")) {
			this.version = version;
		}
		if (!layers.trim().equals("")) {
			this.layers = layers;
		}
		if (!styles.trim().equals("")) {
			this.styles = styles;
		}
		if (!srs.trim().equals("")) {
			this.srs = srs;
		}
		if (!crs.trim().equals("")) {
			this.crs = crs;
		}
		if (!bbox.trim().equals("")) {
			this.bbox = bbox;
		}
		if (width != 0) {
			this.width = width;
		}
		if (height != 0) {
			this.height = height;
		}
		if (!query_layers.trim().equals("")) {
			this.query_layers = query_layers;
		}
		if (x != 0) {
			this.x = x;
		}
		if (y != 0) {
			this.y = y;
		}
		if (i != 0) {
			this.i = i;
		}
		if (j != 0) {
			this.j = j;
		}
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public static String getService() {
		return SERVICE;
	}

	public static String getRequest() {
		return REQUEST;
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

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getStyles() {
		return styles;
	}

	public void setStyles(String styles) {
		this.styles = styles;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public String getBbox() {
		return bbox;
	}

	public void setBbox(String bbox) {
		this.bbox = bbox;
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

	public String getQuery_layers() {
		return query_layers;
	}

	public void setQuery_layers(String query_layers) {
		this.query_layers = query_layers;
	}

	public EnGetFeatureInfoFormat getInfo_format() {
		return info_format;
	}

	public void setInfo_format(EnGetFeatureInfoFormat info_format) {
		this.info_format = info_format;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public int getFeature_count() {
		return feature_count;
	}

	public void setFeature_count(int feature_count) {
		this.feature_count = feature_count;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getExceptions() {
		return exceptions;
	}

	public String getFormat_options() {
		return format_options;
	}

	public void setFormat_options(String format_options) {
		this.format_options = format_options;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * WMS GetFeatureInfo URL 생성
	 * 
	 * @author SG.LEE
	 * @return WMS GetFeatureInfo URL
	 */
	public String getWMSGetFeatureInfoURL() {
		StringBuffer urlBuffer = new StringBuffer();
		if (!this.serverURL.equals("")) {

			if (serverURL.equals("") || version.equals("") || layers.equals("") || (crs.equals("") && srs.equals(""))
					|| bbox.equals("") || width == 0 || height == 0 || query_layers.equals("")
					|| ((x == null || y == null) && (i == null || j == null))) {
				throw new NullPointerException("필수값을 입력하지 않았습니다.");
			}

			urlBuffer.append(serverURL);
			urlBuffer.append("?");
			urlBuffer.append("request=" + REQUEST);
			urlBuffer.append("&");
			urlBuffer.append("service=" + SERVICE);
			if (!this.version.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("version=" + version);
				if (!this.crs.trim().equals("") || !this.srs.trim().equals("")) {
					urlBuffer.append("&");
					if (version.equals("1.3.0")) {
						urlBuffer.append("crs=" + crs);
						urlBuffer.append("&");
						urlBuffer.append("i=" + String.valueOf(this.i));
						urlBuffer.append("&");
						urlBuffer.append("j=" + String.valueOf(this.j));
					} else {
						urlBuffer.append("srs=" + srs);
						urlBuffer.append("&");
						urlBuffer.append("x=" + String.valueOf(this.x));
						urlBuffer.append("&");
						urlBuffer.append("y=" + String.valueOf(this.y));
					}
				}
			}
			if (!this.layers.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("layers=" + layers);
			}
			if (!this.styles.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("styles=" + styles);
			}
			if (this.info_format != null) {
				urlBuffer.append("&");
				urlBuffer.append("info_format=" + info_format.getTypeName());
			}
			if (!this.format_options.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("format_options=" + format_options);
			}
			if (!this.bbox.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("bbox=" + bbox);
			}
			if (!this.query_layers.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("query_layers=" + query_layers);
			}
			if (!this.exceptions.equals("")) {
				urlBuffer.append("&");
				urlBuffer.append("exceptions=" + exceptions);
			}
			if (this.feature_count != 0) {
				urlBuffer.append("&");
				urlBuffer.append("feature_count=" + feature_count);
			}
			urlBuffer.append("&");
			urlBuffer.append("width=" + String.valueOf(this.width));
			urlBuffer.append("&");
			urlBuffer.append("height=" + String.valueOf(this.height));

		} else
			return "";
		return urlBuffer.toString();
	}
}
