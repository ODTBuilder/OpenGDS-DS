package com.gitrnd.gdsbuilder.geoserver.data.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoLayer;
import com.gitrnd.gdsbuilder.geoserver.layer.DTGeoLayerList;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStoreList;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureTypeList;
import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList;

/**
 * {@link DTGeoserverManagerList} 에 해당하는 모든 서버에 대해 
 * jsTree(https://www.jstree.com/) 형식에 맞게 변환해주는 클래스
 * @author SG.LEE
 */
@SuppressWarnings("serial")
public class DTGeoserverTrees extends JSONArray {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static final String delimiter = ":";

	/**
	 * {@link DTGeoserverTrees} 생성자
	 * @author SG.LEE
	 * @param dtGeoserverList {@link DTGeoserverManagerList} 서버정보 리스트
	 */
	@SuppressWarnings("unchecked")
	public DTGeoserverTrees(DTGeoserverManagerList dtGeoserverList) {
		build(dtGeoserverList);
	}

	/**
	 * {@link DTGeoserverManagerList}를 {@link DTGeoserverTrees} 형태로 변환
	 * @author SG.LEE
	 * @param dtGeoserverList {@link DTGeoserverManagerList} 서버정보 리스트
	 * @return {@link DTGeoserverTrees}
	 */
	@SuppressWarnings("unchecked")
	public DTGeoserverTrees build(DTGeoserverManagerList dtGeoserverList) {
		if (dtGeoserverList != null) {
			Iterator<String> keys = dtGeoserverList.keySet().iterator();

			while (keys.hasNext()) {
				String serverName = (String) keys.next();
				DTGeoserverManager dtGeoManager = dtGeoserverList.get(serverName);

				if (dtGeoManager != null) {
					DTGeoserverReader dtGeoserverReader = dtGeoManager.getReader();
					if (dtGeoserverReader != null) {
						JSONObject serverTree = new JSONObject();
						serverTree.put("id", serverName);
						serverTree.put("parent", "#");
						serverTree.put("text", serverName);
						serverTree.put("type", "geoserver");
						super.add(serverTree);

						RESTWorkspaceList restWorkspaceList = dtGeoserverReader.getWorkspaces();

						if (restWorkspaceList != null) {
							for (RESTWorkspaceList.RESTShortWorkspace item : restWorkspaceList) {
								String wsName = item.getName();
								JSONObject wsTree = new JSONObject();
								wsTree.put("id", serverName + delimiter + wsName);
								wsTree.put("parent", serverName);
								wsTree.put("text", wsName);
								wsTree.put("type", "workspace");
								super.add(wsTree);
//								wsTree.clear();

								RESTDataStoreList dataStoreList = dtGeoserverReader.getDatastores(wsName);

								List<String> dsNames = dataStoreList.getNames();

								for (String dsName : dsNames) {
									RESTDataStore dStore = dtGeoserverReader.getDatastore(wsName, dsName);
									if (dStore != null) {
										String dsType = dStore.getStoreType();
										JSONObject dsTree = new JSONObject();
										dsTree.put("id", serverName + delimiter + wsName + delimiter + dsName);
										dsTree.put("parent", serverName + delimiter + wsName);
										dsTree.put("text", dsName);
										dsTree.put("type", "datastore");
										super.add(dsTree);
//										dsTree.clear();

										RESTFeatureTypeList ftList = dtGeoserverReader.getFeatureTypes(wsName, dsName);
										ArrayList<String> layerNames = new ArrayList<String>(ftList.getNames());

										DTGeoLayerList dtGLayerList = dtGeoserverReader.getDTGeoLayerList(wsName,
												layerNames);

										for (DTGeoLayer dtGLayer : dtGLayerList) {
											if (dtGLayer != null) {
												JSONObject layerTree = new JSONObject();
												layerTree.put("id", serverName + delimiter + wsName + delimiter + dsName
														+ delimiter + dtGLayer.getlName());
												layerTree.put("parent",
														serverName + delimiter + wsName + delimiter + dsName);
												layerTree.put("text", dtGLayer.getlName());
												layerTree.put("type", dtGLayer.getGeomType().toLowerCase());
												super.add(layerTree);
//												layerTree.clear();
											}
										}
									}
								}
							}
						} else {
							JSONObject errorJSON = new JSONObject();
							errorJSON.put("id", 500);
							errorJSON.put("parent", "#");
							errorJSON.put("text", "Geoserver에 Workspace가 존재하지 않습니다");
							errorJSON.put("type", "error");

							super.add(errorJSON);
							logger.warn("Geoserver에 Workspace가 존재하지 않습니다");
						}
					}
				}
			}
		} else {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("id", 500);
			errorJSON.put("parent", "#");
			errorJSON.put("text", "Geoserver를 다시 추가해주세요");
			errorJSON.put("type", "error");

			super.add(errorJSON);
			logger.warn("Geoserver를 다시 추가해주세요");
		}
		return this;
	}
}
