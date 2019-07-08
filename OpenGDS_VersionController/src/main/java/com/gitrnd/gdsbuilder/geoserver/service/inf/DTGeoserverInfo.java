package com.gitrnd.gdsbuilder.geoserver.service.inf;

import com.gitrnd.gdsbuilder.geoserver.service.inf.DTGeoserverInfo.EnGeoserverInfo;

/**
 * Geoserver 정보 조회 클래스
 * 
 * @author SG.Lee
 * @since 2018. 7. 17. 오전 9:45:55
 */
public class DTGeoserverInfo {

	/**
	 * Geoserver 정보타입
	 * 
	 * @author SG.LEE
	 */
	public enum EnGeoserverInfo {
	/**
	 * 서버
	 */
	SERVER("SERVER", "server"),
	/**
	 * 작업공간
	 */
	WORKSPACE("WORKSPACE", "workspaces"),
	/**
	 * 저장소
	 */
	DATASTORE("DATASTORE", "datastores"),
	/**
	 * 레이어
	 */
	LAYER("LAYER", "layers"), UNKNOWN(null, null);
		String type;
		String typeName;

		private EnGeoserverInfo(String type, String typeName) {
			this.type = type;
			this.typeName = typeName;
		}

		/**
		 * type명으로 부터 {@link EnGeoserverInfo} 조회
		 * 
		 * @author SG.LEE
		 * @param type type명
		 * @return {@link EnGeoserverInfo}
		 */
		public static EnGeoserverInfo getFromType(String type) {
			for (EnGeoserverInfo format : values()) {
				if (format == UNKNOWN)
					continue;
				if (format.type.equals(type.toUpperCase()))
					return format;
			}
			return UNKNOWN;
		}

		/**
		 * typeName으로 부터 {@link EnGeoserverInfo} 조회
		 * 
		 * @author SG.LEE
		 * @param typeName typeName명
		 * @return {@link EnGeoserverInfo}
		 */
		public static EnGeoserverInfo getFromTypeName(String typeName) {
			for (EnGeoserverInfo format : values()) {
				if (format == UNKNOWN)
					continue;
				if (format.typeName.equals(typeName.toLowerCase()))
					return format;
			}
			return UNKNOWN;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
	}

	/**
	 * 서버 URL
	 */
	private String serverURL = "";
	/**
	 * {@link EnGeoserverInfo} server, workspaces, datastores, layers
	 */
	private EnGeoserverInfo type;
	/**
	 * 작업공간
	 */
	private String workspace = "";
	/**
	 * 저장소
	 */
	private String datastore = "";
	/**
	 * 레이어
	 */
	private String layers = "";
	/**
	 * export format(json, xml...)
	 */
	private String fileFormat = "";

	/**
	 * 생성자 Type이 Server일때
	 * 
	 * @param type       {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL  서버 URL
	 * @param fileFormat Export format(json, xml...)
	 */
	public DTGeoserverInfo(EnGeoserverInfo type, String serverURL, String fileFormat) {
		super();
		if (type != EnGeoserverInfo.SERVER) {
			throw new IllegalAccessError("EnGeoserverInfo Type은 Server만 가능합니다.");
		}
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (type != null) {
			this.type = type;
		}
		if (!fileFormat.trim().equals("")) {
			this.fileFormat = fileFormat;
		}
	}

	/**
	 * 생성자 Type이 workspace일때
	 * 
	 * @param type       {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL  서버 URL
	 * @param workspace  작업공간
	 * @param fileFormat Export format(json, xml...)
	 */
	public DTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String fileFormat) {
		super();
		if (type != EnGeoserverInfo.WORKSPACE) {
			throw new IllegalAccessError("EnGeoserverInfo Type은 workspace만 가능합니다.");
		}
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (type != null) {
			this.type = type;
		}
		if (!workspace.trim().equals("")) {
			this.workspace = workspace;
		}
		if (!fileFormat.trim().equals("")) {
			this.fileFormat = fileFormat;
		}
	}

