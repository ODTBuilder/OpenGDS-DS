package com.gitrnd.qaproducer.file.service;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.filestatus.service.FileStatusService;


@Service
@PropertySources({@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
	@PropertySource(value = "file:./application.yml", ignoreResourceNotFound = true)})
public class DeleteFileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFileService.class);
	
	@Value("${gitrnd.serverhost}")
	private String serverIP;

	/*
	 * @Value("${gitrnd.apache.port}") private String apachePort;
	 */

	@Value("${gitrnd.apache.basedir}")
	private String baseDir;

	@Value("${server.port}")
	private String serverPort;

	@Value("${server.context-path}")
	private String context;

	@Value("${gitrnd.apache.basedrive}")
	private String basePath;
	
	@Value("${gitrnd.apache.host}")
	private String apacheHost;

	@Value("${gitrnd.apache.port}")
	private String apachePort;
	
	@Autowired
	private FileStatusService fileStatusService;

	public boolean deleteErrorZipFile(LoginUser loginUser, String[] filenames) throws Exception{
		String uploadPath = basePath + ":" + File.separator + baseDir + File.separator + loginUser.getUsername()
		+ File.separator + "error";
		
		File file = new File(uploadPath);
		
		if( file.exists() ){ //파일존재여부확인

			if(file.isDirectory()){ //파일이 디렉토리인지 확인
				
				File[] files = file.listFiles();
				for( int i=0; i<files.length; i++){
					
					File[] zipFiles = files[i].listFiles();
					
					for(int j = 0; j < zipFiles.length; j++){
						if(Arrays.asList(filenames).contains(zipFiles[j].getName())){
							if( zipFiles[j].delete() ){
								LOGGER.info("{} file(error layer) delete success!", zipFiles[j].getName());
							}else{
								LOGGER.info("{} file(error layer) delete fail!", zipFiles[j].getName());
							}
						}
					}
					
					if(files[i].delete()){
						LOGGER.info("{} directory delete success!", files[i].getName());
					}else{
						LOGGER.info("{} directory delete fail!", files[i].getName());
					}
				}
			}
			
		}else{
			LOGGER.info("ERROR! : {} directory is not exist!", uploadPath);
			return false;
		}
		
		return true;
	}
	
	public boolean deleteOriginalZipFile(String user, int fid) throws Exception{
		
		FileStatus fileStatus = fileStatusService.retrieveFileStatusById(fid);
		Pattern p = Pattern.compile("(?<=time\\=).*(?=\\&)");
		Matcher m = p.matcher(fileStatus.getLocation());
		
		while(m.find()){
			String path = basePath + ":" + File.separator + baseDir + File.separator + user + File.separator + 
					"upload" + File.separator + m.group();
			File file = new File(path);
			
			if( file.exists() ){ //파일존재여부확인

				if(file.isDirectory()){ //파일이 디렉토리인지 확인
					
						File[] files = file.listFiles();
						for( int i=0; i<files.length; i++){
							
							if(files[i].getName().equals(fileStatus.getFname())){
								if( files[i].delete() ){
									LOGGER.info("{} file delete success!", files[i].getName());
								}else{
									LOGGER.info("{} file delete fail!", files[i].getName());
									return false;
								}
							}
						}
						
						if(file.delete()){
							LOGGER.info("{} directory delete success!", file.getName());
							
						}else{
							LOGGER.info("{} directory delete fail!", file.getName());
							return false;
						}
				} else {
					LOGGER.info("ERROR! : {} is not directory", path);
					return false;
				}
			}else{
				LOGGER.info("ERROR! : {} directory is not exist!", path);
				return false;
			}
		}
		
		return true;
	}
	
public boolean deleteOriginalUnZipFile(String user, int fid) throws Exception{
		
		FileStatus fileStatus = fileStatusService.retrieveFileStatusById(fid);
		Pattern p = Pattern.compile("(?<=time\\=).*(?=\\&)");
		Matcher m = p.matcher(fileStatus.getLocation());
		
		while(m.find()){
			String path = basePath + ":" + File.separator + baseDir + File.separator + user + File.separator + 
					"upload" + File.separator + m.group();
			File file = new File(path);
			
			if( file.exists() ){ //파일존재여부확인

				if(file.isDirectory()){ //파일이 디렉토리인지 확인
					
						File[] files = file.listFiles();
						for( int i=0; i<files.length; i++){
							
							if(files[i].getName().equals(fileStatus.getFname())){
								if( files[i].delete() ){
									LOGGER.info("{} file delete success!", files[i].getName());
								}else{
									LOGGER.info("{} file delete fail!", files[i].getName());
									return false;
								}
							}
						}
						
						if(file.delete()){
							LOGGER.info("{} directory delete success!", file.getName());
							
						}else{
							LOGGER.info("{} directory delete fail!", file.getName());
							return false;
						}
				} else {
					LOGGER.info("ERROR! : {} is not directory", path);
					return false;
				}
			}else{
				LOGGER.info("ERROR! : {} directory is not exist!", path);
				return false;
			}
		}
		
		return true;
	}
}
