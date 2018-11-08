package com.gitrnd.qaproducer.geogig.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.command.geoserver.PublishGeoserverLayer;
import com.gitrnd.gdsbuilder.geogig.command.object.CatObject;
import com.gitrnd.gdsbuilder.geogig.command.repository.LsTreeRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCat;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCommandResponse;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCat.Attribute;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCat.FeatureType;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree.Node;

public class TEST {

	public static void main(String[] args) throws JAXBException {

		String url = "http://175.116.181.32:9999/geoserver";
		String user = "admin";
		String pw = "geoserver";

		String repoName = "oda";
		String branchName = "master";
		String layer = "gis_osm_places";

		String workspace = "oda";
		String datastore = "oda";

		// get crs
		String crs = "";
		LsTreeRepository lsTreeRepos = new LsTreeRepository();
		GeogigRevisionTree geogigLsTree = lsTreeRepos.executeCommand(url, user, pw, repoName, branchName, true);
		List<Node> nodeList = geogigLsTree.getNodes();
		for (Node node : nodeList) {
			if (layer.equalsIgnoreCase(node.getPath())) {
				String metadataId = node.getMetadataId();
				CatObject catObj = new CatObject();
				GeogigCat geogigCat = catObj.executeCommand(url, user, pw, repoName, metadataId);
				FeatureType featureType = geogigCat.getFeaturetype();
				List<Attribute> attrList = featureType.getAttribute();
				for (Attribute attr : attrList) {
					String geogigCrs = attr.getCrs();
					if (geogigCrs != null) {
						crs = geogigCrs;
						break;
					}
				}
			}
		}
		PublishGeoserverLayer publish = new PublishGeoserverLayer();
		GeogigCommandResponse response = new GeogigCommandResponse();
		try {
			boolean isSuccess = publish.executeCommand(url, user, pw, workspace, datastore, layer, crs, true);
			if (isSuccess) {
				response.setSuccess("true");
			}
		} catch (GeogigCommandException e) {
			response.setSuccess("false");
			response.setError(e.getMessage());
		}
	}
}
