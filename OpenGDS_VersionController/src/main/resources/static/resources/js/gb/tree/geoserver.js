/**
 * 지오서버 커스텀 플러그인 로드 필요
 * @external "jsTree-geoserver plugin"
 */

/**
 * 지오서버 레이어 목록을 표시한다.
 * 
 * @class gb.tree.GeoServer
 * @memberof gb.tree
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String |
 *            Element} obj.append - 영역 본문이 삽입될 부모 노드의 ID 또는 Class 또는 Element
 * @param {jstreeol3}
 *            jstree - 클라이언트 레이어 트리 객체
 * @param {ol.Map}
 *            obj.map - 편집 영역을 담당하는 ol.Map
 * @param {gb.geoserver.UploadSHP}
 *            obj.uploadSHP - SHP 파일 업로드 객체
 * @param {Object}
 *            obj.url - 요청을 처리하기 위한 URL 객체
 * @param {String}
 *            obj.url.getTree - 지오서버 트리 구조를 요청하기 위한 URL
 * @param {String}
 *            obj.url.addGeoServer - 지오서버를 추가하기 위한 URL
 * @param {String}
 *            obj.url.deleteGeoServer - 지오서버를 삭제하기 위한 URL
 * @param {String}
 *            obj.url.getMapWMS - WMS 레이어를 요청하기 위한 URL
 * @author SOYIJUN
 * @date 2018.07.02
 * @version 0.01
 * 
 */
