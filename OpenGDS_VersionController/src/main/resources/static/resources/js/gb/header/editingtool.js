var gb;
if (!gb)
	gb = {};
if (!gb.header)
	gb.header = {};

/**
 * 레이어 편집 기능을 정의한다. 필수 라이브러리: jQuery, fontawesome, openlayers, gb.header.Base
 * 
 * @class gb.edit.EditingTool
 * @memberof gb.edit
 * @param {gb.versioning.Feature}
 *            obj.versioning - 피처별 버전 관리 객체
 * @author hochul.kim
 * @date 2018. 06.04
 * @version 0.01
 * @constructor
 */
gb.header.EditingTool = function(obj) {
	gb.header.Base.call(this, obj);
	var options = obj ? obj : {};
	this.map = options.map ? options.map : undefined;
	console.log(this.map.getView().getProjection());
	this.featureRecord = options.featureRecord ? options.featureRecord : undefined;
	this.otree = options.otree ? options.otree : undefined;
	this.treeElement = this.otree ? this.otree.getJSTreeElement() : undefined;
	this.selected = options.selected ? options.selected : undefined;
	this.layerInfo = options.layerInfo ? options.layerInfo : undefined;
	this.imageTile = options.imageTile ? options.imageTile : undefined;
	this.getFeature = options.getFeature ? options.getFeature : undefined;
	this.getFeatureInfo = options.getFeatureInfo ? options.getFeatureInfo : undefined;
	this.versioningFeature = options.versioning instanceof gb.versioning.Feature ? options.versioning : undefined; 
	this.selectedSource = undefined;
	this.selectSources = new ol.Collection();
	this.layer = undefined;

	// hochul
	this.vectorSourcesOfServer_ = {};
	this.customVector_ = {};
	this.copyPaste_ = undefined;
	this.wfsURL = options.wfsURL;
	this.otree.setEditingTool(this);


	this.snapWMS = [];
	this.snapSource = new ol.source.Vector();

	this.snapVector = new ol.Collection();

	this.features = new ol.Collection();
	this.tempSource = new ol.source.Vector({
		features : this.features
	});
	this.tempVector = new ol.layer.Vector({
		source : this.tempSource
	});

	this.managed = new ol.layer.Vector({
		source : this.tempSource
	});
	this.managed.set("name", "temp_vector");
	this.managed.set("id", "temp_vector");

	this.styles = [ new ol.style.Style({
		stroke : new ol.style.Stroke({
			color : 'rgba(0,153,255,1)',
			width : 2
		}),
		fill : new ol.style.Fill({
			color : 'rgba(255, 255, 255, 0.5)'
		})
	}), new ol.style.Style({
		image : new ol.style.Circle({
			radius : 10,
			fill : new ol.style.Fill({
				color : 'rgba(0,153,255,0.4)'
			})
		})
	}) ];

	this.highlightStyles1 = [ new ol.style.Style({
		stroke : new ol.style.Stroke({
			color : 'rgba(255,0,0,1)',
			width : 2
		})
	}), new ol.style.Style({
		image : new ol.style.Circle({
			radius : 10,
			stroke : new ol.style.Stroke({
				color : 'rgba(255,0,0,1)',
				width : 2
			})
		})
	}) ];

	this.highlightStyles2 = [ new ol.style.Style({
		stroke : new ol.style.Stroke({
			color : 'rgba(0, 0, 255, 1)',
			width : 2
		})
	}), new ol.style.Style({
		image : new ol.style.Circle({
			radius : 10,
			stroke : new ol.style.Stroke({
				color : 'rgba(0, 0, 255, 1)',
				width : 2
			})
		})
	}) ];

	this.selectedStyles = [ new ol.style.Style({
		stroke : new ol.style.Stroke({
			color : 'rgba(0,153,255,1)',
			width : 2
		}),
		fill : new ol.style.Fill({
			color : 'rgba(255, 255, 255, 0.5)'
		})
	}), new ol.style.Style({
		image : new ol.style.Circle({
			radius : 10,
			fill : new ol.style.Fill({
				color : 'rgba(0,153,255,0.4)'
			})
		}),
		geometry : function(feature) {

			var coordinates;
			var geom;

			if (feature.getGeometry() instanceof ol.geom.MultiPolygon) {
				coordinates = feature.getGeometry().getCoordinates()[0][0];
				geom = new ol.geom.MultiPoint(coordinates);
			} else if (feature.getGeometry() instanceof ol.geom.Polygon) {
				coordinates = feature.getGeometry().getCoordinates()[0];
				geom = new ol.geom.MultiPoint(coordinates);
			} else if (feature.getGeometry() instanceof ol.geom.MultiLineString) {
				coordinates = feature.getGeometry().getCoordinates()[0];
				geom = new ol.geom.MultiPoint(coordinates);
			} else if (feature.getGeometry() instanceof ol.geom.LineString) {
				coordinates = feature.getGeometry().getCoordinates();
				geom = new ol.geom.MultiPoint(coordinates);
			} else if (feature.getGeometry() instanceof ol.geom.MultiPoint) {
				coordinates = feature.getGeometry().getCoordinates();
				geom = new ol.geom.MultiPoint(coordinates);
			} else if (feature.getGeometry() instanceof ol.geom.Point) {
				coordinates = [ feature.getGeometry().getCoordinates() ];
				geom = new ol.geom.MultiPoint(coordinates);
			}

			return geom;
		}
	}) ];

	this.interval = undefined;
	this.count = 1;

	this.btn = {
			selectBtn : undefined,
			drawBtn : undefined,
			moveBtn : undefined,
			rotateBtn : undefined,
			modiBtn : undefined,
			delBtn : undefined
	};
	this.isOn = {
			select : false,
			draw : false,
			move : false,
			remove : false,
			modify : false,
			rotate : false,
			snap : false
	};
	this.interaction = {
			select : undefined,
			dragbox : undefined,
			draw : undefined,
			updateDraw : undefined,
			move : undefined,
			rotate : undefined,
			modify : undefined,
			snap : undefined,
			remove : undefined
	};
	this.customInteractions = [];

	var that = this;

	/**
	 * default list
	 */
	var defaultList = [
		{
			content: "draw",
			icon: "fas fa-pencil-alt fa-lg",
			clickEvent: function(){
				console.log("draw");
				that.draw();
			},
			color: ""
		},
		{
			content: "move",
			icon: "fas fa-arrows-alt fa-lg",
			clickEvent: function(){
				console.log("move");
				that.move();
			},
			color: ""
		},
		{
			content: "rotate",
			icon: "fas fa-object-ungroup fa-lg",
			clickEvent: function(){
				console.log("rotate");
				that.rotate();
			},
			color: ""
		},
		{
			content: "modify",
			icon: "fas fa-wrench fa-lg",
			clickEvent: function(){
				console.log("modify");
				that.modify();
			},
			color: ""
		},
		{
			content: "delete",
			icon: "fas fa-eraser fa-lg",
			clickEvent: function(){
				console.log("remove");
				that.remove();
			},
			color: ""
		},
		{
			content: "undo",
			icon: "fas fa-undo-alt fa-lg",
			clickEvent: function(){
				gb.undo.undo();
				console.log("execute undo");
			}
		},
		{
			content: "redo",
			icon: "fas fa-redo-alt fa-lg",
			clickEvent: function(){
				gb.undo.redo();
				console.log("execute redo");
			}
		}
		];

	// header element 생성
	this.createContent(defaultList);
	if(!this.isDisplay){
		this.closeTool();
	}

	// this.createContent() 함수 실행 이후 this.contentList 배열안에 content list들의 <a>
	// tag element가 저장됨
	// gb.header.Base 함수 참조
	var eventList = this.contentList;
	var match = {
			"select": "selectBtn",
			"move": "moveBtn",
			"draw": "drawBtn",
			"transform": "rotateBtn",
			"modify": "modiBtn",
			"delete": "delBtn"
	}
	for(var i in eventList){
		if(eventList[i].text()){
			this.btn[match[eventList[i].text().toLowerCase()]] = eventList[i];
		}
	}

	$("body").append(this.panel);

	var fth1 = $("<th>").text("Index");
	var fth2 = $("<th>").text("Description");
	var ftr = $("<tr>").append(fth1).append(fth2);
	var fhd = $("<thead>").append(ftr);
	this.featureTB = $("<tbody>");
	var ftb = $("<table>").addClass("gb-table").append(fhd).append(this.featureTB);

	this.featurePop = new gb.panel.Base({
		"width" : "240px",
		"positionX" : 30,
		"positionY" : 5,
		"autoOpen" : false,
		"body" : ftb
	});

	$(this.featurePop.getPanel()).find(".gb-panel-body").css({
		"max-height" : "300px",
		"overflow-y" : "auto"
	});
	var ath1 = $("<th>").text("Name");
	var ath2 = $("<th>").text("Value");
	var atr = $("<tr>").append(ath1).append(ath2);
	var ahd = $("<thead>").append(atr);
	this.attrTB = $("<tbody>");
	var atb = $("<table>").addClass("gb-table").append(ahd).append(this.attrTB);
	this.attrPop = new gb.panel.Base({
		"width" : "300px",
		"positionX" : 384,
		"positionY" : 150,
		"autoOpen" : false,
		"body" : atb
	});

	this.map.on('postcompose', function(evt) {
		that.map.getInteractions().forEach(function(interaction) {
			if (interaction instanceof gb.interaction.MultiTransform) {
				if (interaction.getFeatures().getLength() && interaction.getActive()) {
					interaction.drawMbr(evt);
				}
			}
		});
	});

	var preventReload = false;
	this.map.on('moveend', function(evt){
		that.loadSnappingLayer(this.getView().calculateExtent(this.getSize()));

		var map = evt.target;
		var view = map.getView();
		var extent = view.calculateExtent();
		var zoom = view.getZoom();

		if(that.getActiveTool()){
			if(zoom > 11 && !preventReload){
				that.loadWFS_();
				that.displayEditZoomHint(false);
				preventReload = true;
			} else if(zoom <= 11 && preventReload) {
				that.setVisibleWFS(false);
				that.displayEditZoomHint(true);
				preventReload = false;
			}
		}
	});

	this.treeElement.on("changed.jstreeol3", function(e, data){
		if(that.getActiveTool()){
			if(that.map.getView().getZoom() > 11){
				if(data.selected.length === 1){
					that.select(that.updateSelected(data.selected[0]));
				}
			}
		}
	});

	this.treeElement.on("delete_node.jstreeol3", function(e, data){
		var id = data.node.id;
		var source = that.getVectorSourceOfServer(id);
		if(!!source){
			source.get("git").tempLayer.setMap(null);
			source.clear();
			delete that.vectorSourcesOfServer_[id];
		}
		that.refreshTileLayer();
	});

	// SOYIJUN
	if (this.getVersioningFeature() !== undefined) {
		console.log(this.ulTagRight);
		var iTag = $("<i>").addClass("fas").addClass("fa-history").attr("aria-hidden", "true").css(this.iStyle);
		var aTag = $("<a>").attr("href", "#").append(iTag).append("Changes").css(this.aStyle).click(function(){
			that.toggleFeatureHistoryModal();
		});
		aTag.hover(function(){
			if(!$(this).hasClass("active")){
				$(this).css("color", "#23527c");
				$(this).css("text-decoration", "none");
			}
		},function(){
			if(!$(this).hasClass("active")){
				$(this).css("color", "rgb(85, 85, 85)");
			}
		});
		var liTag = $("<li>").append(aTag).css(this.liStyle);
		liTag.css("padding-left", "0").css("padding-right", "20px");
		$(this.ulTagRight).append(liTag);
	}
};
gb.header.EditingTool.prototype = Object.create(gb.header.Base.prototype);
gb.header.EditingTool.prototype.constructor = gb.header.EditingTool;

