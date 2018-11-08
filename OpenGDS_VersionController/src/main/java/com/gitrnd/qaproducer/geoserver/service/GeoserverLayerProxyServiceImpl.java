/*
 *  GeoServer-Manager - Simple Manager Library for GeoServer
 *  
 *  Copyright (C) 2007,2011 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gitrnd.qaproducer.geoserver.service;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.service.DTGeoserverServiceManager;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnGetFeatureInfoFormat;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWFSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnWMSOutputFormat;
import com.gitrnd.gdsbuilder.geoserver.service.impl.DTGeoserverServiceManagerImpl;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo.EnGeoserverInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetLegendGraphic;
import com.gitrnd.gdsbuilder.geoserver.service.wms.WMSGetMap;

/**
 * 프록시서버 요청에 대한 요청을 처리하는 클래스
 * 
 * @author SG.Lee
 * @Date 2017. 5. 12. 오전 2:08:04
 */
@Service("proService")
public class GeoserverLayerProxyServiceImpl implements GeoserverLayerProxyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoserverLayerProxyServiceImpl.class);
	

	@Override
	public void requestGetMap(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(dtGeoManager==null){
			response.sendError(500, "Null Geoserver");
		}else{
			if(!workspace.equals("")&&workspace!=null){
				WMSGetMap wmsGetMap = this.createWMSGetMap(dtGeoManager, workspace, request);
				DTGeoserverServiceManager geoserverService = new DTGeoserverServiceManagerImpl(request, response);
				geoserverService.requestWMSGetMap(wmsGetMap);
			}else{
				response.sendError(500, "workspace를 입력해주세요.");
			}
		}
	}

	private WMSGetMap createWMSGetMap(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request) {
		String serverURL = dtGeoManager.getRestURL() +"/"+workspace+"/wms";
		String version = "";
		EnWMSOutputFormat format = null;
		String layers = "";
		String tiled = "";
		String transparent = "";
		String bgcolor = "";
		String crs = "";
		String srs = "";
		String bbox = "";
		int width = 0;
		int height = 0;
		String styles = "";
		String exceptions = "application/vnd.ogc.se_xml";
		String time = "";
		String sld = "";
		String sld_body = "";

		
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);
			
			if (key.toLowerCase().equals("layers")) {
				layers = value;
			} else if (key.toLowerCase().equals("version")) {
				version = value;
			} else if (key.toLowerCase().equals("bbox")) {
				bbox = value;
			} else if (key.toLowerCase().equals("crs")) {
				crs = value;
			} else if (key.toLowerCase().equals("format")) {
				format = EnWMSOutputFormat.getFromTypeName(value);
			} else if (key.toLowerCase().equals("layers")) {
				layers = value;
			} else if (key.toLowerCase().equals("tiled")) {
				tiled = value;
			} else if (key.toLowerCase().equals("transparent")) {
				transparent = value;
			} else if (key.toLowerCase().equals("bgcolor")) {
				bgcolor = value;
			} else if (key.toLowerCase().equals("crs")) {
				crs = value;
			} else if (key.toLowerCase().equals("srs")) {
				srs = value;
			}  else if (key.toLowerCase().equals("bbox")) {
				bbox = value;
			} else if (key.toLowerCase().equals("width")) {
				width = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("height")) {
				height = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("styles")) {
				styles = value;
			} else if (key.toLowerCase().equals("exceptions")) {
				exceptions = value;
			} else if (key.toLowerCase().equals("time")) {
				time = value;
			} else if (key.toLowerCase().equals("sld")) {
				sld = value;
			} else if (key.toLowerCase().equals("sld_body")) {
				sld_body = value;
			}
		}
		return new WMSGetMap(serverURL, version, format, layers, tiled, transparent, bgcolor, crs, srs, bbox, width, height,
				styles, exceptions, time, sld, sld_body);
	}

	@Override
	public void requestGetFeature(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// String urlParam = this.createGetFeatureInfoURL(request);
		if(dtGeoManager==null){
			response.sendError(500, "Null Geoserver");
		}else{
			if(!workspace.equals("")&&workspace!=null){
				WFSGetFeature wfsGetFeature = this.createWFSGetFeature(dtGeoManager, workspace, request);
				DTGeoserverServiceManager geoserverService = new DTGeoserverServiceManagerImpl(request, response);
				geoserverService.requestWFSGetFeature(wfsGetFeature);
			}else{
				response.sendError(500, "workspace를 입력해주세요.");
			}
		}
	};

	@SuppressWarnings("unused")
	private WFSGetFeature createWFSGetFeature(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request) {
		String serverURL = dtGeoManager.getRestURL() +"/"+workspace+"/wfs";
		String version = "";
		String typeName = "";
		int maxFeatures = 0;
		String bbox = "";
		EnWFSOutputFormat outputformat = null;
		String format_options = "";
		String featureID = "";
		String sortBy = "";
		String propertyName = "";
		String srsName = "";

		
		
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);
			
			if (key.toLowerCase().equals("version")) {
				version = value;
			} else if (key.toLowerCase().equals("typename")) {
				typeName = workspace+":"+value;
			} else if (key.toLowerCase().equals("bbox")) {
				bbox = value;
			} else if (key.toLowerCase().equals("outputformat")) {
				outputformat = EnWFSOutputFormat.getFromTypeName(value);
			} else if (key.toLowerCase().equals("maxfeatures")) {
				maxFeatures = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("format_options")) {
				format_options = value;
			} else if (key.toLowerCase().equals("featureid")) {
				featureID = value;
			} else if (key.toLowerCase().equals("sortby")) {
				sortBy = value;
			} else if (key.toLowerCase().equals("propertyname")) {
				propertyName = value;
			} else if (key.toLowerCase().equals("srsname")) {
				srsName = value;
			}
		}
		return new WFSGetFeature(serverURL, version, typeName, outputformat, maxFeatures, bbox, format_options,
				featureID, sortBy, propertyName, srsName);
	}

	@Override
	public void requestGetFeatureInfo(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(dtGeoManager==null){
			response.sendError(500, "Null Geoserver");
		}else{
			if(!workspace.equals("")&&workspace!=null){
				WMSGetFeatureInfo getFeatureInfo = this.getWMSGetFeatureInfo(dtGeoManager, workspace, request);
				DTGeoserverServiceManager geoserverService = new DTGeoserverServiceManagerImpl(request, response);
				geoserverService.requestWMSGetFeatureInfo(getFeatureInfo);
			}else{
				response.sendError(500, "workspace를 입력해주세요.");
			}
		}
	}

	private WMSGetFeatureInfo getWMSGetFeatureInfo(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request) {
		String serverURL = dtGeoManager.getRestURL() +"/"+workspace+"/wms";
		String version = "1.0.0";
		String layers = "";
		String styles = "";
		String crs = "";
		String srs = "";
		String bbox = "";
		String format_options = "";
		int width = 0;
		int height = 0;
		String query_layers = "";
		EnGetFeatureInfoFormat info_format = null;
		int feature_count = 0;
		Integer x = null;
		Integer y = null;
		Integer i = null; //1.3.0 버전일경우 x->i
		Integer j = null; //1.3.0 버전일경우 y->j
		String exceptions = "application/vnd.ogc.se_xml";

		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);

			
			if (key.toLowerCase().equals("version")) {
				version = value;
			} else if (key.toLowerCase().equals("layers")) {
				layers = value;
			} else if (key.toLowerCase().equals("styles")) {
				styles = value;
			} else if (key.toLowerCase().equals("crs")) {
				crs = value;
			} else if (key.toLowerCase().equals("srs")) {
				srs = value;
			} else if (key.toLowerCase().equals("format_options")) {
				format_options = value;
			} 
			 else if (key.toLowerCase().equals("bbox")) {
				bbox = value;
			} else if (key.toLowerCase().equals("width")) {
				width = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("height")) {
				height = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("query_layers")) {
				query_layers = value;
			} else if (key.toLowerCase().equals("info_format")) {
				info_format = EnGetFeatureInfoFormat.getFromTypeName(value);
			} else if (key.toLowerCase().equals("feature_count")) {
				feature_count = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("x")) {
				try{
					x = Integer.parseInt(value);
				}catch (Exception e) {
					// TODO: handle exception
					x=null;
				}
			} else if (key.toLowerCase().equals("y")) {
				try {
					y = Integer.parseInt(value);
				} catch (Exception e) {
					// TODO: handle exception
					y = null;
				}
				
			} else if (key.toLowerCase().equals("i")) {
				try {
					i = Integer.parseInt(value);
				} catch (Exception e) {
					// TODO: handle exception
					i=null;
				}
			} else if (key.toLowerCase().equals("j")) {
				try {
					j = Integer.parseInt(value);
				} catch (Exception e) {
					// TODO: handle exception
					j=null;
				}
			}else if (key.toLowerCase().equals("exceptions")) {
				exceptions = value;
			}
		}
		return new WMSGetFeatureInfo(serverURL, version, layers, styles, srs, crs, bbox, width, height, query_layers,
				info_format, format_options, feature_count, x, y, i, j, exceptions);
	}
	
	
	@Override
	public void requestWMSGetLegendGraphic(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(dtGeoManager==null){
			response.sendError(500, "Null Geoserver");
		}else{
			if(!workspace.equals("")&&workspace!=null){
				WMSGetLegendGraphic getLegendGraphic = this.getWMSGetLegendGraphic(dtGeoManager, workspace, request);
				DTGeoserverServiceManager geoserverService = new DTGeoserverServiceManagerImpl(request, response);
				geoserverService.requestWMSGetLegendGraphic(getLegendGraphic);
			}else{
				response.sendError(500, "workspace를 입력해주세요.");
			}
		}
	}

	private WMSGetLegendGraphic getWMSGetLegendGraphic(DTGeoserverManager dtGeoManager, String workspace, HttpServletRequest request) {
		String serverURL = dtGeoManager.getRestURL() +"/"+workspace+"/wms";
		String version = "1.0.0";
		EnWMSOutputFormat format = null;
		String legend_options = "";
		String layer = "";
		int width = 0;
		int height = 0;
		int scale = 0;
		String style = "";
		String sld = "";
		String sld_body= "";
		String exceptions="";

		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);

			if (key.toLowerCase().equals("version")) {
				version = value;
			} else if (key.toLowerCase().equals("format")) {
				format = EnWMSOutputFormat.getFromTypeName(value);
			} else if (key.toLowerCase().equals("legend_options")) {
				legend_options = value;
			} else if (key.toLowerCase().equals("layer")) {
				layer = value;
			} else if (key.toLowerCase().equals("width")) {
				width = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("height")) {
				height = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("scale")) {
				scale = Integer.parseInt(value);
			} else if (key.toLowerCase().equals("style")) {
				style = value;
			} else if (key.toLowerCase().equals("sld")) {
				sld = value;
			} else if (key.toLowerCase().equals("sld_body")) {
				sld_body = value;
			} else if (key.toLowerCase().equals("exceptions")) {
				exceptions = value;
			}
		}
		return new WMSGetLegendGraphic(serverURL, version, format, width, height, layer, scale, legend_options, style, sld, sld_body, exceptions);
	}
	
	@Override
	public void requestGeoserverInfo(DTGeoserverManager dtGeoManager, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(dtGeoManager==null){
			response.sendError(500, "Null Geoserver");
		}else{
			DTGeoserverInfo dtGeoserverInfo = this.getDTGeoserverInfo(dtGeoManager, request);
			DTGeoserverServiceManager geoserverService = new DTGeoserverServiceManagerImpl(request, response);
			geoserverService.requestGeoserverInfo(dtGeoserverInfo);
		}
		
	}
	
	
	private DTGeoserverInfo getDTGeoserverInfo(DTGeoserverManager dtGeoManager, HttpServletRequest request) {
		DTGeoserverInfo dtGeoInfo = null;
		
		EnGeoserverInfo type = null;
		
		String serverURL = dtGeoManager.getRestURL();
		String workspace ="";
		String datastore="";
		String layers="";
		String fileFormat="";

		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);

			if (key.toLowerCase().equals("workspace")) {
				workspace = value;
			} else if (key.toLowerCase().equals("type")) {
				type = EnGeoserverInfo.getFromType(value);
			} else if (key.toLowerCase().equals("workspace")) {
				workspace = value;
			} else if (key.toLowerCase().equals("datastore")) {
				datastore = value;
			} else if (key.toLowerCase().equals("layers")) {
				layers = value;
			} else if (key.toLowerCase().equals("fileFormat")) {
				fileFormat = value;
			}
		}
		if(type!=null){
			if(type==EnGeoserverInfo.SERVER){
				dtGeoInfo = new DTGeoserverInfo(type,serverURL,fileFormat);
			}else if(type==EnGeoserverInfo.WORKSPACE){
				dtGeoInfo = new DTGeoserverInfo(type,serverURL,workspace,fileFormat);
			}else if(type==EnGeoserverInfo.DATASTORE){
				dtGeoInfo = new DTGeoserverInfo(type,serverURL,workspace,datastore,fileFormat);
			}else if(type==EnGeoserverInfo.LAYER){
				dtGeoInfo = new DTGeoserverInfo(type,serverURL,workspace,datastore,layers,fileFormat);
			}
		}
		return dtGeoInfo;
	} 
}