gb.tree.GeoServer = function(obj) {
	var that = this;
	var options = obj ? obj : {};
	this.map = options.map instanceof ol.Map ? options.map : undefined;
	var url = options.url ? options.url : undefined;
	this.clientTree = options.clientTree ? options.clientTree : undefined;
	this.properties = options.properties || undefined;
	this.getTreeURL = url.getTree ? url.getTree : undefined;
	this.addGeoServerURL = url.addGeoServer ? url.addGeoServer : undefined;
	this.deleteGeoServerURL = url.deleteGeoServer ? url.deleteGeoServer : undefined;
	this.deleteGeoServerLayerURL = url.deleteGeoServerLayer ? url.deleteGeoServerLayer : undefined;
	this.getMapWMS = url.getMapWMS ? url.getMapWMS : undefined;
	this.getWFSFeature = url.getWFSFeature ? url.getWFSFeature : undefined;
	this.getLayerInfo = url.getLayerInfo ? url.getLayerInfo : undefined;
	this.uploadSHP = options.uploadSHP ? options.uploadSHP : undefined;
	this.downloadGeoserver = url.downloadGeoserver || undefined;
	this.panelTitle = $("<p>").text("GeoServer").css({
		"margin" : "0",
		"float" : "left"
	});
	var addIcon = $("<i>").addClass("fas").addClass("fa-plus");
	this.addBtn = $("<button>").addClass("gb-button-clear").append(addIcon).css({
		"float" : "right"
	}).click(function() {
		that.openAddGeoServer();
	});
	var refIcon = $("<i>").addClass("fas").addClass("fa-sync-alt");
	this.refBtn = $("<button>").addClass("gb-button-clear").append(refIcon).css({
		"float" : "right"
	}).click(function() {
		that.refreshList();
	});
	var searchIcon = $("<i>").addClass("fas").addClass("fa-search");
	this.searchBtn = $("<button>").addClass("gb-button-clear").append(searchIcon).css({
		"float" : "right"
	}).click(function() {
		that.openSearchBar();
	});
	this.titleArea = $("<div>").append(this.panelTitle).append(this.searchBtn).append(this.refBtn).append(this.addBtn);
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
	this.closeSearchBtn = $("<button>").addClass("gb-button-clear").append(closeIcon).css({
		"float" : "right"
	}).click(function() {
		$(that.searchInput).val("");
		that.getJSTree().search("");
		that.closeSearchBar();
	});
	this.searchArea = $("<div>").css({
		"display" : "none"
	}).append(this.searchInput).append(this.closeSearchBtn);
	this.panelHead = $("<div>").addClass("gb-article-head").append(this.titleArea).append(this.searchArea);
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

	$(this.panelBody).jstree(
			{
				"core" : {
					"animation" : 0,
					"check_callback" : true,
					"themes" : {
						"stripes" : true
					},
					/*
					 * 'data' : [ { "id" : "geoserver1", "parent" : "#", "text" :
					 * "geoserver1", "type" : "geoserver" }, { "id" :
					 * "workspace1", "parent" : "geoserver1", "text" :
					 * "workspace1", "type" : "workspace" }, { "id" :
					 * "datastore1", "parent" : "workspace1", "text" :
					 * "datastore1", "type" : "datastore" }, { "id" : "layer1",
					 * "parent" : "datastore1", "text" : "layer1", "type" :
					 * "polygon" }, { "id" : "layer2", "parent" : "datastore1",
					 * "text" : "layer2", "type" : "linestring" }, { "id" :
					 * "layer3", "parent" : "datastore1", "text" : "layer3",
					 * "type" : "point" }, { "id" : "raster1", "parent" :
					 * "datastore1", "text" : "raster1", "type" : "raster" } ]
					 */

					"data" : {
						'url' : function(node) {
							var url = that.getGetTreeURL();
							// if (node.id === "#") {
							// url = that.getGetTreeURL() + "&type=server";
							// } else if (node.type === "workspace") {
							// url = that.getGetTreeURL() + "&type=workspace" +
							// "&parent=" + node.parent + "&serverName=" +
							// node.parent;
							// } else if (node.type === "datastore") {
							// url = that.getGetTreeURL() + "&type=datastore" +
							// "&parent=" + node.parent + "&serverName="
							// + node.parents[1];
							// } else if (node.type === "point" || node.type ===
							// "multipoint" || node.type === "linestring"
							// || node.type === "multilinestring" || node.type
							// === "polygon" || node.type === "multipolygon") {
							// url = that.getGetTreeURL() + "&type=layer" +
							// "&parent=" + node.parent + "&serverName=" +
							// node.parents[2];
							// }
							// return that.getGetTreeURL();
							console.log(url);
							return url;
						},
						"data" : function(node) {
							var obj = {};
							if (node.id === "#") {
								obj["type"] = "server";
							} else if (node.type === "geoserver") {
								obj["type"] = "workspace";
								obj["serverName"] = node.id;
								obj["node"] = node.id;
							} else if (node.type === "workspace") {
								obj["type"] = "datastore";
								obj["serverName"] = node.parent;
								obj["node"] = node.id;
							} else if (node.type === "datastore") {
								obj["type"] = "layer";
								obj["serverName"] = node.parents[1];
								obj["node"] = node.id
							}
							// else if (node.type === "point" || node.type ===
							// "multipoint" || node.type === "linestring"
							// || node.type === "multilinestring" || node.type
							// === "polygon" || node.type === "multipolygon") {
							//
							// }
							console.log(obj);
							return obj;
						}
					}

				},
				"geoserver" : {
					"map" : this.map instanceof ol.Map ? this.map : undefined,
					"getMapWMS" : this.getMapWMS,
					"getLayerInfo" : this.getLayerInfo,
					"clientTree" : this.clientTree,
					"getWFSFeature" : this.getWFSFeature
				// "user" : "admin",
				// "layerInfo" : undefined,
				// "layerInfoURL" : "geoserver/getGeoLayerInfoList.ajax",
				// "groupLayerInfoURL" :
				// "geoserver/getGeoGroupLayerInfoList.ajax",
				// "WMSLayerURL" : "http://175.116.181.42:9990/geoserver/wms",
				// "createLayer" : undefined,
				// "deleteGroupLayer" : undefined,
				// "deleteLayer" : undefined,
				// "downloadNGIDXF" : "fileExport/fileExport.ajax",
				// "clientRefer" : undefined
				},
				"search" : {
					show_only_matches : true
				},
				"contextmenu" : {
					items : function(o, cb) { // Could be an object
						// directly
						return {
							"import" : {
								"separator_before" : true,
								"icon" : "fas fa-file-import",
								"separator_after" : true,
								"label" : "Import",
								"action" : false,
								"submenu" : {
									"wms" : {
										"separator_before" : false,
										"icon" : "fa fa-file-image-o",
										"separator_after" : false,
										"label" : "WMS",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var nodes = inst.get_selected();
											console.log(nodes);
											var selectedNum = nodes.length;
											if (selectedNum === 1) {
												var node = inst.get_node(nodes[0]);
												var type = inst.get_type(node);
												if (type === "workspace") {
													inst.load_each_wms_layer(node, that.map.getLayers());
												} else if (type === "datastore") {
													inst.load_each_wms_layer(node, that.map.getLayers());
												} else if (type === "point" || type === "multipoint" || type === "linestring"
														|| type === "multilinestring" || type === "polygon" || type === "multipolygon") {
													console.log(node);
													inst.load_each_wms_layer(node, that.map.getLayers());
												}
											} else if (selectedNum > 1) {
												var serverNum = 0;
												var workNum = 0;
												var storeNum = 0;
												var layerNum = 0;
												for (var i = 0; i < nodes.length; i++) {
													var node = inst.get_node(nodes[i]);
													var type = inst.get_type(node);
													if (type === "geoserver") {
														serverNum++;
													} else if (type === "workspace") {
														workNum++;
													} else if (type === "datastore") {
														storeNum++;
													} else if (type === "layer") {
														layerNum++;
													}
												}
												if (selectedNum !== serverNum && selectedNum !== workNum && selectedNum !== storeNum
														&& selectedNum !== layerNum) {
													console.log("");
												}
											}
										}
									},
									"wfs" : {
										"separator_before" : false,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "WFS",
										"_disabled" : true,
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											inst.import_vector();
										}
									}
								}
							},
							"export" : {
								"separator_before" : true,
								"icon" : "fas fa-file-export",
								"separator_after" : true,
								"label" : "Export",
								"action" : false,
								"submenu" : {
									"shp" : {
										"separator_before" : true,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "SHP",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											for (var i = 0; i < selectedObj.length; i++) {
												inst.download_wfs_layer(selectedObj[i], "shape-zip");
											}
										}
									},
									"gml2" : {
										"separator_before" : false,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "GML2",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											for (var i = 0; i < selectedObj.length; i++) {
												inst.download_wfs_layer(selectedObj[i], "gml2");
											}
										}
									},
									"gml3" : {
										"separator_before" : false,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "GML3",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											for (var i = 0; i < selectedObj.length; i++) {
												inst.download_wfs_layer(selectedObj[i], "gml3");
											}
										}
									},
									"json" : {
										"separator_before" : false,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "JSON",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											for (var i = 0; i < selectedObj.length; i++) {
												inst.download_wfs_layer(selectedObj[i], "application/json");
											}
										}
									},
									"csv" : {
										"separator_before" : false,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "CSV",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											for (var i = 0; i < selectedObj.length; i++) {
												inst.download_wfs_layer(selectedObj[i], "csv");
											}
										}
									},
									"png" : {
										"separator_before" : true,
										"icon" : "fa fa-file-excel-o",
										"separator_after" : false,
										"label" : "PNG",
										"action" : function(data) {
											var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
											var selected = inst.get_selected();
											var selectedObj = inst.get_selected(true);
											for (var i = 0; i < selectedObj.length; i++) {
												if (selectedObj[i].type === "datastore" || selectedObj[i].type === "workspace"
														|| selectedObj[i].type === "geoserver") {
													console.error("not support");
													return;
												}
											}

											var a = {
												serverName : undefined,
												workspace : undefined,
												geoLayerList : undefined
											};

											for (var i = 0; i < selectedObj.length; i++) {
												a.serverName = selectedObj[i].id.split(":")[0];
												a.workspace = selectedObj[i].id.split(":")[1];
												a.geoLayerList = [ selectedObj[i].id.split(":")[3] ];

												$.ajax({
													url : inst._data.geoserver.getLayerInfo,
													method : "POST",
													contentType : "application/json; charset=UTF-8",
													cache : false,
													data : JSON.stringify(a),
													beforeSend : function() { // 호출전실행
														// loadImageShow();
													},
													traditional : true,
													success : function(data, textStatus, jqXHR) {
														var path = inst._data.geoserver.getMapWMS;

														for (var i = 0; i < data.length; i++) {
															data[i].serverName = a.serverName;
															data[i].workspace = a.workspace;
															inst.download_wms_layer(data[i], "image/png");
														}
													}
												});
											}
										}
									}
								}
							},
							"upload" : {
								"separator_before" : false,
								"icon" : "fas fa-upload",
								"separator_after" : false,
								"_disabled" : function() {
									console.log(o);
									console.log(cb);
									var result = true;
									if (o.type === "datastore") {
										result = false;
									}
									return result;
								},
								// data.reference,
								// this.get_parent(data.reference),
								// "")),
								"label" : "Upload",
								/*
								 * ! "shortcut" : 113, "shortcut_label" : 'F2',
								 * "icon" : "glyphicon glyphicon-leaf",
								 */
								"action" : function(data) {
									var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
									if (obj.type === "datastore") {
										var upload = that.getUploadSHP();
										var datastore = obj.text;
										var workspace = inst.get_node(obj.parent).text;
										var geoserver = inst.get_node(obj.parents[1]).text;
										console.log(datastore);
										console.log(workspace);
										console.log(geoserver);
										upload.setGeoServer(geoserver);
										upload.setWorkspace(workspace);
										upload.setDatastore(datastore);
										upload.open();
									}
								}
							},
							"delete" : {
								"separator_before" : false,
								"icon" : "fa fa-trash",
								"separator_after" : false,
								"_disabled" : function() {
									console.log(o);
									console.log(cb);
									var result = true;
									if (o.type === "geoserver" || o.type === "point" || o.type === "multipoint" || o.type === "linestring"
											|| o.type === "multilinestring" || o.type === "polygon" || o.type === "multipolygon") {
										result = false;
									}
									return result;
								},
								"label" : "Delete",
								/*
								 * ! "shortcut" : 113, "shortcut_label" : 'F2',
								 * "icon" : "glyphicon glyphicon-leaf",
								 */
								"action" : function(data) {
									var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
									if (obj.type === "geoserver") {
										that.openDeleteGeoServer(obj.id);
									} else if (obj.type === "geoserver" || obj.type === "point" || obj.type === "multipoint"
											|| obj.type === "linestring" || obj.type === "multilinestring" || obj.type === "polygon"
											|| obj.type === "multipolygon") {
										var server = inst.get_node(obj.parents[2]);
										var work = inst.get_node(obj.parents[1]);
										var layer = obj;
										that.openDeleteGeoServerLayer(server.text, work.text, layer.text);
									}
								}
							},
							"properties" : {
								"separator_before" : false,
								"icon" : "fa fa-info-circle",
								"separator_after" : false,
								"_disabled" : function() {
									console.log(o);
									console.log(cb);
									var result = true;
									if (o.type === "point" || o.type === "multipoint" || o.type === "linestring"
											|| o.type === "multilinestring" || o.type === "polygon" || o.type === "multipolygon") {
										result = false;
									}
									return result;
								},
								"label" : "Properties",
								"action" : function(data) {
									var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
									/*
									 * if (obj.type === "geoserver") {
									 * that.openDeleteGeoServer(obj.id); }
									 */
									if (inst.is_selected(obj)) {
										that.properties.setForm({
											geoserver : obj.id.split(":")[0],
											workspace : obj.id.split(":")[1],
											datastore : obj.id.split(":")[2],
											layername : obj.id.split(":")[3]
										});
										that.properties.open();
									} else {
										// inst.delete_node_layer(obj);
									}
								}
							}
						};
					}
				},
				types : {
					"#" : {
						"valid_children" : [ "geoserver", "default" ]
					},
					"default" : {
						"icon" : "fas fa-exclamation-circle"
					},
					"geoserver" : {
						"icon" : "fas fa-globe",
						"valid_children" : [ "workspace" ]
					},
					"workspace" : {
						"icon" : "fas fa-archive",
						"valid_children" : [ "datastore" ]
					},
					"datastore" : {
						"icon" : "fas fa-hdd",
						"valid_children" : [ "raster", "polygon", "multipolygon", "linestring", "multilinestring", "point", "multipoint" ]
					},
					"raster" : {
						"icon" : "fas fa-chess-board"
					},
					"polygon" : {
						"icon" : "fas fa-square-full"
					},
					"multipolygon" : {
						"icon" : "fas fa-square-full"
					},
					"linestring" : {
						"icon" : "fas fa-minus fa-lg gb-fa-rotate-135"
					},
					"multilinestring" : {
						"icon" : "fas fa-minus fa-lg gb-fa-rotate-135"
					},
					"point" : {
						"icon" : "fas fa-circle gb-fa-xxs"
					},
					"multipoint" : {
						"icon" : "fas fa-circle gb-fa-xxs"
					}
				},
				"plugins" : [ "contextmenu", "search", "types", "geoserver" ]
			});
	this.jstree = $(this.panelBody).jstree(true);
	if (!!this.properties) {
		this.properties.setRefer(this.jstree);
	}
};
gb.tree.GeoServer.prototype = Object.create(gb.tree.GeoServer.prototype);
gb.tree.GeoServer.prototype.constructor = gb.tree.GeoServer;