/**
 * 피처 변경 이력창을 연다
 * 
 * @method toggleFeatureHistoryModal
 */
gb.header.EditingTool.prototype.toggleFeatureHistoryModal = function(feature) {
	var vfeature = this.getVersioningFeature();
	if ($(vfeature.getPanel().getPanel()).css("display") !== "none") {
		vfeature.close();
	} else {
		var layers = $(this.treeElement).jstreeol3("get_selected_layer");
		var feature = feature instanceof ol.Feature ? feature : this.interaction.select.getFeatures().getLength() === 1 ? this.interaction.select.getFeatures().item(0) : undefined;
		if (layers.length === 1 && feature) {
			var layer = layers[0];
			console.log(layer);
			var git = layer.get("git");
			console.log(git);
			var geoserver = git.geoserver;
			console.log(geoserver);
			var repo = git.geogigRepo;
			console.log(repo);
			var branch = git.geogigBranch;
			console.log(branch);	
			var layerName = layer.get("name");
			console.log(layerName);
			console.log(feature);
			var path = layerName+"/"+feature.getId();
			console.log(path);
// vfeature.open();

			if (vfeature !== undefined && branch === "master") {
				if (geoserver+"/"+repo+"/"+path !== vfeature.getIDString()) {

					vfeature.setServer(geoserver);
					vfeature.setRepo(repo);
					vfeature.setPath(path);
					vfeature.setFeature(feature);
					vfeature.refresh();

				} else {
					vfeature.refresh();
				}
				vfeature.open();
			}
		}
	}
};

/**
 * 피처 변경 이력창을 업데이트한다
 * 
 * @method updateFeatureHistoryModal
 */
gb.header.EditingTool.prototype.updateFeatureHistoryModal = function(feature) {
	var vfeature = this.getVersioningFeature();
	var layers = $(this.treeElement).jstreeol3("get_selected_layer");
	var feature = feature instanceof ol.Feature ? feature : this.interaction.select.getFeatures().getLength() === 1 ? this.interaction.select.getFeatures().item(0) : undefined;
	if (layers.length === 1 && feature) {
		var layer = layers[0];
		console.log(layer);
		var git = layer.get("git");
		console.log(git);
		var geoserver = git.geoserver;
		console.log(geoserver);
		var repo = git.geogigRepo;
		console.log(repo);
		var branch = git.geogigBranch;
		console.log(branch);	
		var layerName = layer.get("name");
		console.log(layerName);
		console.log(feature);
		var path = layerName+"/"+feature.getId();
		console.log(path);

		if (vfeature !== undefined && branch === "master") {
			vfeature.open();
			if (geoserver+"/"+repo+"/"+path !== vfeature.getIDString()) {
				vfeature.setServer(geoserver);
				vfeature.setRepo(repo);
				vfeature.setPath(path);
				vfeature.setFeature(feature);
				vfeature.refresh();
			} else {
				vfeature.refresh();
			}
		} else {
			if ($(vfeature.getPanel().getPanel()).css("display") !== "none") {
				vfeature.close();	
			}
		}
	}
};

/**
 * gb.versioning.Feature 객체를 반환한다.
 * 
 * @method getVersioningFeature
 * @return {gb.versioning.Feature} 피처 변경 이력 객체
 */
gb.header.EditingTool.prototype.getVersioningFeature = function() {
	return this.versioningFeature;
};

/**
 * 피처목록을 생성한다.
 * 
 * @method setFeatureList_
 */
gb.header.EditingTool.prototype.setFeatureList_ = function() {
	return this.interaction;
};
/**
 * 내부 인터랙션 구조를 반환한다.
 * 
 * @method getInteractions_
 * @return {Mixed Obj} {select : ol.interaction.Select..}
 */
gb.header.EditingTool.prototype.getInteractions_ = function() {
	return this.interaction;
};
/**
 * 내부 인터랙션 하나를 반환한다.
 * 
 * @method getInteraction_
 * @return {Mixed Obj} {select : ol.interaction.Select..}
 */
gb.header.EditingTool.prototype.getInteraction_ = function(key) {
	return this.interaction[key];
};
/**
 * 내부 인터랙션 구조를 설정한다.
 * 
 * @method setInteraction_
 * @param {String}
 *            key - interaction name
 * @param {ol.interaction.Interaction}
 *            val - interaction
 */
gb.header.EditingTool.prototype.setInteraction_ = function(key, val) {
	this.interaction[key] = val;
};

/**
 * 편집중인 레이어를 반환한다.
 * 
 * @method getLayer
 * @return {ol.layer.Base}
 */
gb.header.EditingTool.prototype.getLayer = function() {
	return this.layer;
};

/**
 * 해당 인터랙션을 활성화 시킨다.
 * 
 * @method activeIntrct_
 * @param {String ||
 *            Array<String>} 활성화 시킬 인터랙션 이름
 */
gb.header.EditingTool.prototype.activeIntrct_ = function(intrct) {
	// var that = this;
	// var keys = Object.keys(this.getInteractions_());
	// for (var i = 0; i < keys.length; i++) {
	// if (this.getInteraction_(keys[i])) {
	// this.getInteraction_(keys[i]).setActive(false);
	// }
	// }
	if (Array.isArray(intrct)) {
		for (var j = 0; j < intrct.length; j++) {
			this.getInteraction_(intrct[j]).setActive(true);
			if (intrct[j] === "select" || intrct[j] === "dragbox") {
				this.isOn["select"] = true;
			} else {
				this.isOn[intrct[j]] = true;
			}
		}
	} else if (typeof intrct === "string") {
		this.getInteraction_(intrct).setActive(true);
		if (intrct === "select" || intrct[j] === "dragbox") {
			this.isOn["select"] = true;
		} else {
			this.isOn[intrct] = true;
		}
	}
};
/**
 * 해당 인터랙션을 비활성화 시킨다.
 * 
 * @method deactiveIntrct_
 * @param {String ||
 *            Array<String>} 인터랙션의 이름 또는 인터랙션 이름의 배열
 */
