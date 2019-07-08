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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import it.geosolutions.geoserver.rest.decoder.utils.JDOMBuilder;

/**
 * {@link DTGeoGroupLayer} 리스트(Geoserver 레이어 정보 리스트)
 * @author SG.Lee
 * @since 2017. 2
 * */
public class DTGeoGroupLayerList extends ArrayList<DTGeoGroupLayer> implements Serializable{

	private static final long serialVersionUID = -8414210792543933648L;

	/**
	 * Geoserver REST Response 결과를 {@link DTGeoGroupLayerList} 클래스로 변환 
	 * @author SG.LEE
	 * @since 2017. 2
	 * @param responses
	 * @return DTGeoGroupLayerList
	 */
	public DTGeoGroupLayerList build(List<String> responses){
		List<Element> elements = new ArrayList<Element>();
		for(String response : responses){
		Element elem = JDOMBuilder.buildElement(response);
		elements.add(elem);
		}
		return elements.size() == 0 ? null : new DTGeoGroupLayerList(elements);
	}
	
	public DTGeoGroupLayerList(){};
	
	/**
	 * {@link DTGeoGroupLayerList} 생성자
	 * @param groupLayerElem Group Layer 리스트 {@link Element}
	 */
	public DTGeoGroupLayerList(List<Element> groupLayerElem){
		for(Element element : groupLayerElem){
			DTGeoGroupLayer groupLayer = new DTGeoGroupLayer(element);
			super.add(groupLayer);
		}
	}
	
	
	/**
	 * {@link DTGeoGroupLayer}에서 해당 그룹레이어명에 대한 {@link DTGeoGroupLayer}를 반환
	 * @author SG.Lee
	 * @since 2017. 5. 10. 오후 10:08:58
	 * @param groupName - 그룹이름
	 * @return DTGeoGroupLayer
	 * @throws
	 * */
	public DTGeoGroupLayer getDTGeoGroupLayer(String groupName){
		for(DTGeoGroupLayer groupLayer : this){
			if(groupLayer.equals(groupName)){
				return groupLayer; 
			}
		}
		return null;
	}
}