	/**
	 * 생성자 Type이 datastore일때
	 * 
	 * @param type       {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL  서버 URL
	 * @param workspace  작업공간
	 * @param datastore  저장소
	 * @param fileFormat Export format(json, xml...)
	 */
	public DTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore,
			String fileFormat) {
		super();
		if (type != EnGeoserverInfo.DATASTORE) {
			throw new IllegalAccessError("EnGeoserverInfo Type은 Layer만 가능합니다.");
		}
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (type != null) {
			this.type = type;
		}
		if (!workspace.trim().equals("")) {
			this.workspace = workspace;
		}
		if (!datastore.trim().equals("")) {
			this.datastore = datastore;
		}
		if (!fileFormat.trim().equals("")) {
			this.fileFormat = fileFormat;
		}
	}

	/**
	 * 생성자 Type이 layer일때
	 * 
	 * @param type       {@link EnGeoserverInfo} Geoserver 정보 타입
	 * @param serverURL  서버 URL
	 * @param workspace  작업공간
	 * @param datastore  저장소
	 * @param layers     레이어
	 * @param fileFormat Export format(json, xml...)
	 */
	public DTGeoserverInfo(EnGeoserverInfo type, String serverURL, String workspace, String datastore, String layers,
			String fileFormat) {

		super();
		if (type != EnGeoserverInfo.LAYER) {
			throw new IllegalAccessError("EnGeoserverInfo Type은 Layer만 가능합니다.");
		}
		if (!serverURL.trim().equals("")) {
			this.serverURL = serverURL;
		}
		if (type != null) {
			this.type = type;
		}
		if (!workspace.trim().equals("")) {
			this.workspace = workspace;
		}
		if (!datastore.trim().equals("")) {
			this.datastore = datastore;
		}
		if (!layers.trim().equals("")) {
			this.layers = layers;
		}
		if (!fileFormat.trim().equals("")) {
			this.fileFormat = fileFormat;
		}
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public EnGeoserverInfo getType() {
		return type;
	}

	public void setType(EnGeoserverInfo type) {
		this.type = type;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getDatastore() {
		return datastore;
	}

	public void setDatastore(String datastore) {
		this.datastore = datastore;
	}

	public String getLayers() {
		return layers;
	}

	public void setLayers(String layers) {
		this.layers = layers;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * Geoserver 정보 URL 생성
	 * 
	 * @author SG.LEE
	 * @return Geoserver 정보 요청URL
	 */
	public String getDTGeoserverInfoURL() {
		StringBuffer urlBuffer = new StringBuffer();
		if (!this.serverURL.trim().equals("")) {
			urlBuffer.append(serverURL);
			if (type == EnGeoserverInfo.SERVER) {
				urlBuffer.append("/rest/about/version." + fileFormat);
			} else if (type == EnGeoserverInfo.WORKSPACE) {
				urlBuffer.append(
						"/rest/" + EnGeoserverInfo.WORKSPACE.getTypeName() + "/" + workspace + "." + fileFormat);
			} else if (type == EnGeoserverInfo.DATASTORE) {
				urlBuffer.append("/rest/" + EnGeoserverInfo.WORKSPACE.getTypeName() + "/" + workspace + "/"
						+ EnGeoserverInfo.DATASTORE.getTypeName() + "/" + datastore + "." + fileFormat);
			} else if (type == EnGeoserverInfo.LAYER) {
				urlBuffer.append("/rest/" + EnGeoserverInfo.WORKSPACE.getTypeName() + "/" + workspace + "/"
						+ EnGeoserverInfo.DATASTORE.getTypeName() + "/" + datastore + "/"
						+ EnGeoserverInfo.LAYER.getTypeName() + "/" + layers + "." + fileFormat);
			}
		} else
			return "";
		return urlBuffer.toString();
	}
}
