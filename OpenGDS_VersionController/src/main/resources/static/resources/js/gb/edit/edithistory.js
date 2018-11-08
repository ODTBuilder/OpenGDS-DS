/**
 * 피처 편집 이력을 관리하는 객체
 * 
 * @class gb.edit.FeatureRecord
 * @memberof gb.edit
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String}
 *            obj.id - feature의 고유 ID로 사용될 컬럼명
 * @version 0.01
 * @author 소이준
 * @date 2017.05.18
 */
gb.edit.FeatureRecord = function(obj) {
	this.created = {};
	this.modified = {};
	this.removed = {};
	this.id = obj.id ? obj.id : false;
	this.wfstURL = obj.wfstURL || '';
	this.layerInfoURL = obj.layerInfoURL || '';
}
/**
 * 임시보관 중인 새로운 feature들을 반환한다.
 * 
 * @method gb.edit.FeatureRecord#getCreated
 * @function
 * @return {Object}
 */
gb.edit.FeatureRecord.prototype.getCreated = function() {
	return this.created;
};
/**
 * 임시보관 중인 변경한 feature들을 반환한다.
 * 
 * @method gb.edit.FeatureRecord#getModified
 * @function
 * @return {Object}
 */
gb.edit.FeatureRecord.prototype.getModified = function() {
	return this.modified;
};
/**
 * 임시보관 중인 삭제한 feature들을 반환한다.
 * 
 * @method gb.edit.FeatureRecord#getRemoved
 * @function
 * @return {Object}
 */
gb.edit.FeatureRecord.prototype.getRemoved = function() {
	return this.removed;
};
/**
 * 임시보관 중인 feature의 목록을 삭제한다.
 * 
 * @method gb.edit.FeatureRecord#clearAll
 * @function
 */
gb.edit.FeatureRecord.prototype.clearAll = function() {
	this.created = {};
	this.modified = {};
	this.removed = {};
};
/**
 * 임시보관 중인 새로운 feature의 목록을 삭제한다.
 * 
 * @method gb.edit.FeatureRecord#clearCreated
 * @function
 */
gb.edit.FeatureRecord.prototype.clearCreated = function() {
	this.created = {};
};
/**
 * 임시보관 중인 변경한 feature의 목록을 삭제한다.
 * 
 * @method gb.edit.FeatureRecord#clearCreated
 * @function
 */
gb.edit.FeatureRecord.prototype.clearModified = function() {
	this.modified = {};
};
/**
 * 임시보관 중인 삭제한 feature의 목록을 삭제한다.
 * 
 * @method gb.edit.FeatureRecord#clearCreated
 * @function
 */
gb.edit.FeatureRecord.prototype.clearRemoved = function() {
	this.removed = {};
};
/**
 * 레이어에 해당하는 임시보관 중인 편집이력이 있는지 확인한다.
 * 
 * @method gb.edit.FeatureRecord#isEditing
 * @function
 * @param {ol.layer.Base}
 *            layer - 편집이력을 확인할 layer 객체
 * @return {Boolean} 해당 레이어의 편집이력 존재 여부
 */
gb.edit.FeatureRecord.prototype.isEditing = function(layer) {
	var result = false;
	var c = this.getCreated();
	var ckeys = Object.keys(c);
	for (var i = 0; i < ckeys.length; i++) {
		if (layer.get("id") === ckeys[i]) {
			result = true;
			return result;
		}
	}
	var m = this.getModified();
	var mkeys = Object.keys(m);
	for (var i = 0; i < mkeys.length; i++) {
		if (layer.get("id") === mkeys[i]) {
			result = true;
			return result;
		}
	}
	var r = this.getRemoved();
	var rkeys = Object.keys(r);
	for (var i = 0; i < rkeys.length; i++) {
		if (layer.get("id") === rkeys[i]) {
			result = true;
			return result;
		}
	}
	return result;
};
/**
 * 해당 feature가 삭제되었는지 임시보관 목록에서 조회한다.
 * 
 * @method gb.edit.FeatureRecord#isRemoved
 * @function
 * @param {ol.layer.Base}
 *            layer - 편집이력(삭제)에서 확인할 layer 객체
 * @param {ol.Feature}
 *            feature - 편집이력(삭제)에서 확인할 feature 객체
 * @return {Boolean} 해당 feature의 편집이력(삭제) 존재 여부
 */
