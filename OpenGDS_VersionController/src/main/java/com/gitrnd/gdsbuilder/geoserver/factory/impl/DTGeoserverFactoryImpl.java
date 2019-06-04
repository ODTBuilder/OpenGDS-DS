package com.gitrnd.gdsbuilder.geoserver.factory.impl;

import java.net.MalformedURLException;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverPublisher;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;
import com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory;

/**
 * Geoserver Manager, Publisher, Reader 생성 클래스
 * @author SG.LEE
 */
public class DTGeoserverFactoryImpl implements DTGeoserverFactory {

	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory#createDTGeoserverManager(java.lang.String, java.lang.String, java.lang.String)
	 */
	public DTGeoserverManager createDTGeoserverManager(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverManager(restURL, userName, password);
	};
	
	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory#createDTGeoserverPublisher(java.lang.String, java.lang.String, java.lang.String)
	 */
	public DTGeoserverPublisher createDTGeoserverPublisher(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverPublisher(restURL, userName, password);
	};
	
	/* (non-Javadoc)
	 * @see com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory#createDTGeoserverReader(java.lang.String, java.lang.String, java.lang.String)
	 */
	public DTGeoserverReader createDTGeoserverReader(String restURL, String userName, String password) throws MalformedURLException{
		return new DTGeoserverReader(restURL, userName, password);
	};
	
}
