/**
 * 
 */
package com.gitrnd.qaproducer.geogig.controller;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gitrnd.gdsbuilder.geogig.type.GeogigCat;
import com.gitrnd.gdsbuilder.geogig.type.GeogigFeatureAttribute;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.controller.AbstractController;
import com.gitrnd.qaproducer.geogig.service.GeogigObjectService;

/**
 * @author GIT
 *
 */
@Controller
@RequestMapping("/geogig")
public class GeogigObjectController extends AbstractController {

	@Autowired
	@Qualifier("objectService")
	GeogigObjectService objectService;

	@RequestMapping(value = "/catObject.do", method = RequestMethod.POST)
	@ResponseBody
	public GeogigCat catObject(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "repoName", required = false) String repoName,
			@RequestParam(value = "objectid", required = false) String objectid) throws JAXBException {

		DTGeoserverManager geoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return objectService.catObject(geoserverManager, repoName, objectid);
	}

	@RequestMapping(value = "/catFeatureObject.do", method = RequestMethod.POST)
	@ResponseBody
	public GeogigFeatureAttribute catFeatureObject(HttpServletRequest request,
			@AuthenticationPrincipal LoginUser loginUser,
			@RequestParam(value = "serverName", required = false) String serverName,
			@RequestParam(value = "repoName", required = false) String repoName,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "commitId", required = false) String commitId,
			@RequestParam(value = "featureId", required = false) String featureId) throws JAXBException {

		DTGeoserverManager geoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		return objectService.catFeatureObject(geoserverManager, repoName, path, commitId, featureId);
	}
}
