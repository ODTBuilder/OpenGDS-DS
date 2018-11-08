package com.gitrnd.gdsbuilder.geoserver.service.wms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;

public class WMSGetMap {
	private final static String SERVICE = "WMS";
	private final static String REQUEST = "GetMap";
	
	private String serverURL ="";
	private String version="1.0.0";
	private EnWMSOutputFormat format=null;
	private String layers="";
	private String tiled="";
	private String transparent="";
	private String bgcolor="";
	private String crs=""; //1.0.0 , 1.1.0 , 1.1.1 버전일경우
	private String srs=""; //1.3.0 버전일경우
	private String bbox="";
	private int width=0;
	private int height=0;
	private String styles="";
	private String exceptions = "application/vnd.ogc.se_xml"; 
	private String time="";
	private String sld="";
	private String sld_body="";
	
	public WMSGetMap(){};
	
	public WMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String transparent,
			String bgcolor, String crs, String srs, String bbox, int width, int height, String styles, String exceptions,
			String time, String sld, String sld_body) {
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
		if (!layers.trim().equals("")) {
			this.layers = layers;
		}
		if (!crs.trim().equals("")) {
			this.crs = crs;
		}
		if (!srs.trim().equals("")) {
			this.srs = srs;
		}
		if (!bbox.trim().equals("")) {
			this.bbox = bbox;
		}
		if (width!=0) {
			this.width = width;
		}
		if (height!=0) {
			this.height = height;
		}
		if (!styles.trim().equals("")) {
			this.styles = styles;
		}
		if (!exceptions.trim().equals("")) {
			this.exceptions = exceptions;
		}
		if (!time.trim().equals("")) {
			this.time = time;
		}
		if (!sld.trim().equals("")) {
			this.sld = sld;
		}
		if (!sld_body.trim().equals("")) {
			this.sld_body = sld_body;
		}
		if (!tiled.trim().equals("")) {
			this.tiled = tiled;
		}
		if (!transparent.trim().equals("")) {
			this.transparent = transparent;
		}
		
	}
	
	public WMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String crs, String srs, String bbox, int width, int height, String styles) {
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
		if (!layers.trim().equals("")) {
			this.layers = layers;
		}
		if (!tiled.trim().equals("")) {
			this.tiled = tiled;
		}
		if (!crs.trim().equals("")) {
			this.crs = crs;
		}
		if (!srs.trim().equals("")) {
			this.srs = srs;
		}
		if (!bbox.trim().equals("")) {
			this.bbox = bbox;
		}
		if (width!=0) {
			this.width = width;
		}
		if (height!=0) {
			this.height = height;
		}
		if (!styles.trim().equals("")) {
			this.styles = styles;
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
	public String getLayers() {
		return layers;
	}
	public void setLayers(String layers) {
		this.layers = layers;
	}
	public String isTiled() {
		return tiled;
	}
	public void setTiled(String tiled) {
		this.tiled = tiled;
	}
	public String isTransparent() {
		return transparent;
	}
	public void setTransparent(String transparent) {
		this.transparent = transparent;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getCrs() {
		return crs;
	}
	public void setCrs(String crs) {
		this.crs = crs;
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
	public String getStyles() {
		return styles;
	}
	public void setStyles(String styles) {
		this.styles = styles;
	}
	public String getExceptions() {
		return exceptions;
	}
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSld() {
		return sld;
	}
	public void setSld(String sld) {
		this.sld = sld;
	}
	public String getSld_body() {
		return sld_body;
	}
	public void setSld_body(String sld_body) {
		this.sld_body = sld_body;
	}
	public static String getService() {
		return SERVICE;
	}
	public static String getRequest() {
		return REQUEST;
	}
	public String getTiled() {
		return tiled;
	}

	public String getTransparent() {
		return transparent;
	}
	
	public String getWMSGetMapURL(){
		StringBuffer urlBuffer = new StringBuffer();
		if(!this.serverURL.trim().equals("")){
			if(serverURL.equals("")||version.equals("")||crs.equals("")||bbox.equals("")||width==0||height==0||format.equals("")){
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
			if(format!=null){
				urlBuffer.append("&");
				urlBuffer.append("format="+format.getTypeName());
			}
			if(!this.layers.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("layers="+layers);
			}
			if(!this.bgcolor.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("bgcolor="+bgcolor);
			}
			if(!this.crs.trim().equals("")||!this.srs.trim().equals("")){
				urlBuffer.append("&");
				if(version.equals("1.3.0")){
					urlBuffer.append("crs="+crs);
				}else{
					urlBuffer.append("srs="+srs);
				}
			}
			if(!this.bbox.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("bbox="+bbox);
			}
			if(!this.styles.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("styles="+styles);
			}
			if(!this.exceptions.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("exceptions="+exceptions);
			}
			/*if(!this.time.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("time="+time);
			}*/
			if(!this.sld.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("sld="+sld);
			}
			if(!this.sld_body.trim().equals("")){
				urlBuffer.append("&");
				try {
					urlBuffer.append("sld_body="+URLEncoder.encode(sld_body,"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					System.err.println("sld_body 파싱에러");
				}
			}
			if(!this.tiled.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("tiled="+tiled);
			}
			if(!this.transparent.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("transparent="+transparent);
			}
			if(this.width!=0){
				urlBuffer.append("&");
				urlBuffer.append("width="+String.valueOf(this.width));
			}
			if(this.height!=0){
				urlBuffer.append("&");
				urlBuffer.append("height="+String.valueOf(this.height));
			}
		}
		else
			return "";
		return urlBuffer.toString();
	}
}
