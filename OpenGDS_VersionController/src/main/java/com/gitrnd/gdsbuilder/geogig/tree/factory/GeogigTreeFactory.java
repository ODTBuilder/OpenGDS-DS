/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.tree.factory;

import org.apache.http.conn.HttpHostConnectException;
import org.json.simple.JSONArray;

import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRemoteRepositoryTree.EnGeogigRemoteRepositoryTreeType;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree;
import com.gitrnd.gdsbuilder.geogig.tree.GeogigRepositoryTree.EnGeogigRepositoryTreeType;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;

/**
 * {@link GeogigRepositoryTree} 생성 요청 클래스.
 * 
 * 
 * @author DY.Oh
 *
 */
public interface GeogigTreeFactory {

	/**
	 * type이 "server" 타입일 경우에 {@link GeogigRepositoryTree} 생성 요청.
	 * 
	 * @param dtGeoManagers Geoserver REST Manager 및 Geoserver 접속 정보 목록
	 * @param type          조회 타입
	 * @return Geoserver 접속 정보 목록에 해당하는 각각의 Geoserver명을 JSONArray 형태의 Tree
	 * @throws HttpHostConnectException HttpHostConnectException
	 */
	public GeogigRepositoryTree createGeogigRepositoryTree(DTGeoserverManagerList dtGeoManagers,
			EnGeogigRepositoryTreeType type) throws HttpHostConnectException;

	/**
	 * type이 "server" 타입 이외일 경우에 {@link GeogigRepositoryTree} 생성 요청.
	 * 
	 * @param dtGeoserver   Geoserver REST Manager 및 Geoserver 접속 정보
	 * @param serverName    Geoserver 이름
	 * @param type          조회 타입
	 * @param parent        상위 노드
	 * @param transactionId Geogig Repository Transaction ID
	 * @return 조회 형태에 따라 JSONArray 형태의 Tree
	 */
	public GeogigRepositoryTree createGeogigRepositoryTree(DTGeoserverManager dtGeoserver, String serverName,
			EnGeogigRepositoryTreeType type, String parent, String transactionId);

	/**
	 * {@link GeogigRemoteRepositoryTree} 생성 요청.
	 * 
	 * @param dtGeoserver Geoserver REST Manager 및 Geoserver 접속 정보
	 * @param serverName  Geoserver 이름
	 * @param type        조회 타입
	 * @param parent      상위 노드
	 * @param local       Geogig Repository ID
	 * @param fetch       버전 갱신 여부
	 * @return Remote Repository 및 Remote Branch를 {@link JSONArray} 형태의 Tree
	 */
	public GeogigRemoteRepositoryTree createGeogigRemoteRepositoryTree(DTGeoserverManager dtGeoserver,
			String serverName, EnGeogigRemoteRepositoryTreeType type, String parent, String local, boolean fetch);

}