/**
 * jstree 객체를 반환한다.
 * 
 * @method gb.tree.GeoServer#getJSTree
 * @return {$.jstree} GeoServer 목록을 표출할 jsTree 객체
 */
gb.tree.GeoServer.prototype.getJSTree = function() {
	return this.jstree;
};

/**
 * jstree 객체를 설정한다.
 * 
 * @method gb.tree.GeoServer#setJSTree
 */
gb.tree.GeoServer.prototype.setJSTree = function(jstree) {
	this.jstree = jstree;
};

/**
 * GeoServer 등록창을 연다.
 * 
 * @method gb.tree.GeoServer#openAddGeoServer
 */
gb.tree.GeoServer.prototype.openAddGeoServer = function() {
	var that = this;
	var gName = $("<div>").text("Name: ").css({
		"display" : "table-cell",
		"width" : "20%",
		"text-align" : "right",
		"vertical-align" : "middle"
	});
	var gNameInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : "EX) Geoserver",
	}).css({
		"width" : "83%",
		"border" : "none",
		"border-bottom" : "solid 1px #a9a9a9",
		"margin-left" : "8px"
	});
	var gNameInputDiv = $("<div>").append(gNameInput).css({
		"display" : "table-cell",
		"width" : "80%",
		"vertical-align" : "middle"
	});
	var gNameArea = $("<div>").append(gName).append(gNameInputDiv).css({
		"display" : "table-row"
	});

	var gURL = $("<div>").text("URL: ").css({
		"display" : "table-cell",
		"width" : "20%",
		"text-align" : "right",
		"vertical-align" : "middle"
	});
	var gURLInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : "EX) http://127.0.0.1:9990/geoserver"
	}).css({
		"width" : "83%",
		"border" : "none",
		"border-bottom" : "solid 1px #a9a9a9",
		"margin-left" : "8px"
	});
	var gURLInputDiv = $("<div>").append(gURLInput).css({
		"display" : "table-cell",
		"width" : "80%",
		"vertical-align" : "middle"
	});
	var gURLArea = $("<div>").append(gURL).append(gURLInputDiv).css({
		"display" : "table-row"
	});

	var gID = $("<div>").text("ID: ").css({
		"display" : "table-cell",
		"width" : "20%",
		"text-align" : "right",
		"vertical-align" : "middle"
	});
	var gIDInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : "EX) admin"
	}).css({
		"width" : "83%",
		"border" : "none",
		"border-bottom" : "solid 1px #a9a9a9",
		"margin-left" : "8px"
	});
	var gIDInputDiv = $("<div>").append(gIDInput).css({
		"display" : "table-cell",
		"width" : "80%",
		"vertical-align" : "middle"
	});
	var gIDArea = $("<div>").append(gID).append(gIDInputDiv).css({
		"display" : "table-row"
	});

	var gPass = $("<div>").text("Password: ").css({
		"display" : "table-cell",
		"width" : "20%",
		"text-align" : "right",
		"vertical-align" : "middle"
	});
	var gPassInput = $("<input>").attr({
		"type" : "password",
		"placeholder" : "EX) geoserver"
	}).css({
		"width" : "83%",
		"border" : "none",
		"border-bottom" : "solid 1px #a9a9a9",
		"margin-left" : "8px"
	});
	var gPassInputDiv = $("<div>").append(gPassInput).css({
		"display" : "table-cell",
		"width" : "80%",
		"vertical-align" : "middle"
	});
	var gPassArea = $("<div>").append(gPass).append(gPassInputDiv).css({
		"display" : "table-row"
	});

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Add");

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);
	var modalFooter = $("<div>").append(buttonArea);

	var gBody = $("<div>").append(gNameArea).append(gURLArea).append(gIDArea).append(gPassArea).css({
		"display" : "table",
		"padding" : "10px",
		"width" : "100%",
		"height" : "250px"
	});
	var addGeoServerModal = new gb.modal.Base({
		"title" : "Add GeoServer",
		"width" : 540,
		"height" : 400,
		"autoOpen" : true,
		"body" : gBody,
		"footer" : modalFooter
	});
	$(closeBtn).click(function() {
		addGeoServerModal.close();
	});
	$(okBtn).click(function() {
		that.addGeoServer($(gNameInput).val(), $(gURLInput).val(), $(gIDInput).val(), $(gPassInput).val(), addGeoServerModal);
	});

	gNameInput.val("geoserver32");
	gURLInput.val("http://175.116.181.32:9999/geoserver");
	gIDInput.val("admin");
	gPassInput.val("geoserver");
};

