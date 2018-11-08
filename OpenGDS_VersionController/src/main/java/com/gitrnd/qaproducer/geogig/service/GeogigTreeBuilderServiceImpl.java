/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree.EnGeogigRemoteRepositoryTreeType;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree.EnGeogigRepositoryTreeType;
import com.gitrnd.gdsbuilder.geogig.tree.factory.impl.GeogigTreeFactoryImpl;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

/**
 * @author GIT
 *
 */
@Service("treeService")
public class GeogigTreeBuilderServiceImpl implements GeogigTreeBuilderService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public GeogigRepositoryTree getWorkingTree(DTGeoserverManagerList dtGeoservers, String serverName,
			EnGeogigRepositoryTreeType type, String parent, String transactionId) {

		if (type == EnGeogigRepositoryTreeType.SERVER) {
			return new GeogigTreeFactoryImpl().createGeogigRepositoryTree(dtGeoservers, type);
		} else {
			if (dtGeoservers != null) {
				DTGeoserverManager dtGeoManager = null;
				Iterator<String> keys = dtGeoservers.keySet().iterator();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					if (key.equals(serverName)) {
						dtGeoManager = dtGeoservers.get(key);
						break;
					}
				}
				return new GeogigTreeFactoryImpl().createGeogigRepositoryTree(dtGeoManager, serverName, type, parent,
						transactionId);
			} else {
				JSONArray result = new JSONArray();
				JSONObject errorJSON = new JSONObject();
				errorJSON.put("id", 500);
				errorJSON.put("parent", "#");
				errorJSON.put("text", "잘못된 요청입니다");
				errorJSON.put("type", "error");

				result.add(errorJSON);
				logger.warn("잘못된 요청입니다.");
				return (GeogigRepositoryTree) result;
			}
		}
	}

	@Override
	public GeogigRemoteRepositoryTree getRemoteRepoTree(DTGeoserverManagerList dtGeoservers, String serverName,
			EnGeogigRemoteRepositoryTreeType type, String parent, String local, boolean fetch) {

		if (dtGeoservers != null) {
			DTGeoserverManager dtGeoManager = null;
			Iterator<String> keys = dtGeoservers.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				if (key.equals(serverName)) {
					dtGeoManager = dtGeoservers.get(key);
					break;
				}
			}
			return new GeogigTreeFactoryImpl().createGeogigRemoteRepositoryTree(dtGeoManager, serverName, type, parent,
					local, fetch);
		} else {
			JSONArray result = new JSONArray();
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("id", 500);
			errorJSON.put("parent", "#");
			errorJSON.put("text", "잘못된 요청입니다");
			errorJSON.put("type", "error");

			result.add(errorJSON);
			logger.warn("잘못된 요청입니다.");
			return (GeogigRemoteRepositoryTree) result;
		}
	}
}
