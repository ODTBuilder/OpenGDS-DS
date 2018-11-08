package com.gitrnd.gdsbuilder.geoserver.factory;

import java.net.MalformedURLException;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverPublisher;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;

public interface DTGeoserverFactory {
	DTGeoserverManager createDTGeoserverManager(String restURL, String userName, String password) throws MalformedURLException;
	
	DTGeoserverPublisher createDTGeoserverPublisher(String restURL, String userName, String password) throws MalformedURLException;
	
	DTGeoserverReader createDTGeoserverReader(String restURL, String userName, String password) throws MalformedURLException;
}
