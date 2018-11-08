/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import javax.xml.bind.JAXBException;

import com.gitrnd.gdsbuilder.geogig.type.GeogigCat;
import com.gitrnd.gdsbuilder.geogig.type.GeogigFeatureAttribute;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
public interface GeogigObjectService {

	GeogigCat catObject(DTGeoserverManager geoserverManager, String repoName, String objectid) throws JAXBException;

	GeogigFeatureAttribute catFeatureObject(DTGeoserverManager geoserverManager, String repoName, String path,
			String commitId, String featureId) throws JAXBException;

}
