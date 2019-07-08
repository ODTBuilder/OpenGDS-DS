package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * Geoserver Service 요청타입
 * @author SG.Lee
 * @since 2017. 6. 5. 오후 5:45:47
 * */
public enum EnGeoserverService {
	WFS("WFS", "wfs"), 
	WMS("WMS", "wms"), 
	WCS("WCS", "wcs"),
	WMTS("WMTS", "wmts"),
	WPS("WPS", "wps"); 
	String state;
	String stateName;
	
	/**
	 * {@link EnGeoserverService} 생성자
	 * @author SG.LEE
	 * @param state 상태코드
	 * @param stateName 상태값
	 */
	EnGeoserverService(String state, String stateName) {
		this.state = state;
		this.stateName = stateName;
	}
	
	public String getState() {
		return state;
		
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