gb.header.EditingTool.prototype.deactiveIntrct_ = function(intrct) {
	var selectInter = true;
	if (Array.isArray(intrct)) {
		for (var j = 0; j < intrct.length; j++) {
			if (!!this.interaction[intrct[j]]) {
				this.interaction[intrct[j]].setActive(false);
			}
			if (intrct[j] === "select" || intrct[j] === "dragbox") {
				this.isOn["select"] = false;
				this.featurePop.close();
				selectInter = false;
			} else {
				this.isOn[intrct[j]] = false;
				// this.map.removeLayer(this.managed);
			}
		}
	} else if (typeof intrct === "string") {
		if (!!this.interaction[intrct]) {
			this.interaction[intrct].setActive(false);
		}
		if (intrct === "select" || intrct === "dragbox") {
			this.isOn["select"] = false;
		} else {
			this.isOn[intrct] = false;
			// this.map.removeLayer(this.managed);
		}

		if (intrct !== "select" && intrct !== "dragbox") {
			for(var i in this.interaction){
				if(!!this.interaction[i]){
					if(this.interaction[i].getActive()){
						selectInter = false;
						break;
					}
				}
			}
		}
	}

	if(selectInter){
		this.getInteraction_("select").setActive(true);
		this.getInteraction_("dragbox").setActive(true);
		this.isOn["select"] = true;
	}

	for(var i in this.customInteractions){
		this.customInteractions[i].setActive(false);
	}

	if(!!this.selectedSource){
		this.map.removeLayer(this.tempVector);
		this.selectedSource.get("git").tempLayer.setMap(this.map);
	}

	// this.map.removeLayer(this.managed);
};
/**
 * 버튼을 누른상태로 만든다
 * 
 * @method activeBtn_
 * @param {String}
 *            button name
 */
gb.header.EditingTool.prototype.activeBtn_ = function(btn) {
	if(!this.btn[btn]){
		return;
	}
	if (!this.btn[btn].hasClass("active")) {
		this.btn[btn].addClass("active");
		this.btn[btn].css("border-bottom", "2px solid #4B5A6A");
		this.btn[btn].css("color", "#23527c");
	}
	var keys = Object.keys(this.btn);
	for (var i = 0; i < keys.length; i++) {
		if (keys[i] !== btn && !!this.btn[keys[i]]) {
			if (this.btn[keys[i]].hasClass("active")) {
				this.btn[keys[i]].removeClass("active");
				this.btn[keys[i]].css("border-bottom", "none");
				this.btn[keys[i]].css("color", "rgb(85, 85, 85)");
			}
		}
	}
};
/**
 * 버튼을 안 누른 상태로 만든다
 * 
 * @method deactiveBtn_
 * @param {String}
 *            button name
 */
gb.header.EditingTool.prototype.deactiveBtn_ = function(btn) {
	if (this.btn[btn].hasClass("active")) {
		this.btn[btn].removeClass("active");
		this.btn[btn].css("border-bottom", "none");
		this.btn[btn].css("color", "rgb(85, 85, 85)");
	}
};

/**
 * 버튼을 안 누른 상태로 만든다
 * 
 * @method deactiveBtn_
 * @param {String}
 *            button name
 */
