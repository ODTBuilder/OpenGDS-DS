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

package com.gitrnd.qaproducer.upload.controller;

import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gitrnd.gdsbuilder.fileread.FileMeta;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.controller.AbstractController;
import com.gitrnd.qaproducer.upload.service.FileUploadService;

/**
 * 파일 업로드와 관련된 요청을 수행한다.
 * 
 * @author SG.Lee
 * @Date 2017.04.11
 */
@Controller("fileUploadController")
@RequestMapping("/file")
public class FileUploadController extends AbstractController{
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	@Qualifier("fileService")
	private FileUploadService fileService;

	/**
	 * @throws Throwable
	 *             파일업로드 @author SG.Lee @Date 2017. 4 @param request @param
	 *             response @return @throws Exception
	 *             LinkedList<FileMeta> @throws
	 */
	@RequestMapping(value = "/fileUpload.ajax", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> fileUploadRequest(MultipartHttpServletRequest request,
			HttpServletResponse response, @AuthenticationPrincipal LoginUser loginUser) throws Throwable {
		if(loginUser==null){
			throw new NullPointerException("로그인 세션이 존재하지 않습니다.");
		}
		String serverName = request.getParameter("serverName");
		DTGeoserverManager dtGeoserverManager = super.getGeoserverManagerToSession(request, loginUser, serverName);
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		files = fileService.filesUpload(dtGeoserverManager,loginUser,request, response);
		return files;
	}

	/**
	 * 파일명 중복체크 @author SG.Lee @Date 2017. 4 @param fileName @return
	 * boolean @throws
	 */
/*	@RequestMapping(value = "/fileNameDupCheckAjax.ajax", method = RequestMethod.GET)
	public @ResponseBody boolean fileNameDupCheck(HttpServletRequest request,
			@RequestParam(value = "fileName", required = true) String fileName) {
		UserVO generalUser = (UserVO) getSession(request, EnUserType.GENERAL.getTypeName());
		if (generalUser == null) {
			return false;
		}
		boolean dupFlag = false;
		dupFlag = fileService.fileNameDupCheck(generalUser, fileName);
		return dupFlag;
	}*/
}
