package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.HashMap;
import java.util.Iterator;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * DTGeoserverManager List
 * @author SG.LEE
 */
public class DTGeoserverManagerList extends HashMap<String, DTGeoserverManager> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * {@link DTGeoserverManagerList}에 {@link DTGeoserverManager} Put
	 * @author SG.LEE
	 * @param serverName 서버명
	 * @param dtGeoserverManager {@link DTGeoserverManager} 
	 */
	public void putDTGeoserver(String serverName, DTGeoserverManager dtGeoserverManager){
		super.put(serverName, dtGeoserverManager);
	}
	
	/**
	 * {@link DTGeoserverManager} Map에서 삭제
	 * @author SG.LEE
	 * @param serverName 서버명
	 */
	public void removeDTGeoserver(String serverName){
		super.remove(serverName);
	}
	
	/**
	 * serverName을 통한 {@link DTGeoserverManager} 조회
	 * @author SG.LEE
	 * @param serverName 서버명
	 * @return {@link DTGeoserverManager}
	 */
	public DTGeoserverManager getDTGeoserverManager(String serverName){
		return super.get(serverName);		
	}
	
	
	/**
	 * {@link DTGeoserverManager} 중복체크
	 * @author SG.Lee
	 * @since 2018. 7. 13. 오전 11:30:21
	 * @param serverName 서버이름
	 * @param url Geoserver주소
	 * @return boolean Geoserver 중복여부
	 * */
	public boolean duplicateCheck(String serverName, String url){
		boolean flag = false;
		Iterator<String> keys = super.keySet().iterator();
        while( keys.hasNext() ){
            String key = keys.next();
            DTGeoserverManager geoManager = super.get(key);
            if(serverName.equals(key)||url.equals(geoManager.getRestURL())){
            	return true;
            }
        }
		return flag; 
	}
}