/**
 * GeoServer를 등록한다.
 * 
 * @method gb.tree.GeoServer#addGeoServer
 * @param {String}
 *            name - 지오서버의 이름
 * @param {String}
 *            url - 지오서버의 URL
 * @param {String}
 *            id - 지오서버 접속을 위한 ID
 * @param {String}
 *            password - 지오서버 접속을 위한 비밀번호
 * @param {gb.modal.Base}
 *            callback - 완료 후 창을 닫을 모달 객체
 */
gb.tree.GeoServer.prototype.addGeoServer = function(name, url, id, password, callback) {
	var that = this;
	console.log("add geoserver");
	console.log(name);
	console.log(url);
	console.log(id);
	console.log(password);
	var params = {
		"serverName" : name,
		"serverURL" : url,
		"Id" : id,
		"pw" : password
	};
	$.ajax({
		url : this.getAddGeoServerURL() + "&" + jQuery.param(params),
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		// data : params,
		beforeSend : function() {
			$("body").css("cursor", "wait");
		},
		complete : function() {
			$("body").css("cursor", "default");
		},
		success : function(data) {
			console.log(data);
			callback.close();
			if (data === 200) {
				that.refreshList();
			} else if (data === 600) {
				that.messageModal("Error", "Session expired.");
			} else if (data === 601) {
				that.messageModal("Error", "Missing parameter.");
			} else if (data === 602) {
				that.messageModal("Error", "Server name or URL duplicated.");
			} else if (data === 603) {
				that.messageModal("Error", "Geoserver not found.");
			}

		}
	});
};

