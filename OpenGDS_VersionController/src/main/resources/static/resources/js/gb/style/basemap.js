/**
 * 베이스맵을 변경하는 객체를 정의한다.
 * 
 * @class gb.style.BaseMap
 * @memberof gb.style
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {ol.Map}
 *            obj.map - 베이스 맵을 표시할 ol.Map 객체
 * @param {String}
 *            obj.defaultBaseMap - 기본값으로 설정할 베이스맵 이름
 * @param {Object[]}
 *            obj.layers - 추가로 사용할 베이스 맵
 * @param {String}
 *            obj.layers[].value - 추가 베이스 맵을 구분할 구분자
 * @param {String}
 *            obj.layers[].name - 화면상에서 표시할 베이스맵의 이름
 * @param {ol.layer.Base}
 *            obj.layers[].layer - 추가 베이스 맵으로 사용될 레이어 객체
 * @param {String}
 *            obj.layers[].thumb - 베이스 맵의 썸네일로 사용할 클래스명
 * @version 0.01
 * @author SOYIJUN
 * @date 2018. 06.04
 */

var gb;
if (!gb)
	gb = {};
if (!gb.style)
	gb.style = {};
gb.style.BaseMap = function(obj) {
	var that = this;
	obj.width = 788;
	obj.height = 331;
	obj.autoOpen = false;
	obj.title = "Base Map";
	obj.keep = true;
	gb.modal.Base.call(this, obj);
	var options = obj ? obj : {};
	this.map = options.map ? options.map : undefined;
	this.defaultMap = options.defaultBaseMap ? options.defaultBaseMap : "black";
	this.layers = options.layers ? options.layers : undefined;
	this.now = undefined;
	this.bases = {
		osm : {
			name : "OpenStreetMap",
			thumb : "gb-base-thumbnail-osm",
			layer : new ol.layer.Tile({
				visible : false,
				source : new ol.source.OSM()
			})
		},
		bing : {
			name : "Bing Map",
			thumb : "gb-base-thumbnail-bing",
			layer : new ol.layer.Tile({
				visible : false,
				preload : Infinity,
				source : new ol.source.BingMaps({
					key : 'AtZS5HHiM9JIaF1cUX-x-zQT_97S8YkWkjxDowNNUGD1D8jWUtgVmdaxsiitNQbo',
					imagerySet : "AerialWithLabels"
				})
			})
		},
		black : {
			name : "Black",
			thumb : "gb-base-thumbnail-none",
			layer : undefined
		},
		white : {
			name : "White",
			thumb : "gb-base-thumbnail-none",
			layer : undefined
		}
	};

	if (Array.isArray(this.layers)) {
		for (var i = 0; i < this.layers.length; i++) {
			if (this.layers[i].hasOwnProperty("value") && this.layers[i].hasOwnProperty("name") && this.layers[i].hasOwnProperty("layer")) {
				if (typeof this.layers[i].value === "string" && typeof this.layers[i].name === "string"
						&& this.layers[i].layer instanceof ol.layer.Base) {
					if (this.layers[i].thumb === undefined) {
						this.layers[i].thumb = "gb-thumbnail-none"
					}
					var obj = {
						name : this.layers[i].name,
						thumb : this.layers[i].thumb,
						layer : this.layers[i].layer
					}
					this.bases[this.layers[i].value] = obj;
				}
			}
		}
	}
	var keys = Object.keys(this.bases);
	for (var i = 0; i < keys.length; i++) {
		if (!(keys[i] === "black" || keys[i] === "white")) {
			this.map.addLayer(this.bases[keys[i]].layer);
		}
	}
	this.changeLayer(this.defaultMap);

	var body = $("<div>");
	$(this.modalBody).append(body);
	var keys = Object.keys(this.bases);
	for (var i = 0; i < keys.length; i++) {
		var radio = $("<input>").attr({
			"type" : "radio",
			"name" : "basemap",
			"value" : keys[i]
		});
		var label = $("<label>").append(radio).hover(function() {
			$(this).css({
				"cursor" : "pointer"
			});
		});
		var span = $("<span>").text(this.bases[keys[i]].name).css({
			"vertical-align" : "text-bottom",
			"margin" : "5px",
			"color" : "#fff"
		});
		label.append(span);

		var heading = $("<div>").addClass("gb-article-head").css({
			"background-color" : "#337ab7"
		});

		$(heading).append(label);

		var pBody = $("<div>").addClass("gb-article-body").css({
			"padding" : "18px"
		});

		var img = $("<div>").css({
			"width" : "120px",
			"height" : "80px",
			"margin" : "0 auto"
		}).addClass(this.bases[keys[i]].thumb);

		$(pBody).append(img);

		var pDefault = $("<div>").css({
			"width" : "165px",
			"display" : "inline-block",
			"margin" : "12px"
		}).addClass("gb-article").css({
			"border-color" : "#337ab7"
		});

		$(pDefault).append(heading);
		$(pDefault).append(pBody);

		$(body).append(pDefault);
	}

	var closeBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-default").text("Close").click(function() {
		that.close();
	});
	var okBtn = $("<button>").css({
		"float" : "right"
	}).addClass("gb-button").addClass("gb-button-primary").text("OK").click(function() {
		var val = $(that.getModalBody()).find(':radio[name="basemap"]:checked').val();
		that.changeLayer(val);
	});

	var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn).append(closeBtn);
	var modalFooter = $("<div>").append(buttonArea);
	$(this.modalFooter).append(modalFooter);

	// $("body").append(this.modal);
	// $("body").append(this.background);
};
gb.style.BaseMap.prototype = Object.create(gb.modal.Base.prototype);
gb.style.BaseMap.prototype.constructor = gb.style.BaseMap;

