package com.gitrnd.gdsbuilder.geoserver.factory.impl;

import java.net.MalformedURLException;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverPublisher;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;
import com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory;

public class DTGeoserverFactoryImpl implements DTGeoserverFactory {

	public DTGeoserverManager createDTGeoserverManager(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverManager(restURL, userName, password);
	};
	
	public DTGeoserverPublisher createDTGeoserverPublisher(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverPublisher(restURL, userName, password);
	};
	
	public DTGeoserverReader createDTGeoserverReader(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverReader(restURL, userName, password);
	};
	
}
