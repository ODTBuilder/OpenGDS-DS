package com.gitrnd.gdsbuilder.geoserver.service.factory;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo.EnGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

/**
 * Geoserver Request 요청을 생성하는 인터페이스
 * @author SG.LEE
 */
public interface DTGeoserverServiceFactory {
	/**
	 * Geoserver WFS GetFeature 서비스
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:03
	 * @param serverURL 서버URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @param outputformat WFS outputformat
	 * @param maxFeatures 최대객체수
	 * @param bbox a1, b1, a2, b2
	 * @param format_options 포맷 옵션
	 * @param featureID 검색조건 
	 * @param sortBy attribute+D(내림차순), attribute+A(오름차순)
	 * @param propertyName 검색조건 
	 * @return WFSGetFeature {@link WFSGetFeature} 
	 * */
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName, EnWFSOutputFormat outputformat, int maxFeatures, String bbox,
			String format_options, String featureID, String sortBy, String propertyName, String srsName);
	
	/**
	 * Geoserver WFS GetFeature 서비스(필수 파라미터)
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:11
	 * @param serverURL 서버URL
	 * @param version Geoserver 버전
	 * @param typeName namespace:featuretype
	 * @return WFSGetFeature {@link WFSGetFeature}
	 * */
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName);
	
	/**
	 * Geoserver WMS GetMap 서비스
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:14
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param format {@link EnWMSOutputFormat} PNG, PNG8, JPEG, GIF, TIFF, TIFF8, GeoTIFF, GeoTIFF8, SVG, PDF, GEORSS, KML, KMZ
	 * @param layers 레이어명
	 * @param tiled Tile 서비스 적용여부
	 * @param transparent 투명도 true or false
	 * @param bgcolor Map 이미지 배경색
	 * @param crs 좌표계 Geoserver 버전이 1.0.0 or 1.1.0 or 1.1.1 일 경우
	 * @param srs 좌표계 Geoserver 버전이 1.3.0
	 * @param bbox Map 크기
	 * @param width Map 너비
	 * @param height Map 높이
	 * @param styles 레이어 스타일
	 * @param exceptions 예외 형식
	 * @param time data 시간값 또는 기간
	 * @param sld 스타일 형식
	 * @param sld_body 스타일 형식 body
	 * @return WMSGetMap {@link WMSGetMap}
	 * */
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String transparent,
			String bgcolor, String crs, String srs, String bbox, int width, int height, String styles, String exceptions,
			String time, String sld, String sld_body);
	
	/**
	 * Geoserver WMS GetMap 서비스(필수파라미터) 
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:17
	 * @param serverURL 서버 URL
	 * @param version Geoserver 버전
	 * @param format {@link EnWMSOutputFormat} PNG, PNG8, JPEG, GIF, TIFF, TIFF8, GeoTIFF, GeoTIFF8, SVG, PDF, GEORSS, KML, KMZ
	 * @param layers 레이어명
	 * @param tiled Tile 서비스 적용여부
	 * @param crs 좌표계 Geoserver 버전이 1.0.0 or 1.1.0 or 1.1.1 일 경우
	 * @param srs 좌표계 Geoserver 버전이 1.3.0
	 * @param bbox Map 크기
	 * @param width Map 너비
	 * @param height Map 높이
	 * @param styles 레이어 스타일
	 * @return WMSGetMap {@link WMSGetMap}
	 * */
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers, String tiled, String crs, String srs, String bbox, int width, int height, String styles);
	
	/**
	 * Geoserver 정보 조회 
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:19
	 * @param type {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL 서버 URL
	 * @param fileFormat Export format(json, xml...)
	 * @return DTGeoserverInfo {@link DTGeoserverInfo}
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String fileFormat);
	
	/**
	 * Geoserver 정보 조회  
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:21
	 * @param type {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL 서버 URL
	 * @param workspace 작업공간
	 * @param fileFormat Export format(json, xml...)
	 * @return DTGeoserverInfo {@link DTGeoserverInfo}
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String fileFormat);
	
	/**
	 * Geoserver 정보 조회  
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:24
	 * @param type {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL 서버 URL
	 * @param workspace 작업공간
	 * @param datastore 저장소
	 * @param fileFormat Export format(json, xml...)
	 * @return DTGeoserverInfo {@link DTGeoserverInfo}
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore, String fileFormat);
	
	/**
	 * Geoserver 정보 조회  
	 * @author SG.Lee
	 * @since 2018. 7. 17. 오전 10:05:26
	 * @param type {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL 서버 URL
	 * @param workspace 작업공간
	 * @param datastore 저장소
	 * @param layers 레이어
	 * @param fileFormat Export format(json, xml...)
	 * @return DTGeoserverInfo {@link DTGeoserverInfo}
	 * */
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore, String layers, String fileFormat);
}