gb.edit.FeatureRecord.prototype.isRemoved = function(layer, feature) {
	var isRemoved = false;
	var lid;
	if (layer instanceof ol.layer.Base) {
		lid = layer.get("id");
	} else if (typeof layer === "string") {
		lid = layer;
	}
	if (this.removed.hasOwnProperty(lid)) {
		if (this.removed[layer.get("id")].hasOwnProperty(this.id ? feature.get(this.id) : feature.getId())) {
			isRemoved = true;
		}
	}
	return isRemoved;
};
/**
 * 새로 그린 feature를 편집이력에 임시저장한다.
 * 
 * @method gb.edit.FeatureRecord#create
 * @function
 * @param {ol.layer.Base}
 *            layer - 편집이력에 임시저장할 layer 객체
 * @param {ol.Feature}
 *            feature - 편집이력에 임시저장할 feature 객체
 */
gb.edit.FeatureRecord.prototype.create = function(layer, feature) {
	var id = layer.get("id");
	if(!id){
		return;
	}
	if (!this.created[id]) {
		this.created[id] = {};
		this.requestLayerInfo(id.split(":")[0], id.split(":")[1], id.split(":")[3], this.created[id]);
	}
	this.created[id][feature.getId()] = feature;
	// console.log(this.created);
}
/**
 * 삭제한 feature를 편집이력에 임시저장한다.
 * 
 * @method gb.edit.FeatureRecord#remove
 * @function
 * @param {ol.layer.Base}
 *            layer - 편집이력에 임시저장할 layer 객체
 * @param {ol.Feature}
 *            feature - 편집이력에 임시저장할 feature 객체
 */
gb.edit.FeatureRecord.prototype.remove = function(layer, feature) {
	var id = layer.get("id");
	if (!this.removed[id]) {
		this.removed[id] = {};
		this.requestLayerInfo(id.split(":")[0], id.split(":")[1], id.split(":")[3], this.removed[id]);
	}
	if (feature.getId().search(".new") !== -1) {
		var keys = Object.keys(this.created[id]);
		for (var i = 0; i < keys.length; i++) {
			if (this.created[id][keys[i]].getId() === feature.getId()) {
				delete this.created[id][keys[i]];
				break;
			}
		}
	} else {
		this.removed[id][this.id ? feature.get(this.id) : feature.getId()] = feature;
		if (this.modified.hasOwnProperty(id)) {
			if (this.modified[id].hasOwnProperty(this.id ? feature.get(this.id) : feature.getId())) {
				delete this.modified[id][this.id ? feature.get(this.id) : feature.getId()];
			}
		}
	}
	// console.log(this.removed);
	// console.log(this.created);
	// console.log(this.modified);
}
/**
 * layer ID를 통해 해당 레이어의 편집이력을 모두 삭제한다.
 * 
 * @method gb.edit.FeatureRecord#removeByLayer
 * @function
 * @param {String}
 *            layerId - 삭제할 Layer의 ID
 */
gb.edit.FeatureRecord.prototype.removeByLayer = function(layerId) {
	if (this.removed.hasOwnProperty(layerId)) {
		delete this.removed[layerId];
	}
	if (this.created.hasOwnProperty(layerId)) {
		delete this.created[layerId];
	}
	if (this.modified.hasOwnProperty(layerId)) {
		delete this.modified[layerId];
	}
	// console.log(this.removed);
	// console.log(this.created);
	// console.log(this.modified);
}
/**
 * 변경한 feature를 편집이력에 임시저장한다.
 * 
 * @method gb.edit.FeatureRecord#update
 * @function
 * @param {ol.layer.Base}
 *            layer - 편집이력에 임시저장할 layer 객체
 * @param {ol.Feature}
 *            feature - 편집이력에 임시저장할 feature 객체
 */
