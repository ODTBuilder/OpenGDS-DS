package com.gitrnd.gdsbuilder.geoserver.data.tree.factory.impl;

import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTree.EnTreeType;
import com.gitrnd.gdsbuilder.geoserver.data.tree.DTGeoserverTrees;
import com.gitrnd.gdsbuilder.geoserver.data.tree.factory.DTGeoserverTreeFactory;

public class DTGeoserverTreeFactoryImpl implements DTGeoserverTreeFactory {

	public DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, EnTreeType type) {
		return new DTGeoserverTree(dtGeoManagers, type);
	}

	public DTGeoserverTree createDTGeoserverTree(DTGeoserverManagerList dtGeoManagers, String parent, String serverName,
			EnTreeType type) {
		return new DTGeoserverTree(dtGeoManagers, parent, serverName, type);
	}

	public DTGeoserverTrees createDTGeoserverTrees(DTGeoserverManagerList dtGeoManagers) {
		return new DTGeoserverTrees(dtGeoManagers);
	}
}
