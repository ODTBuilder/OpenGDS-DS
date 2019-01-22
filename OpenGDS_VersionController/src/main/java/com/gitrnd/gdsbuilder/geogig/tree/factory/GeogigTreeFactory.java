/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.tree.factory;

import org.apache.http.conn.HttpHostConnectException;

import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree.EnGeogigRemoteRepositoryTreeType;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree.EnGeogigRepositoryTreeType;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

/**
 * @author GIT
 *
 */
public interface GeogigTreeFactory {

	/**
	 * type이 Server 타입일 경우에
	 * 
	 * @param dtGeoManagers
	 * @param type
	 * @return
	 * @throws HttpHostConnectException
	 */
	public GeogigRepositoryTree createGeogigRepositoryTree(DTGeoserverManagerList dtGeoManagers,
			EnGeogigRepositoryTreeType type) throws HttpHostConnectException;

	/**
	 * Server type외
	 * 
	 * @param dtGeoserver
	 * @param serverName
	 * @param type
	 * @param parent
	 * @param transactionId
	 * @return
	 */
	public GeogigRepositoryTree createGeogigRepositoryTree(DTGeoserverManager dtGeoserver, String serverName,
			EnGeogigRepositoryTreeType type, String parent, String transactionId);

	/**
	 * @param dtGeoserver
	 * @param serverName
	 * @param type
	 * @param parent
	 * @param local
	 * @param fetch
	 * @return
	 */
	public GeogigRemoteRepositoryTree createGeogigRemoteRepositoryTree(DTGeoserverManager dtGeoserver,
			String serverName, EnGeogigRemoteRepositoryTreeType type, String parent, String local, boolean fetch);

}
