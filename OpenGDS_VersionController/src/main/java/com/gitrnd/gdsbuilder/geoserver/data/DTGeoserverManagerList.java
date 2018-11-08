package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.HashMap;
import java.util.Iterator;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

public class DTGeoserverManagerList extends HashMap<String, DTGeoserverManager> {

	private static final long serialVersionUID = 1L;
	
	public void putDTGeoserver(String serverName, DTGeoserverManager dtGeoserverManager){
		super.put(serverName, dtGeoserverManager);
	}
	
	public void removeDTGeoserver(String serverName){
		super.remove(serverName);
	}
	
	public DTGeoserverManager getDTGeoserverManager(String serverName){
		return super.get(serverName);		
	}
	
	
	/**
	 * @Description Geoserver 중복체크
	 * @author SG.Lee
	 * @Date 2018. 7. 13. 오전 11:30:21
	 * @param serverName 서버이름
	 * @return boolean
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