/**
 * GeoServer 삭제 확인창을 연다.
 * 
 * @method gb.tree.GeoServer#openDeleteGeoServer
 */
gb.tree.GeoServer.prototype.openDeleteGeoServer = function(geoserver) {
	var that = this;
	console.log("open delete geoserver");
	var msg1 = $("<div>").text("Are you sure to delete this server?").css({
		"text-align" : "center",
		"font-size" : "16px"
	});
	var msg2 = $("<div>").text('"' + geoserver + '"').css({
		"text-align" : "center",
		"font-size" : "24px",
		"word-break" : "break-word"
	});
	var body = $("<div>").append(msg1).append(msg2);
	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Delete");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);
	var modalFooter = $("<div>").addClass("gb-modal-footer").append(buttonArea);
	var deleteModal = new gb.modal.Base({
		"title" : "Delete GeoServer",
		"width" : 310,
		"height" : 200,
		"autoOpen" : false,
		"body" : body
	});
	$(closeBtn).click(function() {
		deleteModal.close();
	});
	$(okBtn).click(function() {
		that.deleteGeoServer(geoserver, deleteModal);
	});
	$(deleteModal.getModal()).append(modalFooter);
	deleteModal.open();
};

/**
 * GeoServer를 삭제한다.
 * 
 * @method gb.tree.GeoServer#deleteGeoServer
 * @param {String}
 *            geoserver - 삭제할 지오서버의 이름
 * @param {gb.modal.Base}
 *            callback - 완료후 창을 닫을 모달 객체
 */
