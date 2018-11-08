/**
 * 사용자 설정 레이어 정의 객체
 * 
 * @author yijun.so
 * @date 2017. 07.26
 * @version 0.01
 * @class gb.validation.LayerDefinition
 * @constructor
 * 
 */
var gb;
if (!gb)
	gb = {};
if (!gb.validation)
	gb.validation = {};
gb.validation.LayerDefinition = function(obj) {
	var that = this;
	this.structure = [];
	this.geometryType = [ "point", "linestring", "polygon", "multipoint", "multilinestring", "multipolygon" ];
	this.dataType = [ "DATE", "DATETIME", "INTEGER", "NUMBER", "VARCHAR2", "VARCHAR3", "VARCHAR4" ];
	var options = obj ? obj : {};
	this.locale = options.locale ? options.locale : "en";
	this.translation = {
		"layerCode" : {
			"en" : "Code",
			"ko" : "코드"
		},
		"layerType" : {
			"en" : "Type",
			"ko" : "유형"
		},
		"deleteLayer" : {
			"en" : "Delete layer",
			"ko" : "레이어 삭제"
		},
		"addLayer" : {
			"en" : "Add layer",
			"ko" : "레이어 추가"
		},
		"exLayerCodeField" : {
			"en" : "Layer code ex) F0010000",
			"ko" : "레이어 코드 예시) F0010000"
		},
		"notice" : {
			"en" : "Notice",
			"ko" : "알림"
		},
		"notSet" : {
			"en" : "Not set",
			"ko" : "미설정"
		},
		"deleteCategory" : {
			"en" : "Delete category",
			"ko" : "분류 삭제"
		},
		"exCategoryField" : {
			"en" : "Category Name ex) Contour, F001 etc",
			"ko" : "분류명 예시) 등고선, F001 등"
		},
		"attrName" : {
			"en" : "Attribute name",
			"ko" : "속성명"
		},
		"attrType" : {
			"en" : "Attribute type",
			"ko" : "속성 유형"
		},
		"fixedAttr" : {
			"en" : "Fixed Attribute",
			"ko" : "고정 속성"
		},
		"addFixedAttr" : {
			"en" : "Add fixed attribute",
			"ko" : "고정 속성 추가"
		},
		"deleteFixedAttr" : {
			"en" : "Delete fixed attribute",
			"ko" : "고정 속성 삭제"
		},
		"exFixedAttrNameField" : {
			"en" : "",
			"ko" : "고정 값을 가질 속성명 예시) 구분"
		},
		"length" : {
			"en" : "Length",
			"ko" : "길이"
		},
		"nullAllow" : {
			"en" : "Null value allowed?",
			"ko" : "널 허용"
		},
		"allowValue" : {
			"en" : "Allowed value",
			"ko" : "허용값"
		},
		"exAllowValueField" : {
			"en" : "Separate different values with a comma. ex)value1, value2, value3, ...",
			"ko" : "해당 속성이 가질 수 있는 값들을 콤마(,)로 구분하여 입력 예시)주곡선, 계곡선, 간곡선, ..."
		},
		"success" : {
			"en" : "Success",
			"ko" : "성공"
		},
		"danger" : {
			"en" : "Danger",
			"ko" : "위험"
		},
		"noticeOptionStructError" : {
			"en" : "The top-level structure of options is an array.",
			"ko" : "옵션의 최상위 구조는 배열 형태여야 합니다"
		},
		"noticeLayerDefUpdate" : {
			"en" : "[Layer definition] has been changed.",
			"ko" : "[레이어 정의]가 변경 되었습니다"
		},
		"noticeNotExistLayer" : {
			"en" : "category. There are no layers.",
			"ko" : "번째 분류에 포함된 레이어가 없습니다"
		},
		"noticeInvalidKey" : {
			"en" : "is not a valid key name",
			"ko" : "은/는 유효한 키 이름이 아닙니다"
		},
		"noticeCategoryNameEnter" : {
			"en" : "category. You must enter a category name.",
			"ko" : "번째 분류의 분류명을 입력해야 합니다"
		},
		"keyName" : {
			"en" : "Key name:",
			"ko" : "키 이름:"
		}
	}
	// this.panelBody = $("<div>").addClass("panel-body");
	this.panelBody = $("<div>").addClass("gb-layerdefinition-body");
	// this.panel =
	// $("<div>").addClass("panel").addClass("panel-default").append(this.panelBody);
	this.panel = $("<div>").append(this.panelBody);
	if (typeof options.append === "string") {
		$(options.append).append(this.panel);
	}
	this.msg = typeof options.msgClass === "string" ? options.msgClass : undefined;
	this.fileParent = undefined;
	this.fileClass = undefined;
	this.file = undefined;
	if (typeof options.fileClass === "string") {
		this.file = $("." + options.fileClass)[0];
		this.fileClass = $(this.file).attr("class");
		var jclass = "." + this.fileClass;
		this.fileParent = $(jclass).parent();
		$(this.fileParent).on("change", jclass, function(event) {
			var fileList = that.file.files;
			var reader = new FileReader();
			if (fileList.length === 0) {
				return;
			}
			reader.readAsText(fileList[0]);
			$(reader).on("load", function(event) {
				var obj = JSON.parse(reader.result);
				// var obj = JSON.parse(reader.result.replace(/(\s*)/g, ''));
				var flag = that.setStructure(obj);
				that.updateStructure();
			});
			$(that.file).remove();
			that.file = $("<input>").attr({
				"type" : "file"
			}).css("display", "none").addClass(that.fileClass)[0];
			$(that.fileParent).append(that.file);
		});
	}
	$(this.panelBody).on("click", ".gb-layerdefinition-delete-category", function() {
		that.deleteCategory(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("click", ".gb-layerdefinition-add-layer", function() {
		that.addLayer(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("click", ".gb-layerdefinition-toggle-category", function() {
		that.toggleLayerArea(this);
	});

	$(this.panelBody).on("click", ".gb-layerdefinition-add-attribute", function() {
		that.addAttribute(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("input", ".gb-layerdefinition-input-categoryname", function() {
		that.inputCategoryName(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("input", ".gb-layerdefinition-input-layercode", function() {
		that.inputLayerCode(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("change", ".gb-layerdefinition-select-geometry", function() {
		that.selectLayerGeometry(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("click", ".gb-layerdefinition-delete-layer", function() {
		that.deleteLayer(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("click", ".gb-layerdefinition-delete-attribute", function() {
		that.deleteAttribute(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("input", ".gb-layerdefinition-input-attributename", function() {
		that.inputAttributeName(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("change", ".gb-layerdefinition-select-attributetype", function() {
		that.selectAttributeType(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("input", ".gb-layerdefinition-input-attributelength", function() {
		that.inputAttributeLength(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("change", ".gb-layerdefinition-check-attributenull", function() {
		that.checkAttributeNull(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).on("input", ".gb-layerdefinition-input-attributevalues", function() {
		that.inputAttributeValues(this);
		console.log(that.getStructure());
	});

	$(this.panelBody).tooltip({
		"html" : true,
		"placement" : "right",
		"selector" : ".gb-layerdefinition-input-categoryname"
	});

};
gb.validation.LayerDefinition.prototype = Object.create(gb.validation.LayerDefinition.prototype);
gb.validation.LayerDefinition.prototype.constructor = gb.validation.LayerDefinition;

gb.validation.LayerDefinition.prototype.setMessage = function(type, message) {
	var alert = "alert-";
	switch (type) {
	case "success":
		alert = alert + "success";
		break;
	case "info":
		alert = alert + "info";
		break;
	case "warning":
		alert = alert + "warning";
		break;
	case "danger":
		alert = alert + "danger";
		break;
	default:
		alert = alert + "info";
		break;
	}
	var span = $("<span>").attr("aria-hidden", "true").html("&times;");
	var xbtn = $("<button>").addClass("close").attr("type", "button").attr("data-dismiss", "alert").attr("aria-label", "Close")
			.append(span);
	var head = $("<strong>").text(this.translation.notice[this.locale]);
	var div = $("<div>").addClass("alert").addClass(alert).addClass("alert-dismissible").attr("role", "alert").append(xbtn).append(head)
			.append(message);
	var jclass = "." + this.msg;
	$(jclass).append(div);
};

gb.validation.LayerDefinition.prototype.getInputFile = function() {
	return this.file;
};

gb.validation.LayerDefinition.prototype.updateStructure = function() {
	console.log(this.getStructure());
	$(this.panelBody).empty();
	var strc = this.getStructure();
	if (!Array.isArray(strc)) {
		return;
	}
	for (var a = 0; a < strc.length; a++) {
		// 카테고리 입력
		var toggleIcon = $("<i>").addClass("fas").addClass("fa-caret-up").addClass("fa-lg");
		var toggleBtn = $("<button>").addClass("btn").addClass("btn-link").addClass("gb-layerdefinition-toggle-category")
				.append(toggleIcon);
		var categoryName = $("<input>").attr({
			"type" : "text",
			"placeholder" : this.translation.exCategoryField[this.locale],
			"title" : "some tips"
		}).css({
			"border" : "0",
			"border-bottom" : "solid 1px #9a9a9a",
			"height" : "32px",
			"width" : "100%"
		}).addClass("gb-layerdefinition-input-categoryname").val(strc[a].name);

		var catNameCol = $("<div>").addClass("col-md-11").append(categoryName);
		var toggleCol = $("<div>").addClass("col-md-1").addClass("text-right").append(toggleBtn);
		var categoryHeader = $("<div>").addClass("row").append(catNameCol).append(toggleCol);
		var deleteCategoryBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-category").text(
				this.translation.deleteCategory[this.locale]);
		var addLayerBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-add-layer").text(
				this.translation.addLayer[this.locale]);
		var wrap2 = $("<div>").addClass("col-md-12").addClass("text-right").append(addLayerBtn).append(deleteCategoryBtn);
		var categoryMid = $("<div>").addClass("row").append(wrap2);
		var layerArea = $("<div>").addClass("gb-layerdefinition-layerarea");

		// 레이어 입력
		var layers = strc[a].layers;
		if (Array.isArray(layers)) {
			for (var b = 0; b < layers.length; b++) {
				var codeCol1 = $("<div>").addClass("col-md-1").append(this.translation.layerCode[this.locale]);
				var code = layers[b].code;
				var codeInput = $("<input>").attr({
					"type" : "text",
					"placeholder" : this.translation.exLayerCodeField[this.locale]
				}).addClass("form-control").addClass("gb-layerdefinition-input-layercode").val(code);

				var codeCol2 = $("<div>").addClass("col-md-6").append(codeInput);
				var typeCol1 = $("<div>").addClass("col-md-1").text(this.translation.layerType[this.locale]);
				var geomSelect = $("<select>").addClass("form-control").addClass("gb-layerdefinition-select-geometry");
				var geometry = layers[b].geometry;
				for (var i = 0; i < this.geometryType.length; i++) {
					var option = $("<option>").text(this.geometryType[i].toUpperCase()).attr("value", this.geometryType[i]);
					if (geometry === $(option).val()) {
						$(option).attr("selected", "selected");
					}
					$(geomSelect).append(option);
				}
				var typeCol2 = $("<div>").addClass("col-md-2").append(geomSelect);
				var delBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-layer").text(
						this.translation.deleteLayer[this.locale]).css("width", "100%");
				var deleLayerCol1 = $("<div>").addClass("col-md-2").append(delBtn);
				var row1o = $("<div>").addClass("row").append(codeCol1).append(codeCol2).append(typeCol1).append(typeCol2).append(
						deleLayerCol1);

				var fixAttr = $("<p>").text(this.translation.fixedAttr[this.locale]).css({
					"float" : "left"
				});
				var delFixAttr = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-add-attribute").text(
						this.translation.addFixedAttr[this.locale]).css({
					"width" : "100%"
				});
				var fixCol1 = $("<div>").addClass("col-md-10").append(fixAttr);
				var fixCol2 = $("<div>").addClass("col-md-2").append(delFixAttr);
				var row2o = $("<div>").addClass("row").append(fixCol1).append(fixCol2);

				var attrAreaCol1 = $("<div>").addClass("col-md-12").addClass("gb-layerdefinition-attributearea");

				var fix = layers[b].fix;
				if (Array.isArray(fix)) {
					for (var k = 0; k < fix.length; k++) {
						var r1col1 = $("<div>").addClass("col-md-1").text(this.translation.attrName[this.locale]);

						var attrName = $("<input>").attr({
							"type" : "text",
							"placeholder" : this.translation.exFixedAttrNameField[this.locale]
						}).addClass("form-control").addClass("gb-layerdefinition-input-attributename").val(fix[k].name);
						var r1col2 = $("<div>").addClass("col-md-3").append(attrName);

						var r1col3 = $("<div>").addClass("col-md-1").text(this.translation.attrType[this.locale]);

						var dtypeSelect = $("<select>").addClass("form-control").addClass("gb-layerdefinition-select-attributetype");
						var nullOption = $("<option>").text(this.translation.notSet[this.locale]).attr("value", "null");
						$(dtypeSelect).append(nullOption);
						for (var i = 0; i < this.dataType.length; i++) {
							var option = $("<option>").text(this.dataType[i]).attr("value", this.dataType[i]);
							if (fix[k].type === $(option).val()) {
								$(option).attr("selected", "selected");
							} else if (fix[k].type === null) {
								$(nullOption).attr("selected", "selected");
							}
							$(dtypeSelect).append(option);
						}
						var r1col4 = $("<div>").addClass("col-md-2").append(dtypeSelect);

						var r1col5 = $("<div>").addClass("col-md-1").text(this.translation.length[this.locale]);

						var attrLength = $("<input>").attr("type", "number").addClass("form-control").addClass(
								"gb-layerdefinition-input-attributelength").val(fix[k].length);
						var r1col6 = $("<div>").addClass("col-md-2").append(attrLength);

						var r1col7 = $("<div>").addClass("col-md-1").text(this.translation.nullAllow[this.locale]);

						var nullCheck = $("<input>").attr("type", "checkbox").addClass("gb-layerdefinition-check-attributenull");
						if (fix[k].isnull) {
							$(nullCheck).prop("checked", true);
						}
						var r1col8 = $("<div>").addClass("col-md-1").append(nullCheck);

						var row1 = $("<div>").addClass("row").append(r1col1).append(r1col2).append(r1col3).append(r1col4).append(r1col5)
								.append(r1col6).append(r1col7).append(r1col8);

						var r2col1 = $("<div>").addClass("col-md-1").text(this.translation.allowValue[this.locale]);
						var values = $("<input>").attr({
							"type" : "text",
							"placeholder" : this.translation.exAllowValueField[this.locale]
						}).addClass("form-control").addClass("gb-layerdefinition-input-attributevalues");

						var valuesToString = fix[k].values;
						if (Array.isArray(valuesToString)) {
							$(values).val(valuesToString.toString());
						}

						var r2col2 = $("<div>").addClass("col-md-11").append(values);
						var row2 = $("<div>").addClass("row").append(r2col1).append(r2col2);

						var delBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-attribute")
								.text(this.translation.deleteFixedAttr[this.locale]);
						var r3col1 = $("<div>").addClass("col-md-12").addClass("text-right").append(delBtn);
						var row3 = $("<div>").addClass("row").append(r3col1);

						var pBody = $("<div>").addClass("panel-body").append(row1).append(row2).append(row3);
						var pn = $("<div>").addClass("panel").addClass("panel-default").append(pBody);
						$(attrAreaCol1).append(pn);
					}
				}

				var row3o = $("<div>").addClass("row").append(attrAreaCol1);
				var well = $("<div>").addClass("well").append(row1o).append(row2o).append(row3o);
				$(layerArea).append(well);
			}
		}

		var wrap3 = $("<div>").addClass("col-md-12").append(layerArea);
		var layerAreaRow = $("<div>").addClass("row").append(wrap3);
		var toggleArea = $("<div>").addClass("gb-layerdefinition-togglearea").append(categoryMid).append(layerAreaRow);
		var pBody = $("<div>").addClass("panel-body").append(categoryHeader).append(toggleArea);
		var pn = $("<div>").addClass("panel").addClass("panel-default").append(pBody);
		$(this.panelBody).append(pn);
	}
};

gb.validation.LayerDefinition.prototype.clearStructure = function() {
	this.structure = [];
};

gb.validation.LayerDefinition.prototype.setStructure = function(strc) {
	var isOK = true;
	var elemName = [ "name", "layers" ];
	var elemLayers = [ "fix", "code", "geometry" ];
	var elemFix = [ "name", "type", "isnull", "length", "values" ];
	if (Array.isArray(strc)) {
		for (var i = 0; i < strc.length; i++) {
			var nameKeys = Object.keys(strc[i]);
			for (var j = 0; j < nameKeys.length; j++) {
				if (elemName.indexOf(nameKeys[j]) === -1) {
					isOK = false;
					this.setMessage("danger", " " + this.translation.keyName[this.locale] + nameKeys[i]
							+ this.translation.noticeInvalidKey[this.locale]);
					console.error(this.translation.keyName[this.locale] + nameKeys[i] + this.translation.noticeInvalidKey[this.locale]);
					break;
				}
			}
			if (!strc[i].hasOwnProperty("name")) {
				isOK = false;
				this.setMessage("danger", " " + (i + 1) + this.translation.noticeCategoryNameEnter[this.locale]);
				console.error((i + 1) + this.translation.noticeCategoryNameEnter[this.locale]);
				break;
			}
			if (strc[i].hasOwnProperty("layers")) {
				var layers = strc[i]["layers"];
				if (Array.isArray(layers)) {
					for (var j = 0; j < layers.length; j++) {
						var layerKeys = Object.keys(layers[j]);
						for (var k = 0; k < layerKeys.length; k++) {
							if (elemLayers.indexOf(layerKeys[k]) === -1) {
								isOK = false;
								this.setMessage("danger", " " + this.translation.keyName[this.locale] + layerKeys[k]
										+ this.translation.noticeInvalidKey[this.locale]);
								console.error(this.translation.keyName[this.locale] + layerKeys[k]
										+ this.translation.noticeInvalidKey[this.locale]);
								break;
							}
						}
						if (layers.hasOwnProperty("fix")) {
							if (Array.isArray(layers[j]["fix"])) {
								for (var k = 0; k < layers[j]["fix"].length; k++) {
									var fixKeys = Object.keys(layers[j]["fix"][k]);
									for (var l = 0; l < fixKeys.length; l++) {
										if (elemFix.indexOf(fixKeys[l]) === -1) {
											isOK = false;
											this.setMessage("danger", " " + this.translation.keyName[this.locale] + fixKeys[l]
													+ this.translation.noticeInvalidKey[this.locale]);
											console.error(this.translation.keyName[this.locale] + fixKeys[l]
													+ this.translation.noticeInvalidKey[this.locale]);
											break;
										}
									}
								}
							}
						}
					}
				}
			} else {
				isOK = false;
				this.setMessage("danger", " " + (i + 1) + this.translation.noticeNotExistLayer[this.locale]);
				console.error((i + 1) + this.translation.noticeNotExistLayer[this.locale]);
				break;
			}
		}
	} else {
		isOK = false;
		this.setMessage("danger", " " + this.translation.noticeOptionStructError[this.locale]);
		console.error(this.translation.noticeOptionStructError[this.locale]);
	}
	if (isOK) {
		this.structure = strc;
		this.setMessage("success", " " + this.translation.noticeLayerDefUpdate[this.locale]);
	}
	return isOK;
};

gb.validation.LayerDefinition.prototype.getStructure = function() {
	return this.structure;
};

gb.validation.LayerDefinition.prototype.setJSONFile = function() {

};

gb.validation.LayerDefinition.prototype.getJSONFile = function() {
	// Opera 8.0+
	var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;

	// Firefox 1.0+
	var isFirefox = typeof InstallTrigger !== 'undefined';

	// Safari 3.0+ "[object HTMLElementConstructor]"
	var isSafari = /constructor/i.test(window.HTMLElement) || (function(p) {
		return p.toString() === "[object SafariRemoteNotification]";
	})(!window['safari'] || (typeof safari !== 'undefined' && safari.pushNotification));

	// Internet Explorer 6-11
	var isIE = /* @cc_on!@ */false || !!document.documentMode;

	// Edge 20+
	var isEdge = !isIE && !!window.StyleMedia;

	// Chrome 1+
	var isChrome = !!window.chrome && !!window.chrome.webstore;

	// Blink engine detection
	var isBlink = (isChrome || isOpera) && !!window.CSS;

	if (isIE) {
		download(JSON.stringify(this.getStructure()), "layer_setting.json", "text/plain");
	} else {
		var setting = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.getStructure()));
		var anchor = $("<a>").attr({
			"href" : setting,
			"download" : "layer_setting.json"
		});
		$(anchor)[0].click();
	}
};

gb.validation.LayerDefinition.prototype.inputAttributeValues = function(inp) {
	var catIdx = $(inp).parents().eq(12).index();
	var layerIdx = $(inp).parents().eq(6).index();
	var attrIdx = $(inp).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				fixElem = fix[attrIdx];
				if (fixElem !== undefined && fixElem !== null) {
					fixElem["values"] = $(inp).val().split(",");
					var attrValues = fixElem["values"];
					for (var z = 0; z < attrValues.length; z++) {
						attrValues[z] = attrValues[z].trim();
					}
					// fixElem["values"] = $(inp).val().replace(/(\s*)/g,
					// '').split(",");
				} else {
					var obj = {
						"values" : $(inp).val().split(",")
					// "values" : $(inp).val().replace(/(\s*)/g, '').split(",")
					};
					var attrValues = obj.values;
					for (var z = 0; z < attrValues.length; z++) {
						attrValues[z] = attrValues[z].trim();
					}
				}
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.checkAttributeNull = function(chk) {
	var catIdx = $(chk).parents().eq(12).index();
	var layerIdx = $(chk).parents().eq(6).index();
	var attrIdx = $(chk).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				fixElem = fix[attrIdx];
				if (fixElem !== undefined && fixElem !== null) {
					fixElem["isnull"] = $(chk).is(":checked");
				} else {
					var obj = {
						"isnull" : $(chk).is(":checked")
					};
				}
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.inputAttributeLength = function(inp) {
	var catIdx = $(inp).parents().eq(12).index();
	var layerIdx = $(inp).parents().eq(6).index();
	var attrIdx = $(inp).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				fixElem = fix[attrIdx];
				if (fixElem !== undefined && fixElem !== null) {
					fixElem["length"] = isNaN(parseInt($(inp).val())) ? null : parseInt($(inp).val());
				} else {
					var obj = {
						"length" : isNaN(parseInt($(inp).val())) ? null : parseInt($(inp).val())
					};
				}
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.selectAttributeType = function(sel) {
	var catIdx = $(sel).parents().eq(12).index();
	var layerIdx = $(sel).parents().eq(6).index();
	var attrIdx = $(sel).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				fixElem = fix[attrIdx];
				if (fixElem !== undefined && fixElem !== null) {
					fixElem["type"] = $(sel).val() === "null" ? null : $(sel).val();
				} else {
					var obj = {
						"type" : $(sel).val() === "null" ? null : $(sel).val()
					};
				}
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.inputAttributeName = function(inp) {
	var catIdx = $(inp).parents().eq(12).index();
	var layerIdx = $(inp).parents().eq(6).index();
	var attrIdx = $(inp).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				fixElem = fix[attrIdx];
				if (fixElem !== undefined && fixElem !== null) {
					fixElem["name"] = $(inp).val();
				} else {
					var obj = {
						"name" : $(inp).val()
					};
				}
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.deleteAttribute = function(btn) {
	var catIdx = $(btn).parents().eq(12).index();
	var layerIdx = $(btn).parents().eq(6).index();
	var attrIdx = $(btn).parents().eq(3).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				layer["fix"].splice(attrIdx, 1);
				$(btn).parents().eq(3).remove();
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.deleteLayer = function(btn) {
	var catIdx = $(btn).parents().eq(8).index();
	console.log(catIdx);
	var layerIdx = $(btn).parents().eq(2).index();
	console.log(layerIdx);
	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			cat["layers"].splice(layerIdx, 1);
			$(btn).parents().eq(2).remove();
		}
	}
};

gb.validation.LayerDefinition.prototype.selectLayerGeometry = function(sel) {
	var catIdx = $(sel).parents().eq(8).index();
	console.log(catIdx);
	var codeIdx = $(sel).parents().eq(2).index();
	console.log(codeIdx);
	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[codeIdx];
			layer["geometry"] = $(sel).val();
		}
	}
};

gb.validation.LayerDefinition.prototype.inputLayerCode = function(inp) {
	var catIdx = $(inp).parents().eq(8).index();
	console.log(catIdx);
	var codeIdx = $(inp).parents().eq(2).index();
	console.log(codeIdx);
	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[codeIdx];
			layer["code"] = $(inp).val();
		}
	}
};

gb.validation.LayerDefinition.prototype.deleteCategory = function(btn) {
	var idx = $(btn).parents().eq(4).index();
	var strc = this.getStructure();
	var elem = strc[idx];
	if (elem !== undefined && elem !== null) {
		this.getStructure().splice(idx, 1);
	}
	$(btn).parents().eq(4).remove();
};

gb.validation.LayerDefinition.prototype.inputCategoryName = function(inp) {
	var idx = $(inp).parents().eq(3).index();
	var strc = this.getStructure();
	var elem = strc[idx];
	if (elem !== undefined && elem !== null) {
		elem["name"] = $(inp).val();
	} else {
		strc[idx] = {
			"name" : $(inp).val()
		};
	}
};

gb.validation.LayerDefinition.prototype.toggleLayerArea = function(btn) {
	var toggleArea = $(btn).parent().parent().next();
	var icon = $(btn).find("i");
	if ($(toggleArea).hasClass("gb-layerdefinition-togglearea")) {
		if ($(toggleArea).is(":visible")) {
			$(toggleArea).hide();
			if ($(icon).hasClass("fa-caret-up")) {
				$(icon).removeClass("fa-caret-up");
				$(icon).addClass("fa-caret-down");
			}
		} else {
			$(toggleArea).show();
			if ($(icon).hasClass("fa-caret-down")) {
				$(icon).removeClass("fa-caret-down");
				$(icon).addClass("fa-caret-up");
			}
		}
	}
};

gb.validation.LayerDefinition.prototype.addCategory = function() {
	var toggleIcon = $("<i>").addClass("fas").addClass("fa-caret-up").addClass("fa-lg");
	var toggleBtn = $("<button>").addClass("btn").addClass("btn-link").addClass("gb-layerdefinition-toggle-category").append(toggleIcon);
	var categoryName = $("<input>").attr({
		"type" : "text",
		"placeholder" : this.translation.exCategoryField[this.locale],
		"title" : "some tips"
	}).css({
		"border" : "0",
		"border-bottom" : "solid 1px #9a9a9a",
		"height" : "32px",
		"width" : "100%"
	}).addClass("gb-layerdefinition-input-categoryname");

	var catNameCol = $("<div>").addClass("col-md-11").append(categoryName);
	var toggleCol = $("<div>").addClass("col-md-1").addClass("text-right").append(toggleBtn);
	var categoryHeader = $("<div>").addClass("row").append(catNameCol).append(toggleCol);
	var deleteCategoryBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-category").text(
			this.translation.deleteCategory[this.locale]);
	var addLayerBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-add-layer").text(
			this.translation.addLayer[this.locale]);
	var wrap2 = $("<div>").addClass("col-md-12").addClass("text-right").append(addLayerBtn).append(deleteCategoryBtn);
	var categoryMid = $("<div>").addClass("row").append(wrap2);

	var layerArea = $("<div>").addClass("gb-layerdefinition-layerarea");
	var wrap3 = $("<div>").addClass("col-md-12").append(layerArea);
	var layerAreaRow = $("<div>").addClass("row").append(wrap3);
	var toggleArea = $("<div>").addClass("gb-layerdefinition-togglearea").append(categoryMid).append(layerAreaRow);
	var pBody = $("<div>").addClass("panel-body").append(categoryHeader).append(toggleArea);
	var pn = $("<div>").addClass("panel").addClass("panel-default").append(pBody);
	$(this.panelBody).append(pn);

	var strc = this.getStructure();
	var obj = {
		"name" : null,
		"layers" : null
	};
	strc.push(obj);
};

gb.validation.LayerDefinition.prototype.addLayer = function(btn) {
	var col1 = $("<div>").addClass("col-md-1").append(this.translation.layerCode[this.locale]);
	var codeInput = $("<input>").attr({
		"type" : "text",
		"placeholder" : this.translation.exLayerCodeField[this.locale]
	}).addClass("form-control").addClass("gb-layerdefinition-input-layercode");

	var col2 = $("<div>").addClass("col-md-6").append(codeInput);
	var col3 = $("<div>").addClass("col-md-1").text(this.translation.layerType[this.locale]);
	var geomSelect = $("<select>").addClass("form-control").addClass("gb-layerdefinition-select-geometry");
	for (var i = 0; i < this.geometryType.length; i++) {
		var option = $("<option>").text(this.geometryType[i].toUpperCase()).attr("value", this.geometryType[i]);
		if (i === 0) {
			$(option).attr("selected", "selected");
		}
		$(geomSelect).append(option);
	}
	var col4 = $("<div>").addClass("col-md-2").append(geomSelect);
	var delBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-layer").text(
			this.translation.deleteLayer[this.locale]).css("width", "100%");
	var col5 = $("<div>").addClass("col-md-2").append(delBtn);
	var row1 = $("<div>").addClass("row").append(col1).append(col2).append(col3).append(col4).append(col5);

	var fixAttr = $("<p>").text(this.translation.fixedAttr[this.locale]).css({
		"float" : "left"
	});
	var delFixAttr = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-add-attribute").text(
			this.translation.addFixedAttr[this.locale]).css({
		"width" : "100%"
	});
	var r2col1 = $("<div>").addClass("col-md-10").append(fixAttr);
	var r2col2 = $("<div>").addClass("col-md-2").append(delFixAttr);
	var row2 = $("<div>").addClass("row").append(r2col1).append(r2col2);

	var r3col1 = $("<div>").addClass("col-md-12").addClass("gb-layerdefinition-attributearea");
	var row3 = $("<div>").addClass("row").append(r3col1);
	var well = $("<div>").addClass("well").append(row1).append(row2).append(row3);
	$(btn).parent().parent().next().find(".gb-layerdefinition-layerarea").append(well);

	var obj = {
		"code" : null,
		"geometry" : $(geomSelect).val(),
		"fix" : null
	}
	console.log(obj);
	var catIdx = $(well).parents().eq(5).index();
	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layerIdx = $(well).index();
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			layers[layerIdx] = obj;
		} else {
			cat["layers"] = [];
			cat["layers"].push(obj);
		}
	}
};

gb.validation.LayerDefinition.prototype.addAttribute = function(btn) {
	var r1col1 = $("<div>").addClass("col-md-1").text(this.translation.attrName[this.locale]);

	var attrName = $("<input>").attr({
		"type" : "text",
		"placeholder" : this.translation.exFixedAttrNameField[this.locale]
	}).addClass("form-control").addClass("gb-layerdefinition-input-attributename");
	var r1col2 = $("<div>").addClass("col-md-3").append(attrName);

	var r1col3 = $("<div>").addClass("col-md-1").text(this.translation.attrType[this.locale]);

	var dtypeSelect = $("<select>").addClass("form-control").addClass("gb-layerdefinition-select-attributetype");
	var nullOption = $("<option>").text(this.translation.notSet[this.locale]).attr("value", "null");
	$(dtypeSelect).append(nullOption);
	for (var i = 0; i < this.dataType.length; i++) {
		var option = $("<option>").text(this.dataType[i]).attr("value", this.dataType[i]);
		/*
		 * if (i === 0) { $(option).attr("selected", "selected"); }
		 */
		$(dtypeSelect).append(option);
	}
	var r1col4 = $("<div>").addClass("col-md-2").append(dtypeSelect);

	var r1col5 = $("<div>").addClass("col-md-1").text(this.translation.length[this.locale]);

	var attrLength = $("<input>").attr("type", "number").addClass("form-control").addClass("gb-layerdefinition-input-attributelength");
	var r1col6 = $("<div>").addClass("col-md-2").append(attrLength);

	var r1col7 = $("<div>").addClass("col-md-1").text(this.translation.nullAllow[this.locale]);

	var nullCheck = $("<input>").attr("type", "checkbox").addClass("gb-layerdefinition-check-attributenull");
	var r1col8 = $("<div>").addClass("col-md-1").append(nullCheck);

	var row1 = $("<div>").addClass("row").append(r1col1).append(r1col2).append(r1col3).append(r1col4).append(r1col5).append(r1col6).append(
			r1col7).append(r1col8);

	var r2col1 = $("<div>").addClass("col-md-1").text(this.translation.allowValue[this.locale]);
	var values = $("<input>").attr({
		"type" : "text",
		"placeholder" : this.translation.exAllowValueField[this.locale]
	}).addClass("form-control").addClass("gb-layerdefinition-input-attributevalues");
	var r2col2 = $("<div>").addClass("col-md-11").append(values);
	var row2 = $("<div>").addClass("row").append(r2col1).append(r2col2);

	var delBtn = $("<button>").addClass("btn").addClass("btn-default").addClass("gb-layerdefinition-delete-attribute").text(
			this.translation.deleteFixedAttr[this.locale]);
	var r3col1 = $("<div>").addClass("col-md-12").addClass("text-right").append(delBtn);
	var row3 = $("<div>").addClass("row").append(r3col1);

	var pBody = $("<div>").addClass("panel-body").append(row1).append(row2).append(row3);
	var pn = $("<div>").addClass("panel").addClass("panel-default").append(pBody);
	$(btn).parent().parent().next().find(".gb-layerdefinition-attributearea").append(pn);

	var obj = {
		"name" : null,
		"type" : $(dtypeSelect).val(),
		"isnull" : false,
		"length" : null,
		"values" : null
	};

	var catIdx = $(pn).parents().eq(8).index();
	var layerIdx = $(pn).parents().eq(2).index();
	var attrIdx = $(pn).index();

	var strc = this.getStructure();
	if (Array.isArray(strc)) {
		var cat = strc[catIdx];
		var layers = cat["layers"];
		if (Array.isArray(layers)) {
			var layer = layers[layerIdx];
			var fix = layer["fix"];
			if (Array.isArray(fix)) {
				layer["fix"][attrIdx] = obj;
			} else {
				layer["fix"] = [];
				layer["fix"].push(obj);
			}
		}
	}
};