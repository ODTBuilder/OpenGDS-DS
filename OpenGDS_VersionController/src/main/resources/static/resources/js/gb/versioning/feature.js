/**
 * GeoGig feature 변경이력 객체를 정의한다.
 * 
 * @class gb.versioning.Feature
 * @memberof gb.versioning
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {Object}
 *            obj.url - 요청을 수행할 URL
 * @param {String}
 *            obj.url.serverTree - 서버 목록 트리를 요청할 컨트롤러 주소
 * 
 * @version 0.01
 * @author SOYIJUN
 * @date 2018. 10.26
 */
gb.versioning.Feature = function(obj) {
	var that = this;
	var options = obj ? obj : {};
	this.epsg = options.epsg ? options.epsg : undefined;
	var url = options.url ? options.url : {};
	this.featureLogURL = url.featureLog ? url.featureLog : undefined;
	this.featureDiffURL = url.featureDiff ? url.featureDiff : undefined;

	this.ofeature = $("<div>").css({
		"width" : "100%",
		"height" : "200px",
		"background-color" : "#dbdbdb",
		"border" : "1px solid #ccc",
		"border-radius" : "4px"
	});
	this.cfeature = $("<div>").css({
		"width" : "100%",
		"height" : "200px",
		"background-color" : "#dbdbdb",
		"border" : "1px solid #ccc",
		"border-radius" : "4px"
	});

	this.omap = new ol.Map({
		"target" : $(this.ofeature)[0],
		"layers" : []
	});

	this.cmap = new ol.Map({
		"target" : $(this.cfeature)[0],
		"layers" : []
	});

	this.comfeature = $("<div>").css({
		"width" : "100%",
		"height" : "200px",
		"background-color" : "#dbdbdb",
		"border" : "1px solid #ccc",
		"border-radius" : "4px"
	});
	this.curfeature = $("<div>").css({
		"width" : "100%",
		"height" : "200px",
		"background-color" : "#dbdbdb",
		"border" : "1px solid #ccc",
		"border-radius" : "4px"
	});

	this.tbody = $("<div>").addClass("tbody").addClass("gb-versioning-feature-trg");
	this.panel = new gb.panel.Base({
		"width" : 458,
		"height" : 550,
		"positionX" : 4,
		"right" : true,
		"positionY" : 395,
		"autoOpen" : false
	});

	var refIcon = $("<i>").addClass("fas").addClass("fa-sync-alt");
	var refBtn = $("<button>").addClass("gb-button-clear").append(refIcon).append(" Refresh").click(function() {
		that.refresh();
	});
	var refBtnarea = $("<div>").css({
		"text-align" : "center"
	}).append(refBtn);

	var th1 = $("<div>").addClass("th").addClass("gb-versioning-feature-td").text("Author");
	var th2 = $("<div>").addClass("th").addClass("gb-versioning-feature-td").text("Time");
	var th3 = $("<div>").addClass("th").addClass("gb-versioning-feature-td").text("Type");
	var th4 = $("<div>").addClass("th").addClass("gb-versioning-feature-td").text("Changes");
	var th5 = $("<div>").addClass("th").addClass("gb-versioning-feature-td").text("Revert");
	var thr = $("<div>").addClass("tr").addClass("gb-versioning-feature-tr").append(th1).append(th2).append(th3).append(th4).append(th5);
	var thead = $("<div>").addClass("thead").addClass("gb-versioning-feature-trg").append(thr).css({
		"text-align" : "center"
	});

	var table = $("<div>").addClass("gb-table").css({
		"display" : "table",
		"width" : "100%",
		"padding-left" : "6px"
	}).append(thead).append(this.tbody);

	var moreIcon = $("<i>").addClass("fas").addClass("fa-caret-down");
	var btn = $("<button>").addClass("gb-button-clear").append(moreIcon).append(" Read more").click(function() {
		var geoserver = that.getServer();
		var repo = that.getRepo();
		var path = that.getPath();
		var until = $(that.getTBody()).find(".gb-versioning-feature-tr").last().find(".gb-button").val();
		var idx = $(that.getTBody()).find(".gb-versioning-feature-tr").last().find(".gb-button").attr("idx");
		var head = $(that.getTBody()).find(".gb-versioning-feature-tr").first().find(".gb-button").val();
		that.loadFeatureHistory(geoserver, repo, path, 6, idx, until, head);
	});
	var btnarea = $("<div>").css({
		"text-align" : "center"
	}).append(btn);
	var body = $("<div>").css({
		"overflow-y" : "auto",
		"height" : "510px",
		"margin" : "4px 0"
	}).append(refBtnarea).append(table).append(btnarea);
	this.panel.setPanelBody(body);

	this.commits = {};
	this.curServer;
	this.curRepo;
	this.curPath;
	this.idstring;
	this.feature;
};
gb.versioning.Feature.prototype = Object.create(gb.versioning.Feature.prototype);
gb.versioning.Feature.prototype.constructor = gb.versioning.Feature;

/**
 * 피처 이력창을 닫는다.
 * 
 * @method gb.versioning.Feature#close
 */