gb.edit.FeatureRecord.prototype.update = function(layer, feature) {
	var id = layer.get("id");
	if (!this.modified) {
		this.modified = {};
	}
	if(!feature.getId()){
		return;
	}
	if (feature.getId().search(".new") !== -1) {
		this.created[id][feature.getId()] = feature;
	} else {
		if (!this.modified[id]) {
			this.modified[id] = {};
			this.requestLayerInfo(id.split(":")[0], id.split(":")[1], id.split(":")[3], this.modified[id]);
		}
		this.modified[id][this.id ? feature.get(this.id) : feature.getId()] = feature;
	}
}

/**
 * Layer의 Geometry Key 값을 요청하여 저장한다.
 * 
 * @method gb.edit.FeatureRecord#requestLayerInfo
 * @function
 * @param {String}
 *            serverName - Geoserver name
 * @param {String}
 *            workspace - Workspace name
 * @param {String}
 *            layer - Layer name
 * @param {Object}
 *            object - Geometry Key 값을 저장할 객체
 */
gb.edit.FeatureRecord.prototype.requestLayerInfo = function(serverName, workspace, layer, object){
	var obj = object;
	var a = {
		serverName: serverName,
		workspace: workspace,
		geoLayerList: [layer]
	};
	
	$.ajax({
		method : "POST",
		url: this.layerInfoURL,
		data: JSON.stringify(a),
		contentType: 'application/json; charset=utf-8',
		success: function(data, textStatus, jqXHR) {
			var geomKey = data[0].geomkey;
			obj.geomKey = geomKey;
		},
		error: function(e) {
			var errorMsg = e? (e.status + ' ' + e.statusText) : "";
			console.log(errorMsg);
		},
	});
}

/**
 * 임시저장중인 편집이력을 JSON 형태로 반환한다.
 * 
 * @method gb.edit.FeatureRecord#getStructure
 * @function
 * @return {Object} 현재 임시저장중인 편집이력
 */
