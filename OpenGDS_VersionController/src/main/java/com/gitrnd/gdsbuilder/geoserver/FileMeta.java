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

package com.gitrnd.gdsbuilder.geoserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 클라이언트에서 업로드한 파일정보 클래스
 * @author SG.Lee
 * @since 2017. 5. 12. 오전 2:23:36
 * */
@JsonIgnoreProperties({ "bytes" })
public class FileMeta {

	/**
	 * 파일이름
	 */
	private String fileName;
	/**
	 * 파일경로
	 */
	private String filePath;
	/**
	 * 파일사이즈
	 */
	private String fileSize;
	/**
	 * 파일타입
	 */
	private String fileType;
	/**
	 * 기존 파일경로
	 */
	private String originSrc;
	/**
	 * 업로드 상태코드
	 */
	private long uploadFlag;
	/**
	 * 업로드 성공 레이어 리스트
	 */
	private List<String> successLayers = new ArrayList<String>();
	/**
	 * 업로드 실패 레이어 리스트
	 * List<Map<파일명,업로드 상태코드>>
	 */
	private List<Map<String,Long>> failLayers = new ArrayList<Map<String,Long>>();
	/**
	 * 
	 */
	private byte[] bytes;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * @param bytes
	 *            the bytes to set
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public long isUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(long uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public String getOriginSrc() {
		return originSrc;
	}

	public void setOriginSrc(String originSrc) {
		this.originSrc = originSrc;
	}

	// setters & getters
	
	public List<String> getSuccessLayers() {
		return successLayers;
	}

	public void setSuccessLayers(List<String> successLayers) {
		this.successLayers = successLayers;
	}

	public List<Map<String, Long>> getFailLayers() {
		return failLayers;
	}

	public void setFailLayers(List<Map<String, Long>> failLayers) {
		this.failLayers = failLayers;
	}
}