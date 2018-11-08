/**
 * Map 객체를 정의한다.
 * 
 * @class gb.Map
 * @memberof gb
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String |
 *            Element} obj.target - 지도 영역이 될 Div의 ID 또는 Element
 * @param {ol.View}
 *            obj.view - 지도 영역에 사용될 ol.View 객체
 * @param obj.upperMap -
 *            상위 ol.Map 객체의 생성자 옵션
 * @param {Object}
 *            obj.upperMap - 상위 ol.Map 객체의 생성자 옵션
 * @param {Object}
 *            obj.lowerMap - 하위 ol.Map 객체의 생성자 옵션
 * @version 0.01
 * @author yijun.so
 * @date 2017. 07.26
 */
gb.Map = function(obj) {

	var that = this;
	var options = obj ? obj : {};

	this.view = options.view instanceof ol.View ? options.view : new ol.View({});

	this.upperDiv = $("<div>");
	this.lowerDiv = $("<div>");

	if (typeof options.target === "string") {
		this.bind = $("#" + options.target).append(this.upperDiv).append(this.lowerDiv);
	} else if ($(options.target).is("div")) {
		this.bind = $(options.target).append(this.upperDiv).append(this.lowerDiv);
	}

	$(this.upperDiv).css({
		"top" : 0
	});
	$(this.lowerDiv).css({
		"top" : $(this.upperDiv).outerHeight() !== 0 ? "-" + $(this.upperDiv).outerHeight() + "px" : 0,
		"position" : "relative"
	});

	this.upperMap = new ol.Map(
			{
				controls : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("controls") ? options.upperMap.controls
						: undefined : undefined,
				pixelRatio : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("pixelRatio") ? options.upperMap.pixelRatio
						: undefined
						: undefined,
				interactions : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("interactions") ? options.upperMap.interactions
						: undefined
						: undefined,
				keyboardEventTarget : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("keyboardEventTarget") ? options.upperMap.keyboardEventTarget
						: undefined
						: undefined,
				layers : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("layers") ? options.upperMap.layers
						: undefined : undefined,
				maxTilesLoading : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("maxTilesLoading") ? options.upperMap.maxTilesLoading
						: undefined
						: undefined,
				loadTilesWhileAnimating : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("loadTilesWhileAnimating") ? options.upperMap.loadTilesWhileAnimating
						: undefined
						: undefined,
				loadTilesWhileInteracting : options.hasOwnProperty("upperMap") ? options.upperMap
						.hasOwnProperty("loadTilesWhileInteracting") ? options.upperMap.loadTilesWhileInteracting : undefined : undefined,
				moveTolerance : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("moveTolerance") ? options.upperMap.moveTolerance
						: undefined
						: undefined,
				overlays : options.hasOwnProperty("upperMap") ? options.upperMap.hasOwnProperty("overlays") ? options.upperMap.overlays
						: undefined : undefined,
				target : this.upperDiv[0],
				view : this.view
			});
	this.lowerMap = new ol.Map(
			{
				controls : options.hasOwnProperty("upperMap") ? options.lowerMap.hasOwnProperty("controls") ? options.lowerMap.controls
						: undefined : undefined,
				pixelRatio : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("pixelRatio") ? options.lowerMap.pixelRatio
						: undefined
						: undefined,
				interactions : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("interactions") ? options.lowerMap.interactions
						: undefined
						: undefined,
				keyboardEventTarget : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("keyboardEventTarget") ? options.lowerMap.keyboardEventTarget
						: undefined
						: undefined,
				layers : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("layers") ? options.lowerMap.layers
						: undefined : undefined,
				maxTilesLoading : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("maxTilesLoading") ? options.lowerMap.maxTilesLoading
						: undefined
						: undefined,
				loadTilesWhileAnimating : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("loadTilesWhileAnimating") ? options.lowerMap.loadTilesWhileAnimating
						: undefined
						: undefined,
				loadTilesWhileInteracting : options.hasOwnProperty("lowerMap") ? options.lowerMap
						.hasOwnProperty("loadTilesWhileInteracting") ? options.lowerMap.loadTilesWhileInteracting : undefined : undefined,
				moveTolerance : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("moveTolerance") ? options.lowerMap.moveTolerance
						: undefined
						: undefined,
				overlays : options.hasOwnProperty("lowerMap") ? options.lowerMap.hasOwnProperty("overlays") ? options.lowerMap.overlays
						: undefined : undefined,
				target : this.lowerDiv[0],
				view : this.view
			});

	$(this.lowerDiv).find(".ol-viewport").css("z-index", 1);
	$(this.upperDiv).find(".ol-viewport").css("z-index", 2);
};
/**
 * 상위 영역 ol.Map 객체를 반환한다.
 * 
 * @method gb.Map#getUpperMap
 * @function
 * @return {ol.Map} 상위 영역 ol.Map 객체
 */
gb.Map.prototype.getUpperMap = function() {
	return this.upperMap;
};

/**
 * 하위 영역 ol.Map 객체를 반환한다.
 * 
 * @method gb.Map#getLowerMap
 * @function
 * @return {ol.Map} 하위 영역 ol.Map 객체
 */
gb.Map.prototype.getLowerMap = function() {
	return this.lowerMap;
};

/**
 * 상위 영역 Element를 반환한다.
 * 
 * @method gb.Map#getUpperDiv
 * @function
 * @return {Element} 상위 영역 Element
 */
gb.Map.prototype.getUpperDiv = function() {
	return this.upperDiv;
};

/**
 * 하위 영역 Element를 반환한다.
 * 
 * @method gb.Map#getLowerDiv
 * @function
 * @return {Element} 하위 영역 Element
 */
gb.Map.prototype.getLowerDiv = function() {
	return this.lowerDiv;
};

/**
 * 지도 영역의 크기를 설정한다.
 * 
 * @method gb.Map#setSize
 * @function
 * @param {Number}
 *            width - 지도의 너비
 * @param {Number}
 *            height - 지도의 높이
 */
gb.Map.prototype.setSize = function(width, height) {
	$(this.bind).css({
		"width" : width + "px",
		"height" : height + "px"
	});
	$(this.lowerDiv).css({
		"width" : width + "px",
		"height" : height + "px"
	});
	$(this.upperDiv).css({
		"width" : width + "px",
		"height" : height + "px"
	});
	this.upperMap.updateSize();
	this.lowerMap.updateSize();

	$(this.upperDiv).css({
		"top" : 0
	});
	$(this.lowerDiv).css({
		"top" : $(this.upperDiv).outerHeight() !== 0 ? "-" + $(this.upperDiv).outerHeight() + "px" : 0,
		"position" : "relative"
	});

};