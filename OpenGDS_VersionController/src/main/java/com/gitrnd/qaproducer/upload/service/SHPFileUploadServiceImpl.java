package com.gitrnd.qaproducer.upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gitrnd.gdsbuilder.fileread.FileMeta;
import com.gitrnd.gdsbuilder.fileread.UnZipFile;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.geoserver.service.GeoserverService;

import it.geosolutions.geoserver.rest.Util;

@Service("shpFileuploadService")
public class SHPFileUploadServiceImpl implements SHPFileUploadService {

	@Autowired
	@Qualifier("geoService")
	private GeoserverService geoserverService;

	
	public FileMeta shpFileUpload(FileMeta fileMeta, DTGeoserverManager dtGeoManager, String wsName, String dsName, String style) throws Throwable {
		String zipFilePath = fileMeta.getFilePath();
		String src = fileMeta.getOriginSrc();

		UnZipFile unZipFile = new UnZipFile(new File(zipFilePath));
		long unZipResult = unZipFile.decompress();
		
		String upZipFilePath = unZipFile.getFileDirectory();
		
		
		if(unZipResult==200){
			Map<String, List<String>> fileNames = new HashMap<String,List<String>>();
			
			//압축푼 파일리스트 탐색
			File dir = new File(upZipFilePath);

			File[] fileList = dir.listFiles();

			//확장자가 다르고 같은이름인 파일들 리스트에 각각 추가				
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				if (file.isFile()) {
					String filePath = file.getPath();
					String fFullName = file.getName();
					
					int Idx = fFullName.lastIndexOf(".");
					String _fileName = fFullName.substring(0, Idx).toLowerCase();
					
					List<String> filePaths = fileNames.get(_fileName);
					if(filePaths==null){
						filePaths = new ArrayList<String>();
						filePaths.add(filePath);
						fileNames.put(_fileName, filePaths);
					}else{
						filePaths.add(filePath);
						fileNames.put(_fileName, filePaths);
					}
				}
			}
			
			//같은 파일명 압축 후 Geoserver 발행요청
			Iterator<String> keys = fileNames.keySet().iterator();
			while (keys.hasNext()) {
				String layerName = (String) keys.next();
				boolean dupFlag = dtGeoManager.getReader().existsLayer(wsName, layerName, Util.DEFAULT_QUIET_ON_NOT_FOUND);
				if(dupFlag){
					Map<String, Long> failMap = new HashMap<String,Long>(); 
					failMap.put(layerName, new Long(701));//중복 에러
					fileMeta.getFailLayers().add(failMap);
					continue;
				}else{
					List<String> filePaths = fileNames.get(layerName);
					
					String createZipPath = upZipFilePath + File.separator + layerName + ".zip"; 
					
					try {
					    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(createZipPath));
					 
					    for (int i=0; i<filePaths.size(); i++) {
					        FileInputStream in = new FileInputStream(filePaths.get(i));
					                
					        ZipEntry ze = new ZipEntry(filePaths.get(i));
					        out.putNextEntry(ze);
					          
					        byte[] buf = new byte[4096];
					        
					        int len;
					        while ((len = in.read(buf)) > 0) {
					            out.write(buf, 0, len);
					        }
					        out.closeEntry();
					        in.close();
					    }
					    out.close();
					} catch (IOException e) {
					    e.printStackTrace();
					} finally {
						File shpZipFile = new File(createZipPath);
						
						boolean publishFlag = false;
						if(style!=null&&!style.equals("")){
							publishFlag = geoserverService.shpLayerPublishGeoserver(dtGeoManager, wsName, dsName, layerName, shpZipFile, src);
						}else{
							publishFlag = geoserverService.shpLayerPublishGeoserver(dtGeoManager, wsName, dsName, layerName, shpZipFile, src, style);
						}
						
						if(publishFlag){
							fileMeta.getSuccessLayers().add(layerName);
						}else{
							Map<String, Long> failMap = new HashMap<String,Long>(); 
							failMap.put(layerName, new Long(702));//발행실패
							fileMeta.getFailLayers().add(failMap);
						}
					}
				}
			}
		}else{
			fileMeta.setUploadFlag(unZipResult);
		}
		return fileMeta;
	}
}
