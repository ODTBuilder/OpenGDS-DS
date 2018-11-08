/**
 * 오픈레이어스 커스텀 플러그인 로드 필요
 * @external "jsTree-openlayers plugin"
 */

/**
 * 오픈레이어스 레이어 목록을 표시한다.
 * 
 * @class gb.tree.OpenLayers
 * @memberof gb.tree
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String |
 *            Element} obj.append - 영역 본문이 삽입될 부모 노드의 ID 또는 Class 또는 Element
 * @param {ol.Map}
 *            obj.map - 편집 영역을 담당하는 ol.Map
 * @param {Object}
 *            url - 요청을 처리할 URL 객체
 * @param {String}
 *            obj.url.getLegend - WMS 범례 이미지를 요청할 URL
 * @author SOYIJUN
 * @date 2018.07.02
 * @version 0.01
 * 
 */
gb.tree.OpenLayers = function(obj) {
	var that = this;
	var options = obj ? obj : {};
	this.append = options.append ? options.append : undefined;
	this.map = options.map instanceof ol.Map ? options.map : undefined;
	this.editingTool = options.editingTool || undefined;
	this.token = options.token || "";
	this.locale = options.locale || "en";
	this.createdLayer = {};
	var url = options.url;
	this.geometryType = [ "point", "linestring", "polygon", "multipoint",
			"multilinestring", "multipolygon" ];
	this.translation = {
		"layerCode" : {
			"en" : "Code",
			"ko" : "코드"
		},
		"layerType" : {
			"en" : "Type",
			"ko" : "유형"
		},
		"addLayer" : {
			"en" : "Add layer",
			"ko" : "레이어 추가"
		},
		"exLayerCodeField" : {
			"en" : "Layer code ex) F0010000",
			"ko" : "레이어 코드 예시) F0010000"
		}
	}
	this.getLegend = url.getLegend ? url.getLegend : undefined;
	this.panelTitle = $("<p>").text("Now editing").css({
		"margin" : "0",
		"float" : "left"
	});
	var addIcon = $("<i>").addClass("fas").addClass("fa-plus");
	this.addBtn = $("<button>").addClass("gb-button-clear").append(addIcon)
			.css({
				"float" : "right"
			}).click(function() {
				that.openAddLayer();
			});
	var importFileIcon = $("<i>").addClass("far fa-lg").addClass(
			"fa-file-archive");
	this.importFileBtn = $("<button>").addClass("gb-button-clear").append(
			importFileIcon).css({
		"float" : "right"
	}).click(function() {
		that.createUploadModal();
	});
	var addImgIcon = $("<i>").addClass("far fa-lg").addClass("fa-file-image");
	this.addImgBtn = $("<button>").addClass("gb-button-clear").append(
			addImgIcon).css({
		"float" : "right"
	}).click(function() {
		that.createImageModal();
	});
	var refIcon = $("<i>").addClass("fas").addClass("fa-sync-alt");
	this.refBtn = $("<button>").addClass("gb-button-clear").append(refIcon)
			.css({
				"float" : "right"
			}).click(function() {
				that.refreshList();
			});
	var searchIcon = $("<i>").addClass("fas").addClass("fa-search");
	this.searchBtn = $("<button>").addClass("gb-button-clear").append(
			searchIcon).css({
		"float" : "right"
	}).click(function() {
		that.openSearchBar();
	});

	this.titleArea = $("<div>").append(this.panelTitle).append(this.searchBtn)
			.append(this.refBtn).append(this.addImgBtn).append(
					this.importFileBtn).append(this.addBtn);

	this.searchInput = $("<input>").attr({
		"type" : "text"
	}).css({
		"border" : "0",
		"border-bottom" : "solid 1px #909090",
		"background-color" : "transparent",
		"width" : "90%"
	});
	this.tout = false;
	$(this.searchInput).keyup(function() {
		if (that.tout) {
			clearTimeout(that.tout);
		}
		that.tout = setTimeout(function() {
			var v = $(that.searchInput).val();
			that.getJSTree().search(v);
		}, 250);
	});
	var closeIcon = $("<i>").addClass("fas").addClass("fa-times");
	this.closeSearchBtn = $("<button>").addClass("gb-button-clear").append(
			closeIcon).css({
		"float" : "right"
	}).click(function() {
		$(that.searchInput).val("");
		that.getJSTree().search("");
		that.closeSearchBar();
	});
	this.searchArea = $("<div>").css({
		"display" : "none"
	}).append(this.searchInput).append(this.closeSearchBtn);
	this.panelHead = $("<div>").addClass("gb-article-head").append(
			this.titleArea).append(this.searchArea);
	this.panelBody = $("<div>").addClass("gb-article-body").css({
		"overflow-y" : "auto"
	});
	this.panel = $("<div>").addClass("gb-article").css({
		"margin" : "0"
	}).append(this.panelHead).append(this.panelBody);
	if (typeof options.append === "string") {
		$(options.append).append(this.panel);
	} else if ($(options.append).is("div")) {
		$(options.append).append(this.panel);
	}

	$(document).ready(function() {
		var parentHeight = $(that.panel).parent().innerHeight();
		var headHeight = $(that.panel).find(".gb-article-head").outerHeight();
		var bodyHeight = parentHeight - headHeight;
		$(that.panelBody).outerHeight(bodyHeight);
	});
	$(window).resize(function() {
		var parentHeight = $(that.panel).parent().innerHeight();
		var headHeight = $(that.panel).find(".gb-article-head").outerHeight();
		var bodyHeight = parentHeight - headHeight;
		$(that.panelBody).outerHeight(bodyHeight);
	});
	setTimeout(function() {
		var parentHeight = $(that.panel).parent().innerHeight();
		var headHeight = $(that.panel).find(".gb-article-head").outerHeight();
		var bodyHeight = parentHeight - headHeight;
		$(that.panelBody).outerHeight(bodyHeight);
	}, 1000);

	$(this.panelBody).jstreeol3(
			{
				"core" : {
					"map" : this.map,
					"animation" : 0,
					"themes" : {
						"stripes" : true
					},
				},
				"layerproperties" : {
					"properties" : undefined,
					"navigator": new gb.layer.Navigator({
						map: this.map,
						token: this.token
					}),
					"layerRecord" : undefined,
					"featureRecord" : options.frecord,
					"style" : new gb.style.LayerStyle({}),
					"editingTool" : this.editingTool
				},
				"search" : {
					show_only_matches : true
				},
				"legends" : {
					"types" : {
						"#" : {
							"valid_children" : [ "default", "Group", "Raster",
									"ImageTile", "Polygon", "MultiPolygon",
									"LineString", "MultiLineString", "Point",
									"MultiPoint" ]
						},
						// 편집도구에서 지원할 타입
						"Group" : {
							"icon" : "far fa-folder",
							"valid_children" : [ "default", "Group", "Raster",
									"ImageTile", "Polygon", "MultiPolygon",
									"LineString", "MultiLineString", "Point",
									"MultiPoint" ]
						},
						// 이외의 기본형
						"default" : {
							"icon" : "fas fa-file",
							"valid_children" : []
						},
						"Raster" : {
							"icon" : "fas fa-file-image",
							"valid_children" : []
						},
						"ImageTile" : {
							"icon" : "fas fa-file-image",
							"valid_children" : []
						},
						"Polygon" : {
							"icon" : "gb-icon"
						},
						"MultiPolygon" : {
							"icon" : "gb-icon"
						},
						"LineString" : {
							"icon" : "gb-icon"
						},
						"MultiLineString" : {
							"icon" : "gb-icon"
						},
						"Point" : {
							"icon" : "gb-icon"
						},
						"MultiPoint" : {
							"icon" : "gb-icon"
						}
					},
					"geoserver" : {
						"url" : this.getLegend,
						"width" : "15",
						"height" : "15",
						"format" : "image/png"
					}
				},
				"functionmarker" : {
					"snapping" : "fas fa-magnet"
				},
				plugins : [ "contextmenu", "dnd", "search", "state", "sort",
						"visibility", "layerproperties", "legends",
						"functionmarker" ]
			});
	this.jstree = $(this.panelBody).jstreeol3(true);
};
gb.tree.OpenLayers.prototype = Object.create(gb.tree.OpenLayers.prototype);
gb.tree.OpenLayers.prototype.constructor = gb.tree.OpenLayers;

