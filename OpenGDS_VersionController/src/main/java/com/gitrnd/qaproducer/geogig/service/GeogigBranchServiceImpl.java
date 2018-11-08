/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.command.repository.StatusRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.CheckoutBranch;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.CreateBranch;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.ListBranch;
import com.gitrnd.gdsbuilder.geogig.command.repository.branch.MergeBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigBranch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCheckout;
import com.gitrnd.gdsbuilder.geogig.type.GeogigMerge;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus.Header;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus.Staged;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus.Unmerged;
import com.gitrnd.gdsbuilder.geogig.type.GeogigStatus.Unstaged;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
@Service("branchService")
public class GeogigBranchServiceImpl implements GeogigBranchService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.git.opengds.geogig.service.GeogigBranchService#checkoutBranch(org.json.
	 * simple.JSONObject)
	 */
	@Override
	public GeogigCheckout checkoutBranch(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String reference) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		CheckoutBranch checkoutBranch = new CheckoutBranch();
		GeogigCheckout checkout = null;
		try {
			checkout = checkoutBranch.executeCommand(url, user, pw, repoName, transactionId, reference);
			checkout.setTransactionId(transactionId);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigCheckout.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			checkout = (GeogigCheckout) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return checkout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigBranchService#statusBranch(com.
	 * gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject statusBranch(DTGeoserverManager geoserverManager, String serverName, String repoName,
			String transactionId, String branchName) {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		StatusRepository stausCommand = new StatusRepository();
		GeogigStatus status = stausCommand.executeCommand(url, user, pw, repoName, transactionId);

		Header header = status.getHeader();
		String headerBranch = header.getBranch();
		if (branchName.equalsIgnoreCase(headerBranch)) {
			JSONObject statusObj = new JSONObject();
			statusObj.put("server", serverName);
			statusObj.put("repository", repoName);
			statusObj.put("transactionId", transactionId);
			statusObj.put("header", headerBranch);
			List<Staged> stageds = status.getStaged();
			if (stageds != null) {
				JSONArray nodeArry = new JSONArray();
				for (Staged staged : stageds) {
					String path = staged.getPath();
					String node = path.substring(0, path.indexOf("/"));
					if (!nodeArry.contains(node)) {
						nodeArry.add(node);
					}
				}
				statusObj.put("staged", nodeArry);
			}
			List<Unstaged> unStageds = status.getUnstaged();
			if (unStageds != null) {
				JSONArray nodeArry = new JSONArray();
				for (Unstaged unStaged : unStageds) {
					String path = unStaged.getPath();
					String node = path.substring(0, path.indexOf("/"));
					if (!nodeArry.contains(node)) {
						nodeArry.add(node);
					}
				}
				statusObj.put("unstaged", nodeArry);
			}
			List<Unmerged> unMergeds = status.getUnmerged();
			if (unMergeds != null) {
				JSONArray nodeArry = new JSONArray();
				for (Unmerged unMerged : unMergeds) {
					String path = unMerged.getPath();
					String node = path.substring(0, path.indexOf("/"));
					if (!nodeArry.contains(node)) {
						nodeArry.add(node);
					}
				}
				statusObj.put("unmerged", nodeArry);
			}
			return statusObj;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigBranchService#createBranch(com.
	 * gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigBranch createBranch(DTGeoserverManager geoserverManager, String repoName, String branchName,
			String source) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		CreateBranch create = new CreateBranch();
		GeogigBranch branch = null;
		try {
			branch = create.executeCommand(url, user, pw, repoName, branchName, source);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigBranch.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			branch = (GeogigBranch) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return branch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.GeogigBranchService#listBranch(com.
	 * gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String)
	 */
	@Override
	public GeogigBranch listBranch(DTGeoserverManager geoserverManager, String repoName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		ListBranch list = new ListBranch();
		GeogigBranch branch = null;
		try {
			branch = list.executeCommand(url, user, pw, repoName, true);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigBranch.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			branch = (GeogigBranch) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return branch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigBranchService#mergeBranch(com.
	 * gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigMerge mergeBranch(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String branchName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		MergeBranch merge = new MergeBranch();
		GeogigMerge branch = null;
		try {
			branch = merge.executeCommand(url, user, pw, repoName, transactionId, branchName);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigMerge.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			branch = (GeogigMerge) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return branch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigBranchService#resolveConflict(com.
	 * gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<GeogigCheckout> resolveConflict(DTGeoserverManager geoserverManager, String repoName,
			String transactionId, JSONArray featureArr) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		CheckoutBranch checkout = new CheckoutBranch();
		List<GeogigCheckout> checkoutList = new ArrayList<>();

		int arrySize = featureArr.size();
		for (int i = 0; i < arrySize; i++) {
			JSONObject resolvePath = (JSONObject) featureArr.get(i);
			String path = (String) resolvePath.get("path");
			String version = (String) resolvePath.get("version");
			GeogigCheckout branch = null;
			try {
				branch = checkout.executeCommand(url, user, pw, repoName, transactionId, path, version);
			} catch (GeogigCommandException e) {
				JAXBContext jaxbContext = JAXBContext.newInstance(GeogigCheckout.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				branch = (GeogigCheckout) unmarshaller.unmarshal(new StringReader(e.getMessage()));
			}
			checkoutList.add(branch);
		}
		return checkoutList;
	}

}