gb.header.EditingTool.prototype.deactiveAllBtn_ = function() {
	for(var btn in this.btn){
		if(this.btn[btn] === undefined){
			continue;
		}
		if (this.btn[btn].hasClass("active")) {
			this.btn[btn].removeClass("active");
			this.btn[btn].css("border-bottom", "none");
			this.btn[btn].css("color", "rgb(85, 85, 85)");
		}
	}
};
/**
 * 피처 선택을 활성화 한다
 * 
 * @method select
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.select = function(source) {
	var that = this;
	if(!source){
		return;
	}
	if (this.interaction.select instanceof ol.interaction.Select) {
		this.interaction.select.getFeatures().clear();
	}

	this.map.removeInteraction(this.interaction.select);

	var selectLayers = source.get("git").tempLayer ? [source.get("git").tempLayer] : [];
	this.interaction.select = new ol.interaction.Select({
		layers: selectLayers,
		toggleCondition : ol.events.condition.platformModifierKeyOnly,
		style : this.selectedStyles
	});
	this.selected = this.interaction.select.getFeatures();
	this.map.addInteraction(this.interaction.select);
	this.map.removeInteraction(this.interaction.dragbox);
	this.interaction.dragbox = new ol.interaction.DragBox({
		condition : ol.events.condition.shiftKeyOnly
	});
	this.map.addInteraction(this.interaction.dragbox);

	if(!this.copyPaste_){
		var copyPaste = new gb.interaction.Copypaste({
			features: this.interaction.select.getFeatures(),
			sources: this.getSelectSources(),
			featureRecord: this.featureRecord
		});
		this.map.addInteraction(copyPaste);
	}

	this.interaction.dragbox.on('boxend', function() {
		that.interaction.select.getFeatures().clear();
		if(!that.selectedSource){
			return;
		} else {
			that.selectedSource.forEachFeatureIntersectingExtent(this.getGeometry().getExtent(), function(feature){
				that.interaction.select.getFeatures().push(feature);
			})
		}
	});
	this.interaction.select.on("select", function(evt) {
		console.log("select-interact");
		var features = that.interaction.select.getFeatures();
		if (that.getVersioningFeature() !== undefined && features.getLength() === 1) {
			var feature = features.item(0);
			that.updateFeatureHistoryModal(feature);
		} else {
			var vfeature = that.getVersioningFeature();
			vfeature.close();
		}
	});
	this.interaction.select.getFeatures().on("change:length", function(evt) {
		that.features = that.interaction.select.getFeatures();
		$(that.featureTB).empty();
		if (that.features.getLength() > 1) {
			that.featurePop.close();
			for (var i = 0; i < that.features.getLength(); i++) {
				var idx = that.features.item(i).getId().substring(that.features.item(i).getId().indexOf(".") + 1);
				var td1 = $("<td>").text(idx);
				var feature = that.features.item(i);
				var gitAttr = that.selectedSource.get("git");
				var anc = $("<a>").addClass("gb-edit-sel-flist").css("cursor", "pointer").attr({
					"value" : gitAttr.treeID + "," + feature.getId()
				}).text("Selecting feature").click(function() {
					var param = $(this).attr("value").split(",");
					feature = that.getVectorSourceOfServer(param[0]).getFeatureById(param[1]);
					that.count = 1;
					clearInterval(that.interval);
					feature.setStyle(undefined);
					that.interaction.select.getFeatures().clear();
					that.interaction.select.getFeatures().push(feature);
					that.featurePop.close();
					console.log(feature);
				});
				var td2 = $("<td>").append(anc);
				var tr = $("<tr>").append(td1).append(td2).mouseenter(function(evt) {
					var param = $(this).find("a").attr("value").split(",");
					feature = that.getVectorSourceOfServer(param[0]).getFeatureById(param[1]);
					feature.setStyle(that.highlightStyles1);
					that.interval = setInterval(function() {
						var val = that.count % 2;
						if (val === 0) {
							feature.setStyle(that.highlightStyles1);
						} else {
							feature.setStyle(that.highlightStyles2);
						}
						that.count++;
					}, 500);
				}).mouseleave(function() {
					var param = $(this).find("a").attr("value").split(",");
					feature = that.getVectorSourceOfServer(param[0]).getFeatureById(param[1]);
					that.count = 1;
					clearInterval(that.interval);
					feature.setStyle(undefined);
				});
				$(that.featureTB).append(tr);
			}

			that.featurePop.open();
			that.featurePop.getPanel().position({
				"my" : "left top",
				"at" : "left bottom",
				"of" : that.headerTag,
				"collision" : "fit"
			});
			that.attrPop.close();
		} else if (that.features.getLength() === 1) {
			that.featurePop.close();
			$(that.attrTB).empty();
			that.feature = that.features.item(0);
			var attrInfo = that.feature.getProperties();

			if (1) {
				var attr = that.features.item(0).getProperties();
				var keys = Object.keys(attrInfo);
				for (var i = 0; i < keys.length; i++) {
					if (keys[i] === "geometry") {
						continue;
					}
					var td1 = $("<td>").text(keys[i]);
					var tform = $("<input>").addClass("gb-edit-sel-alist").attr({
						"type" : "text"
					}).css({
						"width" : "100%",
						"border" : "none"
					}).val(attr[keys[i]]).on("input", function() {
						var attrTemp = attrInfo[$(this).parent().prev().text()];
						console.log(attrTemp.type);
						switch (attrTemp.type) {
						case "String":
							if (that.isString($(this).val()) || ($(this).val() === "")) {
								var obj = {};
								obj[$(this).parent().prev().text()] = $(this).val();
								that.feature.setProperties(obj);
								that.featureRecord.update(that.getLayer(), that.feature);
								console.log("set");
							} else {
								$(this).val("");
							}
							break;
						case "Integer":
							if (that.isInteger($(this).val()) || ($(this).val() === "")) {
								var obj = {};
								obj[$(this).parent().prev().text()] = $(this).val();
								that.feature.setProperties(obj);
								that.featureRecord.update(that.getLayer(), that.feature);
								console.log("set");
							} else {
								$(this).val("");
							}
							break;
						case "Double":
							if (that.isDouble($(this).val()) || ($(this).val() === "")) {
								var obj = {};
								obj[$(this).parent().prev().text()] = $(this).val();
								that.feature.setProperties(obj);
								that.featureRecord.update(that.getLayer(), that.feature);
								console.log("set");
							} else {
								$(this).val("");
							}
							break;
						case "Boolean":
							var valid = [ "t", "tr", "tru", "true", "f", "fa", "fal", "fals", "false" ];
							if (valid.indexOf($(this).val()) !== -1) {
								if (that.isBoolean($(this).val())) {
									var obj = {};
									obj[$(this).parent().prev().text()] = $(this).val();
									that.feature.setProperties(obj);
									that.featureRecord.update(that.getLayer(), that.feature);
									console.log("set");
								}
							} else if ($(this).val() === "") {
								var obj = {};
								obj[$(this).parent().prev().text()] = $(this).val();
								that.feature.setProperties(obj);
								that.featureRecord.update(that.getLayer(), that.feature);
								console.log("set");
							} else {
								$(this).val("");
							}
							break;
						case "Date":
							if ($(this).val().length === 10) {
								if (that.isDate($(this).val())) {
									var obj = {};
									obj[$(this).parent().prev().text()] = $(this).val();
									that.feature.setProperties(obj);
									that.featureRecord.update(that.getLayer(), that.feature);
									console.log("set");
								} else {
									$(this).val("");
								}
							} else if ($(this).val().length > 10) {
								$(this).val("");
							}
							break;
						default:
							break;
						}

					});
					var td2 = $("<td>").append(tform);
					var tr = $("<tr>").append(td1).append(td2);
					that.attrTB.append(tr);
				}
				that.attrPop.open();
				that.attrPop.getPanel().position({
					"my" : "left top",
					"at" : "left bottom",
					"of" : that.headerTag,
					"collision" : "fit"
				});
			} else {
				that.attrPop.close();
			}
		} else {
			that.featurePop.close();
			that.attrPop.close();
		}

	});

	this.deactiveAnotherInteraction(this.interaction.select);
	this.deactiveAllBtn_();

	this.isOn.select = true;
	this.activeBtn_("selectBtn");

};
/**
 * 피처 그리기를 활성화 한다
 * 
 * @method draw
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.draw = function(layer) {

	if (this.isOn.draw) {
		if (!!this.interaction.draw || !!this.interaction.updateDraw) {
			this.deactiveIntrct_("snap");
			this.deactiveIntrct_("draw");
			this.deactiveBtn_("drawBtn");
			this.map.removeLayer(this.managed);
		}
		return;
	}
	this.map.removeLayer(this.managed);
	var that = this;
	if (this.interaction.select) {
		this.interaction.select.getFeatures().clear();
		this.deactiveIntrct_([ "dragbox", "select"]);
	}
	var sourceLayer = this.selectedSource;

	if(!sourceLayer){
		return;
	}

	var git = sourceLayer.get("git");
	if (git.editable === true) {
		this.interaction.draw = new ol.interaction.Draw({
			source : sourceLayer,
			type : git.geometry
		});
		this.interaction.snap = new ol.interaction.Snap({
			source : this.snapSource
		});
		this.interaction.draw.selectedType = function() {
			var layer = that.selectedSource;
			if (!layer) {
				return;
			}
			var irreGeom = layer.get("git").geometry;
			var geom;
			switch (irreGeom) {
			case "Polyline":
				geom = "LineString";
				break;
			case "LWPolyline":
				geom = "LineString";
				break;
			case "Insert":
				geom = "Point";
				break;
			case "Text":
				geom = "Point";
				break;
			default:
				geom = that.selectedSource.get("git").geometry;
			break;
			}
			return geom;
		};

		var that = this;
		this.interaction.draw.on("drawstart", function(evt) {
			gb.undo.setActive(false);
		});

		this.interaction.draw.on("drawend", function(evt) {
			console.log(evt);
			gb.undo.setActive(true);

			var source = that.selectedSource;
			var layer = source.get("git").tempLayer;
			if (!!source) {
				var feature = evt.feature;
				var c = that.featureRecord.getCreated();
				var l = c[source.get("git").layerID];
				if (!l) {
					var fid = source.get("git").layerID + ".new0";
					feature.setId(fid);
					that.featureRecord.create(layer, feature);
				} else {
					var count = 0;
					while(!!l[source.get("git").layerID + ".new" + count]){
						count++;
					}
					var fid = source.get("git").layerID + ".new" + count;
					feature.setId(fid);
					that.featureRecord.create(layer, feature);
				}

				gb.undo.pushAction({
					undo: function(data){
						data.layer.getSource().removeFeature(data.feature);
						data.that.featureRecord.deleteFeatureCreated(data.layer.get("id"), data.feature.getId());
					},
					redo: function(data){
						data.layer.getSource().addFeature(data.feature);
						data.that.featureRecord.create(data.layer, data.feature);
					},
					data: {
						that: that,
						layer: layer,
						feature: feature
					}
				});
			}
		});
		this.deactiveIntrct_([ "select", "dragbox", "move", "modify", "rotate" ]);
		this.map.addInteraction(this.interaction.draw);
		this.map.addInteraction(this.interaction.snap);
		this.activeIntrct_("draw");
		this.activeIntrct_("snap");
		this.activeBtn_("drawBtn");
	} else if (git.editable === true && sourceLayer instanceof ol.layer.Base) {
		this.map.addLayer(this.managed);

		this.interaction.draw = new ol.interaction.Draw({
			source : this.tempSource,
			type : git.geometry
		});
		this.interaction.snap = new ol.interaction.Snap({
			source : this.snapSource
		});
		this.interaction.draw.selectedType = function() {
			var result = false;
			var layer = this.selectedSource;
			if (!!layer) {
				result = layer.get("git").geometry;
			}
			return result;
		};
		this.interaction.draw.on("drawend", function(evt) {
			console.log(evt);
			var source = that.selectedSource;
			var layer = source.get("git").tempLayer;
			if (!!source) {
				var feature = evt.feature;
				var c = that.featureRecord.getCreated();
				var l = c[source.get("git").layerID];
				if (!l) {
					var fid = source.get("git").layerID + ".new0";
					feature.setId(fid);
					that.featureRecord.create(layer, feature);
				} else {
					var keys = Object.keys(l);
					var count;
					if (keys.length === 0) {
						count = 0;
					} else {
						var id = keys[keys.length - 1];
						var nposit = (id.search(".new")) + 4;
						count = (parseInt(id.substr(nposit, id.length)) + 1);
					}
					var fid = source.get("git").layerID + ".new" + count;
					feature.setId(fid);
					that.featureRecord.create(layer, feature);
				}
			}
		});
		this.deactiveIntrct_([ "move", "modify", "rotate" ]);
		this.map.addInteraction(this.interaction.draw);
		this.map.addInteraction(this.interaction.snap);
		this.activeIntrct_("draw");
		this.activeIntrct_("snap");
		this.activeBtn_("drawBtn");
	}

};
/**
 * 피처 이동을 활성화 한다
 * 
 * @method move
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.move = function(layer) {
	if (this.interaction.select === undefined) {
		return;
	}
	if (this.isOn.move) {
		if (this.interaction.move) {
			this.interaction.select.getFeatures().clear();
			this.deactiveIntrct_("move");
			this.deactiveBtn_("moveBtn");
			// this.map.removeLayer(this.managed);
		}
		return;
	}
	// this.map.removeLayer(this.managed);
	var that = this;

	var selectSource = this.selectedSource;
	if(!selectSource){
		return;
	}

	if (this.interaction.select.getFeatures().getLength() > 0) {

		// this.map.addLayer(this.managed);
		this.interaction.move = new ol.interaction.Translate({
			features : this.interaction.select.getFeatures()
		});

		// feature move를 시작한 시점의 mouse pointer 위치 좌표
		var lastCoord;
		this.interaction.move.on("translatestart", function(evt) {
			lastCoord = evt.coordinate;
		});
		this.interaction.move.on("translateend", function(evt) {

			// 선택된 feature 객체들을 저장
			var featureList = [];
			var features = evt.features;
			for (var i = 0; i < features.getLength(); i++) {
				that.featureRecord.update(selectSource.get("git").tempLayer, features.item(i));
				featureList.push(features.item(i));
			}

			gb.undo.pushAction({
				undo: function(data){
					var deltaX = data.lastCoord[0] - data.newCoord[0];
					var deltaY = data.lastCoord[1] - data.newCoord[1];
					var geom;
					for(var i = 0; i < data.features.length; i++){
						geom = data.features[i].getGeometry();
						geom.translate(deltaX, deltaY);
						data.features[i].setGeometry(geom);
						data.that.featureRecord.update(data.layer, data.features[i]);
					}
				},
				redo: function(data){
					var deltaX = data.newCoord[0] - data.lastCoord[0];
					var deltaY = data.newCoord[1] - data.lastCoord[1];
					var geom;
					for(var i = 0; i < data.features.length; i++){
						geom = data.features[i].getGeometry();
						geom.translate(deltaX, deltaY);
						data.features[i].setGeometry(geom);
						data.that.featureRecord.update(data.layer, data.features[i]);
					}
				},
				data: {
					that: that,
					layer: selectSource.get("git").tempLayer,
					features: featureList,
					lastCoord: lastCoord,
					newCoord: evt.coordinate
				}
			});
		});
		this.deactiveIntrct_([ "select", "dragbox", "draw", "modify", "rotate", "snap" ]);
		this.map.addInteraction(this.interaction.move);
		this.activeIntrct_("move");
		this.activeBtn_("moveBtn");

		selectSource.get("git").tempLayer.setMap(null);
		this.tempVector.setSource(selectSource);
		this.map.addLayer(this.tempVector);
	} else {
		console.error("select features");
	}
};
/**
 * 피처 멀티편집을 활성화 한다
 * 
 * @method rotate
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.rotate = function(layer) {
	if (this.interaction.select === undefined) {
		return;
	}
	if (this.isOn.rotate) {
		if (!!this.interaction.rotate) {
			this.interaction.select.getFeatures().clear();
			this.deactiveIntrct_("rotate");
			this.deactiveBtn_("rotateBtn");
		}
		return;
	}

	var that = this;

	var selectSource = this.selectedSource;
	if(!selectSource){
		return;
	}

	if (this.interaction.select.getFeatures().getLength() > 0) {

		if (this.interaction.select.getFeatures().getLength() !== 1) {
			console.error("select 1 feature");
			return;
		}

		this.interaction.rotate = new gb.interaction.MultiTransform({
			features : this.interaction.select.getFeatures()
		});

		var lastCoord;
		this.interaction.rotate.on("transformstart", function(evt) {
			if(!evt.feature){
				return;
			}
			lastCoord = evt.feature.getGeometry().getCoordinates();
		});
		this.interaction.rotate.on("transformend", function(evt) {

			var feature = evt.feature;
			that.featureRecord.update(selectSource.get("git").tempLayer, feature);

			gb.undo.pushAction({
				undo: function(data){
					var geom = data.feature.getGeometry();
					geom.setCoordinates(data.lastCoord);
					data.feature.setGeometry(geom);
					data.that.featureRecord.update(data.layer, data.feature);
				},
				redo: function(data){
					var geom = data.feature.getGeometry();
					geom.setCoordinates(data.newCoord);
					data.feature.setGeometry(geom);
					data.that.featureRecord.update(data.layer, data.feature);
				},
				data: {
					that: that,
					layer: selectSource.get("git").tempLayer,
					feature: feature,
					lastCoord: lastCoord,
					newCoord: feature.getGeometry().getCoordinates()
				}
			});
		});
		this.map.addInteraction(this.interaction.rotate);
		this.deactiveIntrct_([ "select", "dragbox", "draw", "move", "modify", "snap" ]);
		this.activeIntrct_("rotate");
		this.activeBtn_("rotateBtn");
	} else {
		console.error("select features");
	}
};
/**
 * 피처 수정을 활성화 한다
 * 
 * @method modify
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.modify = function(layer) {
	if (this.interaction.select === undefined) {
		return;
	}
	if (this.isOn.modify) {
		if (!!this.interaction.modify) {
			this.interaction.select.getFeatures().clear();
			this.deactiveIntrct_("modify");
			this.deactiveIntrct_("snap");
			this.deactiveBtn_("modiBtn");
		}
		return;
	}
	var that = this;

	var selectSource = this.selectedSource;
	if(!selectSource){
		return;
	}

	if (this.interaction.select.getFeatures().getLength() > 0) {

		this.interaction.modify = new ol.interaction.Modify({
			features : this.interaction.select.getFeatures()
		});

		var lastCoord;
		this.interaction.modify.on("modifystart", function(evt) {
			lastCoord = [];
			evt.features.forEach(function(feature){
				lastCoord.push({
					id: feature.getId(),
					coord: feature.getGeometry().getCoordinates()
				});
			});
		});
		this.interaction.modify.on("modifyend", function(evt) {
			var featureList = [];
			var newCoord = [];

			var features = evt.features;
			for (var i = 0; i < features.getLength(); i++) {
				that.featureRecord.update(selectSource.get("git").tempLayer, features.item(i));
				featureList.push(features.item(i));
				newCoord.push({
					id: features.item(i).getId(),
					coord: features.item(i).getGeometry().getCoordinates()
				});
			}

			gb.undo.pushAction({
				undo: function(data){
					console.log("modify undo");
					var geom;
					for(var i = 0; i < data.features.length; i++){
						geom = data.features[i].getGeometry();
						for(var j = 0; j < data.lastCoord.length; j++){
							if(data.features[i].getId() === data.lastCoord[j].id){
								geom.setCoordinates(data.lastCoord[j].coord);
								data.features[i].setGeometry(geom);
								data.that.featureRecord.update(data.layer, data.features[i]);
								break;
							}
						}
					}
				},
				redo: function(data){
					console.log("modify redo");
					var geom;
					for(var i = 0; i < data.features.length; i++){
						geom = data.features[i].getGeometry();
						for(var j = 0; j < data.newCoord.length; j++){
							if(data.features[i].getId() === data.newCoord[j].id){
								geom.setCoordinates(data.newCoord[j].coord);
								data.features[i].setGeometry(geom);
								data.that.featureRecord.update(data.layer, data.features[i]);
								break;
							}
						}
					}
				},
				data: {
					that: that,
					layer: selectSource.get("git").tempLayer,
					features: featureList,
					lastCoord: lastCoord,
					newCoord: newCoord
				}
			});
		});
		this.deactiveIntrct_([ "select", "dragbox", "draw", "move", "rotate" ]);
		this.map.addInteraction(this.interaction.modify);

		var sourceLayer;
		this.interaction.snap = new ol.interaction.Snap({
			source : this.snapSource
		});
		this.map.addInteraction(this.interaction.snap);
		this.activeIntrct_("snap");

		this.activeIntrct_("modify");
		this.activeBtn_("modiBtn");
	} else {
		console.error("select features");
	}
};
/**
 * 피처를 삭제한다
 * 
 * @method remove
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.header.EditingTool.prototype.remove = function(layer) {
	if (this.interaction.select === undefined) {
		return;
	}
	var that = this;

	var selectSource = this.selectedSource;
	if(!selectSource){
		return;
	}

	if (this.interaction.select.getFeatures().getLength() > 0) {
		var features = this.interaction.select.getFeatures();
		var fill = new ol.style.Fill({
			color : "rgba(255,0,0,0.01)"
		});

		var stroke = new ol.style.Stroke({
			color : "rgba(255,0,0,0.7)",
			width : 1.25
		});

		var text = new ol.style.Text({});
		var style = new ol.style.Style({
			image : new ol.style.Circle({
				fill : fill,
				stroke : stroke,
				radius : 5
			}),
			fill : fill,
			stroke : stroke
		});

		if (selectSource.get("git").tempLayer instanceof ol.layer.Vector) {
			for (var i = 0; i < features.getLength(); i++) {
				if (features.item(i).getId().search(".new") !== -1) {
					selectSource.removeFeature(features.item(i));
				} else {
					features.item(i).setStyle(style);
				}
				that.featureRecord.remove(selectSource.get("git").tempLayer, features.item(i));
			}

			gb.undo.pushAction({
				undo: function(data){
					var feature, id;
					for (var i = 0; i < data.features.length; i++) {
						id = data.features[i].getId();
						if (id.search(".new") !== -1) {
							data.source.addFeature(data.features[i]);
						} else {
							feature = data.source.getFeatureById(id);
							data.source.removeFeature(feature);
							data.source.clear();
						}
					}
				},
				redo: function(data){
					var feature, id;
					for (var i = 0; i < data.features.length; i++) {
						id = data.features[i].getId();
						if (id.search(".new") !== -1) {
							data.source.removeFeature(data.features[i]);
						} else {
							feature = data.source.getFeatureById(id);
							feature.setStyle(data.removeStyle);
						}
						data.that.featureRecord.remove(data.source.get("git").tempLayer, data.features[i]);
					}
				},
				data: {
					that: that,
					source: selectSource,
					features: features.getArray().slice(),
					removeStyle: style
				}
			});

		} else if (selectSource.get("git").tempLayer instanceof ol.layer.Base) {
			for (var i = 0; i < features.getLength(); i++) {
				if (features.item(i).getId().search(".new") !== -1) {
					selectSource.removeFeature(features.item(i));
				} else {
					features.item(i).setStyle(style);
				}
				that.featureRecord.remove(selectSource.get("git").tempLayer, features.item(i));
			}
		}
		this.interaction.select.getFeatures().clear();

	} else {
		console.error("select features");
	}
};

/**
 * 선택한 레이어를 업데이트한다
 * 
 * @method updateSelected
 * @return {ol.layer.Base}
 */
