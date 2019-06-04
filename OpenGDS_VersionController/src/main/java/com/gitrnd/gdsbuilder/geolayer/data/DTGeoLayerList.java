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

package com.gitrnd.gdsbuilder.geolayer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import it.geosolutions.geoserver.rest.decoder.utils.JDOMBuilder;


/**
 * {@link DTGeoLayer} 리스트(Geoserver 레이어 정보 리스트)
 * @author SG.Lee
 * @Since 2016.08
 * */
public class DTGeoLayerList extends ArrayList<DTGeoLayer> implements Serializable  {
	
	
	private static final long serialVersionUID = -4772221710449542370L;

	
	/* *
	 * Geoserver REST Response 결과를 {@link DTGeoLayerList} 클래스로 변환 
	 * @author SG.Lee
	 * @Since 2017. 2
	 * @param responses 요청결과(XML)
	 * @return DTGeoLayerList 레이어 정보 리스트
	 * @throws
	 * */
	public static DTGeoLayerList build(List<String> responses){
		List<Element> elements = new ArrayList<Element>();
		for(String response : responses){
		Element elem = JDOMBuilder.buildElement(response);
		elements.add(elem);
		}
		return elements.size() == 0 ? null : new DTGeoLayerList(elements);
	}
	
	/**
	 * 기본 생성자
	 * @author SG.LEE
	 */
	public DTGeoLayerList(){}
	
	/**
	 * {@link DTGeoLayerList} 생성자
	 * @param groupLayerElem 레이어 리스트 {@link Element}
	 */
	public DTGeoLayerList(List<Element> groupLayerElem){
		for(Element element : groupLayerElem){
			DTGeoLayer groupLayer = new DTGeoLayer(element);
			super.add(groupLayer);
		}
	}
	
	
	/**
	 * {@link DTGeoLayerList}에서 해당 레이어이름에 대한 그룹 {@link DTGeoLayer}를 반환
	 * @author SG.Lee
	 * @Since 2017. 5. 10. 오후 10:08:58
	 * @param layerName - 레이어 이름
	 * @return DTGeoLayer 레이어 정보
	 * @throws
	 * */
	public DTGeoLayer getDTGeoGroupLayer(String layerName){
		for(DTGeoLayer layer : this){
			if(layer.equals(layerName)){
				return layer; 
			}
		}
		return null;
	}
}
