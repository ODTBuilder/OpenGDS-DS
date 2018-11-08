package com.gitrnd.gdsbuilder.geoserver.service.factory;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo.EnGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

public interface DTGeoserverServiceFactory {
	/**
	 * @Description Geoserver WFS GetFeature 서비스
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:03
	 * @param serverURL 서버URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @param outputformat WFS outputformat
	 * @param maxFeatures 최대객체수
	 * @param bbox a1, b1, a2, b2
	 * @param format_options 
	 * @param featureID 검색조건 
	 * @param sortBy attribute+D(내림차순), attribute+A(오름차순)
	 * @param propertyName 검색조건 
	 * @return WFSGetFeature
	 * */
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName, EnWFSOutputFormat outputformat, int maxFeatures, String bbox,
			String format_options, String featureID, String sortBy, String propertyName, String srsName);
	
	/**
	 * @Description Geoserver WFS GetFeature 서비스(필수 파라미터)
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:11
	 * @param serverURL 서버URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @return WFSGetFeature
	 * */
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName);
	
	/**
	 * @Description Geoserver WMS GetMap 서비스
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:14
	 * @param serverURL 서버URL
	 * @param version Geoserver 버전
	 * @param format
	 * @param layers
	 * @param tiled
	 * @param transparent
	 * @param bgcolor
	 * @param crs
	 * @param srs
	 * @param bbox
	 * @param width
	 * @param height
	 * @param styles
	 * @param exceptions
	 * @param time
	 * @param sld
	 * @param sld_body
	 * @return WMSGetMap
	 * */
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String transparent,
			String bgcolor, String crs, String srs, String bbox, int width, int height, String styles, String exceptions,
			String time, String sld, String sld_body);
	
	/**
	 * @Description Geoserver WMS GetMap 서비스(필수파라미터) 
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:17
	 * @param serverURL
	 * @param version
	 * @param format
	 * @param layers
	 * @param tiled
	 * @param crs
	 * @param srs
	 * @param bbox
	 * @param width
	 * @param height
	 * @param styles
	 * @return WMSGetMap
	 * */
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String crs, String srs, String bbox, int width, int height, String styles);
	
	/**
	 * @Description 
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:19
	 * @param type
	 * @param serverURL
	 * @param fileFormat
	 * @return DTGeoserverInfo
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String fileFormat);
	
	/**
	 * @Description 
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:21
	 * @param type
	 * @param serverURL
	 * @param workspace
	 * @param fileFormat
	 * @return DTGeoserverInfo
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String fileFormat);
	
	/**
	 * @Description 
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:24
	 * @param type
	 * @param serverURL
	 * @param workspace
	 * @param datastore
	 * @param fileFormat
	 * @return DTGeoserverInfo
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore, String fileFormat);
	
	/**
	 * @Description 
	 * @author SG.Lee
	 * @Date 2018. 7. 17. 오전 10:05:26
	 * @param type
	 * @param serverURL
	 * @param workspace
	 * @param datastore
	 * @param layers
	 * @param fileFormat
	 * @return DTGeoserverInfo
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore, String layers, String fileFormat);
}