gb.header.EditingTool.prototype.updateSelected = function(treeId) {
	var source = undefined;

	if(this.getVectorSourceOfServer(treeId)){
		source = this.getVectorSourceOfServer(treeId);
	} else {
		var layer;
		if(otree.getJSTree().get_LayerById(treeId) instanceof ol.layer.Vector){
			layer = otree.getJSTree().get_LayerById(treeId);
			source = layer.getSource();
			if(!layer.get("id")){
				layer.set("id", layer.get("treeid"));
			}
			if(typeof source.get("git") !== "object"){
				source.set("git", {
					layerID: layer.get("id"),
					treeID: layer.get("treeid"),
					tempLayer: layer,
					editable: layer.get("git").editable,
					geometry: layer.get("git").geometry
				});
			}
			if(!this.customVector_[layer.get("treeid")]){
				this.customVector_[layer.get("treeid")] = source;
			}
		}
	}

	this.selectedSource = source;
	if(source !== undefined){
		this.selectSources.clear();
		this.selectSources.push(source);
	}
	return source;
};
/**
 * 피처를 선택한다
 * 
 * @method setFeatures
 * @param {ol.Feature}
 */
gb.header.EditingTool.prototype.setFeatures = function(newFeature) {
	var that = this;
	/*
	 * if (this.isOn.select) { if (!!this.interaction.select) {
	 * this.interaction.select.getFeatures().clear(); this.deactiveIntrct_([
	 * "dragbox", "select"]); } this.deactiveBtn_("selectBtn"); this.isOn.select =
	 * false; } this.select(this.layer);
	 */
	if (newFeature.length === 1) {
		// this.interaction.select.getFeatures().extend(newFeature);
		this.open();
		this.attrPop.getPanel().position({
			"my" : "left top",
			"at" : "right top",
			"of" : this.getPanel(),
			"collision" : "fit"
		});
	}

};
/**
 * 선택한 피처를 반환한다.
 * 
 * @method getFeatures
 * @return {ol.Collection<ol.Feature>}
 */
