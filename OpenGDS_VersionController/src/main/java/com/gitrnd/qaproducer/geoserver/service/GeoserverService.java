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

package com.gitrnd.qaproducer.geoserver.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gitrnd.gdsbuilder.geolayer.data.DTGeoGroupLayerList;
import com.gitrnd.gdsbuilder.geolayer.data.DTGeoLayerList;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.type.geoserver.layer.GeoLayerInfo;



/** 
* @ClassName: GeoserverService 
* @Description: Geoserver와 관련된 데이터를 처리한다.
* @author JY.Kim 
* @date 2017. 4. 3. 오후 2:59:12 
*/
public interface GeoserverService {
	
	
	
	/**
	 * @Description SHP파일 발행(레이어 존재해있어야함)
	 * @author SG.Lee
	 * @Date 2018. 7. 31. 오전 9:44:27
	 * @param dtGeoManager 
	 * @param workspace 작업공간
	 * @param dsName 저장소
	 * @param layerName 레이어명
	 * @param zipFile 대상파일(shp,dxf,shx...)
	 * @param srs 좌표계
	 * @return boolean
	 * */
	public boolean shpLayerPublishGeoserver(DTGeoserverManager dtGeoManager, String workspace, String dsName, String layerName, File zipFile, String srs);
	
	/**
	 * @Description SHP파일 발행(레이어 존재해있어야함)
	 * @author SG.Lee
	 * @Date 2018. 7. 31. 오전 9:44:29
	 * @param dtGeoManager 
	 * @param workspace 작업공간
	 * @param dsName 저장소
	 * @param layerName 레이어명
	 * @param zipFile 대상파일(shp,dxf,shx...)
	 * @param srs 좌표계
	 * @param defaultStyle 스타일
	 * @return boolean
	 * */
	public boolean shpLayerPublishGeoserver(DTGeoserverManager dtGeoManager, String workspace, String dsName, String layerName, File zipFile, String srs, String defaultStyle);
	
	
	
	
	/**
	 * @Description SHP파일 업로드
	 * @author SG.Lee
	 * @Date 2018. 11. 5. 오후 5:35:37
	 * @param dtGeoManager
	 * @param workspace 작업공간
	 * @param datastore 저장소
	 * @param request MultipartHttpServletRequest(
	 * @return int 
	 *         200 : 성공
	 *         500 : 발행실패
	 *         600 : 로그인세션 없음
	 *         604 : Geoserver 정보오류
	 *         607 :  workspace 또는 datastore 존재 X
	 *         608 : 파일이 2개이상
	 *         609 : 레이어 중복
	 * */
	public int shpCollectionPublishGeoserver(DTGeoserverManager dtGeoManager, String workspace, String datastore, MultipartHttpServletRequest request);
	
	
	/**
	 * @Description 에러 레이어 발행 
	 * @author SG.Lee
	 * @Date 2018. 7. 5. 오전 10:26:25
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param dsName - Geoserver Datasource명
	 * @param layerInfo - 레이어 정보객체
	 * @return boolean
	 * */
	public boolean errLayerPublishGeoserver(DTGeoserverManager dtGeoManager, String workspace, String dsName, GeoLayerInfo layerInfo);
	
