package com.gitrnd.gdsbuilder.geoserver;

import java.net.MalformedURLException;
import java.net.URL;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStructuredGridCoverageReaderManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStyleManager;

/**
 * {@link GeoServerRESTManager} 상속클래스 - Geoserver API 전반적인 관리 클래스
 * 
 * @author SG.LEE
 *
 */
public class DTGeoserverManager extends GeoServerRESTManager {
	/**
	 * DTGeoserverPublisher Geoserver 생성, 수정, 삭제
	 */
	private final DTGeoserverPublisher publisher;
	/**
	 * DTGeoserverReader Geoserver 관련 데이터 Read
	 */
	private final DTGeoserverReader reader;

	/**
	 * GeoServerRESTStoreManager Geoserver 저장소 관리
	 */
	private final GeoServerRESTStoreManager storeManager;
	/**
	 * GeoServerRESTStyleManager Geoserver 스타일 관리
	 */
	private final GeoServerRESTStyleManager styleManager;

	/**
	 * GeoServerRESTStructuredGridCoverageReaderManager Geoserver 그리드
	 */
	private final GeoServerRESTStructuredGridCoverageReaderManager structuredGridCoverageReader;

	/**
	 * Geoserver URL
	 */
	private final String restURL;

	/**
	 * Geoserver ID
	 */
	private final String username;

	/**
	 * Geoserver PW
	 */
	private final String password;

	/**
	 * {@link DTGeoserverManager} 생성자
	 * 
	 * @author SG.LEE
	 * @param restURL  Geoserver URL
	 * @param username Geoserver ID
	 * @param password Geoserver PW
	 * @throws MalformedURLException {@link MalformedURLException}
	 */
	public DTGeoserverManager(String restURL, String username, String password) throws MalformedURLException {
		super(new URL(restURL), username, password);

		// Internal publisher and reader, provide simple access methods.
		publisher = new DTGeoserverPublisher(restURL.toString(), username, password);
		reader = new DTGeoserverReader(restURL, username, password);
		structuredGridCoverageReader = new GeoServerRESTStructuredGridCoverageReaderManager(new URL(restURL), username,
				password);
		storeManager = new GeoServerRESTStoreManager(new URL(restURL), username, password);
		styleManager = new GeoServerRESTStyleManager(new URL(restURL), username, password);
		this.restURL = restURL;
		this.username = username;
		this.password = password;

	}

	/**
	 * Get Geoserver URL
	 * 
	 * @author SG.LEE
	 * @return Geoserver URL
	 */
	public String getRestURL() {
		return restURL;
	}

	/**
	 * Get Geoserver ID
	 * 
	 * @author SG.LEE
	 * @return Geoserver ID
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get Geoserver PW
	 * 
	 * @author SG.LEE
	 * @return Geoserver PW
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTManager#getPublisher()
	 */
	public DTGeoserverPublisher getPublisher() {
		return publisher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTManager#getReader()
	 */
	public DTGeoserverReader getReader() {
		return reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTManager#getStoreManager()
	 */
	public GeoServerRESTStoreManager getStoreManager() {
		return storeManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTManager#getStyleManager()
	 */
	public GeoServerRESTStyleManager getStyleManager() {
		return styleManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTManager#
	 * getStructuredGridCoverageReader()
	 */
	public GeoServerRESTStructuredGridCoverageReaderManager getStructuredGridCoverageReader() {
		return structuredGridCoverageReader;
	}
}