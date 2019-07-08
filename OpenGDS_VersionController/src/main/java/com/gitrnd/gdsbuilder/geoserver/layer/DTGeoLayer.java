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

/*
 *  GeoServer-Manager - Simple Manager Library for GeoServer
 *  
 *  Copyright (C) 2007,2011 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gitrnd.gdsbuilder.geoserver.layer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONObject;

import it.geosolutions.geoserver.rest.decoder.RESTFeatureType;

/**
 * Geoserver Layer 정보를 가지고 있는 클래스
 * @author SG.Lee
 * @since 2017. 2
 */
public class DTGeoLayer {
	/**
	 * 원본이름
	 */
	private String nativeName = "";
	/**
	 * 레이어이름
	 */
	private String lName = ""; 
	/**
	 * 제목
	 */
	private String title = ""; 
	/**
	 * 개요
	 */
	private String abstractContent = "";
	/**
	 * 좌표체계
	 */
	private String srs = ""; 
	/**
	 * 위/경도 영역
	 */
	private JSONObject llbBox = new JSONObject(); // LatLonBoundingBox
	/**
	 * 원본 데이터 최소경계 영역
	 */
	private JSONObject nbBox = new JSONObject(); // NativeBoundingBox
	/**
	 * 저장소타입(ex. shp, postgis..)
	 */
	private String dsType = ""; // 저장소타입
	/**
	 * Geometry 컬럼명 - geom
	 */
	private String geomkey = "";
	/**
	 * 공간정보타입(Point, LineString, Polygon...)
	 */
	private String geomType = "";
	/**
	 * 속성정보 - {key1 : String, key2 : Integer...}
	 */
	private JSONObject attInfo = new JSONObject(); // 속성정보
	/**
	 * 스타일
	 */
	private String style = "";
	/**
	 * 스타일 작업공간
	 */
	private String styleWorkspace = "";
	/**
	 * SLD 스타일 형식
	 */
	private String sld = "";

	/**
	 * Geoserver REST Response 결과를 {@link DTGeoLayer} 클래스로 변환 
	 * @author SG.Lee 
	 * @since 2017. 2 
	 * @param response - 요청결과(XML) 
	 * @return DTGeoLayer  레이어 정보
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static DTGeoLayer build(String response) {
		Element elem = null;
		try {
			elem = buildElement(response);
		} catch (JDOMException | IOException e) {
			elem = null;
		}
		return elem == null ? null : new DTGeoLayer(elem);
	}

	/**
	 * XML 전체 Element를 각각의 Element객체로 빌드
	 * @author SG.LEE
	 * @param response - 요청결과(XML)
	 * @return {@link Element} 단일 {@link Element}
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Element buildElement(String response) throws JDOMException, IOException {
		if (response == null)
			return null;
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(response));
			return doc.getRootElement();
		} catch (JDOMException ex) {
			throw new JDOMException();
		} catch (IOException ex) {
			throw new IOException();
		}
	}

	/**
	 * {@link DTGeoLayer} 생성자
	 * @param layerElem 단일 레이어 {@link Element}
	 */
	@SuppressWarnings("unchecked")
	public DTGeoLayer(Element layerElem) {
		RESTFeatureType featureType = new RESTFeatureType(layerElem);
		this.nativeName = featureType.getNativeName();
		this.lName = featureType.getName();
		this.title = featureType.getTitle();
		this.abstractContent = featureType.getAbstract();
		this.geomType = this.buildGeomType(layerElem);
		this.attInfo = this.buildAttType(layerElem);
		this.dsType = this.buildStoreType(layerElem);
		this.srs = this.buildSRS(layerElem);
		
		try {
			this.nbBox.put("minx", featureType.getNativeBoundingBox().getMinX());
			this.nbBox.put("miny", featureType.getNativeBoundingBox().getMinY());
			this.nbBox.put("maxx", featureType.getNativeBoundingBox().getMaxX());
			this.nbBox.put("maxy", featureType.getNativeBoundingBox().getMaxY());
			this.llbBox.put("minx", featureType.getLatLonBoundingBox().getMinX());
			this.llbBox.put("miny", featureType.getLatLonBoundingBox().getMinY());
			this.llbBox.put("maxx", featureType.getLatLonBoundingBox().getMaxX());
			this.llbBox.put("maxy", featureType.getLatLonBoundingBox().getMaxY());
		} catch (Exception e) {
			// TODO: handle exception
			this.nbBox.put("minx", null);
			this.nbBox.put("miny", null);
			this.nbBox.put("maxx", null);
			this.nbBox.put("maxy", null);
			this.llbBox.put("minx", null);
			this.llbBox.put("miny", null);
			this.llbBox.put("maxx", null);
			this.llbBox.put("maxy", null);
		}
	}

