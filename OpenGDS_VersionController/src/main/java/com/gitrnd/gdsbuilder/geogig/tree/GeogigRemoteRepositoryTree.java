/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.tree;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.GeogigExceptionStatus;
import com.gitrnd.gdsbuilder.geogig.command.repository.DiffRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.FetchRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.ListBranch;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.ListRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.PingRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch.Branch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigDiff;
import com.gitrnd.gdsbuilder.geogig.type.GeogigFetch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRemoteRepository.Remote;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
@SuppressWarnings("serial")
public class GeogigRemoteRepositoryTree extends JSONArray {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public enum EnGeogigRemoteRepositoryTreeType {
		REMOTEREPOSITORY("remoteRepository"), REMOTEBRANCH("remoteBranch"), UNKNOWN(null);

		String type;

		private EnGeogigRemoteRepositoryTreeType(String type) {
			this.type = type;
		}

		public static EnGeogigRemoteRepositoryTreeType getFromType(String type) {
			for (EnGeogigRemoteRepositoryTreeType tt : values()) {
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

	public GeogigRemoteRepositoryTree(DTGeoserverManager dtGeoserver, String serverName,
			EnGeogigRemoteRepositoryTreeType type, String node, String local, boolean fetch) {
		this.build(dtGeoserver, serverName, type, node, local, fetch);
	}

	public GeogigRemoteRepositoryTree build(DTGeoserverManager dtGeoserver, String serverName,
			EnGeogigRemoteRepositoryTreeType type, String node, String local, boolean fetch) {

		// node : repos_postgress_master
		// local : geoserver32:fetchtest_postgres

		if (dtGeoserver == null || serverName == null || node == null) {
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("id", 500);
			errorJSON.put("parent", "#");
			errorJSON.put("text", "요청이 잘못되었습니다.");
			errorJSON.put("type", "error");
			this.add(errorJSON);
			logger.warn("요청이 잘못되었습니다.");
		} else {
			if (dtGeoserver != null && type != null && node != null) {
				String baseURL = dtGeoserver.getRestURL();
				String username = dtGeoserver.getUsername();
				String password = dtGeoserver.getPassword();

				String[] param = node.split(":");

				if (param.length > 0) {
					if (type == EnGeogigRemoteRepositoryTreeType.REMOTEREPOSITORY) {
						String repos = param[1];
						ListRemoteRepository listRemoteRepos = new ListRemoteRepository();
						GeogigRemoteRepository remoteRepos = null;
						try {
							remoteRepos = listRemoteRepos.executeCommand(baseURL, username, password, repos, true);
							if (remoteRepos == null) {
								this.addDefaultRepo(repos, GeogigExceptionStatus.REMOTE_NOT_FOUND.getStatus());
							} else {
								List<Remote> remoteList = remoteRepos.getRemotes();
								if (remoteList == null) {
									this.addDefaultRepo(repos, GeogigExceptionStatus.REMOTE_NOT_FOUND.getStatus());
								} else {
									if (remoteList.size() < 0) {
										this.addDefaultRepo(repos, GeogigExceptionStatus.REMOTE_NOT_FOUND.getStatus());
									} else {
										ListBranch listBranch = new ListBranch();
										GeogigBranch branch = listBranch.executeCommand(baseURL, username, password,
												repos, true);
										for (Remote remote : remoteList) {
											String name = remote.getName();
											String url = remote.getUrl();

											// ping
											PingRemoteRepository pingRemote = new PingRemoteRepository();
											GeogigRemoteRepository pingRepos = pingRemote.executeCommand(baseURL,
													username, password, repos, name);
											String pingStr = pingRepos.getPing().getSuccess();

											boolean ping;
											if (pingStr.equalsIgnoreCase("true")) {
												ping = true;
											} else {
												ping = false;
											}
											String remoteId = node + ":" + name; // ex) repository:remoteRepository
											if (branch != null) {
												List<Branch> remoteBraches = branch.getRemoteBranchList();
												int branchSize = 0;
												for (Branch remoteBranch : remoteBraches) {
													if (name.equals(remoteBranch.getRemoteName())) {
														branchSize++;
													}
												}
												if (branchSize > 0) {
													this.addRemoteRepo(remoteId, name, url, true, ping);
												} else {
													this.addRemoteRepo(remoteId, name, url, false, ping);
												}
											} else {
												this.addRemoteRepo(remoteId, name, url, false, ping);
											}
										}
									}
								}
							}
						} catch (GeogigCommandException e) {
							GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
							this.addDefaultRepo(repos, geogigStatus.getStatus());
						}
					} else if (type == EnGeogigRemoteRepositoryTreeType.REMOTEBRANCH && fetch == false) {
						String remoteRepos = param[0];
						String[] localParams = local.split(":");
						String localRepos = localParams[1];
						String parent = local + ":" + node;
						try {
							ListBranch listBranch = new ListBranch();
							GeogigBranch branch = listBranch.executeCommand(baseURL, username, password, localRepos,
									true);
							List<Branch> remoteBraches = branch.getRemoteBranchList();
							for (Branch remoteBranch : remoteBraches) {
								if (remoteRepos.equals(remoteBranch.getRemoteName())) {
									String branchName = remoteBranch.getName();
									if (branchName.equals("HEAD")) {
										continue;
									}
									String branchId = parent + ":" + branchName;
									this.addRemoteBranch(parent, branchId, branchName);
								}
							}
						} catch (GeogigCommandException e) {
							GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
							String status = geogigStatus.getStatus();
							this.addRemoteBranch(parent, status, status);
						}
					} else if (type == EnGeogigRemoteRepositoryTreeType.REMOTEBRANCH && fetch == true) {
						String remoteRepos = param[0];
						String[] localParams = local.split(":");
						String localRepos = localParams[1];
						String parent = local + ":" + node;

						ListBranch listBranch = new ListBranch();
						GeogigBranch branch = null;
						try {
							branch = listBranch.executeCommand(baseURL, username, password, localRepos, true);
							if (branch != null) {
								List<Branch> remoteBraches = branch.getRemoteBranchList();
								FetchRepository fetchRepos = new FetchRepository();
								GeogigFetch geogigFetch = fetchRepos.executeCommand(baseURL, username, password,
										localRepos, remoteRepos);
								List<com.gitrnd.gdsbuilder.geogig.type.GeogigFetch.Remote> remoteFetches = geogigFetch
										.getRemotes();
								if (remoteFetches != null && remoteFetches.size() > 0) {
									for (com.gitrnd.gdsbuilder.geogig.type.GeogigFetch.Remote remoteFetch : remoteFetches) {
										// String remoteURL = remoteFetch.getRemoteURL();
										List<com.gitrnd.gdsbuilder.geogig.type.GeogigFetch.Remote.Branch> fetchBranches = remoteFetch
												.getBranchs();
										for (Branch remoteBranch : remoteBraches) {
											if (remoteRepos.equals(remoteBranch.getRemoteName())) {
												String branchName = remoteBranch.getName();
												if (branchName.equals("HEAD")) {
													continue;
												}
												String branchId = parent + ":" + branchName;
												boolean isEquals = false;
												for (com.gitrnd.gdsbuilder.geogig.type.GeogigFetch.Remote.Branch fetchBranch : fetchBranches) {
													String fetchBranchName = fetchBranch.getName();
													if (fetchBranchName.equals(branchName)) {
														DiffRepository diffRepos = new DiffRepository();
														GeogigDiff geogigDiff = diffRepos.executeCommand(baseURL,
																username, password, localRepos,
																fetchBranch.getOldValue(), fetchBranch.getNewValue(),
																null, null);
														int fetchSize = geogigDiff.getDiffs().size();
														String nextPage = geogigDiff.getNextPage();
														if (nextPage != null) {
															Integer page = 1;
															while (nextPage != null) {
																GeogigDiff nextDiff = diffRepos.executeCommand(baseURL,
																		username, password, localRepos,
																		fetchBranch.getOldValue(),
																		fetchBranch.getNewValue(), null, page);
																fetchSize += nextDiff.getDiffs().size();
																nextPage = nextDiff.getNextPage();
																page++;
															}
														}
														this.addFeatchRemoteBranch(parent, branchId, branchName,
																fetchSize);
														isEquals = true;
													}
												}
												if (isEquals == false) {
													this.addFeatchRemoteBranch(parent, branchId, branchName, 0);
												}
											}
										}
									}
								} else {
									for (Branch remoteBranch : remoteBraches) {
										if (remoteRepos.equals(remoteBranch.getRemoteName())) {
											String branchName = remoteBranch.getName();
											if (branchName.equals("HEAD")) {
												continue;
											}
											String branchId = parent + ":" + branchName;
											this.addFeatchRemoteBranch(parent, branchId, branchName, 0);
										}
									}
								}
							}
						} catch (GeogigCommandException e) {
							GeogigExceptionStatus geogigStatus = GeogigExceptionStatus.getStatus(e.getMessage());
							String status = geogigStatus.getStatus();
							this.addFeatchRemoteBranch(parent, parent + ":" + status, status, null);
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
		}
		return this;

	}

	/**
	 * @param repo
	 */
	private void addDefaultRepo(String repo, String text) {
		JSONObject repoJson = new JSONObject();
		repoJson.put("parent", "#");
		repoJson.put("id", repo);
		repoJson.put("text", text);
		repoJson.put("type", "defalut");
		super.add(repoJson);
	}

	/**
	 * @param id
	 * @param text
	 * @param url
	 * @param children
	 * @param ping
	 */
	private void addRemoteRepo(String id, String text, String url, boolean children, boolean ping) {
		JSONObject remoteRepoJson = new JSONObject();
		remoteRepoJson.put("parent", "#");
		remoteRepoJson.put("id", id);
		remoteRepoJson.put("text", text);
		remoteRepoJson.put("url", url);
		remoteRepoJson.put("type", EnGeogigRemoteRepositoryTreeType.REMOTEREPOSITORY.getType());
		remoteRepoJson.put("children", children);
		remoteRepoJson.put("ping", ping);
		super.add(remoteRepoJson);
	}

	/**
	 * @param parent
	 * @param id
	 * @param text
	 */
	private void addRemoteBranch(String parent, String id, String text) {
		JSONObject remoteBranchJson = new JSONObject();
		remoteBranchJson.put("parent", parent);
		remoteBranchJson.put("id", id);
		remoteBranchJson.put("text", text);
		remoteBranchJson.put("type", EnGeogigRemoteRepositoryTreeType.REMOTEBRANCH.getType());
		super.add(remoteBranchJson);
	}

	/**
	 * @param parent
	 * @param id
	 * @param text
	 * @param fetchSize
	 */
	private void addFeatchRemoteBranch(String parent, String id, String text, Integer fetchSize) {
		JSONObject remoteBranchJson = new JSONObject();
		remoteBranchJson.put("parent", parent);
		remoteBranchJson.put("id", id);
		remoteBranchJson.put("text", text);
		remoteBranchJson.put("type", EnGeogigRemoteRepositoryTreeType.REMOTEBRANCH.getType());
		remoteBranchJson.put("fetchSize", fetchSize);
		super.add(remoteBranchJson);
	}
}
