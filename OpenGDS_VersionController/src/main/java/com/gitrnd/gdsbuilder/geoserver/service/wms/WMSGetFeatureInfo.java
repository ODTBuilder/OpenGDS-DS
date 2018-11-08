package com.gitrnd.gdsbuilder.geoserver.service.wms;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnGetFeatureInfoFormat;

/**
 * @Description  
 * @author SG.Lee
 * @Date 2018. 7. 17. 오후 1:26:26
 * */
public class WMSGetFeatureInfo {
	private final static String SERVICE = "WMS";
	private final static String REQUEST = "GetFeatureInfo";
	private String serverURL = "";
	private String version = "1.0.0";
	private String layers = "";
	private String styles = "";
	private String srs = "";
	private String crs = "";
	private String bbox = "";
	private int width = 0;
	private int height = 0;
	private String query_layers = "";
	private EnGetFeatureInfoFormat info_format = null;
	private String format_options = "";
	private int feature_count = 0;
	private Integer x = null;
	private Integer y = null;
	private Integer i = null; //1.3.0 버전일경우 x->i
	private Integer j = null; //1.3.0 버전일경우 y->j
	private String exceptions = "";

	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String crs,String bbox,
			int width, int height, String query_layers, EnGetFeatureInfoFormat info_format,String format_options,  int feature_count, Integer x, Integer y, Integer i, Integer j, String exceptions) {
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
		if (info_format!=null) {
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
	
	

	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String crs, String bbox,
			int width, int height, String query_layers, Integer x, Integer y, Integer i, Integer j) {
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

	public String getWMSGetFeatureInfoURL() {
		StringBuffer urlBuffer = new StringBuffer();
		if (!this.serverURL.equals("")) {
			
			if(serverURL.equals("")||version.equals("")||layers.equals("")||(crs.equals("")&&srs.equals(""))
					||bbox.equals("")||width==0||height==0||query_layers.equals("")||((x==null||y==null)&&(i==null||j==null))){
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
				if(!this.crs.trim().equals("")||!this.srs.trim().equals("")){
					urlBuffer.append("&");
					if(version.equals("1.3.0")){
						urlBuffer.append("crs="+crs);
						urlBuffer.append("&");
						urlBuffer.append("i=" + String.valueOf(this.i));
						urlBuffer.append("&");
						urlBuffer.append("j=" + String.valueOf(this.j));
					}else{
						urlBuffer.append("srs="+srs);
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
			if (this.info_format!=null) {
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
			if(this.feature_count!=0){
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
