package com.gitrnd.gdsbuilder.geoserver.service;

import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

/**
 * Geoserver Request 요청을 처리하는 인터페이스 
 * @author SG.LEE
 */
public interface DTGeoserverServiceManager {
	/**
	 * WFS GetFeature 요청
	 * @author SG.LEE
	 * @param feature {@link WFSGetFeature} WFS GetFeature 요청을 위한 정보
	 */
	public void requestWFSGetFeature(WFSGetFeature feature);
	/**
	 * WMS GetFeatureInfo 요청
	 * @author SG.LEE
	 * @param feature {@link WMSGetFeatureInfo} WMS GetFeatureInfo 요청을 위한 정보
	 */
	public void requestWMSGetFeatureInfo(WMSGetFeatureInfo feature);
	/**
	 * WMS GetLegendGraphic 요청
	 * @author SG.LEE
	 * @param feature {@link WMSGetLegendGraphic} WMS GetLegendGraphic 요청을 위한 정보
	 */
	public void requestWMSGetLegendGraphic(WMSGetLegendGraphic feature);
	/**
	 * WMS GetMap 요청
	 * @author SG.LEE
	 * @param feature {@link WMSGetMap} WMS GetMap 요청을 위한 정보
	 */
	public void requestWMSGetMap(WMSGetMap feature);
}
