/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import javax.xml.bind.JAXBException;

import com.gitrnd.gdsbuilder.geogig.type.GeogigTransaction;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
public interface GeogigTransactionService {

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @return
	 * @throws JAXBException
	 */
	public GeogigTransaction beginTransaction(DTGeoserverManager geoserverManager, String repoName)
			throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param transactionId
	 * @return
	 * @throws JAXBException
	 */
	public GeogigTransaction endTransaction(DTGeoserverManager geoserverManager, String repoName, String transactionId)
			throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param transactionId
	 * @return
	 * @throws JAXBException
	 */
	public GeogigTransaction cancelTransaction(DTGeoserverManager geoserverManager, String repoName,
			String transactionId) throws JAXBException;

}
