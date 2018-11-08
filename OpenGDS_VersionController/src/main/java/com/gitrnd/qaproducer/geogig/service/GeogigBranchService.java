/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCheckout;
import com.gitrnd.gdsbuilder.geogig.type.GeogigMerge;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
public interface GeogigBranchService {

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param transactionId
	 * @param reference
	 * @return GeogigCheckout
	 * @throws JAXBException
	 */
	public GeogigCheckout checkoutBranch(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String reference) throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param serverName
	 * @param repoName
	 * @param branchName
	 * @return JSONObject
	 */
	public JSONObject statusBranch(DTGeoserverManager geoserverManager, String serverName, String repoName,
			String transactionId, String branchName);

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param branchName
	 * @param source
	 * @return GeogigBranch
	 * @throws JAXBException
	 */
	public GeogigBranch createBranch(DTGeoserverManager geoserverManager, String repoName, String branchName,
			String source) throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @return GeogigBranch
	 * @throws JAXBException
	 */
	public GeogigBranch listBranch(DTGeoserverManager geoserverManager, String repoName) throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param transactionId
	 * @param branchName
	 * @return GeogigMerge
	 * @throws JAXBException
	 */
	public GeogigMerge mergeBranch(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String branchName) throws JAXBException;

	/**
	 * @param geoserverManager
	 * @param repoName
	 * @param transactionId
	 * @param featuresArr
	 * @param version
	 * @return List<GeogigCheckout>
	 * @throws JAXBException
	 */
	public List<GeogigCheckout> resolveConflict(DTGeoserverManager geoserverManager, String repoName,
			String transactionId, JSONArray featuresArr) throws JAXBException;

}