/**
 * 모달을 나타낸다.
 * 
 * @override
 * @method gb.style.BaseMap#open
 */
gb.style.BaseMap.prototype.open = function() {
	var keys = Object.keys(this.bases);
	for (var i = 0; i < keys.length; i++) {
		if (!(keys[i] === "black" || keys[i] === "white")) {
			if (this.bases[keys[i]].layer.getVisible()) {
				$("input:radio[name='basemap']:radio[value='" + keys[i] + "']").prop("checked", true);
			}
		} else {
			if (this.now === keys[i]) {
				$("input:radio[name='basemap']:radio[value='" + keys[i] + "']").prop("checked", true);
			}
		}
	}

	this.modal.css("display", "block");
	this.background.css("display", "block");
	this.refreshPosition();
};

/**
 * 베이스맵을 변경한다.
 * 
 * @method gb.style.BaseMap#changeLayer
 * @param {String}
 *            value - 변경하고자 하는 베이스맵의 구분자
 */
gb.style.BaseMap.prototype.changeLayer = function(value) {
	var keys = Object.keys(this.bases);
	for (var i = 0; i < keys.length; i++) {
		if (value === keys[i]) {
			if (value === "black") {
				var div = this.map.getTarget();
				if (typeof div === "string") {
					$("#" + div).css({
						"background-color" : "#000"
					});
				} else if ($(div).is("div")) {
					$(div).css({
						"background-color" : "#000"
					});
				}
				this.now = value;
			} else if (value === "white") {
				var div = this.map.getTarget();
				if (typeof div === "string") {
					$("#" + div).css({
						"background-color" : "#fff"
					});
				} else if ($(div).is("div")) {
					$(div).css({
						"background-color" : "#fff"
					});
				}
				this.now = value;
			} else {
				var div = this.map.getTarget();
				if (typeof div === "string") {
					$("#" + div).css({
						"background-color" : "#000"
					});
				} else if ($(div).is("div")) {
					$(div).css({
						"background-color" : "#000"
					});
				}
				this.bases[keys[i]].layer.setVisible(true);
				this.now = value;
			}
		} else {
			if (!(keys[i] === "black" || keys[i] === "white")) {
				this.bases[keys[i]].layer.setVisible(false);
			}
		}
	}
	this.close();
};