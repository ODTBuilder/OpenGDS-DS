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

package com.gitrnd.gdsbuilder.geoserver.data.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geogig.command.repository.branch.ListBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch.Branch;
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
 * {@link DTGeoserverManagerList}에 해당하는 서버에 대해 
 * {@link EnTreeType}에 맞는 정보를 
 * jsTree(https://www.jstree.com/) 형식에 맞게 변환해주는 클래스
 * @author SG.Lee
 * @since 2018. 7. 12. 오후 6:56:21
 */
@SuppressWarnings("serial")
public class DTGeoserverTree extends JSONArray {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static final String delimiter = ":";

	/**
	 * jsTree 출력 타입
	 * @author SG.LEE
	 */
	public enum EnTreeType {
		SERVER("server"), WORKSPACE("workspace"), DATASTORE("datastore"), LAYER("layer"), UNKNOWN(null);

		String type;

		private EnTreeType(String type) {
			this.type = type;
		}

		/**
		 * type명으로 부터 {@link EnTreeType} 조회
		 * @author SG.LEE
		 * @param type type명
		 * @return {@link EnTreeType}
		 */
		public static EnTreeType getFromType(String type) {
			for (EnTreeType tt : values()) {
				if (tt == UNKNOWN)
					continue;
				if (tt.type.equals(type))
					return tt;
			}
			return UNKNOWN;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	/**
	 * type이 EnTreeType.SERVER 일경우에
	 * 
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @param type EnTreeType.SERVER
	 */
	public DTGeoserverTree(DTGeoserverManagerList dtGeoManagers, EnTreeType type) {
		if (type == EnTreeType.SERVER) {
			build(dtGeoManagers);
		} else {
			logger.error("TreeType이 Server가 아닙니다.");
		}
	}

	/**
	 * type이 EnTreeType.SERVER가 아닐때
	 * 
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @param parent 상위 트리명
	 * @param serverName 서버이름
	 * @param type EnTreeType.SERVER을 제외한 {@link EnTreeType}
	 */
	public DTGeoserverTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName, EnTreeType type) {
		build(dtGeoManagers, parent, serverName, type);
	}

	/**
	 * {@link DTGeoserverManagerList}를 {@link DTGeoserverTree} 형태로 변환
	 * EnTreeType.SERVER 타입일 경우
	 * @author SG.Lee
	 * @since 2018. 7. 19. 오후 3:42:51
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @return {@link DTGeoserverTree}
	 */
	@SuppressWarnings("unchecked")
	public DTGeoserverTree build(DTGeoserverManagerList dtGeoManagers) {
		if (dtGeoManagers != null) {
			Iterator<String> keys = dtGeoManagers.keySet().iterator();

			while (keys.hasNext()) {
				String serverName = (String) keys.next();
				DTGeoserverManager dtGeoManager = dtGeoManagers.get(serverName);

				if (dtGeoManager != null) {
					DTGeoserverReader dtGeoserverReader = dtGeoManager.getReader();
					if (dtGeoserverReader != null) {

						JSONObject serverTree = new JSONObject();
						serverTree.put("id", serverName);
						serverTree.put("parent", "#");
						serverTree.put("text", serverName);
						serverTree.put("type", "geoserver");

						RESTWorkspaceList rwList = dtGeoserverReader.getWorkspaces();

						if (rwList != null) {
							if (rwList.size() > 0) {
								serverTree.put("children", true);
							} else {
								serverTree.put("children", false);
							}
						} else {
							serverTree.put("children", false);
						}

						super.add(serverTree);
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

	/**
	 * {@link DTGeoserverManagerList}를 {@link DTGeoserverTree} 형태로 변환
	 * @author SG.Lee
	 * @since 2018. 7. 19. 오후 3:46:01
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @param parent jstree parent ID
	 * @param serverName 서버이름
	 * @param type jsTree 출력 타입
	 * @return DTGeoserverTree {@link DTGeoserverTree}
	 */
	@SuppressWarnings("unchecked")
	public DTGeoserverTree build(DTGeoserverManagerList dtGeoManagers, String parent, String serverName,
			EnTreeType type) {
		if (dtGeoManagers != null && type != null) {
			Iterator<String> keys = dtGeoManagers.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (key.equals(serverName)) {
					String[] param = parent.split(":");// ex) 테스트서버

					DTGeoserverManager dtGeoManager = dtGeoManagers.get(serverName);

					if (dtGeoManager != null) {
						DTGeoserverReader dtGeoserverReader = dtGeoManager.getReader();

						if (type == EnTreeType.WORKSPACE) {
							RESTWorkspaceList restWorkspaceList = dtGeoserverReader.getWorkspaces();
							if (restWorkspaceList != null) {
								for (RESTWorkspaceList.RESTShortWorkspace item : restWorkspaceList) {
									String wsName = item.getName();
									JSONObject wsTree = new JSONObject();
									wsTree.put("id", serverName + delimiter + wsName);
									wsTree.put("parent", parent);
									wsTree.put("text", wsName);
									wsTree.put("type", "workspace");

									RESTDataStoreList dataStoreList = dtGeoserverReader.getDatastores(wsName);
									if (dataStoreList != null) {
										if (dataStoreList.size() > 0) {
											wsTree.put("children", true);
										} else {
											wsTree.put("children", false);
										}
									} else {
										wsTree.put("children", false);
									}
									super.add(wsTree);
								}
							}
						} else if (type == EnTreeType.DATASTORE) {
							if (param != null) {
								if (param.length > 1) {
									String workspace = param[1];
									RESTDataStoreList dataStoreList = dtGeoserverReader.getDatastores(workspace);
									if (dataStoreList != null) {
										List<String> dsNames = dataStoreList.getNames();
										for (String dsName : dsNames) {
											RESTDataStore dStore = dtGeoserverReader.getDatastore(workspace, dsName);
											if (dStore != null) {
												String storeType = dStore.getStoreType();
												JSONObject dsTree = new JSONObject();
												dsTree.put("id",
														serverName + delimiter + workspace + delimiter + dsName);
												dsTree.put("parent", parent);
												dsTree.put("text", dsName);
												dsTree.put("type", "datastore");
												dsTree.put("storeType", storeType);
												if(storeType!=null){
													if (storeType.equals("GeoGIG")) {
														Map<String, String> connetParams = dStore.getConnectionParameters();
														String geogigRepos = connetParams.get("geogig_repository");
														String reposName = geogigRepos.replace("geoserver://", "");
														dsTree.put("geogigRepos", reposName);
														String branchName = connetParams.get("branch");
														dsTree.put("geogigBranch", branchName);
														ListBranch listBranch = new ListBranch();
														GeogigBranch geogigBranch = listBranch.executeCommand(
																dtGeoManager.getRestURL(), dtGeoManager.getUsername(),
																dtGeoManager.getPassword(), reposName, false);
														List<Branch> branchList = geogigBranch.getLocalBranchList();
														JSONArray branchArr = new JSONArray();
														for (Branch branch : branchList) {
															branchArr.add(branch.getName());
														}
														dsTree.put("geogigBranches", branchArr);
														System.out.println("");
													}
												}
												RESTFeatureTypeList ftList = dtGeoserverReader
														.getFeatureTypes(workspace, dsName);
												if (ftList != null) {
													if (ftList.size() > 0) {
														dsTree.put("children", true);
													} else {
														dsTree.put("children", false);
													}
												} else {
													dsTree.put("children", false);
												}
												super.add(dsTree);
											}
										}
									}
								}
							}
						} else if (type == EnTreeType.LAYER) {
							if (param != null) {
								if (param.length > 2) {
									String wsName = param[1];
									String dsName = param[2];

									RESTFeatureTypeList ftList = dtGeoserverReader.getFeatureTypes(wsName, dsName);
									ArrayList<String> layerNames = new ArrayList<String>(ftList.getNames());

									DTGeoLayerList dtGLayerList = dtGeoserverReader.getDTGeoLayerList(wsName,
											layerNames);

									for (DTGeoLayer dtGLayer : dtGLayerList) {
										if (dtGLayer != null) {
											JSONObject layerTree = new JSONObject();
											layerTree.put("id", serverName + delimiter + wsName + delimiter + dsName
													+ delimiter + dtGLayer.getlName());
											layerTree.put("parent", parent);
											layerTree.put("text", dtGLayer.getlName());
											layerTree.put("type", dtGLayer.getGeomType().toLowerCase());
											layerTree.put("children", false);
											super.add(layerTree);
										}
									}
								}
							}
						}
					}
				}
			}
		} else {
			logger.warn("DTGeoserverManagerList or EnTreeType null");
		}
		return this;
	}
}
