/*
 *    OpenGDS/Builder
 *    http://git.co.kr
 *
 *    (C) 2014-2017, GeoSpatial Information Technology(GIT)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

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

package com.gitrnd.gdsbuilder.geoserver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.jdom.JDOMException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTrees;
import com.gitrnd.gdsbuilder.geoserver.data.tree.factory.impl.DTGeoserverTreeFactoryImpl;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoGroupLayer;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoGroupLayerList;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoLayer;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoLayerList;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnFeatureTypeList;
import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo;

import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureTypeList;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;

/**
 * {@link GeoServerRESTReader} 상속 클래스 - Geoserver 정보 및 데이터 읽기 지원 
 * @author SG.LEE
 * @since 2017. 5. 2. 오후 2:38:58
 */
public class DTGeoserverReader extends GeoServerRESTReader {

	/**
	 * LOGGER
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(DTGeoserverReader.class);

	/**
	 * Geoserver URL 
	 */
	private final String baseurl;
	/**
	 * Geoserver ID
	 */
	private String username;
	/**
	 * Geoserver PW
	 */
	private String password;

	/**
	 * DTGeoserverReader 생성자
	 * @author SG.LEE
	 * @param gsUrl Geoserver URL
	 * @param username Geoserver ID
	 * @param password Geoserver PW
	 * @throws MalformedURLException
	 */
	public DTGeoserverReader(String gsUrl, String username, String password) throws MalformedURLException {
		super(gsUrl, username, password);
		this.baseurl = gsUrl;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * GET Geoserver ID
	 * @author SG.LEE
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * SET Geoserver ID 
	 * @author SG.LEE
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * GET Geoserver PW
	 * @author SG.LEE
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * SET Geoserver PW 
	 * @author SG.LEE
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * GET Geoserver URL
	 * @author SG.LEE
	 * @return
	 */
	public String getBaseurl() {
		return baseurl;
	}
	
	

	/**
	 * REST 요청을 통한 Geoserver Layer정보 조회   
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param name 레이어명
	 * @return Geoserver Layer 정보를 담고있는 {@link DTGeoLayer} 객체반환 
	 */
	public DTGeoLayer getDTGeoLayer(String workspace, String name) {
		DTGeoLayer dtGeolayer = null;

		if (workspace == null || workspace.isEmpty())
			throw new IllegalArgumentException("Workspace may not be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Layername may not be null");

		RESTLayer layer = getLayer(workspace, name);

		if (layer != null) {
			if (layer.getType() != RESTLayer.Type.VECTOR)
				throw new RuntimeException("Bad layer type for layer " + layer.getName());
			String response = loadFullURL(layer.getResourceUrl());
			dtGeolayer = DTGeoLayer.build(response);
			if (dtGeolayer != null) {
				dtGeolayer.setStyle(layer.getDefaultStyle());
				dtGeolayer.setStyleWorkspace(layer.getDefaultStyleWorkspace());
			}
		}
		return dtGeolayer;
	}

	/**
	 * REST 요청을 통한 Geoserver Group Layer정보 조회
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param name 그룹레이어명
	 * @return Geoserver Group Layer 정보를 담고있는 {@link DTGeoGroupLayer} 객체반환 
	 */
	@SuppressWarnings("unused")
	public DTGeoGroupLayer getDTGeoGroupLayer(String workspace, String name) {
		if (workspace == null || workspace.isEmpty())
			throw new IllegalArgumentException("Workspace may not be null");
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Layername may not be null");

		String url;
		if (workspace == null) {
			url = "/rest/layergroups/" + name + ".xml";
		} else {
			url = "/rest/workspaces/" + workspace + "/layergroups/" + name + ".xml";
		}

		return DTGeoGroupLayer.build(load(url));
	}

	/**
	 * REST 요청을 통한 Geoserver Layer정보 리스트 조회 
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param layerNames 레이어 이름
	 * @return Geoserver Layer 정보를 담고있는 {@link DTGeoLayer} 객체들 반환 
	 */
	public DTGeoLayerList getDTGeoLayerList(String workspace, ArrayList<String> layerNames) {
		if (workspace == null || workspace.isEmpty())
			throw new IllegalArgumentException("Workspace may not be null");
		if (layerNames == null)
			throw new IllegalArgumentException("LayerNames may not be null");

		DTGeoLayerList geoLayerList = new DTGeoLayerList();
		if (layerNames != null) {
			for (String layerName : layerNames) {
				DTGeoLayer dtGeoLayer = null;
				dtGeoLayer = getDTGeoLayer(workspace, layerName);
				if (dtGeoLayer != null) {
					geoLayerList.add(dtGeoLayer);
				}
			}
		}
		return geoLayerList;
	}

	/**
	 * REST 요청을 통한 Geoserver Group Layer정보 리스트 조회
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param groupNames 그룹레이어 이름
	 * @return Geoserver Group Layer 정보를 담고있는 {@link DTGeoGroupLayerList} 객체들 반환 
	 */
	public DTGeoGroupLayerList getDTGeoGroupLayerList(String workspace, ArrayList<String> groupNames) {
		if (workspace == null || workspace.isEmpty())
			throw new IllegalArgumentException("Workspace may not be null");
		if (groupNames == null)
			throw new IllegalArgumentException("GroupNames may not be null");
		/*
		 * if(groupNames.size()==0) throw new
		 * IllegalArgumentException("GroupNames may not be null");
		 */

		DTGeoGroupLayerList groupLayerList = new DTGeoGroupLayerList();

		for (String groupName : groupNames) {
			DTGeoGroupLayer geoGroupLayer = getDTGeoGroupLayer(workspace, groupName);
			groupLayerList.add(geoGroupLayer);
		}
		return groupLayerList;
	};

	/**
	 * Geoserver 레이어 리스트를 jsTree(https://www.jstree.com/) 형식에 맞게 변환
	 * @author SG.LEE
	 * @param dtGeoserverList {@link DTGeoserverManager} 리스트
	 * @param parent 상위 트리명
	 * @param serverName 서버이름
	 * @param type {@link EnTreeType} SERVER, WORKSPACE, DATASTORE, LAYER
	 * @return {@link DTGeoserverTree} jsTree
	 * @throws JDOMException
	 * @throws IOException
	 */
	public DTGeoserverTree getGeoserverLayerCollectionTree(DTGeoserverManagerList dtGeoserverList, String parent,
			String serverName, EnTreeType type) throws JDOMException, IOException {
		if (dtGeoserverList == null) {
			throw new IllegalArgumentException("DTGeoserverList may not be null");
		}
		return new DTGeoserverTreeFactoryImpl().createDTGeoserverTree(dtGeoserverList, parent, serverName, type);
	}

	/**
	 * Geoserver 레이어 리스트를 jsTree(https://www.jstree.com/) 형식에 맞게 변환
	 * @author SG.LEE
	 * @param dtGeoserverList {@link DTGeoserverManager} 리스트
	 * @return {@link DTGeoserverTree} jsTree
	 * @throws JDOMException
	 * @throws IOException
	 */
	public DTGeoserverTrees getGeoserverLayerCollectionTrees(DTGeoserverManagerList dtGeoserverList)
			throws JDOMException, IOException {
		if (dtGeoserverList == null) {
			throw new IllegalArgumentException("DTGeoserverList may not be null");
		}
		return new DTGeoserverTreeFactoryImpl().createDTGeoserverTrees(dtGeoserverList);
	}


	/**
	 * Geoserver 발행되어있는 FeatureType 리스트조회
	 * @author SG.LEE
	 * @param wsName 작업공간
	 * @param dsName 저장소
	 * @param format 출력포맷
	 * @return 출력포맷에 맞는 FeatureType 정보
	 */
	public String getConfiguredFeatureTypes(String wsName, String dsName, String format) {
		String url = "/rest/workspaces/" + wsName + "/datastores/" + dsName + "featuretypes" + "." + format
				+ "?list=configured";
		return load(url);
	}

	/**
	 * Geoserver 발행되어있지 않은 FeatureType 리스트조회
	 * @author SG.LEE
	 * @param wsName 작업공간
	 * @param dsName 저장소
	 * @param format 출력포맷
	 * @return 출력포맷에 맞는 FeatureType 정보
	 */
	public String getAvailableFeatureTypes(String wsName, String dsName, String format) {
		String url = "/rest/workspaces/" + wsName + "/datastores/" + dsName + "/featuretypes" + "." + format
				+ "?list=available";
		return load(url);
	}

	/**
	 * Geoserver에 있는 전체 FeatureType 리스트 조회
	 * @author SG.LEE
	 * @param wsName 작업공간
	 * @param dsName 저장소
	 * @param format 출력포맷
	 * @return 출력포맷에 맞는 FeatureType 정보
	 */
	public String getAllFeatureTypes(String wsName, String dsName, String format) {
		String url = "/rest/workspaces/" + wsName + "/datastores/" + dsName + "/featuretypes" + "." + format
				+ "?list=all";
		return load(url);
	}

	/**
	 * list 조건에 맞는 {@link RESTFeatureTypeList} 조회 
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param datastores 저장소
	 * @param type {@link EnFeatureTypeList} CONFIGURED, AVAILABLE, ALL
	 * @return {@link RESTFeatureTypeList}
	 */
	public RESTFeatureTypeList getFeatureTypes(String workspace, String datastores, EnFeatureTypeList type) {
		String url = "/rest/workspaces/" + workspace + "/datastores/" + datastores + "/featuretypes.xml?list="+type.getType();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("### Retrieving featuretypes from " + url);
		}
		return RESTFeatureTypeList.build(load(url));
	}
	
	
	/**
	 * Geoserver 상세정보 조회
	 * @author SG.LEE
	 * @param dtGeoserverInfo Geoserver 기본정보
	 * @return JSON String 형식의 Geoserver 기본정보 반환 
	 */
	@SuppressWarnings("unchecked")
	public String getGeoserverInfo(DTGeoserverInfo dtGeoserverInfo) {
		String url = dtGeoserverInfo.getDTGeoserverInfoURL();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("### Retrieving featuretypes from " + url);
		}
		String result = loadFullURL(url);
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse( result );
			JSONObject jsonObj = (JSONObject) obj;
			
