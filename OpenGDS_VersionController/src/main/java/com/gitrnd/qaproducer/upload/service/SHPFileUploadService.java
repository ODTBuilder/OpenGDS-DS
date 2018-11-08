package com.gitrnd.qaproducer.upload.service;

import com.gitrnd.gdsbuilder.fileread.FileMeta;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

public interface SHPFileUploadService {

	public FileMeta shpFileUpload(FileMeta fileMeta, DTGeoserverManager dtGeoManager, String wsName, String dsName, String style) throws Exception, Throwable;
	
}