	/**
	 * @Description Workspace 조건에 따른 Workspace단위 트리생성
	 * @author SG.Lee
	 * @Date 2018. 7. 13. 오후 4:48:25
	 * @param dtGeoManagers
	 * @param serverName 서버명
	 * @return JSONArray
	 * */
	public JSONArray getGeoserverLayerCollectionTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName, String type);
	
	/**
	 * @Description 전체 트리생성 
	 * @author SG.Lee
	 * @Date 2018. 7. 13. 오후 4:58:43
	 * @param dtGeoserverMList
	 * @return JSONArray
	 * */
	public JSONArray getGeoserverLayerCollectionTrees(DTGeoserverManagerList dtGeoserverMList);
	
	
	/**
	 * 레이어를 중복체크한다.
	 * @author SG.Lee
	 * @Date 2017. 7
	 * @param dtGeoManager - DTGeoserverManager Object 
	 * @param workspace - Geoserver Workspace명
	 * @param layerList - 중복체크할 레이어명 리스트
	 * @return JSONObject - {레이어명 : 중복여부}
	 * */
	public JSONObject duplicateCheck(DTGeoserverManager dtGeoManager, String workspace, ArrayList<String> layerList);
	
	/**
	 * DTGeoLayerList를 조회한다.
	 * @author SG.Lee
	 * @Date 2017. 4
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param layerList - 레이어명 리스트
	 * @return DTGeoLayerList
	 * */
	public DTGeoLayerList getGeoLayerList(DTGeoserverManager dtGeoManager, String workspace, ArrayList<String> layerList);
	
	/**
	 * DTGeoGroupLayerList를 조회한다.
	 * @author SG.Lee 
	 * @Date 2017. 4
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param groupList - 그룹레이어명 리스트
	 * @return DTGeoGroupLayerList
	 * */
	public DTGeoGroupLayerList getGeoGroupLayerList(DTGeoserverManager dtGeoManager, String workspace, ArrayList<String> groupList);
	
	/**
	 * 그룹레이어 삭제
	 * @author SG.Lee
	 * @Date 2018. 7. 5. 오후 1:17:28
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param dsName - Geoserver Datasource명
	 * @param groupLayerName - 그룹레이어명
	 * @param layerName - 레이어명
	 * @return boolean
	 * */
	public boolean removeDTGeoserverLayer(DTGeoserverManager dtGeoManager, String workspace, String dsName, String groupLayerName, String layerName);
	
	/**
	 * 다중 레이어를 삭제
	 * @author SG.Lee
	 * @Date 2017. 6. 5. 오전 10:40:17
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param layerNameList 삭제할 레이어 이름 리스트
	 * @return int - 200 성공
	 *              - 500 요청실패
	 *              - 605 해당 조건에 맞는 서버존재X
	 *              - 606 일부성공 또는 실패
	 * */
	public int removeDTGeoserverLayers(DTGeoserverManager dtGeoManager, String workspace, List<String> layerNameList);
	
	/**
	 * Geoserver Workspace내의 모든 레이어삭제
	 * @author SG.Lee
	 * @Date 2017. 6. 5. 오전 11:08:03
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param workspace - Geoserver Workspace명
	 * @param dsName - Geoserver Datasource명
	 * @param groupLayerName 삭제할 그룹레이어
	 * @return boolean - 삭제여부
	 * */
	public boolean removeDTGeoserverAllLayer(DTGeoserverManager dtGeoManager, String workspace, String dsName, final String groupLayerName);
	
	
	/**
	 * Geoserver 스타일리스트 조회
	 * @author SG.Lee
	 * @Date 2017. 6. 19. 오후 9:15:07
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @return boolean
	 * */
	public List<String> getGeoserverStyleList(DTGeoserverManager dtGeoManager);
	
	
	/**
	 * Geoserver 스타일을 생성
	 * @author SG.Lee
	 * @Date 2017. 6. 7. 오후 6:15:55
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param sldBody
	 * @param name
	 * @return boolean
	 * */
	public boolean publishStyle(DTGeoserverManager dtGeoManager, String sldBody, String name);
	
	/**
	 * Geoserver 스타일을 수정한다.
	 * @author SG.Lee
	 * @Date 2017. 6. 7. 오후 6:15:57
	 * @param dtGeoManager - DTGeoserverManager Object 
	 * @param sldBody
	 * @param name
	 * @return boolean
	 * */
	public boolean updateStyle(DTGeoserverManager dtGeoManager, final String sldBody, final String name);
	
	/**
	 * Geoserver 스타일을 삭제한다.
	 * @author SG.Lee
	 * @Date 2017. 6. 7. 오후 6:16:01
	 * @param dtGeoManager - DTGeoserverManager Object
	 * @param styleName
	 * @return boolean
	 * */
	public boolean removeStyle(DTGeoserverManager dtGeoManager, final String styleName);
	
	
	
	/**
	 * Geoserver 레이어를 업데이트한다.
	 * @author SG.Lee
	 * @Date 2017. 6. 19. 오후 7:45:22
	 * @param orginalName
	 * @param name
	 * @param title
	 * @param abstractContent
	 * @param style
	 * @param attChangeFlag
	 * @return boolean
	 * */
	public boolean updateFeatureType(DTGeoserverManager dtGeoManager, String workspace, String dsName, final String orginalName,final String name,final String title,final String abstractContent,final String srs, final String style, boolean attChangeFlag);
	
	
	
	/**
	 * @Description WFST 서비스를 요청한다.
	 * @author SG.Lee
	 * @Date 2018. 7. 20. 오후 2:47:50
	 * @param dtGeoManager
	 * @param wfstXml
	 * @return String
	 * */
	public String requestWFSTransaction(DTGeoserverManager dtGeoManager, String workspace, String wfstXml);
	
	
	/**
	 * @Description sld 조회
	 * @author SG.Lee
	 * @Date 2018. 8. 16. 오후 1:26:03
	 * @param dtGeoManager
	 * @param workspace
	 * @param layerName
	 * @return String
	 * */
	public String getLayerStyleSld(DTGeoserverManager dtGeoManager, String workspace, String layerName);
	
	
	/**
	 * @Description 레이어 유효성체크
	 * @author SG.Lee
	 * @Date 2018. 11. 5. 오후 3:22:37
	 * @param dtGeoManager
	 * @param workspace 작업공간
	 * @param layerName 레이어명
	 * @return boolean
	 * */
	public boolean exsistLayer(DTGeoserverManager dtGeoManager, String workspace, String layerName);
}