gb.tree.GeoServer.prototype.deleteGeoServer = function(geoserver, callback) {
	var that = this;
	console.log("delete geoserver");
	var params = {
		"serverName" : geoserver
	};
	$.ajax({
		url : this.getDeleteGeoServerURL() + "&" + jQuery.param(params),
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		// data : params,
		beforeSend : function() {
			$("body").css("cursor", "wait");
		},
		complete : function() {
			$("body").css("cursor", "default");
		},
		success : function(data) {
			console.log(data);
			callback.close();
			if (data === 200) {
				that.refreshList();
			} else if (data === 600) {
				that.messageModal("Error", "Session expired.");
			} else if (data === 603) {
				that.messageModal("Error", "Geoserver not found.");
			} else if (data === 605) {
				that.messageModal("Error", "Nothing to delete.");
			}
		}
	});
};

/**
 * GeoServer 레이어 삭제 확인창을 연다.
 * 
 * @method gb.tree.GeoServer#openDeleteGeoServerLayer
 */
gb.tree.GeoServer.prototype.openDeleteGeoServerLayer = function(server, work, layer) {
	var that = this;
	console.log("open delete geoserver layer");
	console.log(layer);
	var msg1 = $("<div>").text("Are you sure to delete this layer?").css({
		"text-align" : "center",
		"font-size" : "16px"
	});
	var msg2 = $("<div>").text('"' + layer + '"').css({
		"text-align" : "center",
		"font-size" : "24px",
		"word-break" : "break-word"
	});
	var body = $("<div>").append(msg1).append(msg2);
	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Delete");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);
	var modalFooter = $("<div>").addClass("gb-modal-footer").append(buttonArea);
	var deleteModal = new gb.modal.Base({
		"title" : "Delete Layer",
		"width" : 310,
		"height" : 200,
		"autoOpen" : false,
		"body" : body
	});
	$(closeBtn).click(function() {
		deleteModal.close();
	});
	$(okBtn).click(function() {
		that.deleteGeoServerLayer(server, work, layer, deleteModal);
	});
	$(deleteModal.getModal()).append(modalFooter);
	deleteModal.open();
};