	/**
	 * {@link DTGeoLayer}에 Geometry Type 정보 조회
	 * @author SG.Lee 
	 * @since 2017. 2 
	 * @param layerElem 단일 레이어 {@link Element} 
     * @return String Geometry type(Point, LineString, Polygon...)
	 */
	private String buildGeomType(Element layerElem) {
		String geomType = "";
		Element attsElement = layerElem.getChild("attributes");
		if (attsElement != null) {
			List<Element> list = attsElement.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element attElement = list.get(i);
				String nameAtt = attElement.getChildText("name");
				int flag = nameAtt.indexOf("geom");
				if (flag != -1) {
					String geomAtt = attElement.getChildText("binding");
					int size = geomAtt.length();
					if (size > 28) {
						int pos = geomAtt.lastIndexOf( "." );
						geomType = geomAtt.substring( pos + 1 );
//						geomType = attElement.getChildText("binding").substring(28);
						break;
					} else {
						geomType = "";
						break;
					}
				}
			}
		}
		return geomType;
	}

	/**
	 * {@link DTGeoLayer}에 attInfo 조회 
	 * @author SG.Lee 
	 * @since 2017. 5. 10. 오후 9:40:23
	 * @param layerElem 단일 레이어 {@link Element} 
	 * @return JSONObject 속성정보 {key1 : String, key2 : Integer...}
	 */
	@SuppressWarnings("unchecked")
	private JSONObject buildAttType(Element layerElem) {
		JSONObject object = new JSONObject();
		Element attsElement = layerElem.getChild("attributes");
//		System.out.println(attsElement.toString());
		if (attsElement != null) {
			List<Element> list = attsElement.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element attElement = list.get(i);
				String nameAtt = attElement.getChildText("name");
				String nillable = attElement.getChildText("nillable");
				int flag = nameAtt.indexOf("geom");
				if (flag == -1) {
					String binding = attElement.getChildText("binding");
					JSONObject attContent = new JSONObject();
					int bindingSize = binding.length();
					if(bindingSize>9){
						String type = binding.substring(10);
						if (type.equals("BigDecimal")) {
							type = "Double";
						}
						attContent.put("type", type);
						attContent.put("nillable", nillable);
						object.put(nameAtt, attContent);
					}else{
						if(binding.endsWith("[B")){
							attContent.put("type", "byte[]");
							attContent.put("nillable", nillable);
							object.put(nameAtt, attContent);
						}
					}
				} else {
					this.geomkey = nameAtt;
				}
			}
		}
		return object;
	}

	/**
	 * {@link DTGeoLayer}의 srs 조회
	 * @author SG.Lee 
	 * @since 2017. 2 
	 * @param layerElem 단일 레이어 {@link Element} 
	 * @return String 좌표계(ex. EPSG:4326)
	 */
	@SuppressWarnings("unused")
	private String buildSRS(Element layerElem) {
		return layerElem.getChildText("srs");
	}

	/**
	 * {@link DTGeoLayer}의 저장소 타입 조회
	 * @author SG.Lee 
	 * @since 2017. 2 
	 * @param layerElem layerElem 단일 레이어 {@link Element} 
	 * @return String 저장소 타입(shp or postgis...)
	 */
	@SuppressWarnings("unused")
	private String buildStoreType(Element layerElem) {
		return layerElem.getChild("store").getAttributeValue("class");
	}

	public String getNativeName() {
		return nativeName;
	}

	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstractContent() {
		return abstractContent;
	}

	public void setAbstractContent(String abstractContent) {
		this.abstractContent = abstractContent;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public JSONObject getNbBox() {
		return nbBox;
	}

	public void setNbBox(JSONObject nbBox) {
		this.nbBox = nbBox;
	}

	public String getDsType() {
		return dsType;
	}

	public void setDsType(String dsType) {
		this.dsType = dsType;
	}

	public String getGeomType() {
		return geomType;
	}

	public void setGeomType(String geomType) {
		this.geomType = geomType;
	}

	public JSONObject getAttInfo() {
		return attInfo;
	}

	public void setAttInfo(JSONObject attInfo) {
		this.attInfo = attInfo;
	}

	public JSONObject getLlbBox() {
		return llbBox;
	}

	public void setLlbBox(JSONObject llbBox) {
		this.llbBox = llbBox;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getGeomkey() {
		return geomkey;
	}

	public String getSld() {
		return sld;
	}

	public void setSld(String sld) {
		this.sld = sld;
	}

	public void setGeomkey(String geomkey) {
		this.geomkey = geomkey;
	}
	public String getStyleWorkspace() {
		return styleWorkspace;
	}

	public void setStyleWorkspace(String styleWorkspace) {
		this.styleWorkspace = styleWorkspace;
	}
}
