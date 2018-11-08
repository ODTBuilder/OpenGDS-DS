package com.gitrnd.gdsbuilder.geoserver.service;

import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

public interface DTGeoserverServiceManager {
	public void requestWFSGetFeature(WFSGetFeature feature);
	public void requestWMSGetFeatureInfo(WMSGetFeatureInfo feature);
	public void requestWMSGetLegendGraphic(WMSGetLegendGraphic feature);
	public void requestWMSGetMap(WMSGetMap feature);
	public void requestGeoserverInfo(DTGeoserverInfo dtGeoInfo);
}
