package com.gitrnd.gdsbuilder.geoserver.data.tree.factory;

import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTrees;

/**
 * {@link DTGeoserverTree} or {@link DTGeoserverTrees} 생성하는 인터페이스 
 * @author SG.LEE
 */
public interface DTGeoserverTreeFactory {
	
	/**
	 * {@link DTGeoserverTree} 생성
	 * type이 EnTreeType.SERVER 일경우에
	 * @author SG.LEE
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @param type jsTree 출력 타입
	 * @return {@link DTGeoserverTree} jsTree(https://www.jstree.com/)
	 */
	DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, EnTreeType type);
	/**
	 * {@link DTGeoserverTree} 생성
	 * type이 EnTreeType.SERVER가 아닐때
	 * @author SG.LEE
	 * @param dtGeoManagers {@link DTGeoserverManagerList} 서버정보 리스트
	 * @param parent jstree parent ID
	 * @param serverName 서버이름
	 * @param type jsTree 출력 타입
	 * @return DTGeoserverTree {@link DTGeoserverTree} jsTree(https://www.jstree.com/)
	 */
	DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName,
			EnTreeType type);
	/**
	 * {@link DTGeoserverTrees} 생성
	 * @author SG.LEE
	 * @param dtGeoserverList {@link DTGeoserverManagerList} 서버정보 리스트
	 * @return {@link DTGeoserverTrees} jsTree(https://www.jstree.com/)
	 */
	DTGeoserverTrees createDTGeoserverTrees(DTGeoserverManagerList dtGeoserverList);
}