/**
 * jstree가 적용된 jquery 객체를 반환한다.
 * 
 * @method gb.tree.OpenLayers#getJSTreeElement
 */
gb.tree.OpenLayers.prototype.getJSTreeElement = function() {
	return $(this.panelBody);
};

/**
 * jstree 객체를 반환한다.
 * 
 * @method gb.tree.OpenLayers#getJSTree
 */
gb.tree.OpenLayers.prototype.getJSTree = function() {
	return this.jstree;
};

/**
 * jstree 객체를 설정한다.
 * 
 * @method gb.tree.OpenLayers#setJSTree
 */
gb.tree.OpenLayers.prototype.setJSTree = function(jstree) {
	this.jstree = jstree;
};

/**
 * EditingTool 객체를 설정한다.
 * 
 * @method gb.tree.OpenLayers#setEditingTool
 */
gb.tree.OpenLayers.prototype.setEditingTool = function(param) {
	this.jstree._data.layerproperties.editingTool = param;
};

/**
 * Openlayers 삭제 확인창을 연다.
 * 
 * @method gb.tree.OpenLayers#openDeleteLayer
 */
gb.tree.OpenLayers.prototype.openDeleteLayer = function(layer) {
	console.log("open delete geoserver");
	var msg1 = $("<div>").text("Are you sure to delete this server?");
	var msg2 = $("<div>").text('"' + layer + '"');
	var body = $("<div>").append(msg1).append(msg2);
	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Cancel")
			.click(function() {
			});
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Delete")
			.click(function() {
			});
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn)
			.append(closeBtn);
	var modalFooter = $("<div>").addClass("gb-modal-footer").append(buttonArea);
	var deleteModal = new gb.modal.Base({
		"title" : "Delete GeoServer",
		"width" : 540,
		"height" : 400,
		"autoOpen" : false,
		"body" : body
	});
	$(deleteModal.getModal()).append(modalFooter);
	deleteModal.open();
};

