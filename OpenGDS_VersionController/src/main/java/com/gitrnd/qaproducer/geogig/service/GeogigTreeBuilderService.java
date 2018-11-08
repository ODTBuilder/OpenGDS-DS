/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree.EnGeogigRemoteRepositoryTreeType;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree.EnGeogigRepositoryTreeType;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

/**
 * @author GIT
 *
 */
public interface GeogigTreeBuilderService {

	/**
	 * @param dtGeoservers  DTGeoserverManagerList
	 * @param serverName    geoserver 이름
	 * @param type          node type
	 * @param node          node ex) server, server:repository,
	 *                      server:repository:branch
	 * @param transactionId geogig transactionId
	 * @return GeogigRepositoryTree
	 */
	public GeogigRepositoryTree getWorkingTree(DTGeoserverManagerList dtGeoservers, String serverName,
			EnGeogigRepositoryTreeType type, String node, String transactionId);

	/**
	 * @param dtGeoservers DTGeoserverManagerList
	 * @param serverName   geoserver 이름
	 * @param type         node type
	 * @param node         node ex) server, server:repository,
	 *                     server:repository:branch
	 * @param fetch
	 * @return GeogigRemoteRepositoryTree
	 */
	public GeogigRemoteRepositoryTree getRemoteRepoTree(DTGeoserverManagerList dtGeoservers, String serverName,
			EnGeogigRemoteRepositoryTreeType type, String node, String local, boolean fetch);
}