			JSONObject serverInfoDetail = new JSONObject();
			serverInfoDetail.put("url", this.baseurl);
			serverInfoDetail.put("id", this.username);
			jsonObj.put("info", serverInfoDetail);
			result = jsonObj.toJSONString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			result = "Geoserver Info Error";
		}
		return result;
	}
	
	
	/**
	 * Geoserver 이용가능한 레이어 존재여부(발행만 안된상태) 
	 * @author SG.Lee
	 * @since 2018. 12. 19. 오후 3:34:53
	 * @param workspace 작업공간
	 * @param datastores 저장소
	 * @param layerName 레이어명
	 * @return boolean 존재여부
	 * */
	public boolean existsFeatureTypesAvailable(String workspace, String datastores, String layerName){
		boolean result = false;
		String featuresString = this.getAvailableFeatureTypes(workspace, datastores, "json");
		if(featuresString!=null){
			JSONParser parser = new JSONParser();
			try {
				Object obj;
				obj = parser.parse( featuresString );
				try {
					JSONObject jsonObj = (JSONObject) obj;
					JSONObject jsonList = (JSONObject) jsonObj.get("list");
					JSONArray arrString = (JSONArray) jsonList.get("string"); 
					if(arrString!=null){
						for(int i =0; i<arrString.size(); i++){
							if(layerName!=null){
								if(arrString.get(i).equals(layerName)){
									result = true;
									break;
								}
							}else{
								LOGGER.debug("layerName Null");
								break;
							}
						}
					}
				} catch (ClassCastException e) {}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				LOGGER.debug("Json 파싱에러");
			}
		}
		return result;
	}
	
	/**
	 * Geoserver FeatureType 리스트 조회
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param datastores 저장소
	 * @return {@link RESTFeatureTypeList}
	 */
	public RESTFeatureTypeList getFeatureTypes(String workspace, String datastores) {
		String url = "/rest/workspaces/" + workspace + "/datastores/" + datastores + "/featuretypes.xml";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("### Retrieving featuretypes from " + url);
		}
		return RESTFeatureTypeList.build(load(url));
	}
	
	private String load(String url) {
		LOGGER.info("Loading from REST path " + url);
		String response = HTTPUtils.get(baseurl + url, username, password);
		return response;
	}

	private String loadFullURL(String url) {
		LOGGER.info("Loading from REST path " + url);
		String response = HTTPUtils.get(url, username, password);
		return response;
	}

}
