var gb;
if (!gb)
	gb = {};
if (!gb.map)
	gb.map = {};

/**
 * 현재 ol.Map 위에서의 Mouse pointer 위치 좌표정보를 제공
 * 
 * @class gb.map.MousePosition
 * @memberof gb.map
 * @param {Object}
 *            obj - 생성자 옵션을 담은 객체
 * @param {String}
 *            obj.id - 레이어의 ID
 * @version 0.01
 * @author 김호철
 * @date 2018.07.30
 */
gb.map.MousePosition = function(obj){
	var options = obj || {};
	
	/**
	 * ol.Map 객체
	 * @type {ol.Map}
	 * @private
	 */
	this.map = options.map;
	
	/**
	 * projection
	 * @type {String}
	 * @private
	 */
	this.projection = options.projection || this.map.getView().getProjection().getCode();
	
	/**
	 * 좌표정보를 가시화할 jquery 인스턴스
	 * @type {$}
	 * @private
	 */
	this.element = options.element || $(".mouse-position");
	
	/**
	 * openlayers mouse position control
	 * @type {ol.control.MousePosition}
	 * @private
	 */
	this.mousePositionControl = new ol.control.MousePosition({
		projection: this.projection,
		className: "map-mouse-position",
		target: this.element[0],
		undefinedHTML: "&nbsp"
	});
	
	this.map.addControl(this.mousePositionControl);
}

gb.map.MousePosition.prototype.setProjection = function(proj){
	var projection;
	if(!proj){
		projection = this.map.getView().getProjection().getCode();
	} else {
		projection = proj;
	}
	this.mousePositionControl.setProjection(proj);
}