/**
 * 목록을 새로고침한다.
 * 
 * @method gb.tree.OpenLayers#refreshList
 */
gb.tree.OpenLayers.prototype.refreshList = function() {
	console.log("refresh");
	this.getJSTree().refresh();
};
/**
 * 레이어 검색창을 연다.
 * 
 * @method gb.tree.OpenLayers#openSearchBar
 */
gb.tree.OpenLayers.prototype.openSearchBar = function() {
	console.log("open search on geoserver");
	$(this.titleArea).css({
		"display" : "none"
	});
	$(this.searchArea).css({
		"display" : "block"
	});
};
/**
 * 레이어 검색창을 닫는다.
 * 
 * @method gb.tree.OpenLayers#closeSearchBar
 */
gb.tree.OpenLayers.prototype.closeSearchBar = function() {
	console.log("close search geoserver");
	$(this.titleArea).css({
		"display" : "block"
	});
	$(this.searchArea).css({
		"display" : "none"
	});
};

/**
 * Layer 생성창을 연다.
 * 
 * @method gb.tree.OpenLayers#openAddLayer
 */
gb.tree.OpenLayers.prototype.openAddLayer = function() {
	var that = this;

	var col1 = $("<div>").addClass("col-md-2").append(
			this.translation.layerCode[this.locale]);
	var codeInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : this.translation.exLayerCodeField[this.locale]
	}).addClass("form-control").addClass("gb-layerdefinition-input-layercode");

	var col2 = $("<div>").addClass("col-md-10").append(codeInput);
	var row1 = $("<div>").addClass("row").append(col1).append(col2).css({
		"margin-bottom" : "15px"
	});
	;

	var col3 = $("<div>").addClass("col-md-2").text(
			this.translation.layerType[this.locale]);
	var geomSelect = $("<select>").addClass("form-control").addClass(
			"gb-layerdefinition-select-geometry");
	for (var i = 0; i < this.geometryType.length; i++) {
		var option = $("<option>").text(this.geometryType[i].toUpperCase())
				.attr("value", this.geometryType[i]);
		if (i === 0) {
			$(option).attr("selected", "selected");
		}
		$(geomSelect).append(option);
	}
	var col4 = $("<div>").addClass("col-md-10").append(geomSelect);
	var row2 = $("<div>").addClass("row").append(col3).append(col4);

	var well = $("<div>").addClass("well").append(row1).append(row2);

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Add");

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn)
			.append(closeBtn);
	var modalFooter = $("<div>").append(buttonArea);

	var gBody = $("<div>").append(well).css({
		"display" : "table",
		"width" : "100%"
	});
	var addGeoServerModal = new gb.modal.Base({
		"title" : this.translation.addLayer[this.locale],
		"width" : 540,
		"height" : 250,
		"autoOpen" : true,
		"body" : gBody,
		"footer" : modalFooter
	});
	$(closeBtn).click(function() {
		addGeoServerModal.close();
	});
	$(okBtn).click(
		function() {
			var geoType = { 
				"point": "Point", 
				"linestring": "LineString", 
				"polygon": "Polygon", 
				"multipoint": "MultiPoint",
				"multilinestring": "MultiLineString", 
				"multipolygon": "MultiPolygon"
			};
			var vectorLayer = new ol.layer.Vector({
				source : new ol.source.Vector({})
			});
			var type = geomSelect.find("option:selected").val();
			var gitLayer = {
				"editable" : true,
				"geometry" : geoType[type],
				"validation" : false
			};
			vectorLayer.set("git", gitLayer);
			vectorLayer.set("name", codeInput.val());
			that.map.addLayer(vectorLayer);
			addGeoServerModal.close();
		});
};

