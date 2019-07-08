
package com.gitrnd.gdsbuilder.geoserver;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitrnd.gdsbuilder.geoserver.data.DTGSGeogigDatastoreEncoder;
import com.gitrnd.gdsbuilder.geoserver.net.DTHTTPUtils;
import com.gitrnd.gdsbuilder.geoserver.service.en.EnLayerBboxRecalculate;
import com.gitrnd.gdsbuilder.type.geoserver.GeoLayerInfo;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.HTTPUtils;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

/**
 * {@link GeoServerRESTPublisher} 상속 클래스 - Geoserver 관련 데이터 생성, 수정, 삭제
 * @author SG.LEE
 *
 */
public class DTGeoserverPublisher extends GeoServerRESTPublisher {
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DTGeoserverPublisher.class);
	/**
	 * Geoserver URL
	 */
	private final String restURL;
	/**
	 * Geoserver ID
	 */
	private final String gsuser;
	/**
	 * Geoserver PW
	 */
	private final String gspass;

	/**
	 * DTGeoserverPublisher 생성자
	 * @author SG.LEE
	 * @param restURL Geoserver URL
	 * @param username Geoserver ID
	 * @param password Geoserver PW
	 */
	public DTGeoserverPublisher(String restURL, String username, String password) {
		super(restURL, username, password);
		this.restURL = HTTPUtils.decurtSlash(restURL);
		this.gsuser = username;
		this.gspass = password;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.geoserver.rest.GeoServerRESTPublisher#publishDBLayer(java.lang.String, java.lang.String, it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder, it.geosolutions.geoserver.rest.encoder.GSLayerEncoder)
	 */
	@SuppressWarnings("deprecation")
	public boolean publishDBLayer(final String workspace, final String storename, final GSFeatureTypeEncoder fte,
			final GSLayerEncoder layerEncoder) {
		String ftypeXml = fte.toString();
		StringBuilder postUrl = new StringBuilder(restURL).append("/rest/workspaces/").append(workspace)
				.append("/datastores/").append(storename).append("/featuretypes");

		final String layername = fte.getName();
		if (layername == null || layername.isEmpty()) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error(
						"GSFeatureTypeEncoder has no valid name associated, try using GSFeatureTypeEncoder.setName(String)");
			return false;
		}

		String configuredResult = HTTPUtils.postXml(postUrl.toString(), ftypeXml, this.gsuser, this.gspass);
		boolean published = configuredResult != null;
		boolean configured = false;

		if (!published) {
			LOGGER.warn(
					"Error in publishing (" + configuredResult + ") " + workspace + ":" + storename + "/" + layername);
		} else {
			LOGGER.info("DB layer successfully added (layer:" + layername + ")");

			if (layerEncoder == null) {
				if (LOGGER.isErrorEnabled())
					LOGGER.error("GSLayerEncoder is null: Unable to find the defaultStyle for this layer");
				return false;
			}

			configured = configureLayer(workspace, layername, layerEncoder);

			if (!configured) {
				LOGGER.warn("Error in configuring (" + configuredResult + ") " + workspace + ":" + storename + "/"
						+ layername);
			} else {
				LOGGER.info("DB layer successfully configured (layer:" + layername + ")");
			}
		}
		return published && configured;
	}

	/**
	 * Geoserver에 검수결과 레이어 발행
	 * @author SG.LEE
	 * @param wsName 작업공간
	 * @param dsName 저장소
	 * @param geoLayerInfo Geoserver에 발행할 레이어 정보
	 * @return 발행결과 true or false
	 */
	public boolean publishErrLayer(String wsName, String dsName, GeoLayerInfo geoLayerInfo) {
		String fileName = geoLayerInfo.getFileName();
		String src = geoLayerInfo.getOriginSrc();
		// String fullName = "err_" +geoLayerInfo.getFileType()+"_"+ fileName;
		String fullName = geoLayerInfo.getFileName();

		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		GSLayerEncoder layerEncoder = new GSLayerEncoder();

		fte.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.REPROJECT_TO_DECLARED);
		fte.setTitle(fullName);
		fte.setNativeName(fullName);
		fte.setName(fullName);
		fte.setSRS(src);

		layerEncoder.setDefaultStyle("defaultStyle");
		boolean flag = super.publishDBLayer(wsName, dsName, fte, layerEncoder);
		return flag;
	}

	/**
	 * Geoserver 레이어 삭제
	 * @author SG.LEE
	 * @param wsName 작업공간
	 * @param storeName 저장소
	 * @param layerNameList 삭제할 레이어명 리스트
	 * @return 삭제결과 true or false
	 */
	public boolean removeLayers(String wsName, String storeName, List<String> layerNameList) {
		boolean flag = false;
		int flagCount = 0;
		for (int i = 0; i < layerNameList.size(); i++) {
			String layerName = (String) layerNameList.get(i);
			flag = unpublishFeatureType(wsName, storeName, layerName);
			if (!flag) {
				flagCount++;
			}
		}

		if (flagCount > 0) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * Geoserver 레이어 정보 업데이트
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param storename 저장소
	 * @param layername 레이어명
	 * @param fte {@link GSFeatureTypeEncoder} 변경할 정보
	 * @param layerEncoder {@link GSLayerEncoder} 레이어 Encoder 정보
	 * @param attChangeFlag 속성 변경여부
	 * @return 업데이트 수행결과 true or false
	 */
	public boolean updateFeatureType(String workspace, String storename, String layername, GSFeatureTypeEncoder fte,
			GSLayerEncoder layerEncoder, boolean attChangeFlag) {
		String ftypeXml = fte.toString();

		StringBuilder putUrl = new StringBuilder(this.restURL).append("/rest/workspaces/").append(workspace)
				.append("/datastores/").append(storename).append("/featuretypes/").append(layername + ".xml");

		if ((layername == null) || (layername.isEmpty())) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error(
						"GSFeatureTypeEncoder has no valid name associated, try using GSFeatureTypeEncoder.setName(String)");
			return false;
		}

		String configuredResult = HTTPUtils.putXml(putUrl.toString(), ftypeXml, this.gsuser, this.gspass);
		boolean updated = configuredResult != null;
		boolean configured = false;

		if (!updated) {
			LOGGER.warn(
					"Error in updating (" + configuredResult + ") " + workspace + ":" + storename + "/" + layername);
		} else {
			LOGGER.info("DB layer successfully updated (layer:" + layername + ")");

			if (layerEncoder == null) {
				if (LOGGER.isErrorEnabled())
					LOGGER.error("GSLayerEncoder is null: Unable to find the defaultStyle for this layer");
				return false;
			}

			configured = configureLayer(workspace, layername, layerEncoder);

			if (!configured)
				LOGGER.warn("Error in configuring (" + configuredResult + ") " + workspace + ":" + storename + "/"
						+ layername);
			else {
				if (attChangeFlag) {// attribute 수정시 서버 리로드
					reload();
				}
				LOGGER.info("layer successfully configured (layer:" + layername + ")");
			}
		}
		return (updated) && (configured);
	}

	/**
	 * Geoserver Layer정보 영역계산(주로 레이어 업데이트 후 요청)
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param storename 저장소
	 * @param layername 레이어명
	 * @param type Geoserver 영역타입 {@link EnLayerBboxRecalculate} ALL, NATIVEBBOX, LATLONBBOX 
	 * @return 재계산 여부 true or false
	 */
	public boolean recalculate(String workspace, String storename, String layername, EnLayerBboxRecalculate type) {
		String recalculateType = "";
		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		fte.setName(layername);

		if (type == null) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("EnLayerBBoxRecalculate has no valid value associated");
			return false;
		}

		recalculateType = type.getValue();

		String ftypeXml = fte.toString();
		StringBuilder putUrl = new StringBuilder(this.restURL).append("/rest/workspaces/").append(workspace)
				.append("/datastores/").append(storename).append("/featuretypes/")
				.append(layername + "?" + recalculateType);

		if ((layername == null) || (layername.isEmpty())) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Please enter a layername");
			return false;
		}
		String configuredResult = HTTPUtils.putXml(putUrl.toString(), ftypeXml, this.gsuser, this.gspass);
		boolean updated = configuredResult != null;
		boolean configured = false;

		if (!updated)
			LOGGER.warn("Error in recalculating (" + configuredResult + ") " + workspace + ":" + storename + "/"
					+ layername);
		else {
			LOGGER.info("layer successfully recalculated (layer:" + layername + ")");
		}
		return (updated) && (configured);
	}

	/**
	 * Geoserver WFST요청(Feature 추가, 업데이트, 삭제)
	 * @author SG.LEE
	 * @param workspace 작업공간
	 * @param wfstXml WFST 요청 XML
	 * @return WFST 요청결과
	 */
	public String requestWFSTransaction(String workspace, String wfstXml) {
		StringBuilder postUrl = new StringBuilder(restURL).append("/" + workspace).append("/ows");

		Charset charset = Charset.forName("utf-8");

		String configuredResult = DTHTTPUtils.postXml(postUrl.toString(), wfstXml, this.gsuser, this.gspass,
				charset.name());

		boolean requestFlag = configuredResult != null;

		if (!requestFlag) {
			LOGGER.warn("WFST 요청 실패");
		} else {
			LOGGER.info("WFST 요청 성공");
		}
		return configuredResult;
	}

	/**
	 * Geogig Repository 기반의 Geoserver datastore 환경 설정 변경.
	 * <p>
	 * Checkout Branch 변경 시 Geoserver에 발행된 Layer 또한 해당 Branch의 Layer로 변경됨.
	 * 
	 * @param workspace geoserver ws
	 * @param datastore geoserver ds
	 * @param dsEncoder Geogig Repository 기반의 Geoserver datastore Encoder
	 * @return Geoserver datastore 환경 설정 변경 성공 여부
	 *         <p>
	 *         {@code true} : 변경 성공, {@code false} : 변경 실패
	 * @author DY.Oh
	 */
	public boolean updateDatastore(String workspace, String datastore, DTGSGeogigDatastoreEncoder dsEncoder) {

		String dsXml = dsEncoder.toString();
		StringBuilder putUrl = new StringBuilder(this.restURL).append("/rest/workspaces/").append(workspace)
				.append("/datastores/").append(datastore).append(".xml");

		String updateResult = HTTPUtils.putXml(putUrl.toString(), dsXml, this.gsuser, this.gspass);
		boolean updated = updateResult != null;
		updated = reload();
		return updated;
	}

}