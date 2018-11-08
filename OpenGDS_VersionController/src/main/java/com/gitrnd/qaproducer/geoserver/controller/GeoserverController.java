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

package com.gitrnd.qaproducer.geoserver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gitrnd.gdsbuilder.geolayer.data.DTGeoGroupLayerList;
import com.gitrnd.gdsbuilder.geolayer.data.DTGeoLayerList;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.controller.AbstractController;
import com.gitrnd.qaproducer.geoserver.service.GeoserverLayerProxyService;
import com.gitrnd.qaproducer.geoserver.service.GeoserverService;

/**
 * @ClassName: GeoserverController
 * @Description: Geoserver 관련된 요청을 처리한다.
 * @author JY.Kim
 * @date 2017. 4. 3. 오후 2:16:03
 * 
 */
@Controller("geoserverController")
@RequestMapping("/geoserver")
public class GeoserverController extends AbstractController {

	@Autowired
	@Qualifier("geoService")
	private GeoserverService geoserverService;

	@Autowired
	@Qualifier("proService")
	private GeoserverLayerProxyService proService;

	@RequestMapping(value = "/addGeoserver.ajax", method = RequestMethod.POST)
	@ResponseBody
	public int addGeoserver(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		return super.addGeoserverToSession(request, loginUser);
	}
	
	@RequestMapping(value = "/removeGeoserver.ajax", method = RequestMethod.POST)
	@ResponseBody
	public int removeGeoserver(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		return super.removeGeoserverToSession(request, loginUser);
	}
	
