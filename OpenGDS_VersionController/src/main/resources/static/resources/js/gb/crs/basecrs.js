/**
 * 베이스 좌표계 변경 모달 객체를 정의한다.
 * 
 * @class gb.crs.BaseCRS
 * @memberof gb.crs
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String}
 *            obj.title - 모달의 제목
 * @param {Number}
 *            obj.width - 모달의 너비 (픽셀)
 * @param {Number}
 *            obj.height - 모달의 높이 (픽셀)
 * @param {Boolean}
 *            obj.autoOpen - 선언과 동시에 표출 할 것인지 선택
 * @param {DOM}
 *            obj.message - 현재 좌표계를 출력할 DOM
 * @param {ol.Map}
 *            obj.map - 베이스 좌표계를 변경할 ol.Map 객체
 * @param {String}
 *            obj.epsg - 설정하고자 하는 좌표계의 EPSG 코드
 * @version 0.01
 * @author SOYIJUN
 * @date 2017. 07.26
 */
gb.crs.BaseCRS = function(obj) {
	obj.width = 435;
	obj.height = 180;
	obj.keep = true;
	gb.modal.Base.call(this, obj);
	var that = this;
	var options = obj ? obj : {};
	this.message = options.message ? options.message : undefined;
	this.maps = options.maps ? options.maps : undefined;
	this.epsg = options.epsg ? options.epsg : "3857";
	this.callback = options.callback ? options.callback : undefined;
	var label = $("<span>").text("EPSG: ");
	this.searchBar = $("<input>").attr({
		"type" : "number",
		"placeholder" : "EX) 3857"
	}).addClass("gb-form").css({
		"width" : "312px",
		"display" : "inline-block"
	});

	this.tout = false;
	$(this.searchBar).keyup(function() {
		if (that.tout) {
			clearTimeout(that.tout);
		}
		that.tout = setTimeout(function() {
			var v = $(that.searchBar).val();
			console.log(v);
			that.searchEPSGCode(v);
		}, 250);
	});

	this.validIconSpan = $("<span>").css({
		"margin-left" : "15px",
		"margin-right" : "0"
	});
	var area = $("<div>").append(label).append(this.searchBar).append(this.validIconSpan).css({
		"margin" : "10px 10px"
	});
	this.setModalBody(area);

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close").click(function() {
		that.close();
	});
	this.searchBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("OK").click(
			function() {
				var val = $(that.searchBar).val().replace(/(\s*)/g, '');
				// that.searchEPSGCode(val);
				if (that.getValidEPSG()) {
					that.applyProjection(that.getProjection().code, that.getProjection().name, that.getProjection().proj4, that
							.getProjection().bbox);
					that.close();
				}
			});

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(this.searchBtn).append(closeBtn);

	this.setModalFooter(buttonArea);

	$("body").append(this.modal);
	$("body").append(this.background);

	this.searchEPSGCode(this.epsg, true, this.callback);
};
gb.crs.BaseCRS.prototype = Object.create(gb.modal.Base.prototype);
gb.crs.BaseCRS.prototype.constructor = gb.crs.BaseCRS;

/**
 * 베이스 좌표계를 변경하고자 하는 ol.Map 객체를 반환한다.
 * 
 * @method gb.crs.BaseCRS#getMaps
 * @return {ol.Map} 베이스 좌표계를 변경하고자 하는 ol.Map 객체
 */
gb.crs.BaseCRS.prototype.getMaps = function() {
	return this.maps;
};

/**
 * 베이스 좌표계를 변경하고자 하는 ol.Map 객체를 설정한다.
 * 
 * @method gb.crs.BaseCRS#setMaps
 * @param {ol.Map}
 *            map - 베이스 좌표계를 변경하고자 하는 ol.Map 객체
 */
gb.crs.BaseCRS.prototype.setMaps = function(maps) {
	this.maps = maps;
};

/**
 * 현재 좌표계를 표시할 DOM 객체를 반환한다.
 * 
 * @method gb.crs.BaseCRS#getMessage
 * @return 현재 좌표계를 표시할 DOM 객체
 */
gb.crs.BaseCRS.prototype.getMessage = function() {
	return this.message;
};

/**
 * 현재 좌표계를 표시할 DOM 객체를 설정한다.
 * 
 * @method gb.crs.BaseCRS#setMessage
 * @param {DOM}
 *            message - 현재 좌표계를 표시할 DOM 객체
 */
