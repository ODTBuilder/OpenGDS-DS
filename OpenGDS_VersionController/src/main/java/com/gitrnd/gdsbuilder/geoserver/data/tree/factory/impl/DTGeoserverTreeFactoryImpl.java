package com.gitrnd.gdsbuilder.geoserver.data.tree.factory.impl;

import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTrees;
import com.gitrnd.gdsbuilder.geoserver.data.tree.factory.DTGeoserverTreeFactory;

/**
 * {@link DTGeoserverTree} or {@link DTGeoserverTrees} 생성하는 클래스
 * @author SG.LEE
 */
public class DTGeoserverTreeFactoryImpl implements DTGeoserverTreeFactory {

	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.data.tree.factory.DTGeoserverTreeFactory#createDTGeoserverTree(com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList, com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType)
	 */
	public DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, EnTreeType type) {
		return new DTGeoserverTree(dtGeoManagers, type);
	}

	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.data.tree.factory.DTGeoserverTreeFactory#createDTGeoserverTree(com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList, java.lang.String, java.lang.String, com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType)
	 */
	public DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName,
			EnTreeType type) {
		return new DTGeoserverTree(dtGeoManagers, parent, serverName, type);
	}

	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.data.tree.factory.DTGeoserverTreeFactory#createDTGeoserverTrees(com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList)
	 */
	public DTGeoserverTrees createDTGeoserverTrees(DTGeoserverManagerList dtGeoManagers) {
		return new DTGeoserverTrees(dtGeoManagers);
	}
}