	/**
	 * @Description 로그인한 계정에 대한 Geoserver 트리요청(serverName 조건부) 
	 * @author SG.Lee
	 * @Date 2018. 7. 13. 오후 5:00:28
	 * @param request
	 * @param loginUser
	 * @param workspace
	 * @return JSONArray
	 * */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/getGeolayerCollectionTree.ajax")
	@ResponseBody
	public JSONArray getGeolayerCollectionTree(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser, @RequestParam(value = "node", required = false) String parent,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "serverName", required = false) String serverName) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		DTGeoserverManagerList sessionGMList = super.getGeoserverManagersToSession(request, loginUser);
		return geoserverService.getGeoserverLayerCollectionTree(sessionGMList, parent, serverName, type);
	}
	
	/**
	 * @Description 로그인한 계정에 대한 Geoserver 전체 트리 요청  
	 * @author SG.Lee
	 * @Date 2018. 7. 13. 오후 5:00:28
	 * @param request
	 * @param loginUser
	 * @param workspace
	 * @return JSONArray
	 * */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/getGeolayerCollectionTrees.ajax")
	@ResponseBody
	public JSONArray getGeolayerCollectionTrees(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		DTGeoserverManagerList sessionGMList = super.getGeoserverManagersToSession(request, loginUser);
		JSONArray trees = geoserverService.getGeoserverLayerCollectionTrees(sessionGMList); 
		return trees;
	}
	
	
	/**
	 * @Description WFST
	 * @author SG.Lee
	 * @Date 2018. 7. 20. 오후 2:59:37
	 * @param request
	 * @param loginUser
	 * @return String
	 * */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/geoserverWFSTransaction.ajax", method = RequestMethod.POST)
	@ResponseBody
	public String geoserverWFSTransaction(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) jsonObject.get("serverName");
		String workspace = (String) jsonObject.get("workspace");
		String wfstXml = (String) jsonObject.get("wfstXml");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return geoserverService.requestWFSTransaction(dtGeoserverManager,workspace, wfstXml);
	}
	
	/**
	 * @Description
	 * @author SG.Lee
	 * @Date 2018. 7. 20. 오후 2:59:37
	 * @param request
	 * @param loginUser
	 * @return String
	 * @throws IOException 
	 * */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/updateLayer.ajax", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateLayer(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) throws IOException {
		boolean flag = false;
		
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) jsonObject.get("serverName");
		String workspace = (String) jsonObject.get("workspace");
		String datastore = (String) jsonObject.get("datastore");
		String originalName = (String) jsonObject.get("originalName");
		String name = (String) jsonObject.get("name");
		String title = (String) jsonObject.get("title");
		String abstractContent = (String) jsonObject.get("abstractContent");
		String srs = (String) jsonObject.get("srs");
		String style = (String) jsonObject.get("style");
		
		
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}
		
		if(serverName==null||serverName.isEmpty()||workspace==null||workspace.isEmpty()||datastore==null||datastore.isEmpty()||originalName==null||originalName.isEmpty()){
			response.sendError(500, "필수값을 입력하지 않았습니다.");
		}else{
			flag = geoserverService.updateFeatureType(dtGeoserverManager, workspace, datastore, originalName, name, title, abstractContent, srs, style, false);
		}
		
		return flag;
	}
	


	/**
	 * WMS레이어 요청 
	 * @author SG.Lee 
	 * @Date 2017. 4 
	 * @param request
	 * @param response 
	 * @throws Exception 
	 */
	@RequestMapping(value = "geoserverWMSGetMap.ajax", method = RequestMethod.GET)
	@ResponseBody
	public void geoserverGetWMSGetMap(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser)
			throws Exception {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		String workspace = (String) request.getParameter("workspace");
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}else if(workspace.equals("")||workspace==null){
			response.sendError(500, "workspace를 입력하지 않았습니다.");
		}
		else{
			proService.requestGetMap(dtGeoserverManager, workspace, request, response);
		}
	}

	/**
	 * WFSGetFeature GET
	 * @author SG.Lee
	 * @Date 2018. 7. 9. 오후 3:30:17
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException void
	 * */
	@RequestMapping(value = "geoserverWFSGetFeature.ajax", method = RequestMethod.GET)
	@ResponseBody
	public void geoserverGetWFSGetFeature(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser)
			throws ServletException, IOException, Exception{
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		String workspace = (String) request.getParameter("workspace");
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}else if(workspace.equals("")||workspace==null){
			response.sendError(500, "workspace를 입력하지 않았습니다.");
		}
		else{
			proService.requestGetFeature(dtGeoserverManager, workspace, request, response);
		}
	}

	/**
	 * WMSGetFeatureInfo
	 * @author SG.Lee
	 * @Date 2018. 7. 9. 오후 3:32:51
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException void
	 * */
	@RequestMapping(value = "geoserverWMSGetFeatureInfo.ajax", method = RequestMethod.GET)
	@ResponseBody
	public void geoserverWMSGetFeatureInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser)
			throws ServletException, IOException{
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		String workspace = (String) request.getParameter("workspace");
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}else if(workspace.equals("")||workspace==null){
			response.sendError(500, "workspace를 입력하지 않았습니다.");
		}
		else{
			proService.requestGetFeatureInfo(dtGeoserverManager, workspace, request, response);
		}
	}

	/**
	 * getWMSGetLegendGraphic
	 * @author SG.Lee
	 * @Date 2018. 7. 9. 오후 3:33:02
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException void
	 * */
	@RequestMapping(value = "geoserverWMSGetLegendGraphic.ajax")
	@ResponseBody
	public void geoserverWMSGetLegendGraphic(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser)
			throws ServletException, IOException{
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		String workspace = request.getParameter("workspace");
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}else if(workspace.equals("")||workspace==null){
			response.sendError(500, "workspace를 입력하지 않았습니다.");
		}
		else{
			proService.requestWMSGetLegendGraphic(dtGeoserverManager, workspace, request, response);
		}
	}
	
	
	
	/**
	 * GeoserverInfo Request
	 * @author SG.Lee
	 * @Date 2018. 7. 9. 오후 3:30:17
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException void
	 * */
	@RequestMapping(value = "getDTGeoserverInfo.ajax")
	@ResponseBody
	public void getDTGeoserverInfo(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser)
			throws ServletException, IOException, Exception{
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}
		else{
			proService.requestGeoserverInfo(dtGeoserverManager, request, response);
		}
	}
	
	

	/**
	 * Geoserver Layer 조회
	 * @author SG.Lee 
	 * @Date 2017. 4 
	 * @param request 
	 * @param jsonObject
	 * @return DTGeoLayerList 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getGeoLayerInfoList.ajax")
	@ResponseBody
	public DTGeoLayerList getGeoLayerList(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) throws Exception{
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		List<String> geoLayerList = new ArrayList<String>();
		geoLayerList = (ArrayList<String>) jsonObject.get("geoLayerList");
		if (geoLayerList.size() == 0) {
			return null;
		} else{
			String serverName = (String) jsonObject.get("serverName");
			DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
			if(dtGeoserverManager==null){
				return null;
			}else{
				String workspace = (String) jsonObject.get("workspace");
				return geoserverService.getGeoLayerList(dtGeoserverManager, workspace, (ArrayList<String>) geoLayerList);
			}
		}
	}

	/**
	 * 레이어 중복체크
	 * @author SG.Lee 
	 * @Date 2017. 5 
	 * @param request 
	 * @param jsonObject 
	 * @return DTGeoLayerList 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "layerDuplicateCheck.ajax")
	@ResponseBody
	public JSONObject duplicateCheck(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		List<String> layerList = new ArrayList<String>();
		layerList = (ArrayList<String>) jsonObject.get("layerList");
		if (layerList.size() == 0) {
			return null;
		}else{
			String serverName = (String) jsonObject.get("serverName");
			DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
			if(dtGeoserverManager==null){
				return null;
			}else{
				String workspace = request.getParameter("workspace");
				return geoserverService.duplicateCheck(dtGeoserverManager, workspace, (ArrayList<String>) layerList);
			}
		}
	}
	
	
	/**
	 * @Description 레이어 삭제
	 * @author SG.Lee
	 * @Date 2018. 8. 2. 오전 10:14:37
	 * @param request
	 * @param jsonObject
	 * @param loginUser
	 * @return JSONObject
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "geoserverRemoveLayers.ajax")
	@ResponseBody
	public int geoserverRemoveLayers(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser){
		int resultFlag = 500;
		if(loginUser==null){
			resultFlag = 600; 
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		List<String> layerList = new ArrayList<String>();
		layerList = (ArrayList<String>) jsonObject.get("layerList");
		String serverName = (String) jsonObject.get("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		if(dtGeoserverManager==null){
			resultFlag = 603;
		}else{
			String workspace = (String) jsonObject.get("workspace");
			resultFlag =  geoserverService.removeDTGeoserverLayers(dtGeoserverManager, workspace, layerList);
		}
		return resultFlag;
	}
	

	/**
	 * Geoserver Group레이어 조회 
	 * @author SG.Lee 
	 * @Date 2017. 4 
	 * @param request 
	 * @param jsonObject 
	 * @return DTGeoLayerList 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getGeoGroupLayerInfoList.ajax")
	@ResponseBody
	public DTGeoGroupLayerList getGeoGroupLayerInfoList(HttpServletRequest request,
			@RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		List<String> geoLayerList = new ArrayList<String>();
		geoLayerList = (ArrayList<String>) jsonObject.get("geoLayerList");
		if (geoLayerList.size() == 0) {
			return null;
		}else{
			String serverName = (String) jsonObject.get("serverName");
			DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
			if(dtGeoserverManager==null){
				return null;
			}else{
				String workspace = request.getParameter("workspace");
				return geoserverService.getGeoGroupLayerList(dtGeoserverManager, workspace, (ArrayList<String>) geoLayerList);
			}
		}
	}
	
	@RequestMapping(value = "publishGeoserverStyle.ajax")
	@ResponseBody
	public void publishGeoserverStyle(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		String sldBody = (String) jsonObject.get("sldBody");
		String name = (String) jsonObject.get("name");

		String serverName = (String) jsonObject.get("serverName");
		
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		
		geoserverService.publishStyle(dtGeoserverManager, sldBody, name);
	}

	@RequestMapping(value = "updateGeoserverStyle.ajax")
	@ResponseBody
	public void updateGeoserverStyle(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		String sldBody = (String) jsonObject.get("sldBody");
		String name = (String) jsonObject.get("name");
		String serverName = (String) jsonObject.get("serverName");

		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		
		geoserverService.updateStyle(dtGeoserverManager, sldBody, name);
	}

	@RequestMapping(value = "removeGeoserverStyle.ajax")
	@ResponseBody
	public void removeGeoserverStyle(HttpServletRequest request, @RequestBody JSONObject jsonObject, @AuthenticationPrincipal LoginUser loginUser) {
		String name = (String) jsonObject.get("name");
		String serverName = (String) jsonObject.get("serverName");
		
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		
		geoserverService.removeStyle(dtGeoserverManager, name);
	}
	
	@RequestMapping(value = "/getLayerStyleSld.ajax")
	@ResponseBody
	public String getLayerStyleSld(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser, 
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "workspace", required = false) String workspace,
			@RequestParam(value = "layerName", required = false) String layerName) {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return geoserverService.getLayerStyleSld(dtGeoserverManager, workspace, layerName);
	}
	
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	public void uploadProcess(MultipartHttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = (String) request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		String workspace = (String) request.getParameter("workspace");
		String datastore = (String) request.getParameter("datastore");
		if(dtGeoserverManager==null){
			response.sendError(500, "Geoserver 세션이 존재하지 않습니다.");
		}else if(workspace.equals("")||workspace==null){
			response.sendError(500, "workspace를 입력하지 않았습니다.");
		}
		else{
			geoserverService.shpCollectionPublishGeoserver(dtGeoserverManager,workspace,datastore,request);
		}
		geoserverService.shpCollectionPublishGeoserver(dtGeoserverManager,workspace,datastore,request);
	}
}