gb.crs.BaseCRS.prototype.setMessage = function(message) {
	this.message = message;
};

/**
 * 현재 적용된 베이스 좌표계의 EPSG 코드를 반환한다.
 * 
 * @method gb.crs.BaseCRS#getEPSGCode
 * @return {String} 현재 적용된 베이스 좌표계의 EPSG 코드
 */
gb.crs.BaseCRS.prototype.getEPSGCode = function() {
	return this.epsg;
};

/**
 * 현재 적용된 베이스 좌표계의 EPSG 코드를 설정한다.
 * 
 * @method gb.crs.BaseCRS#getEPSGCode
 * @param {String}
 *            code - 현재 적용된 베이스 좌표계의 EPSG 코드
 */
gb.crs.BaseCRS.prototype.setEPSGCode = function(code) {
	this.epsg = code;
};

/**
 * 베이스 좌표계를 변경하기 위한 EPSG 코드를 검색한다.
 * 
 * @method gb.crs.BaseCRS#searchEPSGCode
 * @param {String}
 *            code - 베이스 좌표계를 변경하기 위한 EPSG 코드
 */
gb.crs.BaseCRS.prototype.searchEPSGCode = function(code, apply, callback) {
	console.log(code);
	var that = this;
	fetch('https://epsg.io/?format=json&q=' + code).then(function(response) {
		return response.json();
	}).then(function(json) {
		if (json.number_result !== 1) {
			// $(that.getMessage()).text("Error: Couldn't find EPSG Code. EPSG:"
			// +
			// that.getEPSGCode());
			that.setValidEPSG(false);
			that.setProjection(undefined, undefined, undefined, undefined);
			console.error("no crs");
			// that.close();
			return;
		} else if (json.number_result < 1) {
			// $(that.getMessage()).text("Error: Couldn't find EPSG Code. EPSG:"
			// +
			// that.getEPSGCode());
			that.setValidEPSG(false);
			that.setProjection(undefined, undefined, undefined, undefined);
			console.error("no crs");
			// that.close();
			return;
		}
		var results = json['results'];
		if (results && results.length > 0) {
			for (var i = 0, ii = results.length; i < ii; i++) {
				var result = results[i];
				if (result) {
					var codes = result['code'], name = result['name'], proj4def = result['proj4'], bbox = result['bbox'];
					if (codes && codes.length > 0 && proj4def && proj4def.length > 0 && bbox && bbox.length == 4) {

						if (code === codes) {
							that.setEPSGCode(code);
							that.setValidEPSG(true);
							that.setProjection(code, name, proj4def, bbox);
							if (apply) {
								that.applyProjection(code, name, proj4def, bbox, callback);
							}
						}

						return;
					} else {
						$(that.getMessage()).text("Error: Not support EPSG Code. EPSG:" + that.getEPSGCode());
						console.error("no crs");
						// that.close();
						that.setValidEPSG(false);
						that.setProjection(undefined, undefined, undefined, undefined);
						return;
					}
				}
			}
		}
		// that.close();
		return;
	});
};

/**
 * 베이스 좌표계를 적용한다.
 * 
 * @method gb.crs.BaseCRS#applyProjection
 * @param {String}
 *            code - EPSG 코드
 * @param {String}
 *            name - 좌표계 이름
 * @param {String}
 *            proj4def - proj4 좌표계
 * @param {Number[]}
 *            bbox - 좌표계 영역
 */
gb.crs.BaseCRS.prototype.applyProjection = function(code, name, proj4def, bbox, callback) {
	var that = this;
	if (code === null || name === null || proj4def === null || bbox === null) {
		if (Array.isArray(this.getMaps())) {
			var maps = this.getMaps();
			for (var i = 0; i < maps.length; i++) {
				if (maps[i] instanceof ol.Map) {
					maps[i].setView(new ol.View({
						projection : 'EPSG:3857',
						center : [ 0, 0 ],
						zoom : 1
					}));
				}
			}
			return;
		} else if (this.getMaps() instanceof ol.Map) {
			this.getMaps().setView(new ol.View({
				projection : 'EPSG:3857',
				center : [ 0, 0 ],
				zoom : 1
			}));
			return;
		}
	}
	this.setEPSGCode(code);
	var newProjCode = 'EPSG:' + code;
	$(this.getMessage()).text(newProjCode);
	proj4.defs(newProjCode, proj4def);
	var newProj = ol.proj.get(newProjCode);
	var fromLonLat = ol.proj.getTransform('EPSG:4326', newProj);

	// very approximate calculation of projection extent
	var extent = ol.extent.applyTransform([ bbox[1], bbox[2], bbox[3], bbox[0] ], fromLonLat);
	newProj.setExtent(extent);
	var newView = new ol.View({
		projection : newProj
	});
	if (Array.isArray(this.getMaps())) {
		var maps = this.getMaps();
		for (var i = 0; i < maps.length; i++) {
			if (maps[i] instanceof ol.Map) {
				maps[i].setView(newView);
			}
		}
	} else if (this.getMaps() instanceof ol.Map) {
		this.getMaps().setView(newView);
	}
	newView.fit(extent);
	console.log(this.getEPSGCode());
	if (typeof callback === "function") {
		callback();
	}
};