gb.header.EditingTool.prototype.getFeatures = function() {
	return this.features;
};
/**
 * 삭제한 레이어에 포함되는 피처를 임시 레이어에서 지운다
 * 
 * @method removeFeatureFromUnmanaged
 * @param {ol.layer.Base}
 *            layer
 */
gb.header.EditingTool.prototype.removeFeatureFromUnmanaged = function(layer) {
	var that = this;

	if (layer instanceof ol.layer.Group) {
		var layers = layer.getLayers();
		for (var i = 0; i < layers.getLength(); i++) {
			this.featureRecord.removeByLayer(layers.item(i).get("id"));
			// that.tempVector.setMap(this.map);
			this.removeFeatureFromUnmanaged(layers.item(i));
		}
	} else if (layer instanceof ol.layer.Base) {
		var git = layer.get("git");
		if (git) {
			// git 변수가 있음
			if (git.hasOwnProperty("fake")) {
				// 가짜 레이어임
				if (git["fake"] === "parent") {
					// 가짜 그룹 레이어임
					var layers = git["layers"];
					for (var i = 0; i < layers.getLength(); i++) {
						this.featureRecord.removeByLayer(layers.item(i).get("id"));
						// that.tempVector.setMap(this.map);
						this.removeFeatureFromUnmanaged(layers.item(i));
					}
				} else if (git["fake"] === "child") {
					var layerId = layer.get("id");
					this.tempVector.getSource().forEachFeature(function(feature) {
						var id = feature.getId();
						if (id.indexOf(layerId) !== -1) {
							that.tempVector.getSource().removeFeature(feature);
							// that.tempVector.setMap(this.map);
						}
					});
				}
			} else {
				var layerId = layer.get("id");
				this.tempVector.getSource().forEachFeature(function(feature) {
					var id = feature.getId();
					if (id.indexOf(layerId) !== -1) {
						that.tempVector.getSource().removeFeature(feature);
						// that.tempVector.setMap(this.map);
					}
				});
			}
		} else {
			var layerId = layer.get("id");
			this.tempVector.getSource().forEachFeature(function(feature) {
				var id = feature.getId();
				if (id.indexOf(layerId) !== -1) {
					that.tempVector.getSource().removeFeature(feature);
					// that.tempVector.setMap(this.map);
				}
			});
		}
	}
	// that.tempVector.setMap(this.map);
	return;
};

/**
 * 임시 레이어에 있는 피처를 전부 삭제한다.
 * 
 * @method clearUnmanaged
 */
gb.header.EditingTool.prototype.clearUnmanaged = function() {
	if (this.tempVector instanceof ol.layer.Vector) {
		this.tempVector.clear();
	}
	// this.tempVector.setMap(this.map);
	return;
};

/**
 * 패널을 나타낸다.
 * 
 * @method open
 */
gb.header.EditingTool.prototype.open = function() {
	var layer = this.updateSelected();
	if (layer instanceof ol.layer.Group) {
		console.error("group layer can not edit");
	} else if (layer instanceof ol.layer.Tile) {
		var git = layer.get("git");
		if (git.hasOwnProperty("fake")) {
			if (git.fake === "parent") {
				console.error("fake parent layer can not edit");
			} else {
				// this.headerTag.css("display", "block");
			}
		} else {
			// this.headerTag.css("display", "block");
		}
	} else if (layer instanceof ol.layer.Base) {
		// this.headerTag.css("display", "block");
	}

};

/**
 * 베이스 타입 레이어에 소스를 입력한다.
 * 
 * @method setWMSSource(layer)
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.setWMSSource = function(sourceLayer, callback) {
	var that = this;
	if (sourceLayer instanceof ol.layer.Vector || sourceLayer instanceof ol.layer.Group) {
		return;
	}
	var arr = {
			"geoLayerList" : [ sourceLayer.get("id") ]
	}
	var names = [];
	// console.log(JSON.stringify(arr));

	$.ajax({
		url : this.layerInfo,
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		cache : false,
		data : JSON.stringify(arr),
		beforeSend : function() { // 호출전실행
			$("body").css("cursor", "wait");
		},
		traditional : true,
		success : function(data2, textStatus, jqXHR) {
			console.log(data2);
			if (Array.isArray(data2)) {
				for (var i = 0; i < 1; i++) {
					var source = new ol.source.TileWMS({
						url : this.imageTile,
						params : {
							'LAYERS' : data2[i].lName,
							'TILED' : true,
							'FORMAT' : 'image/png8',
							'VERSION' : '1.1.0',
							'CRS' : that.getMap().getView().getProjection().getCode(),
							'SRS' : that.getMap().getView().getProjection().getCode(),
							'BBOX' : data2[i].nbBox.minx.toString() + "," + data2[i].nbBox.miny.toString() + ","
							+ data2[i].nbBox.maxx.toString() + "," + data2[i].nbBox.maxy.toString()
						},
						serverType : 'geoserver'
					});
					sourceLayer.setSource(source);
					var ogit = sourceLayer.get("git");
					ogit["attribute"] = data2[i].attInfo;
					ogit["geometry"] = data2[i].geomType;
					var getPosition = function(str, subString, index) {
						return str.split(subString, index).join(subString).length;
					};
					var id = sourceLayer.get("id");
					var format = id.substring((getPosition(id, "_", 1) + 1), getPosition(id, "_", 2));
					var layer;
					if (format === "ngi") {
						layer = new gb.layer.LayerInfo({
							name : sourceLayer.get("name"),
							id : id,
							format : format,
							epsg : data2[i].srs,
							mbound : [ [ data2[i].nbBox.minx.toString(), data2[i].nbBox.miny.toString() ],
								[ data2[i].nbBox.maxx.toString(), data2[i].nbBox.maxy.toString() ] ],
								lbound : [ [ 122.71, 28.6 ], [ 134.28, 40.27 ] ],
								isNew : false,
								geometry : id.substring(getPosition(id, "_", 4) + 1),
								sheetNum : id.substring((getPosition(id, "_", 2) + 1), getPosition(id, "_", 3))
						});
					} else if (format === "dxf") {
						layer = new gb.layer.LayerInfo({
							name : sourceLayer.get("name"),
							id : id,
							format : format,
							epsg : data2[i].srs,
							mbound : [ [ data2[i].nbBox.minx.toString(), data2[i].nbBox.miny.toString() ],
								[ data2[i].nbBox.maxx.toString(), data2[i].nbBox.maxy.toString() ] ],
								isNew : false,
								lbound : [ [ 122.71, 28.6 ], [ 134.28, 40.27 ] ],
								isNew : false,
								geometry : id.substring(getPosition(id, "_", 4) + 1),
								sheetNum : id.substring((getPosition(id, "_", 2) + 1), getPosition(id, "_", 3))
						});
					} else if (format === "shp") {
						layer = new gb.layer.LayerInfo({
							name : sourceLayer.get("name"),
							id : id,
							format : format,
							epsg : data2[i].srs,
							mbound : [ [ data2[i].nbBox.minx.toString(), data2[i].nbBox.miny.toString() ],
								[ data2[i].nbBox.maxx.toString(), data2[i].nbBox.maxy.toString() ] ],
								lbound : [ [ 122.71, 28.6 ], [ 134.28, 40.27 ] ],
								isNew : false,
								geometry : id.substring(getPosition(id, "_", 4) + 1),
								sheetNum : id.substring((getPosition(id, "_", 2) + 1), getPosition(id, "_", 3))
						});
					}
					ogit["information"] = layer;
					console.log(ogit["attribute"]);
					console.log("source injected");
					if (typeof callback === "function") {
						callback(source);
					}
				}
				$("body").css("cursor", "default");
			}
		}
	});
};
/**
 * ol.Map을 입력한다.
 * 
 * @method setMap(map)
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.setMap = function(map) {
	this.map = map;
}
/**
 * ol.Map을 반환한다.
 * 
 * @method getMap(map)
 * @return {ol.layer.Base}
 */
gb.header.EditingTool.prototype.getMap = function() {
	return this.map;
}
/**
 * String인지 검사한다.
 * 
 * @method isString()
 * @param {mixed}
 *            va
 * @return {Boolean} is String?
 */
