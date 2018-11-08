/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.gitrnd.gdsbuilder.geogig.type.GeogigCommandResponse;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
public interface GeogigGeoserverService {

	JSONObject getDataStoreList(DTGeoserverManager geoserverManager, String repoName, String branchName);

	GeogigCommandResponse publishGeogigLayer(DTGeoserverManager geoserverManager, String workspace, String datastore,
			String layer, String reposName, String branchName);

	JSONArray listGeoserverLayer(DTGeoserverManager geoserverManager, String workspace, String datastore);

}
