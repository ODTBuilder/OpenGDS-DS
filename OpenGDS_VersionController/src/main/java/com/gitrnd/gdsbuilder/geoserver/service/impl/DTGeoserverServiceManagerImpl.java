package com.gitrnd.gdsbuilder.geoserver.service.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;
import com.gitrnd.gdsbuilder.net.impl.ProxyServerImpl;

/**
 * Geoserver Request 요청을 처리하는 클래스
 * @author SG.LEE
 *
 */
public class DTGeoserverServiceManagerImpl implements DTGeoserverServiceManager {
	private HttpServletRequest request=null;
	private HttpServletResponse response=null;
	
	/**
	 * @author SG.LEE
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	public DTGeoserverServiceManagerImpl(HttpServletRequest request, HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager#requestWFSGetFeature(com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature)
	 */
	@Override
	public void requestWFSGetFeature(WFSGetFeature feature) {
		String url = feature.getWFSGetFeatureURL();
		this.requestProxyService(url);
	};
	
	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager#requestWMSGetFeatureInfo(com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo)
	 */
	@Override
	public void requestWMSGetFeatureInfo(WMSGetFeatureInfo featureInfo) {
		String url = featureInfo.getWMSGetFeatureInfoURL();
		this.requestProxyService(url);
	};

	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager#requestWMSGetMap(com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap)
	 */
	@Override
	public void requestWMSGetMap(WMSGetMap map) {
		String url = map.getWMSGetMapURL();
		this.requestProxyService(url);
	};
	
	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager#requestWMSGetLegendGraphic(com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic)
	 */
	@Override
	public void requestWMSGetLegendGraphic(WMSGetLegendGraphic legendGraphic) {
		String url = legendGraphic.getWMSGetLegendGraphicURL();
		this.requestProxyService(url);
	};
	
	
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
