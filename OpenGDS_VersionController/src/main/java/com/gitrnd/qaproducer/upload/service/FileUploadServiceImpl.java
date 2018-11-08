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

package com.gitrnd.qaproducer.upload.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gitrnd.gdsbuilder.fileread.FileMeta;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.common.security.LoginUser;

@Service("fileService")
public class FileUploadServiceImpl implements FileUploadService {

	private static final String dirPath = System.getProperty("user.home")+"\\.val";
	
//	private static final String dirPath = "D:\\files";

	
	
	@Autowired
	@Qualifier("shpFileuploadService")
	private SHPFileUploadService shpFileService;


	/*
	 * public FileServiceImpl(UserVO userVO) { // TODO Auto-generated
	 * constructor stub id = userVO.getId(); qa20FileService = new
	 * QA20FileUploadServiceImpl(userVO); qa10FileService = new
	 * QA10FileUploadServiceImpl(userVO); fileDAO = new FileDAOImpl(userVO); }
	 */

	public LinkedList<FileMeta> filesUpload(DTGeoserverManager dtGeoManager, LoginUser loginUser, MultipartHttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		String fullDirPath = dirPath + "\\" + loginUser.getUsername();
		File dir = new File(dirPath);
		File targetDir = new File(fullDirPath);

		// 최상위 디렉토리 생성
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 사용자별 디렉토리 생성
		if (!targetDir.exists()) {
			File shpDir = new File(fullDirPath + "\\shp");
			File otherDir = new File(fullDirPath + "\\other");

			targetDir.mkdirs();
			shpDir.mkdirs();
			otherDir.mkdirs();
		}

		LinkedList<FileMeta> files = new LinkedList<FileMeta>();

		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 2. get each file
		while (itr.hasNext()) {
			FileMeta fileMeta = new FileMeta();
			// 2.1 get next MultipartFile
			mpf = request.getFile(itr.next());

			System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());

			// 2.2 if files > 10 remove the first from the list
			if (files.size() >= 10)
				files.pop();

			try {
				int pos = mpf.getOriginalFilename().lastIndexOf(".");
				String ext = mpf.getOriginalFilename().substring(pos + 1).toLowerCase();
				String epsg = request.getParameter("epsg");
				String wsName = request.getParameter("workspace");
				String dsName = request.getParameter("datastore");
				// 2.3 create new fileMeta
				
				
				fileMeta.setFileName(mpf.getOriginalFilename().substring(0, pos));
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(ext);
				fileMeta.setOriginSrc(epsg);
				fileMeta.setBytes(mpf.getBytes());

				String saveFilePath = "";

				if (ext.endsWith("zip")) {
					if (ext.endsWith("zip")) {
						saveFilePath = fullDirPath + "\\shp\\" + mpf.getOriginalFilename();
					}
				} else{
					saveFilePath = fullDirPath + "\\other\\" + mpf.getOriginalFilename();
				}

				// copy file to local disk (make sure the path "e.g.
				// D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(saveFilePath));
				fileMeta.setFilePath(saveFilePath);
				if(dtGeoManager==null){
					files.add(fileMeta);
					fileMeta.setUploadFlag(603);
				}else{
					files.add(fileMeta);
					files = this.filesPublish(dtGeoManager, wsName, dsName, files);
					fileMeta.setUploadFlag(200);
				}
				targetDir.deleteOnExit();//작업완료후 디렉토리 삭제
			} catch (IOException e) {
				// TODO Auto-generated catch block
				targetDir.deleteOnExit();//작업완료후 디렉토리 삭제
				fileMeta.setUploadFlag(500);
			}
			// 2.4 add to files
//			files = this.filesPublish(dtGeoManager, wsName, dsName, files);
		}
		return files;
	}

	private LinkedList<FileMeta> filesPublish(DTGeoserverManager dtGeoManager, String wsName, String dsName, LinkedList<FileMeta> fileMetaList) throws Throwable {
		LinkedList<FileMeta> fileMetas = fileMetaList;

		for (int i = 0; i < fileMetas.size(); i++) {
			FileMeta fileMeta = fileMetas.get(i);
			int pos = fileMeta.getFilePath().lastIndexOf(".");
			String ext = fileMeta.getFilePath().substring(pos + 1).toLowerCase();

			if (ext.endsWith("zip")) {
				FileMeta refileMeta = null;
				try {
					if (ext.equals("zip")) {
						fileMeta.setFileType("shp");
						refileMeta = shpFileService.shpFileUpload(fileMeta, dtGeoManager, wsName, dsName, "");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileMetas.set(i, refileMeta);
			}
		}
		return fileMetas;
	}
}
