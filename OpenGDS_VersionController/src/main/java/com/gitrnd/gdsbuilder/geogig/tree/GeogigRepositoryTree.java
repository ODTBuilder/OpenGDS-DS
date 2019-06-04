/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.tree;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.GeogigExceptionStatus;
import com.gitrnd.gdsbuilder.geogig.command.repository.ConfigRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.ListRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.LsTreeRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.StatusRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.ListBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch.Branch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigConfig;
import com.gitrnd.gdsbuilder.geogig.type.GeogigConfig.Config;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepository.Repo;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree.Node;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus.Header;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStoreList;
import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList;

/**
 * Geoserver내에 존재하는 모든 Geogig Repository, Branch, Layer를 {@link JSONArray} 형태의
 * Tree로 생성하는 클래스.
 * 
 * @author DY.Oh
 *
 */
@SuppressWarnings("serial")
public class GeogigRepositoryTree extends JSONArray {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 조회 성공 여부
	 */
	private String success;
	/**
	 * 실패 시 오류 메세지
	 */
	private String error;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Repository 트리 생성 요청 타입 enum
	 * 
	 * @author DY.Oh
	 *
	 */
	public enum EnGeogigRepositoryTreeType {
		/**
		 * Geoserver 목록 조회
		 */
		SERVER("server"),
		/**
		 * Geogig Repository 목록 조회
		 */
		REPOSITORY("repository"),
		/**
		 * Branch 목록 조회
		 */
		BRANCH("branch"),
		/**
		 * Layer 목록 조회
		 */
		LAYER("layer"),
		/**
		 * Unknown
		 */
		UNKNOWN(null);

		String type;

		private EnGeogigRepositoryTreeType(String type) {
			this.type = type;
		}

