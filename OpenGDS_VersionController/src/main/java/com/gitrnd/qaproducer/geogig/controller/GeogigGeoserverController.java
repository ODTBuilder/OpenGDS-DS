/**
 * 
 */
package com.gitrnd.qaproducer.geogig.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.controller.AbstractController;
import com.gitrnd.qaproducer.geogig.service.GeogigGeoserverService;

/**
 * @author GIT
 *
 */
@Controller
@RequestMapping("/geogig")
public class GeogigGeoserverController extends AbstractController {

	@Autowired
	@Qualifier("test")
	GeogigGeoserverService geoserverService;

	@RequestMapping(value = "/getDataStoreList.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getDataStoreList(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "repoName", required = false) String repoName,
			@RequestParam(value = "branchName", required = false) String branchName) {

		DTGeoserverManager geoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return geoserverService.getDataStoreList(geoserverManager, repoName, branchName);
		// {"ws1":["ds1"],"ws2":["ds1","ds2","ds3"]}
	}

	@RequestMapping(value = "/listGeoserverLayer.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray publishGeogigLayer(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "workspace", required = false) String workspace,
			@RequestParam(value = "datastore", required = false) String datastore) {

		DTGeoserverManager geoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return geoserverService.listGeoserverLayer(geoserverManager, workspace, datastore);
	}

	@RequestMapping(value = "/publishGeogigLayer.do", method = RequestMethod.POST)
	@ResponseBody
	public void publishGeogigLayer(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "workspace", required = false) String workspace,
			@RequestParam(value = "datastore", required = false) String datastore,
			@RequestParam(value = "layer", required = false) String layer,
			@RequestParam(value = "repoName", required = false) String repoName,
			@RequestParam(value = "branchName", required = false) String branchName) {

		DTGeoserverManager geoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		geoserverService.publishGeogigLayer(geoserverManager, workspace, datastore, layer, repoName, branchName);
	}
}
