package com.gitrnd.gdsbuilder.geoserver.service.wfs;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnGeoserverService;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;

/**
 * WFS GetFeature 정보를 받아 요청 URL을 생성해주는 클래스
 * <p>
 * 데이터 소스에서 선정된 피처들을 반환
 * @author SG.LEE
 */
public class WFSGetFeature {
	/**
	 * 서비스명
	 */
	private final static String SERVICE = EnGeoserverService.WFS.getState();
	/**
	 * 작업명
	 */
	private final static String REQUEST = "GetFeature";
	
	/**
	 * 서버 URL
	 */
	private String serverURL ="";
	/**
	 * Geoserver 버전
	 */
	private String version="1.1.1";
	/**
	 * typeName=namespace:featuretype
	 */
	private String typeName="";
	/**
	 * WFSGetFeature Output Format
	 * {@link EnWFSOutputFormat} GML2, GML3, SHP, JSON, JSONP, CSV
	 */
	private EnWFSOutputFormat outputformat=null;
	/**
	 * 최대 피처개수
	 */
	private int maxFeatures = 0; 
	/**
	 * Map 크기
	 */
	private String bbox="";
	/**
	 * 포맷 옵션
	 */
	private String format_options="";
	/**
	 * 특정 피처 ID(특정 피처 검색원할시)
	 */
	private String featureID = "";
	/**
	 * 정렬 파라미터
	 * {attribute}+D(내림차순) or A(오름차순)
	 */
	private String sortBy = "";
	/**
	 * 검색하고 싶은 조건
	 */
	private String propertyName = "";
	/**
	 * 좌표계(ex. epsg:4326)
	 */
	private String srsName="";
	/**
	 * cql_filter - 상세정보는 Geoserver 매뉴얼 참고
	 */
	private String cql_filter="";
	/**
	 * 페이징시 startIndex설정
	 * 0보다 작을경우 적용안함
	 */
	private int startIndex = -1;
	
	
	/**
	 * {@link WFSGetFeature} 생성자
	 * @author SG.LEE
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @param outputformat WFSGetFeature Output Format
	 *                    {@link EnWFSOutputFormat} GML2, GML3, SHP, JSON, JSONP, CSV
	 * @param maxFeatures 최대 피처개수
	 * @param bbox Map 크기
	 * @param format_options 포맷 옵션
	 * @param featureID 특정 피처 ID(특정 피처 검색원할시)
	 * @param sortBy 정렬 파라미터
	 *               {attribute}+D(내림차순) or A(오름차순)
	 * @param propertyName 검색하고 싶은 조건
	 * @param srsName 좌표계(ex. epsg:4326)
	 */
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
	
	/**
	 * {@link WFSGetFeature} 생성자
	 * @author SG.LEE
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @param outputformat WFSGetFeature Output Format
	 *                    {@link EnWFSOutputFormat} GML2, GML3, SHP, JSON, JSONP, CSV
	 * @param maxFeatures 최대 피처개수
	 * @param bbox Map 크기
	 * @param format_options 포맷 옵션
	 * @param featureID 특정 피처 ID(특정 피처 검색원할시)
	 * @param sortBy 정렬 파라미터
	 *               {attribute}+D(내림차순) or A(오름차순)
	 * @param propertyName 검색하고 싶은 조건
	 * @param srsName 좌표계(ex. epsg:4326)
	 * @param startIndex 페이징시 startIndex설정
	 *                  0보다 작을경우 적용안함
	 * @param cql_filter cql_filter - 상세정보는 Geoserver 매뉴얼 참고
	 */
	public WFSGetFeature(String serverURL, String version, String typeName, EnWFSOutputFormat outputformat, int maxFeatures, String bbox,
			String format_options, String featureID, String sortBy, String propertyName, String srsName, int startIndex, String cql_filter) {
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
		if(startIndex>-1){
			this.startIndex = startIndex;
		}
		if (!cql_filter.trim().equals("")) {
			this.cql_filter = cql_filter;
		}
	}
	
	/**
	 * {@link WFSGetFeature} 생성자
	 * @author SG.LEE
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 */
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
	
	public String getSrsName() {
		return srsName;
	}
	public void setSrsName(String srsName) {
		this.srsName = srsName;
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
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/**
	 * WFS GetFeature URL 생성
	 * @author SG.LEE
	 * @return WFS GetFeature URL
	 */
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
			}if(startIndex>-1){
				urlBuffer.append("&");
				urlBuffer.append("startIndex="+startIndex);
			}if(!this.cql_filter.trim().equals("")){
				urlBuffer.append("&");
				urlBuffer.append("cql_filter="+cql_filter);
			}
		}
		else
			return "";
		return urlBuffer.toString();
	}
}
