package com.gitrnd.gdsbuilder.geoserver.factory;

import java.net.MalformedURLException;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverPublisher;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverReader;

/**
 * Geoserver Manager, Publisher, Reader 생성 인터페이스
 * 
 * @author SG.LEE
 *
 */
public interface DTGeoserverFactory {
	/**
	 * {@link DTGeoserverManager} 생성
	 * 
	 * @author SG.LEE
	 * @param restURL  Geoserver URL
	 * @param userName Geoserver ID
	 * @param password Geoserver PW
	 * @return {@link DTGeoserverManager} Geoserver API 전반적인 관리 클래스
	 * @throws MalformedURLException {@link MalformedURLException}
	 */
	DTGeoserverManager createDTGeoserverManager(String restURL, String userName, String password)
			throws MalformedURLException;

	/**
	 * {@link DTGeoserverPublisher} 생성
	 * 
	 * @author SG.LEE
	 * @param restURL  Geoserver URL
	 * @param userName Geoserver ID
	 * @param password Geoserver PW
	 * @return {@link DTGeoserverPublisher} Geoserver 관련 데이터 생성, 수정, 삭제
	 * @throws MalformedURLException {@link MalformedURLException}
	 */
	DTGeoserverPublisher createDTGeoserverPublisher(String restURL, String userName, String password)
			throws MalformedURLException;

	/**
	 * {@link DTGeoserverReader} 생성
	 * 
	 * @author SG.LEE
	 * @param restURL  Geoserver URL
	 * @param userName Geoserver ID
	 * @param password Geoserver PW
	 * @return {@link DTGeoserverReader} Geoserver 정보 및 데이터 읽기 지원
	 * @throws MalformedURLException {@link MalformedURLException}
	 */
	DTGeoserverReader createDTGeoserverReader(String restURL, String userName, String password)
			throws MalformedURLException;
}
