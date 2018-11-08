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
package com.gitrnd.gdsbuilder.fileread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

/**
 * @Description ZIP 파일을 지정된 경로에 압축해제하는 기능을 제공 
 * @author SG.Lee
 * @Date 2018. 7. 31. 오후 5:49:40
 * */
public class UnZipFile {

	
	private File zipFile;
	
	/**
	 * 압축해제 경로
	 */
	private String OUTPUT_DIR;
	/**
	 * 압축폴더명
	 */
	private String entryName;
	/**
	 * 압축파일 경로
	 */
	private String fileDirectory;
	/**
	 * 파일 포맷별 목록
	 * <파일명,파일경로들(shp,shx,dbf)>
	 */

	
	public UnZipFile(File zipFile){
		this.zipFile = zipFile;
		this.OUTPUT_DIR = zipFile.getParent();
	}
	
	/**
	 * 압축폴더명 반환
	 * 
	 * @return String
	 */
	public String getEntryName() {
		return entryName;
	}

	/**
	 * 압축폴더명 설정
	 * 
	 * @param entryName
	 *            압축폴더명
	 */
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}


	/**
	 * 압축파일 경로 반환
	 * 
	 * @return String
	 */
	public String getFileDirectory() {
		return fileDirectory;
	}

	/**
	 * 압축파일 경로 설정
	 * 
	 * @param fileDirectory
	 *            압축파일 경로
	 */
	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	/**
	 * 압축해제 경로 반환
	 * 
	 * @return String
	 */
	public String getOUTPUT_DIR() {
		return OUTPUT_DIR;
	}

	/**
	 * @Description zip 파일의 압축 해제 후 지정된 경로에 압축 해제
	 * @author SG.Lee
	 * @Date 2018. 8. 1. 오전 11:37:15
	 * @return 에러코드 200 성공
	 *                 700 파일구조 에러
	 *                 
	 * @throws Throwable long
	 * */
	public long decompress() throws Throwable {

		long flagNum = 200;
		
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipentry = null;
		try {
			// 디렉토리생성
			String zipFileName = this.zipFile.getName(); // 파일 이름.확장자
			int comma = zipFileName.lastIndexOf(".");
			this.entryName = zipFileName.substring(0, comma); // 파일 이름
			this.fileDirectory = OUTPUT_DIR+ "\\" + entryName;
			
			
			FileUtils.forceMkdir(new File(OUTPUT_DIR, entryName)); // 폴더 생성
			// 파일 스트림
			fis = new FileInputStream(this.zipFile);
			// Zip 파일 스트림
			zis = new ZipInputStream(fis);

			

			// Fentry가 없을때까지 뽑기
			while ((zipentry = zis.getNextEntry()) != null) {
				String zipentryName = zipentry.getName();
				File file = new File(OUTPUT_DIR, zipentryName);

				
				// entiry가 폴더면 구조에러
				if (zipentry.isDirectory()) {
					flagNum = 700;
					file.deleteOnExit();
					break;
				} else {
					// 파일이면 파일 만들기
					createFile(file, zis);
				}
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			if (zis != null)
				zis.close();
			if (fis != null)
				fis.close();
		}
		return flagNum;
	}

	private void createFile(File file, ZipInputStream zis) throws Throwable {
		// 디렉토리 확인
		File parentDir = new File(file.getParent());
		// 디렉토리가 없으면 생성하자
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		// 파일 스트림 선언
		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] buffer = new byte[256];
			int size = 0;
			// Zip스트림으로부터 byte뽑아내기
			while ((size = zis.read(buffer)) > 0) {
				// byte로 파일 만들기
				fos.write(buffer, 0, size);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
