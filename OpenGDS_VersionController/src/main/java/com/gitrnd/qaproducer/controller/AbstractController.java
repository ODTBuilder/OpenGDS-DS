/*
 *    OpenGDS/Builder
 *    http://git.co.kr
 *
 *    (C) 2014-2017, GeoSpatial Information Technology(GIT)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package com.gitrnd.qaproducer.controller;

import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.gdsbuilder.geoserver.data.DTGeoserverManagerList;
import com.gitrnd.gdsbuilder.geoserver.factory.DTGeoserverFactory;
import com.gitrnd.gdsbuilder.geoserver.factory.impl.DTGeoserverFactoryImpl;
import com.gitrnd.qaproducer.common.security.LoginUser;




/**
 * Session을 관리한다.
 * @author SG.Lee
 * @Date 2016.02
 * */
@Controller
public class AbstractController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
	
	/**
	 * 세션 저장
	 * 
	 * @param request
	 * @param sessionName
	 *            : 세션 이름(EnSessionName에 정의)
	 * @param object
	 *            : 저장할 객체
	 */
	public void setSession(HttpServletRequest request, String sessionName,
			Object object) {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(sessionName, object);
	}

	/**
	 * 세션 로드
	 * 
	 * @param request
	 * @param sessionName
	 *            : 불러올 세션 이름(EnSessionName에 정의)
	 * @return
	 */
	public Object getSession(HttpServletRequest request, String sessionName) {
		HttpSession httpSession = request.getSession();
		return httpSession.getAttribute(sessionName);
	}

	/**
	 * 세션 삭제
	 * 
	 * @param request
	 * @param sessionName
	 *            : 삭제할 세션 이름(EnSessionName에 정의)
	 */
	public void removeSession(HttpServletRequest request, String sessionName) {
		HttpSession httpSession = request.getSession();
		httpSession.removeAttribute(sessionName);
	}

	/**
	 * 세션 변경
	 * 
	 * @param request
	 * @param sessionName
	 */
	public void updateSession(HttpServletRequest request, String sessionName,
			Object object) {
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute(sessionName, object);
	}
	
	
	
	/**
	 * Geoserver Session 추가
	 * @author SG.Lee
	 * @Date 2018. 7. 5. 오후 4:25:03
	 * @param request
	 * @return int
	 *         200 : 성공
	 *         600 : 로그인세션 없음
	 *         601 : 미입력 텍스트 존재
	 *         602 : 서버이름중복
	 *         603 : Geoserver 세션없음
	 *         604 : Geoserver 정보오류 
	 * */
	public int addGeoserverToSession(HttpServletRequest request, LoginUser loginUser){
		String serverName = "";
		String serverURL = "";
		String id="";
		String pw="";
		
		//사용자 로그인 세션체크
		LoginUser user = loginUser;
		if(user==null){
			return 600;
		}
		
		//input 파라미터 체크
		int flagNum = 0;
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);

			if (key.toLowerCase().equals("servername")) {
				serverName = value;
				flagNum++;
			} else if (key.toLowerCase().equals("serverurl")) {
				serverURL = value;
				flagNum++;
			} else if (key.toLowerCase().equals("id")) {
				id = value;
				flagNum++;
			} else if (key.toLowerCase().equals("pw")) {
				pw = value;
				flagNum++;
			}
		}
		
		DTGeoserverManagerList dtGeoManagers = (DTGeoserverManagerList)this.getSession(request, "geoserver");
		if(dtGeoManagers==null){
			return 603;
		}
		
		
		//서버이름 또는 URL 중복체크
        if(dtGeoManagers.duplicateCheck(serverName,serverURL)){
        	return 602;
        }
		
		//Geoserver 체크
		if(flagNum!=4){
			return 601;
		}
		else{
			DTGeoserverFactory factory = new DTGeoserverFactoryImpl();
			try {
				DTGeoserverManager dtManager = factory.createDTGeoserverManager(serverURL, id, pw);
				dtGeoManagers.put(serverName, dtManager);
				this.updateSession(request, "geoserver", dtGeoManagers);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				return 604;
			}
		}
		return 200;
	}
	
	
	/**
	 * Geoserver Seesion 삭제
	 * @author SG.Lee
	 * @Date 2018. 7. 6. 오후 4:41:04
	 * @param request
	 * @return int
	 *         200 : 성공
	 *         600 : 로그인세션 없음
	 *         603 : Geoserver 세션없음
	 *         605 : 서버이름존재 X 
	 * */
	public int removeGeoserverToSession(HttpServletRequest request, LoginUser loginUser){
		String serverName = "";
		
		
		//사용자 로그인 세션체크
		LoginUser user = loginUser;
		if(user==null){
			return 600;
		}
		
		//input 파라미터 체크
		int flagNum = 0;
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);
			if (key.toLowerCase().equals("servername")) {
				serverName = value;
				flagNum++;
				break;
			} 
		}
		
		DTGeoserverManagerList dtGeoManagers = (DTGeoserverManagerList)this.getSession(request, "geoserver");
		if(dtGeoManagers==null){
			return 603;
		}
		
		//Geoserver 체크
		if(flagNum!=1){
			return 601;
		}
		else{
			//서버이름 중복체크
			Iterator<String> keys = dtGeoManagers.keySet().iterator();
	        while( keys.hasNext() ){
	            String key = keys.next();
	            //삭제성공
	            if(serverName.equals(key)){
	            	dtGeoManagers.remove(key);
	            	return 200;
	            }
	        }
		}
		return 605; //같은 이름의 서버가 없음
	}
	
	/**
	 *
	 * @author SG.Lee
	 * @Date 2018. 7. 6. 오후 5:36:50
	 * @param request
	 * @return DTGeoserverManager
	 * */
	public DTGeoserverManager getGeoserverManagerToSession(HttpServletRequest request, LoginUser loginUser, String serverName){
		DTGeoserverManager dtGeoserverManager = null;
		
		//사용자 로그인 세션체크
		LoginUser user = loginUser;
		if(user==null){
			LOGGER.error("사용자 세션 존재 X");
			return null;
		}
		
		//input 파라미터 체크
		if(!serverName.equals("")&&serverName!=null){
			DTGeoserverManagerList dtGeoManagers = (DTGeoserverManagerList)this.getSession(request, "geoserver");
			if(dtGeoManagers==null){
				LOGGER.error("Geoserver 세션 존재 X");
				return null;
			}
			
			//Geoserver 체크
			//해당 조건에 맞는 DTGeoManager 객체 리턴
			Iterator<String> keys = dtGeoManagers.keySet().iterator();
	        while( keys.hasNext() ){
	            String key = keys.next();
	            if(serverName.equals(key)){
	            	dtGeoserverManager=  dtGeoManagers.get(key);
	            }
	        }
		}else{
			LOGGER.error("서버이름 입력 X");
			return null;
		}
		return dtGeoserverManager;
	}
	
	/**
	 * @Description Geoserver 세션 리스트조회
	 * @author SG.Lee
	 * @Date 2018. 7. 16. 오전 9:59:52
	 * @param request
	 * @param loginUser
	 * @return DTGeoserverManagerList
	 * */
	public DTGeoserverManagerList getGeoserverManagersToSession(HttpServletRequest request, LoginUser loginUser){
		DTGeoserverManagerList dtGeoserverManagers = null;
		
		//사용자 로그인 세션체크
		LoginUser user = loginUser;
		if(user==null){
			LOGGER.error("사용자 세션 존재 X");
			return null;
		}
		
		dtGeoserverManagers = (DTGeoserverManagerList)this.getSession(request, "geoserver");
		if(dtGeoserverManagers==null){
			LOGGER.error("Geoserver 세션 존재 X");
			return null;
		}
		
		return dtGeoserverManagers;
	}
	
	
	/**
	 * @Description 
	 * @author SG.Lee
	 * @Date 2018. 7. 16. 오전 10:03:14
	 * @param request
	 * @param loginUser
	 * @return boolean
	 * */
	public boolean checkUserGeoserver(HttpServletRequest request, LoginUser loginUser){
		boolean flag = false;
		
		String serverName = "";
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement().toString();
			String value = request.getParameter(key);
			if (key.toLowerCase().equals("servername")) {
				serverName = value;
				break;
			} 
		}
		
		if(serverName.equals("")){
			LOGGER.error("Geoserver 이름이 입력되지 않음");
			return false;
		}
		
		DTGeoserverManagerList dtGeoManagers = (DTGeoserverManagerList)this.getSession(request, "geoserver");
		if(dtGeoManagers==null){
			LOGGER.error("Geoserver 세션 존재 X");
			return false;
		}else{
			Iterator<String> keys = dtGeoManagers.keySet().iterator();
	        while( keys.hasNext() ){
	            String key = keys.next();
	            //세션에 같은 이름이 존재할시 
	            if(serverName.equals(key)){
	            	flag = true;
	            	break;
	            }
	        }
		}
		return flag;
	}
}
