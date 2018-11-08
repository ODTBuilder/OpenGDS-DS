package com.gitrnd.gdsbuilder.geoserver.service.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;
import com.gitrnd.gdsbuilder.net.impl.ProxyServerImpl;

public class DTGeoserverServiceManagerImpl implements DTGeoserverServiceManager {
	private HttpServletRequest request=null;
	private HttpServletResponse response=null;
	
	public DTGeoserverServiceManagerImpl(HttpServletRequest request, HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	@Override
	public void requestWFSGetFeature(WFSGetFeature feature) {
		String url = feature.getWFSGetFeatureURL();
		this.requestProxyService(url);
	};
	
	@Override
	public void requestWMSGetFeatureInfo(WMSGetFeatureInfo featureInfo) {
		String url = featureInfo.getWMSGetFeatureInfoURL();
		this.requestProxyService(url);
	};

	@Override
	public void requestWMSGetMap(WMSGetMap map) {
		String url = map.getWMSGetMapURL();
		this.requestProxyService(url);
	};
	
	@Override
	public void requestWMSGetLegendGraphic(WMSGetLegendGraphic legendGraphic) {
		String url = legendGraphic.getWMSGetLegendGraphicURL();
		this.requestProxyService(url);
	};
	
	@Override
	public void requestGeoserverInfo(DTGeoserverInfo dtGeoInfo){
		String url = dtGeoInfo.getDTGeoserverInfoURL();
		this.requestProxyService(url);
	}
	
	private void requestProxyService(String url) {
		try {
			new ProxyServerImpl(request, response, url).requestProxyService();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
