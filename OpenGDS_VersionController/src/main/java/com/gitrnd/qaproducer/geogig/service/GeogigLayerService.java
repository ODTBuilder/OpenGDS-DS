/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import javax.xml.bind.JAXBException;

import com.gitrnd.gdsbuilder.geogig.type.GeogigDiff;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryLog;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
public interface GeogigLayerService {

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param layerName
	 * @param limint
	 * @param until
	 * @param head
	 * @return
	 * @throws JAXBException
	 */
//	GeogigRepositoryLog logLayer(DTGeoserverManager geoserverManager, String repoName, String layerName, String limit,
//			String until, String head) throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param oldIndex
	 * @param newIndex
	 * @param layerName
	 * @return GeogigDiff
	 * @throws JAXBException
	 */
	GeogigDiff diffLayer(DTGeoserverManager geoserverManager, String repoName, int oldIndex, int newIndex,
			String layerName) throws JAXBException;

}