gb.header.EditingTool.prototype.isString = function(va) {
	var result = false;
	if (typeof va === "string") {
		result = true;
	}
	return result;
}
/**
 * Integer인지 검사한다.
 * 
 * @method isString()
 * @param {mixed}
 *            va
 * @return {Boolean} is Integer?
 */
gb.header.EditingTool.prototype.isInteger = function(va) {
	var result = false;
	if (va == parseInt(va)) {
		result = true;
	}
	return result;
}
/**
 * Double인지 검사한다.
 * 
 * @method isDouble()
 * @param {mixed}
 *            va
 * @return {Boolean} is Double?
 */
gb.header.EditingTool.prototype.isDouble = function(va) {
	var result = false;
	if (typeof va === "string") {
		var p = parseFloat(va);
		if (!isNaN(p)) {
			result = true;
		}
	}
	return result;
}
/**
 * Boolean인지 검사한다.
 * 
 * @method isBoolean()
 * @param {mixed}
 *            va
 * @return {Boolean} is Boolean?
 */
gb.header.EditingTool.prototype.isBoolean = function(va) {
	var result = false;
	if (va == "true" || va == "false") {
		result = true;
	}
	return result;
}
/**
 * Date인지 검사한다.
 * 
 * @method isDate()
 * @param {mixed}
 *            va
 * @return {Boolean} is Date?
 */
gb.header.EditingTool.prototype.isDate = function(va) {
	var result = false;
	if (typeof va === "string") {
		if ((new Date(va)).getTime() > 0) {
			result = true;
		}
	}
	return result;
}
/**
 * 스냅핑 레이어를 설정한다.
 * 
 * @method addSnappingLayer()
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.addSnappingLayer = function(layer) {
	var success = false;
	if (layer instanceof ol.layer.Group) {
		var layers = layer.getLayers();
		for (var i = 0; i < layers.getLength(); i++) {
			this.addSnappingLayer(layers.item(i));
		}
		success = true;
	} else if (layer instanceof ol.layer.Vector) {
		for (var i = 0; i < this.snapVector.getLength(); i++) {
			if (this.snapVector.item(i).get("id") === layer.get("id")) {
				success = true;
				break;
			}
		}
		if (!success) {
			this.snapVector.push(layer);
			success = true;
		}
	} else if (layer instanceof ol.layer.Tile) {

		var treeid = layer.get("treeid");
		if(!!this.vectorSourcesOfServer_[treeid]){
			this.snapVector.push(this.vectorSourcesOfServer_[treeid].get("git").tempLayer);
			success = true;
		}

	} else if (layer instanceof ol.layer.Layer) {
		var git = layer.get("git");
		if (git) {
			if (git.hasOwnProperty("fake")) {
				if (git.fake === "child") {
					if (this.snapWMS.indexOf(layer.get("id")) === -1) {
						this.snapWMS.push(layer.get("id"));
						success = true;
					}
				}
			}
		}
	}
	return success;
}
/**
 * 스냅핑 레이어를 삭제한다.
 * 
 * @method addSnappingLayer()
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.removeSnappingLayer = function(layer) {
	var that = this;
	var success = false;
	if (layer instanceof ol.layer.Group) {
		var layers = layer.getLayers();
		for (var i = 0; i < layers.getLength(); i++) {
			this.removeSnappingLayer(layers.item(i));
		}
		success = true;
	} else if (layer instanceof ol.layer.Vector) {
		for (var i = 0; i < this.snapVector.getLength(); i++) {
			if (this.snapVector.item(i).get("id") === layer.get("id")) {
				this.snapVector.removeAt(i);
				success = true;
				break;
			}
		}
	} else if (layer instanceof ol.layer.Tile) {

		var treeid = layer.get("treeid");
		if(!!this.vectorSourcesOfServer_[treeid]){
			this.snapVector.pop(this.vectorSourcesOfServer_[treeid].get("git").tempLayer);
			success = true;
		}

	} else if (layer instanceof ol.layer.Layer) {
		var git;
		if (layer) {
			git = layer.get("git");
		}
		if (!!git) {
			if (git.hasOwnProperty("fake")) {
				if (git.fake === "child") {
					if (this.snapWMS.indexOf(layer.get("id")) !== -1) {
						this.snapWMS.splice(this.snapWMS.indexOf(layer.get("id")), 1);
						success = true;
					}
				}
			} else {
				if (this.snapWMS.indexOf(layer.get("id")) !== -1) {
					this.snapWMS.splice(this.snapWMS.indexOf(layer.get("id")), 1);
					success = true;
				}
			}
		} else {
			if (this.snapWMS.indexOf(layer.get("id")) !== -1) {
				this.snapWMS.splice(this.snapWMS.indexOf(layer.get("id")), 1);
				success = true;
			}
		}
	}
	return success;
}
/**
 * 스냅핑 레이어를 로드한다.
 * 
 * @method addSnappingLayer()
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.loadSnappingLayer = function(extent) {
	var that = this;
	that.snapSource.clear();

	if (this.snapVector.getLength() > 0) {
		for (var i = 0; i < this.snapVector.getLength(); i++) {
			this.snapVector.item(i).getSource().forEachFeatureIntersectingExtent(extent, function(feature) {
				that.snapSource.addFeature(feature);
			});
		}
	}
};
/**
 * zoom to fit
 * 
 * @method zoomToFit()
 * @param {ol.layer.Base}
 */
gb.header.EditingTool.prototype.zoomToFit = function(layer) {
	var that = this;
	if (layer instanceof ol.layer.Group) {
		var extent = ol.extent.createEmpty();
		layer.getLayers().forEach(function(layer2) {
			if (layer2.getSource() instanceof ol.source.TileWMS) {
				var param = layer2.getSource().getParams();
				var keys = Object.keys(param);
				var bbx = "bbox";
				for (var i = 0; i < keys.length; i++) {
					if (keys[i].toLowerCase() === bbx.toLowerCase()) {
						var bbox = param[keys[i]].split(",");
						ol.extent.extend(extent, bbox);
						break;
					}
				}
			} else if (layer2.source instanceof ol.source.Vector) {
				ol.extent.extend(extent, layer2.getSource().getExtent());
			}
		});
		this.getMap().getView().fit(extent, this.getMap().getSize());
	} else if (layer instanceof ol.layer.Vector) {
		var view = this.getMap().getView();
		view.fit(source.getExtent(), this.getMap().getSize());
	} else if (layer instanceof ol.layer.Tile) {
		var source = layer.getSource();
		if (source instanceof ol.source.TileWMS) {
			var param = source.getParams();
			var keys = Object.keys(param);
			var bbx = "bbox";
			for (var i = 0; i < keys.length; i++) {
				if (keys[i].toLowerCase() === bbx.toLowerCase()) {
					var bbox = param[keys[i]].split(",");
					var view = this.getMap().getView();
					view.fit(bbox, this.getMap().getSize());
					break;
				}
			}
		}
	} else if (layer instanceof ol.layer.Layer) {
		var source = layer.getSource();
		var func = function(src) {
			var param = src.getParams();
			var keys = Object.keys(param);
			var bbx = "bbox";
			for (var i = 0; i < keys.length; i++) {
				if (keys[i].toLowerCase() === bbx.toLowerCase()) {
					var bbox = param[keys[i]].split(",");
					var view = that.getMap().getView();
					view.fit(bbox, that.getMap().getSize());
					break;
				}
			}
		};
		if (typeof source === "undefined" || source === null) {
			this.setWMSSource(layer, func);
		} else if (source instanceof ol.source.TileWMS) {
			func(source);
		}
	}
	return;
};

// hochul
gb.header.EditingTool.prototype.addInteraction = function(options){
	function adjustStyle(element, style){
		for(var content in style){
			element.css(content, style[content]);
		}
	}
	var interaction = options.interaction;
	this.customInteractions.push(interaction);
	this.map.addInteraction(interaction);
	interaction.setActive(false);

	var content = options.content || "Unknown";
	var icon = options.icon || "fas fa-asterisk";
	var clickEvent = options.clickEvent;
	var className = options.className;
	var color = options.color;
	var selectActive = options.selectActive || false;


	var iTag = $("<i>").addClass(icon).attr("aria-hidden", "true");
	var aTag = $("<a>").attr("href", "#");
	var liTag = $("<li>");

	this.btn[content] = aTag;

	aTag.hover(function(){
		if(!$(this).hasClass("active")){
			$(this).css("color", "#23527c");
			$(this).css("text-decoration", "none");
		}
	},function(){
		if(!$(this).hasClass("active")){
			$(this).css("color", "rgb(85, 85, 85)");
		}
	});

	// content element 저장
	this.contentList.push(aTag);

	var that = this;
	aTag.click(function(){
		that.deactiveAnotherInteraction(interaction, selectActive);
		if(typeof clickEvent === "function"){
			clickEvent();
		}
		if(interaction.getActive()){
			interaction.setActive(false);
			that.deactiveBtn_(content);
		} else {
			if(interaction.setSelectFeatures instanceof Function){
				interaction.setSelectFeatures(that.selected);
			}
			interaction.setActive(true);
			that.activeBtn_(content);
		}
	});

	if(className){
		liTag.addClass(className);
	}

	if(color){
		iTag.css("color", color);
	}

	adjustStyle(iTag, this.iStyle);
	adjustStyle(aTag, this.aStyle);
	adjustStyle(liTag, this.liStyle);

	if(this.translator[content]){
		aTag.html(this.translator[content][this.locale]);
	} else {
		aTag.html(content);
	}

	aTag.prepend(iTag);
	liTag.append(aTag);

	if(!options.float){
		this.ulTagLeft.append(liTag);
	} else {
		if(options.float === "right"){
			liTag.css("padding-left", "0").css("padding-right", "20px");
			this.ulTagRight.append(liTag);
		} else if(options.float === "left"){
			this.ulTagLeft.append(liTag);
		} else {
			this.ulTagLeft.append(liTag);
		}
	}
}

