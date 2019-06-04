package com.gitrnd.gdsbuilder.geoserver.service.en;

/**
 * Geoserver BBOX 범위 계산 타입
 * @author SG.LEE
 */
public enum EnLayerBboxRecalculate {
	ALL("ALL", "all", "recalculate=nativebbox,latlonbbox"), 
	NATIVEBBOX("NATIVEBBOX", "nativebbox","recalculate=nativebbox"), 
	LATLONBBOX("LATLONBBOX", "latlonbbox", "recalculate=latlonbbox");
	String state;
	String stateName;
	String value;

	/**
	 * @author SG.LEE
	 * @param state 상태
	 * @param stateName 상태값
	 * @param value 최종요청 값
	 */
	EnLayerBboxRecalculate(String state, String stateName, String value) {
		this.state = state;
		this.stateName = stateName;
		this.value= value;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