/**
 * Shp file 업로드창을 생성한다.
 * 
 * @method gb.tree.OpenLayers#createUploadModal
 */
gb.tree.OpenLayers.prototype.createUploadModal = function() {
	var that = this;

	var file;

	// 파일 선택 input
	var fileSelect = $("<input type='file' id='layer_shp_file' accept='.zip'>")
			.change(function() {
				if (!!this.files) {
					file = this.files[0];
					if (file.size > 0) {
						fileInfo.text(file.name + ' , ' + file.size + ' kb');
					}
				}
			});

	var uploadBtn = $("<button type='button'>").addClass(
			"btn btn-primary btn-lg btn-block").text("Upload zip file")
			.mouseenter(function() {
				$(this).css({
					"background-color" : "#00c4bc"
				});
			}).mouseleave(function() {
				$(this).css({
					"background-color" : "#00b5ad"
				});
			}).click(function() {
				fileSelect.click();
			}).css({
				"background-color" : "#00b5ad",
				"border-color" : "transparent"
			});

	var fileInfo = $("<div role='alert'>").addClass("alert alert-light").css({
		"text-align" : "center"
	});

	var epsg = $("<div>").addClass("col-md-2").append("EPSG");
	var epsgInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : "Default: 4326"
	}).addClass("form-control");

	var col1 = $("<div>").addClass("col-md-10").append(epsgInput);
	var row1 = $("<div>").addClass("row").append(epsg).append(col1).css({
		"margin-bottom" : "15px"
	});

	var encode = $("<div>").addClass("col-md-2").append("Encoding");
	var encodeInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : "Set your desired encoding UTF-8, Big5, Big5-HKSCS ..."
	}).addClass("form-control");

	var col2 = $("<div>").addClass("col-md-10").append(encodeInput);
	var row2 = $("<div>").addClass("row").append(encode).append(col2);

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Add");

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn)
			.append(closeBtn);
	var modalFooter = $("<div>").append(buttonArea);

	var gBody = $("<div>").append(uploadBtn).append(fileInfo).append(row1)
			.append(row2).css({
				"display" : "table",
				"width" : "100%"
			});

	var addGeoServerModal = new gb.modal.Base({
		"title" : this.translation.addLayer[this.locale],
		"width" : 540,
		"height" : 350,
		"autoOpen" : true,
		"body" : gBody,
		"footer" : modalFooter
	});
	$(closeBtn).click(function() {
		addGeoServerModal.close();
	});
	$(okBtn).click(function() {
		gb.tree.loadShpZip(epsgInput.val(), encodeInput.val(), file, that.map);
		addGeoServerModal.close();
	});
};

