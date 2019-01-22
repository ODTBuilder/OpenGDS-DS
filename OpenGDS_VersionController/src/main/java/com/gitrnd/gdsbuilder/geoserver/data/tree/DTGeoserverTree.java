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
import com.gitrnd.gdsbuilder.geolayer.data.DTGeoLayer;
import com.gitrnd.gdsbuilder.geolayer.data.DTGeoLayerList;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStoreList;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureTypeList;
import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList;

/**
 * @Description GeoserverLayer Tree 관련 클래스
 * @author SG.Lee
 * @Date 2018. 7. 12. 오후 6:56:21
 */
@SuppressWarnings("serial")
public class DTGeoserverTree extends JSONArray {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	static final String delimiter = ":";

	public enum EnTreeType {
		SERVER("server"), WORKSPACE("workspace"), DATASTORE("datastore"), LAYER("layer"), UNKNOWN(null);

		String type;

		private EnTreeType(String type) {
			this.type = type;
		}

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
	 * @param dtGeoManagers
	 * @param type
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
	 * @param dtGeoManagers
	 * @param parent
	 * @param serverName
	 * @param type
	 */
	public DTGeoserverTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName, EnTreeType type) {
		build(dtGeoManagers, parent, serverName, type);
	}

	/**
	 * @Description Server type
	 * @author SG.Lee
	 * @Date 2018. 7. 19. 오후 3:42:51
	 * @param dtGeoManagers
	 * @return DTGeoserverTree
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
	 * @Description server Type 외
	 * @author SG.Lee
	 * @Date 2018. 7. 19. 오후 3:46:01
	 * @param dtGeoManagers
	 * @param parent        jstree parent ID
	 * @param serverName    서버이름
	 * @param type
	 * @return DTGeoserverTree
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
												if (storeType != null) {
													if (storeType.equals("GeoGIG")) {
														Map<String, String> connetParams = dStore
																.getConnectionParameters();
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

											String krName = null;
											String name = dtGLayer.getlName();

											if (name.contains("A0080000")) {
												krName = "교차로";
											}
											if (name.contains("A0070000")) {
												krName = "교량";
											}
											if (name.contains("C0390000")) {
												krName = "계단";
											}
											if (name.contains("D0010000")) {
												krName = "경지계";
											}
											if (name.contains("B0010000")) {
												krName = "건물경계";
											}
											if (name.contains("E0052114")) {
												krName = "호수저수지";
											}
											if (name.contains("E0020000")) {
												krName = "하천중심선";
											}
											if (name.contains("E0010001")) {
												krName = "하천경계";
											}
											if (name.contains("D0020000")) {
												krName = "지류계";
											}
											if (name.contains("C0430000")) {
												krName = "주차장";
											}
											if (name.contains("C0423365")) {
												krName = "주유소";
											}
											if (name.contains("C0050000")) {
												krName = "제방";
											}
											if (name.contains("A0090000")) {
												krName = "입체교차부";
											}
											if (name.contains("A0100000")) {
												krName = "인터체인지";
											}
											if (name.contains("A0033320")) {
												krName = "인도";
											}
											if (name.contains("A0053326")) {
												krName = "안전지대";
											}
											if (name.contains("E0032111")) {
												krName = "실폭하천";
											}
											if (name.contains("F0010000")) {
												krName = "등고선";
											}
											if (name.contains("A0020000")) {
												krName = "도로중심선";
											}
											if (name.contains("N3A_A0010000")) {
												krName = "도로경계";
											}
											if (name.contains("N3L_A0010000")) {
												krName = "소로";
											}
											if (name.contains("H0010000")) {
												krName = "도곽";
											}
											if (name.contains("B0020000")) {
												krName = "담장";
											}
											if (krName != null) {
												layerTree.put("id", serverName + delimiter + wsName + delimiter + dsName
														+ delimiter + name);
												layerTree.put("parent", parent);
												layerTree.put("text", krName);
//												layerTree.put("kr", krName);
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
			}
		} else {
			logger.warn("DTGeoserverManagerList or EnTreeType null");
		}
		return this;
	}
}
