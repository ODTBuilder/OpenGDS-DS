package com.gitrnd.gdsbuilder.geoserver.service.impl;

import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.factory.DTGeoserverServiceFactory;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo.EnGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

/**
 * Geoserver Request 요청을 생성하는 클래스
 * 
 * @author SG.LEE
 */
public class DTGeoserverServiceFactoryImpl implements DTGeoserverServiceFactory {

	@Override
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName,
			EnWFSOutputFormat outputformat, int maxFeatures, String bbox, String format_options, String featureID,
			String sortBy, String propertyName, String srsName) {
		return new WFSGetFeature(serverURL, version, typeName, outputformat, maxFeatures, bbox, format_options,
				featureID, sortBy, propertyName, srsName);
	};

	@Override
	public WFSGetFeature createWFSGetFeature(String serverURL, String version, String typeName) {
		return new WFSGetFeature(serverURL, version, typeName);
	};

	@Override
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers,
			String tiled, String transparent, String bgcolor, String crs, String srs, String bbox, int width,
			int height, String styles, String exceptions, String time, String sld, String sld_body) {
		return new WMSGetMap(serverURL, version, format, layers, tiled, transparent, bgcolor, crs, srs, bbox, width,
				height, styles, exceptions, time, sld, sld_body);
	};

	@Override
	public WMSGetMap createWMSGetMap(String serverURL, String version, EnWMSOutputFormat format, String layers,
			String tiled, String crs, String srs, String bbox, int width, int height, String styles) {
		return new WMSGetMap(serverURL, version, format, layers, tiled, crs, srs, bbox, width, height, styles);
	}

	@Override
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String fileFormat) {
		return new DTGeoserverInfo(type, serverURL, fileFormat);
	}

	@Override
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace,
			String fileFormat) {
		return new DTGeoserverInfo(type, serverURL, workspace, fileFormat);
	}

	@Override
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace,
			String datastore, String fileFormat) {
		return new DTGeoserverInfo(type, serverURL, workspace, datastore, fileFormat);
	}

	@Override
	public DTGeoserverInfo createDTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace,
			String datastore, String layers, String fileFormat) {
		// TODO Auto-generated method stub
		return new DTGeoserverInfo(type, serverURL, workspace, datastore, layers, fileFormat);
	};
}