		public static EnGeogigRepositoryTreeType getFromType(String type) {
			for (EnGeogigRepositoryTreeType tt : values()) {
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
	 * EnGeogigRepositoryTreeType이 SERVER 일 경우 {@link GeogigRepositoryTree} 생성자
	 * 
	 * @param dtGeoManagers Geoserver REST Manager 및 Geoserver 접속 정보
	 * @param type          조회 타입
	 */
	public GeogigRepositoryTree(DTGeoserverManagerList dtGeoManagers, EnGeogigRepositoryTreeType type) {
		if (type == EnGeogigRepositoryTreeType.SERVER) {
			build(dtGeoManagers, type);
		} else {
			logger.error("TreeType이 Server가 아닙니다.");
		}
	}

	/**
	 * EnGeogigRepositoryTreeType이 REPOSITORY, BRANCH, LAYER 일 경우
	 * {@link GeogigRepositoryTree} 생성자
	 * 
	 * @param dtGeoserver   Geoserver REST Manager 및 Geoserver 접속 정보
	 * @param serverName    Geoserver 이름
	 * @param type          조회 타입
	 * @param parent        상위 노드
	 * @param transactionId Geogig Repository Transaction ID
	 */
	public GeogigRepositoryTree(DTGeoserverManager dtGeoserver, String serverName, EnGeogigRepositoryTreeType type,
			String parent, String transactionId) {
		this.build(dtGeoserver, serverName, type, parent, transactionId);
	}

	/**
	 * Geoserver 접속 정보 목록에 해당하는 각각의 Geoserver명을 JSONArray 형태의 Tree로 생성 후 반환함.
	 * 
	 * @param dtGeoserverMList Geoserver REST Manager 및 Geoserver 접속 정보 목록
	 * @param type             조회 타입
	 * @return Geoserver 접속 정보 목록에 해당하는 각각의 Geoserver명을 JSONArray 형태의 Tree
	 * 
	 * @author DY.Oh
	 */
	@SuppressWarnings("unchecked")
	public GeogigRepositoryTree build(DTGeoserverManagerList dtGeoserverMList, EnGeogigRepositoryTreeType type) {
		if (dtGeoserverMList != null && type != null) {
			Iterator<String> keys = dtGeoserverMList.keySet().iterator();
			if (type == EnGeogigRepositoryTreeType.SERVER) {
				while (keys.hasNext()) {
					String server = (String) keys.next();
					DTGeoserverManager dtGeoManager = dtGeoserverMList.get(server);

					if (dtGeoManager != null) {
						DTGeoserverReader dtGeoserverReader = dtGeoManager.getReader();
						if (dtGeoserverReader != null) {
							ListRepository listRepos = new ListRepository();
							try {
								GeogigRepository geogigRepo = listRepos.executeCommand(dtGeoManager.getRestURL(),
										dtGeoManager.getUsername(), dtGeoManager.getPassword());
								List<Repo> repos = geogigRepo.getRepos();
								if (repos == null) {
									this.addServer(server, false);
								} else {
									if (repos.size() > 0) {
										this.addServer(server, true);
									} else {
										this.addServer(server, false);
									}
								}
							} catch (GeogigCommandException e) {
								this.addServer(server, false);
							}
						}
					}
				}
				if (dtGeoserverMList.size() == 0) {
					JSONObject errorJSON = new JSONObject();
					errorJSON.put("id", 500);
					errorJSON.put("parent", "#");
					errorJSON.put("text", "Geoserver를 추가해주세요");
					errorJSON.put("type", "error");

					this.add(errorJSON);
					logger.warn("Geoserver를 다시 추가해주세요");
				}
			}
		} else {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("id", 500);
			errorJSON.put("parent", "#");
			errorJSON.put("text", "Geoserver를 다시 추가해주세요");
			errorJSON.put("type", "error");

			this.add(errorJSON);
			logger.warn("Geoserver를 다시 추가해주세요");
		}
		return this;
	}

	/**
	 * 특정 Geoserver 접속 정보에 해당하는 하위의 Repository, Branch, Layer를 조회 형태에 따라 JSONArray
	 * 형태의 Tree로 생성 후 반환함.
	 * 
	 * @param dtGeoserver   Geoserver REST Manager 및 Geoserver 접속 정보
	 * @param serverName    Geoserver 이름
	 * @param type          조회 형태
	 * @param parent        상위 노드
	 * @param transactionId Geogig Repository Transaction ID
	 * @return 조회 형태에 따라 JSONArray 형태의 Tree
	 * 
	 * @author DY.Oh
	 */
	public GeogigRepositoryTree build(DTGeoserverManager dtGeoserver, String serverName,
			EnGeogigRepositoryTreeType type, String parent, String transactionId) {
		String[] param = parent.split(":");// ex)
											// serverName_repository_brench_layer

		if (param != null && dtGeoserver != null) {
			String baseURL = dtGeoserver.getRestURL();
			String username = dtGeoserver.getUsername();
			String password = dtGeoserver.getPassword();

			if (type == EnGeogigRepositoryTreeType.REPOSITORY) {
				if (param.length > 0) {
					String server = param[0];
					// repository
					try {
						ListRepository listRepos = new ListRepository();
						GeogigRepository geogigRepo = listRepos.executeCommand(baseURL, username, password);
						if (geogigRepo != null) {
							List<Repo> repos = geogigRepo.getRepos();
							for (Repo repo : repos) {
								String name = repo.getName();
								String reposId = server + ":" + name;
								String storageType = null;
								// repos type
								ConfigRepository configRepos = new ConfigRepository();
								GeogigConfig geogigConfig = configRepos.executeCommand(baseURL, username, password,
										name, null);
								List<Config> configs = geogigConfig.getConfigs();
								for (Config config : configs) {
									if (config.getName().equals("storage.refs")) {
										storageType = config.getValue();
									}
								}
								ListBranch listBranch = new ListBranch();
								GeogigBranch branches = listBranch.executeCommand(baseURL, username, password, name,
										false);
								List<Branch> localList = branches.getLocalBranchList();
								if (localList != null) {
									if (localList.size() > 0) {
										this.addRepository(parent, reposId, name, storageType, true);
									} else {
										this.addRepository(parent, reposId, name, storageType, false);
									}
								} else {
									this.addRepository(parent, reposId, name, storageType, false);
								}
							}
						}
					} catch (GeogigCommandException e) {
						GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
						String status = geogigStatus.getStatus();
						this.addRepository(parent, status, status, null, false);
					}
				}
			} else if (type == EnGeogigRepositoryTreeType.BRANCH) {
				if (param.length > 1) {
					String repository = param[1];
					try {
						ListBranch listBranch = new ListBranch();
						GeogigBranch branches = listBranch.executeCommand(baseURL, username, password, repository,
								false);
						List<Branch> localList = branches.getLocalBranchList();
						for (Branch localBranch : localList) {
							String branchName = localBranch.getName();
							String branchId = parent + ":" + branchName;
							boolean children = false;
							boolean geoserver = false;
							// geoserver 저장소 존재 여부 확인
							DTGeoserverReader dtGeoserverReader = dtGeoserver.getReader();
							RESTWorkspaceList restWorkspaceList = dtGeoserverReader.getWorkspaces();
							if (restWorkspaceList != null) {
								for (RESTWorkspaceList.RESTShortWorkspace item : restWorkspaceList) {
									String wsName = item.getName();
									RESTDataStoreList dataStoreList = dtGeoserverReader.getDatastores(wsName);
									if (dataStoreList != null) {
										List<String> dsNames = dataStoreList.getNames();
										for (String dsName : dsNames) {
											RESTDataStore dStore = dtGeoserverReader.getDatastore(wsName, dsName);
											if (dStore != null) {
												String storeType = dStore.getStoreType();
												if (storeType != null && storeType.equals("GeoGIG")) {
													Map<String, String> connetParams = dStore.getConnectionParameters();
													String geogigRepos = connetParams.get("geogig_repository");
													String reposName = geogigRepos.replace("geoserver://", "");
													String reposBranch = connetParams.get("branch");
													if (repository.equals(reposName)
															&& branchName.equalsIgnoreCase(reposBranch)) {
														geoserver = true;
													}
												}
											}
										}
									}
								}
							}
							LsTreeRepository lsTree = new LsTreeRepository();
							GeogigRevisionTree revisionTree = lsTree.executeCommand(baseURL, username, password,
									repository, branchName, false);
							List<Node> nodes = revisionTree.getNodes();
							if (nodes != null) {
								if (nodes.size() > 0) {
									children = true;
								}
							}
							if (transactionId != null) {
								StatusRepository stausCommand = new StatusRepository();
								GeogigStatus status = stausCommand.executeCommand(baseURL, username, password,
										repository, transactionId);
								Header header = status.getHeader();
								String headerBranch = header.getBranch();
								if (branchName.equalsIgnoreCase(headerBranch)) {
									if (status.getUnstaged() != null) {
										this.addBranch(parent, branchId, branchName, "Unstaged", children, geoserver);
									} else if (status.getStaged() != null) {
										this.addBranch(parent, branchId, branchName, "Staged", children, geoserver);
									} else if (status.getUnmerged() != null) {
										this.addBranch(parent, branchId, branchName, "UnMerged", children, geoserver);
									} else {
										this.addBranch(parent, branchId, branchName, "Merged", children, geoserver);
									}
								} else {
									this.addBranch(parent, branchId, branchName, null, children, geoserver);
								}
							} else {
								this.addBranch(parent, branchId, branchName, null, children, geoserver);
							}
						}
					} catch (GeogigCommandException e) {
						GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
						String status = geogigStatus.getStatus();
						this.addBranch(parent, status, status, null, null, null);
					}
				}
			} else if (type == EnGeogigRepositoryTreeType.LAYER) {
				if (param.length > 2) {
					String repository = param[1];
					String branch = param[2];
					try {
						// branch ls-tree : default master
						LsTreeRepository lsTree = new LsTreeRepository();
						GeogigRevisionTree revisionTree = lsTree.executeCommand(baseURL, username, password, repository,
								branch, false);
						List<Node> nodes = revisionTree.getNodes();
						for (Node node : nodes) {
							String path = node.getPath();
							String pathId = parent + ":" + path;
							this.addLayer(parent, pathId, path);
						}
					} catch (GeogigCommandException e) {
						GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
						String status = geogigStatus.getStatus();
						this.addLayer(parent, status, status);
					}
				}
			} else {
				JSONObject errorJSON = new JSONObject();
				errorJSON.put("id", 500);
				errorJSON.put("parent", "#");
				errorJSON.put("text", "요청이 잘못되었습니다.");
				errorJSON.put("type", "error");

				this.add(errorJSON);
				logger.warn("요청이 잘못되었습니다.");
			}
		} else {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("id", 500);
			errorJSON.put("parent", "#");
			errorJSON.put("text", "요청이 잘못되었습니다.");
			errorJSON.put("type", "error");

			this.add(errorJSON);
			logger.warn("요청이 잘못되었습니다.");
		}
		return this;
	}

	private void addServer(String serverName, boolean children) {
		JSONObject geoserver = new JSONObject(); // baseURL
		geoserver.put("id", serverName);
		geoserver.put("parent", "#");
		geoserver.put("text", serverName);
		geoserver.put("type", "geoserver");
		geoserver.put("children", children);
		super.add(geoserver);
	}

	private void addRepository(String parent, String id, String text, String type, boolean children) {
		JSONObject repoJson = new JSONObject();
		repoJson.put("parent", parent);
		repoJson.put("id", id);
		repoJson.put("text", text);
		repoJson.put("repoType", type);
		repoJson.put("type", "repository");
		repoJson.put("children", children);
		super.add(repoJson);
	}

	private void addBranch(String parent, String id, String text, String status, Boolean children, Boolean geoserver) {
		JSONObject branchJson = new JSONObject();
		branchJson.put("parent", parent);
		branchJson.put("id", id);
		branchJson.put("text", text);
		branchJson.put("status", status);
		branchJson.put("type", "branch");
		branchJson.put("children", children);
		branchJson.put("geoserver", geoserver);
		super.add(branchJson);
	}

	private void addLayer(String parent, String id, String text) {
		JSONObject repoJson = new JSONObject();
		repoJson.put("parent", parent);
		repoJson.put("id", id);
		repoJson.put("text", text);
		repoJson.put("type", "layer");
		repoJson.put("children", false);
		super.add(repoJson);
	}

}