// hochul
gb.header.EditingTool.prototype.deactiveAnotherInteraction = function(interaction, select){
	var bool = select || false;
	for(var i in this.interaction){
		if(interaction !== this.interaction[i] && !!this.interaction[i]){
			if(this.interaction[i] instanceof ol.interaction.Select && !bool){
				this.interaction[i].getFeatures().clear();
			}

			if(this.interaction[i] instanceof ol.interaction.Translate){
				this.move();
			}
			this.interaction[i].setActive(false);
		}
	}

	for(var i in this.customInteractions){
		if(interaction !== this.customInteractions[i]){
			this.customInteractions[i].setActive(false);
		}
	}

	if(interaction instanceof ol.interaction.Select || interaction instanceof ol.interaction.DragBox){
		this.getInteraction_("select").setActive(true);
		this.getInteraction_("dragbox").setActive(true);
		this.isOn["select"] = true;
		this.isOn["dragbox"] = true;
	}
}

// hochul
gb.header.EditingTool.prototype.getTileLayersInMap = function(map){
	var tileLayers = [];
	var wmsLayers;

	map.getLayers().forEach(function(layer){
		if(layer instanceof ol.layer.Tile){
			tileLayers.push(layer);
		}
		if(layer instanceof ol.layer.Group){
			layer.getLayers().forEach(function(tile){
				if(tile instanceof ol.layer.Tile){
					tileLayers.push(tile);
				}
				if(tile instanceof ol.layer.Group){
					tile.getLayers().forEach(function(node){
						if(node instanceof ol.layer.Tile){
							tileLayers.push(node);
						}
					});
				}
			});
		}
	});

	return tileLayers;
}
// hochul
gb.header.EditingTool.prototype.loadWFS_ = function(){

	var tileLayers = this.getTileLayersInMap(this.map);
	var selectedLayer;
	var vectorSource;

	for(var i in tileLayers){
		if(typeof tileLayers[i].get("git") === "object"){
			if(!this.getVectorSourceOfServer(tileLayers[i].get("treeid"))){
				vectorSource = this.setVectorSourceOfServer(tileLayers[i].get("git"), tileLayers[i].get("id"), 
						tileLayers[i].get("name"), tileLayers[i].get("treeid"), tileLayers[i].getSource().getParams()["SLD_BODY"]);
				selectedLayer = $(this.treeElement).jstreeol3("get_selected_layer");
				if(selectedLayer.length === 1){
					if(tileLayers[i].get("treeid") === selectedLayer[0].get("treeid")){
						this.updateSelected(selectedLayer[0].get("treeid"));
						this.select(vectorSource);
					}
				}
			} else {
				this.getVectorSourceOfServer(tileLayers[i].get("treeid")).get("git").tempLayer.setMap(this.map);
			}
		}
	}

	for(var i in this.customVector_){
		this.customVector_[i].get("git").tempLayer.setVisible(true);
	}
}

// hochul
gb.header.EditingTool.prototype.setVisibleWFS = function(bool){
	var set;
	if(bool){
		set = this.map;
	} else {
		set = null;
	}

	for(var i in this.vectorSourcesOfServer_){
		this.vectorSourcesOfServer_[i].get("git").tempLayer.setMap(set);
	}

	for(var i in this.customVector_){
		this.customVector_[i].get("git").tempLayer.setVisible(set);
	}
}

// hochul
gb.header.EditingTool.prototype.setVisibleWMS = function(bool){
	var tileLayers = this.getTileLayersInMap(this.map);

	for(var i = 0; i < tileLayers.length; i++){
		tileLayers[i].setVisible(bool);
	}
}

// hochul
gb.header.EditingTool.prototype.refreshTileLayer = function(){
	var tileLayers = this.getTileLayersInMap(this.map);

	for(var i = 0; i < tileLayers.length; i++){
		tileLayers[i].getSource().refresh();
	}
}

// hochul
gb.header.EditingTool.prototype.setVectorSourceOfServer = function(obj, layerId, layerName, treeId, sld){
	var git = obj || {};
	var layerid = layerId;
	var layername = layerName;
	var treeid = treeId;
	var url = this.wfsURL;
	if(!this.getVectorSourceOfServer(treeid)){
		var vectorSource = new ol.source.Vector({
			format: new ol.format.GeoJSON(),
			loader: function(extent, resolution, projection){

				params = {
						"serverName": git.geoserver,
						"workspace": git.workspace,
						"version" : "1.0.0",
						"typeName" : layername,
						"bbox" : extent.join(","),
						"outputformat" : "application/json"
				};

				$.ajax({
					url : url,
					type : "GET",
					contentType : "application/json; charset=UTF-8",
					data : params,
					dataType : "JSON",
					success : function(data) {
						var features = vectorSource.getFormat().readFeatures(data);
						vectorSource.addFeatures(features);
					},
					error: function(jqXHR, textStatus, errorThrown){
						console.log(errorThrown);
					}
				});
			},
			strategy: ol.loadingstrategy.bbox
		});

		this.vectorSourcesOfServer_[treeid] = vectorSource;

		var layer = new ol.layer.Vector({
			source: vectorSource
		});
		layer.set("id", layerid);
		layer.set("name", layername);
		layer.setMap(this.map);

		if(sld !== undefined){
			var symbol = gb.style.LayerStyle.prototype.parseSymbolizer.call(this, sld);
			var style = new ol.style.Style({
				"fill": new ol.style.Fill({
					"color": ol.color.asArray(symbol.fillRGBA)
				}),
				"stroke": new ol.style.Stroke({
					"color": ol.color.asArray(symbol.strokeRGBA),
					"width": symbol.strokeWidth,
					"lineDash": symbol.strokeDashArray,
					"lineCap": "butt"
				}),
				"image": new ol.style.Circle({
					"radius": !!symbol.pointSize ? parseFloat(symbol.pointSize) : undefined,
							"fill": new ol.style.Fill({
								"color": ol.color.asArray(symbol.fillRGBA)
							}),
							"stroke": new ol.style.Stroke({
								"color": ol.color.asArray(symbol.strokeRGBA),
								"width": symbol.strokeWidth,
								"lineDash": symbol.strokeDashArray,
								"lineCap": "butt"
							})
				})
			});
			layer.setStyle(style);
		}

		git.layerID = layerid;
		git.tempLayer = layer;
		git.treeID = treeid;
		vectorSource.set("git", git);

		return vectorSource;
	}
	return null;
}

// hochul
gb.header.EditingTool.prototype.getVectorSourceOfServer = function(treeId){
	return this.vectorSourcesOfServer_[treeId];
}

// hochul
gb.header.EditingTool.prototype.getVectorSourcesOfServer = function(){
	var a = [];
	for(var i in this.vectorSourcesOfServer_){
		a.push(this.vectorSourcesOfServer_[i]);
	}
	return a;
}

// hochul
gb.header.EditingTool.prototype.editToolToggle = function(){
	if(this.getActiveTool()){
		this.setActiveTool(false);
		this.setVisibleWMS(true);
		this.setVisibleWFS(false);
		this.deactiveAnotherInteraction(this.interaction.select);
		this.deactiveAllBtn_();
	} else {
		this.setActiveTool(true);
		this.setVisibleWMS(false);
		if(this.map.getView().getZoom() > 11){
			this.displayEditZoomHint(false);
			this.loadWFS_();
		} else {
			this.displayEditZoomHint(true);
		}
	}
}

// hochul
gb.header.EditingTool.prototype.displayEditZoomHint = function(bool){
	if(this.getActiveTool()){
		if(bool){
			if(this.headerTag.find(".edit-zoom-hint").length === 0){
				this.ulTagLeft.css("display", "none");

				var editZoomHintTag = $("<h1 class='edit-zoom-hint'>");
				var icon = $("<span>").html("<i class='fas fa-exclamation-circle'></i>");
				var text = $("<span>").html("Zoom in to edit");

				editZoomHintTag.css("margin-top", "6px");
				editZoomHintTag.css("padding-left", "6px");
				editZoomHintTag.css("display", "inline-block");

				editZoomHintTag.append(icon);
				editZoomHintTag.append(text);
				this.headerTag.append(editZoomHintTag);
			}

			this.deactiveAnotherInteraction();
		} else {
			this.headerTag.find(".edit-zoom-hint").remove();
			this.ulTagLeft.css("display", "inline-block");
		}
	}
}

// hochul
gb.header.EditingTool.prototype.getSelectSources = function(){
	return this.selectSources;
}