gb.versioning.Feature.prototype.close = function() {
	this.getPanel().close();
}
/**
 * 피처 이력창을 연다.
 * 
 * @method gb.versioning.Feature#open
 */
gb.versioning.Feature.prototype.open = function() {
	this.panel.open();
};

/**
 * 피처 이력을 요청한다.
 * 
 * @method gb.versioning.Feature#loadFeatureHistory
 */
gb.versioning.Feature.prototype.loadFeatureHistory = function(server, repo, path, limit, idx, until, head) {
	var that = this;
	var params = {
		"serverName" : server,
		"repoName" : repo,
		"path" : path,
		"limit" : limit
	}
	if (until !== undefined) {
		params["until"] = until;
	}
	if (head !== undefined) {
		params["head"] = head;
	}
	if (idx !== undefined) {
		params["index"] = idx;
	}
	if (until === undefined || head === undefined) {
		if (this.getIDString() !== server + "/" + repo + "/" + path) {
			this.clearChangesTbody();
		}
	}
	var tranURL = this.getFeatureLogURL();
	if (tranURL.indexOf("?") !== -1) {
		tranURL += "&";
		tranURL += jQuery.param(params);
	} else {
		tranURL += "?";
		tranURL += jQuery.param(params);
	}

	$.ajax({
		url : tranURL,
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		beforeSend : function() {
			// $("body").css("cursor", "wait");
		},
		complete : function() {
			// $("body").css("cursor", "default");
		},
		success : function(data) {
			console.log(data);

			if (data.success === "true") {
				that.setIDString(server + "/" + repo + "/" + path);
				if (Array.isArray(data.simpleCommits)) {
					that.setCommitsByInfo(server, repo, path);
				}

				for (var i = 0; i < data.simpleCommits.length; i++) {
					if ((until !== undefined || head !== undefined) && (i === 0)) {
						var early = $(that.getTBody()).find(".gb-versioning-feature-tr").last().find(".gb-button").val();
						if (data.simpleCommits[i].commitId === early) {
							if (data.simpleCommits.length === 1) {
								var title = "Message";
								var msg = "No commits to load";
								that.messageModal(title, msg);
							}
							continue;
						}
					}
					var td1 = $("<div>").addClass("td").addClass("gb-versioning-feature-td").append(data.simpleCommits[i].authorName).css({
						"text-align" : "center"
					});
					var td2 = $("<div>").addClass("td").addClass("gb-versioning-feature-td").append(data.simpleCommits[i].date);
					var td3 = $("<div>").addClass("td").addClass("gb-versioning-feature-td").append(data.simpleCommits[i].changeType);
					var button = $("<button>").addClass("gb-button").addClass("gb-button-default").text("Detail").attr({
						"title" : data.simpleCommits[i].message,
						"value" : data.simpleCommits[i].commitId,
						"idx" : data.simpleCommits[i].cIdx
					}).click(function() {
						var geoserver = that.getServer();
						var repo = that.getRepo();
						var path = that.getPath();
						var nidx = parseInt($(this).attr("idx"));
						var oidx = parseInt($(this).parents().eq(1).next().find(".gb-button").attr("idx"));
						// var oidx =
						// $(that.getTBody()).find(".gb-versioning-feature-tr").last().find(".gb-button").attr("idx");
						that.openDetailChanges(geoserver, repo, path, nidx, oidx);
					});
					var td4 = $("<div>").addClass("td").addClass("gb-versioning-feature-td").css({
						"text-align" : "center"
					}).append(button);

					var rvButton = $("<button>").addClass("gb-button").addClass("gb-button-default").text("Run");
					var td5 = $("<div>").addClass("td").addClass("gb-versioning-feature-td").css({
						"text-align" : "center"
					}).append(rvButton);

					var msg = $("<div>").addClass("gb-tooltip-text").text(data.simpleCommits[i].message);
					var tr = $("<div>").addClass("tr").addClass("gb-versioning-feature-tr").addClass("gb-tooltip").append(td1).append(td2)
							.append(td3).append(td4).append(td5);
					$(that.tbody).append(tr);
				}
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {

		}
	});
};

/**
 * 피처 디테일 창을 연다.
 * 
 * @method gb.versioning.Feature#openDetailChanges
 * @param {String}
 *            server - 서버 이름
 * @param {String}
 *            repo - 레파지토리 이름
 * @param {String}
 *            path - 피처 패스
 * @param {Number}
 *            nidx - 최신 커밋 인덱스
 * @param {Number}
 *            oidx - 타겟 커밋 인덱스
 */
gb.versioning.Feature.prototype.openDetailChanges = function(server, repo, path, nidx, oidx) {
	var that = this;

	var olabel = $("<div>").append("Previous Feature").addClass("gb-form").css({
		"text-align" : "center"
	});

	var oheadtd1 = $("<th>").text("Name");
	var oheadtd2 = $("<th>").text("Value");
	var oheadth = $("<tr>").append(oheadtd1).append(oheadtd2);
	var oattrthead = $("<thead>").append(oheadth);
	this.oattrtbody = $("<tbody>").css({
		"overflow-y" : "auto",
		"height" : "340px",
		"width" : "354px"
	});
	var oattrtable = $("<table>").append(oattrthead).append(this.oattrtbody).addClass("gb-table");
	var oattribute = $("<div>").append(oattrtable).css({
		"height" : "370px",
		"width" : "100%",
		"overflow" : "hidden"
	});
	var oarea = $("<div>").append(olabel).append(this.ofeature).append(oattribute).css({
		"float" : "left",
		"width" : "50%",
		"padding" : "10px"
	});

	var clabel = $("<div>").append("Changed Feature").addClass("gb-form").css({
		"text-align" : "center"
	});

	var cheadtd1 = $("<th>").text("Name");
	var cheadtd2 = $("<th>").text("Value");
	var cheadth = $("<tr>").append(cheadtd1).append(cheadtd2);
	var cattrthead = $("<thead>").append(cheadth);
	this.cattrtbody = $("<tbody>").css({
		"overflow-y" : "auto",
		"height" : "340px",
		"width" : "354px"
	});
	var cattrtable = $("<table>").append(cattrthead).append(this.cattrtbody).addClass("gb-table").css({
		"width" : "100%",
		"table-layout" : "fixed"
	});
	var cattribute = $("<div>").append(cattrtable).css({
		"height" : "370px",
		"width" : "100%",
		"overflow" : "hidden"
	});

	$(this.oattrtbody).on("scroll", function() {
		$(that.cattrtbody).prop("scrollTop", this.scrollTop).prop("scrollLeft", this.scrollLeft);
	});

	var carea = $("<div>").append(clabel).append(this.cfeature).append(cattribute).css({
		"float" : "left",
		"width" : "50%",
		"padding" : "10px"
	});

	var ocarea = $("<div>").append(oarea).append(carea);

	var body = $("<div>").append(ocarea);

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Use");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(closeBtn);

	var modal = new gb.modal.Base({
		"title" : "Contrast The Changes",
		"width" : 770,
		"height" : 840,
		"autoOpen" : true,
		"body" : body,
		"footer" : buttonArea
	});

	$(closeBtn).click(function() {
		modal.close();
	});
	$(okBtn).click(function() {
		console.log(idx);
		$(branchSelect).val();
		console.log($(branchSelect).val());
		var select = $(that.conflictFeatureTbody).find("tr").eq(idx).find(".gb-repository-instead-branch");
		$(select).filter("option:selected").text();
		console.log($(select).find("option").filter(":selected").val());
		$(select).val($(branchSelect).val());
		$(select).trigger("change");
		modal.close();
	});

	var that = this;
	var params = {
		"serverName" : server,
		"repoName" : repo,
		"path" : path,
		"newIndex" : nidx,
		"oldIndex" : oidx
	}

	var tranURL = this.getFeatureDiffURL();
	if (tranURL.indexOf("?") !== -1) {
		tranURL += "&";
		tranURL += jQuery.param(params);
	} else {
		tranURL += "?";
		tranURL += jQuery.param(params);
	}

	$.ajax({
		url : tranURL,
		method : "POST",
		contentType : "application/json; charset=UTF-8",
		beforeSend : function() {
			// $("body").css("cursor", "wait");
		},
		complete : function() {
			// $("body").css("cursor", "default");
		},
		success : function(data) {
			console.log(data);
			if (data.success === "true") {
				if (data.hasOwnProperty("diffs")) {
					var diffs = data.diffs
					for (var i = 0; i < diffs.length; i++) {
						if (diffs[i]["geometry"] === "true") {
							var crs = diffs[i]["crs"].substring(diffs[i]["crs"].indexOf(":") + 1);

							var oldwkt = diffs[i]["oldvalue"];
							var newwkt = diffs[i]["newvalue"];
							if (oldwkt !== undefined && oldwkt !== null) {
								var format = new ol.format.WKT();
								var geom = format.readGeometry(oldwkt);
								var feature = new ol.Feature({
									"geometry" : geom
								});

								var style = new ol.style.Style({
									image : new ol.style.Circle({
										radius : 5,
										fill : new ol.style.Fill({
											color : 'orange'
										})
									}),
									stroke : new ol.style.Stroke({
										width : 1,
										color : 'orange'
									}),
									fill : new ol.style.Fill({
										color : 'orange'
									})
								});

								var vlayer = new ol.layer.Vector({
									"style" : style,
									"source" : new ol.source.Vector({
										"features" : [ feature ]
									}),
									"zIndex" : 2
								});

								var osm = new ol.layer.Tile({
									"source" : new ol.source.OSM(),
									"zIndex" : 1
								});

								that.getLeftMap().updateSize();
								that.getLeftMap().getLayers().clear();
								that.getLeftMap().addLayer(osm);
								that.getLeftMap().addLayer(vlayer);
								// that.getLeftMap().getView().fit(geom);

								if (newwkt === undefined || newwkt === null) {
									that.getRightMap().updateSize();
									that.getRightMap().getLayers().clear();
									that.getRightMap().addLayer(osm);
									that.getRightMap().addLayer(vlayer);
								}

								this.crs = new gb.crs.BaseCRS({
									"autoOpen" : false,
									"title" : "Base CRS",
									"message" : $(".epsg-now"),
									"maps" : [ that.getLeftMap(), that.getRightMap() ],
									"epsg" : crs,
									"callback" : function() {
										that.getLeftMap().getView().fit(geom);
									}
								});
							}

							if (newwkt !== undefined && newwkt !== null) {
								var format = new ol.format.WKT();
								var geom = format.readGeometry(newwkt);
								var feature = new ol.Feature({
									"geometry" : geom
								});

								var style = new ol.style.Style({
									image : new ol.style.Circle({
										radius : 5,
										fill : new ol.style.Fill({
											color : 'orange'
										})
									}),
									stroke : new ol.style.Stroke({
										width : 1,
										color : 'orange'
									}),
									fill : new ol.style.Fill({
										color : 'orange'
									})
								});

								var vlayer = new ol.layer.Vector({
									"style" : style,
									"source" : new ol.source.Vector({
										"features" : [ feature ]
									}),
									"zIndex" : 2
								});

								var osm = new ol.layer.Tile({
									"source" : new ol.source.OSM(),
									"zIndex" : 1
								});

								that.getRightMap().updateSize();
								that.getRightMap().getLayers().clear();
								that.getRightMap().addLayer(osm);
								that.getRightMap().addLayer(vlayer);
								// that.getRightMap().getView().fit(geom);
							}

						} else {

							var otd1 = $("<td>").text(diffs[i]["attributename"]);
							var otd2 = $("<td>").text(diffs[i]["oldvalue"]);
							var otr1 = $("<tr>").append(otd1).append(otd2);
							$(that.getLeftTBody()).append(otr1);
							var ctd1 = $("<td>").text(diffs[i]["attributename"]);
							var ctd2 = $("<td>").text(diffs[i]["newvalue"] ? diffs[i]["newvalue"] : diffs[i]["oldvalue"]);
							var ctr1 = $("<tr>").append(ctd1).append(ctd2);
							$(that.getRightTBody()).append(ctr1);

							if (diffs[i]["changetype"] !== "NO_CHANGE") {
								$(otr1).css({
									"background-color" : "#ffc523"
								});
								$(ctr1).css({
									"background-color" : "#ffc523"
								});
							}
						}
					}
				}
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {

		}
	});

};

/**
 * 피처 revert 요청 모달을 연다.
 * 
 * @method gb.versioning.Feature#openRevertModal
 */
gb.versioning.Feature.prototype.openRevertModal = function() {
	var that = this;
	var msg1 = $("<div>").text("Revert the feature to the point in time when it was committed.").css({
		"text-align" : "center",
		"font-size" : "16px"
	});
	var msg2 = $("<div>").text('Do you want to proceed?').css({
		"text-align" : "center",
		"font-size" : "16px"
	});
	var body = $("<div>").append(msg1).append(msg2);
	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Revert");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);

	var commitModal = new gb.modal.Base({
		"title" : "Revert",
		"width" : 350,
		"height" : 200,
		"autoOpen" : true,
		"body" : body,
		"footer" : buttonArea
	});
	$(closeBtn).click(function() {
		commitModal.close();
	});
	$(okBtn).click(function() {
		// mModal.close();
		// that.endTransaction(server, repo, tid,
		// commitModal);
		// that.resolveConflictModal(server, repo, repo,
		// that.getNowBranch().text,
		// branch, data.merge.ours, data.merge.theirs,
		// data.merge.features, commitModal);
		that.revert();
	});
};

/**
 * 피처 revert 요청한다.
 * 
 * @method gb.versioning.Feature#revert
 */
gb.versioning.Feature.prototype.revert = function() {
	var that = this;
	var data = {
		"success" : "false"
	};
	if (data.success === "true") {
		var msg1 = $("<div>").text("Feature reverted successfully.").css({
			"text-align" : "center",
			"font-size" : "16px"
		});
		var body = $("<div>").append(msg1);
		var closeBtn = $("<button>").css({
			"float" : "right"
		}).addClass("gb-button").addClass("gb-button-default").text("OK");
		var buttonArea = $("<span>").addClass("gb-modal-buttons").append(closeBtn);

		var commitModal = new gb.modal.Base({
			"title" : "Revert",
			"width" : 350,
			"height" : 200,
			"autoOpen" : true,
			"body" : body,
			"footer" : buttonArea
		});
		$(closeBtn).click(function() {
			commitModal.close();
		});
	} else {
		var msg1 = $("<div>").text("Revert failed.").css({
			"text-align" : "center",
			"font-size" : "16px"
		});
		var msg2 = $("<div>").text('There are conflicting features. Do you want to resolve?').css({
			"text-align" : "center",
			"font-size" : "16px"
		});
		var body = $("<div>").append(msg1).append(msg2);
		var closeBtn = $("<button>").css({
			"float" : "right"
		}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
		var okBtn = $("<button>").css({
			"float" : "right"
		}).addClass("gb-button").addClass("gb-button-primary").text("Resolve");
		var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);

		var commitModal = new gb.modal.Base({
			"title" : "Revert",
			"width" : 350,
			"height" : 200,
			"autoOpen" : true,
			"body" : body,
			"footer" : buttonArea
		});
		$(closeBtn).click(function() {
			commitModal.close();
		});
		$(okBtn).click(function() {
			// mModal.close();
			// that.endTransaction(server, repo, tid,
			// commitModal);
			// that.resolveConflictModal(server, repo, repo,
			// that.getNowBranch().text,
			// branch, data.merge.ours, data.merge.theirs,
			// data.merge.features, commitModal);
			that.openConflictDetailModal();
		});
	}
};

/**
 * 피처 충돌 정보창을 연다.
 * 
 * @method gb.versioning.Feature#openConflictDetailModal
 */
gb.versioning.Feature.prototype.openConflictDetailModal = function() {
	var that = this;

	var crepo = $("<div>").append("Reverting Feature").addClass("gb-form").css({
		"text-align" : "center"
	});

	var cheadtd1 = $("<th>").text("Name");
	var cheadtd2 = $("<th>").text("Value");
	var cheadth = $("<tr>").append(cheadtd1).append(cheadtd2);
	var cattrthead = $("<thead>").append(cheadth);
	var cattrtbody = $("<tbody>").css({
		"overflow-y" : "auto",
		"height" : "340px",
		"width" : "354px"
	});
	var cattrtable = $("<table>").append(cattrthead).append(cattrtbody).addClass("gb-table");
	var cattribute = $("<div>").append(cattrtable).css({
		"height" : "370px",
		"width" : "100%",
		"overflow" : "hidden"
	});
	var carea = $("<div>").append(crepo).append(this.comfeature).append(cattribute).css({
		"float" : "left",
		"width" : "50%",
		"padding" : "10px"
	});
	// this.conflictView = new ol.View({
	// "center" : [ 0, 0 ],
	// "zoom" : 1
	// });
	// this.cmap = new ol.Map({
	// "target" : $(cfeature)[0],
	// "view" : this.conflictView,
	// "layers" : []
	// });

	var trepo = $("<div>").append("Conflicted Feature").addClass("gb-form").css({
		"text-align" : "center"
	});

	// var tfeature = $("<div>").css({
	// "width" : "100%",
	// "height" : "200px",
	// "background-color" : "#dbdbdb"
	// });
	var theadtd1 = $("<th>").text("Name");
	var theadtd2 = $("<th>").text("Value");
	var theadth = $("<tr>").append(theadtd1).append(theadtd2);
	var tattrthead = $("<thead>").append(theadth);
	var tattrtbody = $("<tbody>").css({
		"overflow-y" : "auto",
		"height" : "340px",
		"width" : "354px"
	});
	var tattrtable = $("<table>").append(tattrthead).append(tattrtbody).addClass("gb-table").css({
		"width" : "100%",
		"table-layout" : "fixed"
	});
	var tattribute = $("<div>").append(tattrtable).css({
		"height" : "370px",
		"width" : "100%",
		"overflow" : "hidden"
	});

	$(cattrtbody).on("scroll", function() {
		$(tattrtbody).prop("scrollTop", this.scrollTop).prop("scrollLeft", this.scrollLeft);
	});

	// $(tattribute).on("scroll", function() {
	// $(cattribute).prop("scrollTop", this.scrollTop).prop("scrollLeft",
	// this.scrollLeft);
	// });

	var tarea = $("<div>").append(trepo).append(this.curfeature).append(tattribute).css({
		"float" : "left",
		"width" : "50%",
		"padding" : "10px"
	});
	// this.tmap = new ol.Map({
	// "target" : $(tfeature)[0],
	// "view" : this.conflictView,
	// "layers" : []
	// });

	var ctarea = $("<div>").append(carea).append(tarea);

	var cubOpt = $("<option>").text("Reverting Feature").attr({
		"value" : "ours"
	});
	var tabOpt = $("<option>").text("Conflicted Feature").attr({
		"value" : "theirs"
	});
	var branchSelect = $("<select>").addClass("gb-form").append(cubOpt).append(tabOpt);
	// $(branchSelect).val(val);
	var sarea = $("<div>").append(branchSelect).css({
		"padding" : "10px"
	});

	var body = $("<div>").append(ctarea).append(sarea);

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("Override");
	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);

	var modal = new gb.modal.Base({
		"title" : "Compare Conflicts",
		"width" : 770,
		"height" : 840,
		"autoOpen" : true,
		"body" : body,
		"footer" : buttonArea
	});

	$(closeBtn).click(function() {
		modal.close();
	});
	$(okBtn).click(function() {
		console.log(idx);
		$(branchSelect).val();
		console.log($(branchSelect).val());
		var select = $(that.conflictFeatureTbody).find("tr").eq(idx).find(".gb-repository-instead-branch");
		$(select).filter("option:selected").text();
		console.log($(select).find("option").filter(":selected").val());
		$(select).val($(branchSelect).val());
		$(select).trigger("change");
		modal.close();
	});

	var cparams1 = {
		"serverName" : server,
		"repoName" : crepos,
		"path" : path,
		"commitId" : this.getCommitId().ours,
		"featureId" : fid1
	}

	var cparams2 = {
		"serverName" : server,
		"repoName" : crepos,
		"path" : path,
		"commitId" : this.getCommitId().theirs,
		"featureId" : fid2
	}

	var wkt1;
	var wkt2;
	if (fid1 !== "0000000000000000000000000000000000000000") {
		var fobjectURL1 = this.getCatFeatureObjectURL();
		if (fobjectURL1.indexOf("?") !== -1) {
			fobjectURL1 += "&";
			fobjectURL1 += jQuery.param(cparams1);
		} else {
			fobjectURL1 += "?";
			fobjectURL1 += jQuery.param(cparams1);
		}

		$.ajax({
			url : fobjectURL1,
			method : "POST",
			contentType : "application/json; charset=UTF-8",
			// data : cparams1,
			// dataType : 'jsonp',
			// jsonpCallback : 'getJson',
			beforeSend : function() {
				// $("body").css("cursor", "wait");
			},
			complete : function() {
				// $("body").css("cursor", "default");
			},
			success : function(data) {
				console.log(data);
				if (data.success === "true") {
					var attrs = data.attributes;
					for (var i = 0; i < attrs.length; i++) {
						if (attrs[i].type === "POINT" || attrs[i].type === "LINESTRING" || attrs[i].type === "POLYGON"
								|| attrs[i].type === "MULTIPOINT" || attrs[i].type === "MULTILINESTRING"
								|| attrs[i].type === "MULTIPOLYGON") {
							var wkt = attrs[i].value;
							wkt1 = wkt;
							console.log(wkt1);
							var format = new ol.format.WKT();
							var geom = format.readGeometry(wkt);
							var feature = new ol.Feature({
								"geometry" : geom
							});
							feature.setId(data.featureId);
							console.log(feature);
							console.log(feature.getId());
							var style = new ol.style.Style({
								image : new ol.style.Circle({
									radius : 5,
									fill : new ol.style.Fill({
										color : 'orange'
									})
								}),
								stroke : new ol.style.Stroke({
									width : 1,
									color : 'orange'
								}),
								fill : new ol.style.Fill({
									color : 'orange'
								})
							});

							var vlayer = new ol.layer.Vector({
								"style" : style,
								"source" : new ol.source.Vector({
									"features" : [ feature ]
								}),
								"zIndex" : 2
							});

							var osm = new ol.layer.Tile({
								"source" : new ol.source.OSM(),
								"zIndex" : 1
							});

							var epsg = attrs[i].crs.toLowerCase();
							var code = epsg.substring(epsg.indexOf("epsg:") + 5);
							var intcode = parseInt(code);
							console.log(code);

							var ccrs = new gb.crs.BaseCRS({
								"title" : "Base CRS",
								"width" : 300,
								"height" : 200,
								"autoOpen" : false,
								"message" : undefined,
								"map" : that.getCurrentMap(),
								"epsg" : Number.isInteger(intcode) ? code : "4326"
							});

							that.getCurrentMap().updateSize();
							that.getCurrentMap().getLayers().clear();
							that.getCurrentMap().addLayer(osm);
							that.getCurrentMap().addLayer(vlayer);
							that.getCurrentMap().getView().fit(geom);

						} else {
							var name = attrs[i].name;
							var value = attrs[i].value;
							var td1 = $("<td>").text(name);
							var td2 = $("<td>").text(value).css({
								"word-break" : "break-word",
								"overflow-wrap" : "break-word"
							});
							var tr = $("<tr>").append(td1).append(td2);
							$(cattrtbody).append(tr);
						}

					}

					if (fid2 !== "0000000000000000000000000000000000000000") {
						var fobjectURL2 = that.getCatFeatureObjectURL();
						if (fobjectURL2.indexOf("?") !== -1) {
							fobjectURL2 += "&";
							fobjectURL2 += jQuery.param(cparams2);
						} else {
							fobjectURL2 += "?";
							fobjectURL2 += jQuery.param(cparams2);
						}

						$.ajax({
							url : fobjectURL2,
							method : "POST",
							contentType : "application/json; charset=UTF-8",
							// data : cparams2,
							// dataType : 'jsonp',
							// jsonpCallback : 'getJson',
							beforeSend : function() {
								// $("body").css("cursor", "wait");
							},
							complete : function() {
								// $("body").css("cursor", "default");
							},
							success : function(data) {
								console.log(data);
								if (data.success === "true") {
									var attrs = data.attributes;
									for (var i = 0; i < attrs.length; i++) {
										if (attrs[i].type === "POINT" || attrs[i].type === "LINESTRING" || attrs[i].type === "POLYGON"
												|| attrs[i].type === "MULTIPOINT" || attrs[i].type === "MULTILINESTRING"
												|| attrs[i].type === "MULTIPOLYGON") {
											var wkt = attrs[i].value;
											wkt2 = wkt;
											if (wkt1 !== wkt2) {
												$(that.cfeature).css({
													"border" : "3px solid #ffc523"
												});
												$(that.tfeature).css({
													"border" : "3px solid #ffc523"
												});
											} else {
												$(that.cfeature).css({
													"border" : "1px solid #ccc"
												});
												$(that.tfeature).css({
													"border" : "1px solid #ccc"
												});
											}
											console.log(wkt2);
											var format = new ol.format.WKT();
											var geom = format.readGeometry(wkt);
											var feature = new ol.Feature({
												"geometry" : geom
											});
											feature.setId(data.featureId);
											console.log(feature);
											console.log(feature.getId());
											var style = new ol.style.Style({
												image : new ol.style.Circle({
													radius : 5,
													fill : new ol.style.Fill({
														color : 'orange'
													})
												}),
												stroke : new ol.style.Stroke({
													width : 1,
													color : 'orange'
												}),
												fill : new ol.style.Fill({
													color : 'orange'
												})
											});

											var vlayer = new ol.layer.Vector({
												"style" : style,
												"source" : new ol.source.Vector({
													"features" : [ feature ]
												}),
												"zIndex" : 2
											});

											var osm = new ol.layer.Tile({
												"source" : new ol.source.OSM(),
												"zIndex" : 1
											});

											var epsg = attrs[i].crs.toLowerCase();
											var code = epsg.substring(epsg.indexOf("epsg:") + 5);
											var intcode = parseInt(code);
											console.log(code);

											var ccrs = new gb.crs.BaseCRS({
												"title" : "Base CRS",
												"width" : 300,
												"height" : 200,
												"autoOpen" : false,
												"message" : undefined,
												"map" : that.getTargetMap(),
												"epsg" : Number.isInteger(intcode) ? code : "4326"
											});

											that.getTargetMap().updateSize();
											that.getTargetMap().getLayers().clear();
											that.getTargetMap().addLayer(osm);
											that.getTargetMap().addLayer(vlayer);
											var geom = feature.getGeometry();

											that.getTargetMap().getView().fit(geom);

										} else {
											var name = attrs[i].name;
											var value = attrs[i].value;
											var td1 = $("<td>").text(name);
											var td2 = $("<td>").text(value).css({
												"word-break" : "break-word",
												"overflow-wrap" : "break-word"
											});
											var tr = $("<tr>").append(td1).append(td2);
											$(tattrtbody).append(tr);
										}

									}
									if ($(cattrtbody).find("tr").length === $(tattrtbody).find("tr").length) {
										var trs = $(cattrtbody).find("tr");
										var ttrs = $(tattrtbody).find("tr");
										for (var j = 0; j < trs.length; j++) {
											if ($(trs[j]).find("td").eq(0).text() === $(ttrs[j]).find("td").eq(0).text()) {

												if ($(trs[j]).find("td").eq(1).text() !== $(ttrs[j]).find("td").eq(1).text()) {
													$(trs[j]).css({
														"background-color" : "#ffc523"
													});
													$(ttrs[j]).css({
														"background-color" : "#ffc523"
													});
												}
											}
										}
									}
								} else {
									var title = "Error";
									var msg = "Retrieve feature failed."
									that.messageModal(title, msg);
								}
							},
							error : function(jqXHR, textStatus, errorThrown) {

							}
						});
					} else {
						that.getTargetMap().updateSize();
						var td1 = $("<td>").text("Deleted");
						var td2 = $("<td>").text("Deleted");
						var tr = $("<tr>").append(td1).append(td2);
						$(tattrtbody).append(tr);
					}
				} else {
					var title = "Error";
					var msg = "Retrieve feature failed."
					that.messageModal(title, msg);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {

			}
		});
	} else {

	}
};

/**
 * 충돌 피처를 머지한다.
 * 
 * @method gb.versioning.Feature#mergeConflictFeature
 */
gb.versioning.Feature.prototype.mergeConflictFeature = function() {

};

/**
 * 피처 로그 요청 URL을 반환한다.
 * 
 * @method gb.versioning.Feature#getFeatureLogURL
 */
gb.versioning.Feature.prototype.getFeatureLogURL = function() {
	return this.featureLogURL;
};

/**
 * 피처 비교 요청 URL을 반환한다.
 * 
 * @method gb.versioning.Feature#getFeatureDiffURL
 */
gb.versioning.Feature.prototype.getFeatureDiffURL = function() {
	return this.featureDiffURL;
};

/**
 * 피처이력 목록을 비운다.
 * 
 * @method gb.versioning.Feature#clearChangesTbody
 */
gb.versioning.Feature.prototype.clearChangesTbody = function() {
	$(this.tbody).empty();
};

/**
 * 피처이력 목록 테이블 바디를 반환한다.
 * 
 * @method gb.versioning.Feature#getTBody
 */
gb.versioning.Feature.prototype.getTBody = function() {
	return this.tbody;
};

/**
 * 피처이력 객체를 설정한다.
 * 
 * @method gb.versioning.Feature#setCommits
 */
gb.versioning.Feature.prototype.setCommits = function(obj) {
	this.commits = obj;
};

/**
 * 피처이력 객체를 반환한다.
 * 
 * @method gb.versioning.Feature#getCommits
 */
gb.versioning.Feature.prototype.getCommits = function() {
	return this.commits;
};

/**
 * 조회한 피처이력을 분류 보관한다.
 * 
 * @method gb.versioning.Feature#setCommitsByInfo
 */
gb.versioning.Feature.prototype.setCommitsByInfo = function(server, repo, path, arr) {
	if (server || repo || path || arr) {
		return;
	}
	if (!this.commits.hasOwnProperty(server)) {
		this.commits[server] = {};
	}
	if (!this.commits[server].hasOwnProperty(repo)) {
		this.commits[server][repo] = {};
	}
	if (!this.commits[server][repo].hasOwnProperty(path)) {
		this.commits[server][repo][path] = [];
	}
	this.commits[server][repo][path] = this.commits[server][repo][path].concat(arr);
};

/**
 * 현재 편집중인 객체 path를 설정한다.
 * 
 * @method gb.versioning.Feature#setPath
 */
gb.versioning.Feature.prototype.setPath = function(path) {
	this.curPath = path;
};

/**
 * 현재 편집중인 객체 path를 반환한다.
 * 
 * @method gb.versioning.Feature#getPath
 */
gb.versioning.Feature.prototype.getPath = function() {
	return this.curPath;
};

/**
 * 현재 편집중인 객체 repo 를 설정한다.
 * 
 * @method gb.versioning.Feature#setRepo
 */
gb.versioning.Feature.prototype.setRepo = function(repo) {
	this.curRepo = repo;
};

/**
 * 현재 편집중인 객체 repo 를 반환한다.
 * 
 * @method gb.versioning.Feature#getRepo
 */
gb.versioning.Feature.prototype.getRepo = function() {
	return this.curRepo;
};

/**
 * 현재 편집중인 객체 server 를 설정한다.
 * 
 * @method gb.versioning.Feature#setServer
 */
gb.versioning.Feature.prototype.setServer = function(server) {
	this.curServer = server;
};

/**
 * 현재 편집중인 객체 server 를 반환한다.
 * 
 * @method gb.versioning.Feature#getServer
 */
gb.versioning.Feature.prototype.getServer = function() {
	return this.curServer;
};

/**
 * 현재 편집중인 객체 idstring을 설정한다.
 * 
 * @method gb.versioning.Feature#setIDString
 */
gb.versioning.Feature.prototype.setIDString = function(id) {
	this.idstring = id;
};

/**
 * 현재 편집중인 객체 idstring을 반환한다.
 * 
 * @method gb.versioning.Feature#getIDString
 */
gb.versioning.Feature.prototype.getIDString = function() {
	return this.idstring;
};

/**
 * panel 을 반환한다.
 * 
 * @method gb.versioning.Feature#getPanel
 */
gb.versioning.Feature.prototype.getPanel = function() {
	return this.panel;
};

/**
 * 현재 편집중인 객체 이력을 새로고침한다.
 * 
 * @method gb.versioning.Feature#refresh
 */
gb.versioning.Feature.prototype.refresh = function() {
	if ($(this.getPanel().getPanel()).css("display") !== "none") {
		this.clearChangesTbody();
		var geoserver = this.getServer();
		var repo = this.getRepo();
		var path = this.getPath();
		this.loadFeatureHistory(geoserver, repo, path, 10, 0);
	}
};

/**
 * 현재 편집중인 객체를 설정한다.
 * 
 * @method gb.versioning.Feature#setFeature
 */
gb.versioning.Feature.prototype.setFeature = function(feature) {
	this.feature = feature;
};

/**
 * 현재 편집중인 객체를 반환한다.
 * 
 * @method gb.versioning.Feature#getIDString
 */
gb.versioning.Feature.prototype.getFeature = function() {
	return this.feature;
};

/**
 * 다음 편집이력을 로드한다.
 * 
 * @method gb.versioning.Feature#loadMoreHistory
 */
gb.versioning.Feature.prototype.loadMoreHistory = function() {

};
/**
 * 오류 메시지 창을 생성한다.
 * 
 * @method gb.versioning.Feature#messageModal
 * @param {Object}
 *            server - 작업 중인 서버 노드
 * @param {Object}
 *            repo - 작업 중인 리포지토리 노드
 * @param {Object}
 *            branch - 작업 중인 브랜치 노드
 */
gb.versioning.Feature.prototype.messageModal = function(title, msg) {
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

/**
 * 왼쪽 ol.Map을 반환한다.
 * 
 * @method gb.versioning.Feature#getLeftMap
 * @return {ol.Map}
 * 
 */
gb.versioning.Feature.prototype.getLeftMap = function() {
	return this.omap;
}

/**
 * 오른쪽 ol.Map을 반환한다.
 * 
 * @method gb.versioning.Feature#getRightMap
 * @return {ol.Map}
 * 
 */
gb.versioning.Feature.prototype.getRightMap = function() {
	return this.cmap;
}

/**
 * 왼쪽 피처 tbody를 반환한다.
 * 
 * @method gb.versioning.Feature#getLeftTBody
 * @return {element}
 * 
 */
gb.versioning.Feature.prototype.getLeftTBody = function() {
	return this.oattrtbody;
}

/**
 * 오른쪽 피처 tbody를 반환한다.
 * 
 * @method gb.versioning.Feature#getRightTBody
 * @return {element}
 * 
 */
gb.versioning.Feature.prototype.getRightTBody = function() {
	return this.cattrtbody;
}