gb.tree.loadShpZip = function(epsg, encode, file, map) {
	var epsg = epsg || 4326;
	var encode = encode || "EUC-KR";
	var fileL = file;
	if (fileL.name.split(".")[1] === "zip") {

		loadshp({
			url : fileL,
			encoding : encode
		}, function(geojson) {
			var features = (new ol.format.GeoJSON()).readFeatures(geojson);

			if (!!features.length) {
				var vectorLayer = new ol.layer.Vector({
					source : new ol.source.Vector({
						features : features
					})
				});
				var gitLayer = {
					"editable" : true,
					"geometry" : features[0].getGeometry().getType(),
					"validation" : false
				};
				vectorLayer.set("git", gitLayer);
				vectorLayer.set("name", fileL.name);
				map.addLayer(vectorLayer);
				map.getView().fit([ geojson.bbox[1], geojson.bbox[0] ],
						[ geojson.bbox[3], geojson.bbox[2] ]);
			}
		});
	}
}

/**
 * Image file 업로드창을 생성한다.
 * 
 * @method gb.tree.OpenLayers#createImageModal
 */
gb.tree.OpenLayers.prototype.createImageModal = function() {
	var that = this;

	var file, result;

	// 파일 선택 input
	var fileSelect = 
		$("<input type='file' accept='image/*'>")
			.change(function() {
				if (!!this.files) {
					file = this.files[0];
					if (file.size > 0) {
						fileInfo.text(file.name + ' , ' + file.size + ' kb');
						var reader = new FileReader();
						reader.onload = function(){
							var output = document.getElementById('imagePreview');
							var image = new Image();
							
							output.src = reader.result;
							
							image.src = reader.result;
							image.onload = function(){
								new gb.layer.ImageLayer({
									map: that.map,
									url: reader.result,
									width: image.width,
									height: image.height,
									title: file.name
								});
							}
						}
						reader.readAsDataURL(file);
					}
				}
			});
	
	// Iamge preview
	var preview = $("<img id='imagePreview' height='218' width='518'>");

	var uploadBtn = $("<button type='button'>").addClass(
			"btn btn-primary btn-lg btn-block").text("Upload Image")
			.mouseenter(function() {
				$(this).css({
					"background-color" : "#00c4bc"
				});
			}).mouseleave(function() {
				$(this).css({
					"background-color" : "#00b5ad"
				});
			}).click(function() {
				fileSelect.click();
			}).css({
				"background-color" : "#00b5ad",
				"border-color" : "transparent"
			});

	var fileInfo = $("<div role='alert'>").addClass("alert alert-light").css({
		"text-align" : "center"
	});

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Add");

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn)
			.append(closeBtn);
	var modalFooter = $("<div>").append(buttonArea);

	var gBody = $("<div>").append(uploadBtn).append(fileInfo).append(preview)
			.css({
				"display" : "table",
				"width" : "100%"
			});

	var addGeoServerModal = new gb.modal.Base({
		"title" : this.translation.addLayer[this.locale],
		"width" : 540,
		"height" : 460,
		"autoOpen" : true,
		"body" : gBody,
		"footer" : modalFooter
	});
	
	$(closeBtn).click(function() {
		addGeoServerModal.close();
	});
	
	$(okBtn).click(function() {
		addGeoServerModal.close();
	});
};