gb.edit.FeatureRecord.prototype.getStructure = function() {
	var format = new ol.format.GeoJSON();
	var obj = {};
	var cLayers = Object.keys(this.created);
	for (var i = 0; i < cLayers.length; i++) {
		if (Object.keys(this.created[cLayers[i]]).length < 1) {
			continue;
		}
		obj[cLayers[i]] = {};
	}

	for (var j = 0; j < cLayers.length; j++) {
		var names = Object.keys(this.created[cLayers[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[cLayers[j]].hasOwnProperty("created")) {
				obj[cLayers[j]]["created"] = {};
			}
			if (!obj[cLayers[j]]["created"].hasOwnProperty("features")) {
				obj[cLayers[j]]["created"]["features"] = [];
			}
			obj[cLayers[j]]["created"]["features"].push(format.writeFeature(this.created[cLayers[j]][names[k]]));
		}
	}

	var mLayers = Object.keys(this.modified);
	for (var i = 0; i < mLayers.length; i++) {
		if (Object.keys(this.modified[mLayers[i]]).length < 1 || obj.hasOwnProperty(mLayers[i])) {
			continue;
		}
		obj[mLayers[i]] = {};
	}

	for (var j = 0; j < mLayers.length; j++) {
		var names = Object.keys(this.modified[mLayers[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[mLayers[j]].hasOwnProperty("modified")) {
				obj[mLayers[j]]["modified"] = {};
			}
			if (!obj[mLayers[j]]["modified"].hasOwnProperty("features")) {
				obj[mLayers[j]]["modified"]["features"] = [];
			}
			var clone = this.modified[mLayers[j]][names[k]].clone();
			if (this.id) {
				clone.setId(this.modified[mLayers[j]][names[k]].get(this.id));
			}
			obj[mLayers[j]]["modified"]["features"].push(format.writeFeature(clone));
		}
	}

	var rLayers = Object.keys(this.removed);
	for (var i = 0; i < rLayers.length; i++) {
		if (Object.keys(this.removed[rLayers[i]]).length < 1 || obj.hasOwnProperty(rLayers[i])) {
			continue;
		}
		obj[rLayers[i]] = {};
	}

	for (var j = 0; j < rLayers.length; j++) {
		var names = Object.keys(this.removed[rLayers[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[rLayers[j]].hasOwnProperty("removed")) {
				obj[rLayers[j]]["removed"] = [];
			}
			obj[rLayers[j]]["removed"].push(names[k]);
		}
	}
	return obj;
}
/**
 * 선택한 레이어들의 임시저장중인 편집이력을 JSON 형태로 반환한다.
 * 
 * @method gb.edit.FeatureRecord#getPartStructure
 * @function
 * @param {String[]}
 *            bringLayer - 레이어 ID들의 배열
 * @return {Object} 현재 임시저장중인 편집이력
 */
gb.edit.FeatureRecord.prototype.getPartStructure = function(bringLayer) {
	if (!Array.isArray(bringLayer)) {
		console.error("type error");
		return;
	}
	var format = new ol.format.GeoJSON();
	var obj = {};

	var cLayers = Object.keys(this.created);
	var bringC = [];
	for (var i = 0; i < cLayers.length; i++) {
		if (bringLayer.indexOf(cLayers[i]) !== -1) {
			bringC.push(cLayers[i]);
		} else {
			continue;
		}
		if (Object.keys(this.created[cLayers[i]]).length < 1) {
			continue;
		}
		obj[cLayers[i]] = {};
	}

	for (var j = 0; j < bringC.length; j++) {
		var names = Object.keys(this.created[bringC[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[bringC[j]].hasOwnProperty("created")) {
				obj[bringC[j]]["created"] = {};
			}
			if (!obj[bringC[j]]["created"].hasOwnProperty("features")) {
				obj[bringC[j]]["created"]["features"] = [];
			}
			obj[bringC[j]]["created"]["features"].push(format.writeFeature(this.created[bringC[j]][names[k]]));
		}
	}

	var mLayers = Object.keys(this.modified);
	var bringM = [];
	for (var i = 0; i < mLayers.length; i++) {
		if (bringLayer.indexOf(mLayers[i]) !== -1) {
			bringM.push(mLayers[i]);
		} else {
			continue;
		}
		if (Object.keys(this.modified[mLayers[i]]).length < 1 || obj.hasOwnProperty(mLayers[i])) {
			continue;
		}
		obj[mLayers[i]] = {};
	}

	for (var j = 0; j < bringM.length; j++) {
		var names = Object.keys(this.modified[bringM[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[bringM[j]].hasOwnProperty("modified")) {
				obj[bringM[j]]["modified"] = {};
			}
			if (!obj[bringM[j]]["modified"].hasOwnProperty("features")) {
				obj[bringM[j]]["modified"]["features"] = [];
			}
			var clone = this.modified[bringM[j]][names[k]].clone();
			if (this.id) {
				clone.setId(this.modified[bringM[j]][names[k]].get(this.id));
			}
			obj[bringM[j]]["modified"]["features"].push(format.writeFeature(clone));
		}
	}

	var rLayers = Object.keys(this.removed);
	var bringR = [];
	for (var i = 0; i < rLayers.length; i++) {
		if (bringLayer.indexOf(rLayers[i]) !== -1) {
			bringR.push(rLayers[i]);
		} else {
			continue;
		}
		if (Object.keys(this.removed[rLayers[i]]).length < 1 || obj.hasOwnProperty(rLayers[i])) {
			continue;
		}
		obj[rLayers[i]] = {};
	}

	for (var j = 0; j < bringR.length; j++) {
		var names = Object.keys(this.removed[bringR[j]]);
		for (var k = 0; k < names.length; k++) {
			if (!obj[bringR[j]].hasOwnProperty("removed")) {
				obj[bringR[j]]["removed"] = [];
			}
			obj[bringR[j]]["removed"].push(names[k]);
		}
	}
	return obj;
}

gb.edit.FeatureRecord.prototype.deleteFeatureCreated = function(layerId, featureId) {
	var feature = undefined;
	if(!!this.created[layerId]){
		if(this.created[layerId][featureId] instanceof ol.Feature){
			feature = this.created[layerId][featureId];
			delete this.created[layerId][featureId];
		}
	}
	return feature;
};
gb.edit.FeatureRecord.prototype.deleteFeatureModified = function(layerId, featureId) {
	var feature = undefined;
	if(!!this.modified[layerId]){
		if(this.modified[layerId][featureId] instanceof ol.Feature){
			feature = this.modified[layerId][featureId];
			delete this.modified[layerId][featureId];
		}
	}
	return feature;
};
gb.edit.FeatureRecord.prototype.deleteFeatureRemoved = function(layerId, featureId) {
	var feature = undefined;
	if(!!this.removed[layerId]){
		if(this.removed[layerId][featureId] instanceof ol.Feature){
			feature = this.removed[layerId][featureId];
			delete this.removed[layerId][featureId];
		}
	}
	return feature;
};

gb.edit.FeatureRecord.prototype.sendWFSTTransaction = function(editTool){
	
	var layers = {};
	var format = new ol.format.WFS();
	var edit = editTool;
	
	for(let layer in this.created){
		for(let feature in this.created[layer]){
			if(feature === "geomKey"){
				continue;
			}
			if(!layers[layer]){
				layers[layer] = {};
				layers[layer].created = [];
				layers[layer].modified = null;
				layers[layer].removed = null;
			} else {
				if(layers[layer].created === null){
					layers[layer].created = [];
				}
			}
			
			var geomKey = this.created[layer][feature].getGeometryName();
			this.created[layer][feature].setGeometryName(this.created[layer].geomKey);
			this.created[layer][feature].set(this.created[layer].geomKey, this.created[layer][feature].get(geomKey));
			this.created[layer][feature].unset(geomKey);
			layers[layer].created.push(this.created[layer][feature]);
		}
	}
	for(let layer in this.modified){
		for(let feature in this.modified[layer]){
			if(feature === "geomKey"){
				continue;
			}
			if(!layers[layer]){
				layers[layer] = {};
				layers[layer].created = null;
				layers[layer].modified = [];
				layers[layer].removed = null;
			} else {
				if(layers[layer].modified === null){
					layers[layer].modified = [];
				}
			}
			var geomKey = this.modified[layer][feature].getGeometryName();
			this.modified[layer][feature].setGeometryName(this.modified[layer].geomKey);
			this.modified[layer][feature].set(this.modified[layer].geomKey, this.modified[layer][feature].get(geomKey));
			this.modified[layer][feature].unset(geomKey);
			layers[layer].modified.push(this.modified[layer][feature]);
		}
	}
	for(let layer in this.removed){
		for(let feature in this.removed[layer]){
			if(feature === "geomKey"){
				continue;
			}
			if(!layers[layer]){
				layers[layer] = {};
				layers[layer].created = null;
				layers[layer].modified = null;
				layers[layer].removed = [];
			} else {
				if(layers[layer].removed === null){
					layers[layer].removed = [];
				}
			}
			var geomKey = this.removed[layer][feature].getGeometryName();
			this.removed[layer][feature].setGeometryName(this.removed[layer].geomKey);
			this.removed[layer][feature].set(this.removed[layer].geomKey, this.removed[layer][feature].get(geomKey));
			this.removed[layer][feature].unset(geomKey);
			layers[layer].removed.push(this.removed[layer][feature]);
		}
	}
	
	var geoserver, workspace, repo, layername, split, node;
	for(let layer in layers){
		split = layer.split(":");
		geoserver = layer.split(":")[0];
		workspace = layer.split(":")[1];
		repo = layer.split(":")[2];
		layername = "";
		for(let i in split){
			if(i > 2){
				layername += "_" + split[i];
			}
		}
		layername = layername.substring(1);
		
		node = format.writeTransaction(layers[layer].created, layers[layer].modified, layers[layer].removed, {
			"featureNS": workspace,
			"featurePrefix": workspace,
			"featureType": layername,
			"version": "1.0.0"
		});
		
		var param = {
			"serverName": geoserver,
			"workspace": workspace,
			"wfstXml": '<?xml version="1.0" encoding="utf-8"?>'+new XMLSerializer().serializeToString(node)
		};
		$.ajax({
			type: "POST",
			url: this.wfstURL,
			data: JSON.stringify(param),
			contentType: 'application/json; charset=utf-8',
			success: function(data) {
				var result = format.readTransactionResponse(data);
				edit.refreshTileLayer();
			},
			error: function(e) {
				var errorMsg = e? (e.status + ' ' + e.statusText) : "";
				console.log(errorMsg);
			},
			context: this
		});
	}
}