/**
 * 베이스 좌표계를 설정한다.
 * 
 * @method gb.crs.BaseCRS#setProjection
 * @param {String}
 *            code - EPSG 코드
 * @param {String}
 *            name - 좌표계 이름
 * @param {String}
 *            proj4def - proj4 좌표계
 * @param {Number[]}
 *            bbox - 좌표계 영역
 */
gb.crs.BaseCRS.prototype.setProjection = function(code, name, proj4def, bbox) {
	var that = this;
	this.projObj = {
		"code" : code,
		"name" : name,
		"proj4" : proj4def,
		"bbox" : bbox
	};
};

/**
 * 베이스 좌표계를 반환한다.
 * 
 * @method gb.crs.BaseCRS#getProjection
 * @return {Object} 좌표계 정보
 */
gb.crs.BaseCRS.prototype.getProjection = function() {
	return this.projObj;
};

/**
 * epsg 코드의 유효성을 설정한다.
 * 
 * @method gb.crs.BaseCRS#setValidEPSG
 * @param {Boolean}
 *            flag - EPSG 코드 유효성
 */
gb.crs.BaseCRS.prototype.setValidEPSG = function(flag) {
	this.validEPSG = flag;

	$(this.validIconSpan).empty();

	if (flag) {
		var validIcon = $("<i>").addClass("fas").addClass("fa-check");
		$(this.validIconSpan).append(validIcon);

		if ($(this.validIconSpan).hasClass("gb-geoserver-uploadshp-epsg-valid-icon")) {
			// $(this.validIconSpan).addClass("gb-geoserver-uploadshp-epsg-invalid-icon");
		} else {
			if ($(this.validIconSpan).hasClass("gb-geoserver-uploadshp-epsg-invalid-icon")) {
				$(this.validIconSpan).removeClass("gb-geoserver-uploadshp-epsg-invalid-icon");
			}
			$(this.validIconSpan).addClass("gb-geoserver-uploadshp-epsg-valid-icon");
		}
		$(this.searchBtn).prop("disabled", false);
	} else {
		var validIcon = $("<i>").addClass("fas").addClass("fa-times");
		$(this.validIconSpan).append(validIcon);

		if ($(this.validIconSpan).hasClass("gb-geoserver-uploadshp-epsg-invalid-icon")) {
			// $(this.validIconSpan).addClass("gb-geoserver-uploadshp-epsg-invalid-icon");
		} else {
			if ($(this.validIconSpan).hasClass("gb-geoserver-uploadshp-epsg-valid-icon")) {
				$(this.validIconSpan).removeClass("gb-geoserver-uploadshp-epsg-valid-icon");
			}
			$(this.validIconSpan).addClass("gb-geoserver-uploadshp-epsg-invalid-icon");
		}
		$(this.searchBtn).prop("disabled", true);
	}
};

/**
 * epsg 코드의 유효성을 반환한다.
 * 
 * @method gb.crs.BaseCRS#getValidEPSG
 * @return {Boolean} EPSG 코드 유효성
 */
gb.crs.BaseCRS.prototype.getValidEPSG = function() {
	return this.validEPSG;
};

/**
 * 모달을 연다
 * 
 * @method gb.crs.BaseCRS#open
 * @override
 */
gb.crs.BaseCRS.prototype.open = function() {
	gb.modal.Base.prototype.open.call(this);
	$(this.searchBar).val(this.getEPSGCode());
	if (this.getValidEPSG()) {
		$(this.searchBtn).prop("disabled", false);
	} else {
		$(this.searchBtn).prop("disabled", true);
	}
};