/**
 * GeoServer Layer 를 삭제한다.
 * 
 * @method gb.tree.GeoServer#deleteGeoServerLayer
 * @param {String}
 *            geoserver - 삭제할 레이어의 지오서버 이름
 * @param {String}
 *            work - 삭제할 레이어의 지오서버 워크스페이스 이름
 * @param {String}
 *            layer - 삭제할 레이어의 이름
 * @param {gb.modal.Base}
 *            callback - 완료후 창을 닫을 모달 객체
 */
gb.tree.GeoServer.prototype.deleteGeoServerLayer = function(geoserver, work, layer, callback) {
	var that = this;
	console.log("delete geoserver layer");
	var params = {
		"serverName" : geoserver,
		"workspace" : work,
		"layerList" : [ layer ]
	};

	$.ajax({
		url : this.getDeleteGeoServerLayerURL(),
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		data : JSON.stringify(params),
		beforeSend : function() {
			$("body").css("cursor", "wait");
		},
		complete : function() {
			$("body").css("cursor", "default");
		},
		success : function(data) {
			console.log(data);
			callback.close();
			if (data === 200) {
				that.refreshList();
			} else if (data === 500) {
				that.messageModal("Error", "Request failed.");
			} else if (data === 600) {
				that.messageModal("Error", "Session expired.");
			} else if (data === 603) {
				that.messageModal("Error", "Geoserver not found.");
			} else if (data === 605) {
				that.messageModal("Error", "Geoserver info not found.");
			} else if (data === 606) {
				that.messageModal("Error", "Some layers have not been deleted.");
			}
		}
	});
};

/**
 * GeoServer 목록을 새로고침한다.
 * 
 * @method gb.tree.GeoServer#refreshList
 */
gb.tree.GeoServer.prototype.refreshList = function() {
	console.log("refresh list");
	this.getJSTree().refresh();
};
/**
 * 레이어 검색창을 연다.
 * 
 * @method gb.tree.GeoServer#openSearchBar
 */
gb.tree.GeoServer.prototype.openSearchBar = function() {
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
 * @method gb.tree.GeoServer#closeSearchBar
 */
gb.tree.GeoServer.prototype.closeSearchBar = function() {
	console.log("close search geoserver");
	$(this.titleArea).css({
		"display" : "block"
	});
	$(this.searchArea).css({
		"display" : "none"
	});
};
/**
 * 지오서버 추가를 위한 URL을 반환한다.
 * 
 * @method gb.tree.GeoServer#getAddGeoServerURL
 */
gb.tree.GeoServer.prototype.getAddGeoServerURL = function() {
	return this.addGeoServerURL;
};
/**
 * 지오서버 추가를 위한 URL을 설정한다.
 * 
 * @method gb.tree.GeoServer#setAddGeoServerURL
 */
gb.tree.GeoServer.prototype.setAddGeoServerURL = function(url) {
	this.addGeoServerURL = url;
};
/**
 * 지오서버 삭제를 위한 URL을 반환한다.
 * 
 * @method gb.tree.GeoServer#getDeleteGeoServerURL
 */
gb.tree.GeoServer.prototype.getDeleteGeoServerURL = function() {
	return this.deleteGeoServerURL;
};

/**
 * 지오서버 삭제를 위한 URL을 설정한다.
 * 
 * @method gb.tree.GeoServer#setDeleteGeoServerURL
 */
gb.tree.GeoServer.prototype.setDeleteGeoServerURL = function(url) {
	this.deleteGeoServerURL = url;
};
/**
 * 지오서버 레이어 삭제를 위한 URL을 반환한다.
 * 
 * @method gb.tree.GeoServer#getDeleteGeoServerLayerURL
 */
gb.tree.GeoServer.prototype.getDeleteGeoServerLayerURL = function() {
	return this.deleteGeoServerLayerURL;
};
/**
 * 지오서버 트리구조 요청을 위한 URL을 반환한다.
 * 
 * @method gb.tree.GeoServer#getGetTreeURL
 */
gb.tree.GeoServer.prototype.getGetTreeURL = function() {
	return this.getTreeURL;
};
/**
 * 지오서버 트리구조 요청을 위한 URL을 설정한다.
 * 
 * @method gb.tree.GeoServer#setGetTreeURL
 */
gb.tree.GeoServer.prototype.setGetTreeURL = function(url) {
	this.getTreeURL = url;
};
/**
 * SHP 파일 업로드 객체를 설정한다.
 * 
 * @method gb.tree.GeoServer#setUploadSHP
 */
gb.tree.GeoServer.prototype.setUploadSHP = function(upload) {
	this.uploadSHP = upload;
};
/**
 * SHP 파일 업로드 객체를 반환한다.
 * 
 * @method gb.tree.GeoServer#getUploadSHP
 */
gb.tree.GeoServer.prototype.getUploadSHP = function() {
	return this.uploadSHP;
};

/**
 * 오류 메시지 창을 생성한다.
 * 
 * @method gb.tree.GeoServer#messageModal
 * @param {Object}
 *            server - 작업 중인 서버 노드
 * @param {Object}
 *            repo - 작업 중인 리포지토리 노드
 * @param {Object}
 *            branch - 작업 중인 브랜치 노드
 */
gb.tree.GeoServer.prototype.messageModal = function(title, msg) {
	var that = this;
	var msg1 = $("<div>").text(msg).css({
		"text-align" : "center",
		"font-size" : "16px",
		"padding-top" : "26px"
	});
	var body = $("<div>").append(msg1);
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("OK");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn);

	var modal = new gb.modal.Base({
		"title" : title,
		"width" : 310,
		"height" : 200,
		"autoOpen" : true,
		"body" : body,
		"footer" : buttonArea
	});
	$(okBtn).click(function() {
		modal.close();
	});
};