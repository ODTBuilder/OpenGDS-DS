var gb;
if (!gb)
	gb = {};
if (!gb.interaction)
	gb.interaction = {};

/**
 * 길이, 면적을 나타내는 element overlay를 생성한다. 길이, 면적을 측정하기위해 그려지는 feature들은 ol.Map 상에 임시적으로 그려지는 vector layer에 속해있다.
 * @author hochul.kim
 * @date 2018. 06. 25
 * @version 0.01
 * @class gb.interaction.MeasureTip
 * @constructor
 * @param {Object} opt_options - gb.interaction.MeasureTip 생성 옵션
 */
gb.interaction.MeasureTip = function(opt_options) {
	
	var options = opt_options ? opt_options : {};

	/**
	 * 길이, 면적 측정 기능을 사용할 ol.Map 객체
	 * @type {ol.Map}
	 * @private
	 */
	this.map = options.map;
	
	/**
	 * 길이, 면적 측정 피쳐 생성을 위한 임시 vector source
	 * @type {ol.source.Vector}
	 * @private
	 */
	this.source_ = new ol.source.Vector();
	
	/**
	 * Snap Interaction
	 * @type {ol.interaction.Snap}
	 * @private
	 */
	this.snapSource =  options.snapSource || undefined;
	
	/**
	 * Snap Interaction
	 * @type {ol.interaction.Snap}
	 * @private
	 */
	this.snapInteraction = undefined;
	
	/**
	 * 길이, 면적 측정 피쳐 생성을 위한 임시 vector layer
	 * @type {ol.layer.Vector}
	 * @private
	 */
	this.vector_ = new ol.layer.Vector({
		source : this.source_,
		style: new ol.style.Style({
			fill: new ol.style.Fill({
				color: 'rgba(255, 255, 255, 0.2)'
			}),
			stroke: new ol.style.Stroke({
				color: '#ffcc33',
				width: 2
			}),
			image: new ol.style.Circle({
				radius: 7,
				fill: new ol.style.Fill({
					color: '#ffcc33'
				})
			})
		})
	});
	this.vector_.setMap(this.map);
	
	ol.interaction.Draw.call(this, {
		type: options.type,
		source: this.source_
	});
	
	/**
	 * listener
	 * 
	 * @type {Array.<ol.events.Event>}
	 * @private
	 */
	this.listener_ = [];

	/**
	 * The measure tooltip element.
	 * 
	 * @type {Overlay<Element>}
	 * @private
	 */
	this.measureTipElement_ = [];

	/**
	 * Overlay to show the measurement.
	 * 
	 * @type {Array.<ol.Overlay>}
	 * @private
	 */
	this.measureTipOverlay_ = [];

	/**
	 * measure 수치를 map에 나타내어줄 element와 overlay를 생성한다 두 개를 객체형태로 저장하여 반환한다
	 * 
	 * @type {Function}
	 * @return {Object}
	 * @private
	 */
	this.createMeasureTip_ = function() {
		var map = this.getMap();
		var measureTipElement;
		var measureTipOverlay;

		measureTipElement = document.createElement('div');
		measureTipElement.className = 'tip tip-measure';
		
		measureTipOverlay = new ol.Overlay({
			element : measureTipElement,
			offset : [ 0, -15 ],
			positioning : 'bottom-center'
		});
		map.addOverlay(measureTipOverlay);

		this.measureTipElement_.push(measureTipElement);
		this.measureTipOverlay_.push(measureTipOverlay);

		return {
			element : measureTipElement,
			overlay : measureTipOverlay
		};
	};
	/**
	 * 인자값으로 <ol.Feature>가 주어진다면 해당 feature에 관련된 element와 overlay만 삭제하고 인자값이
	 * 주어지지 않는다면 this.measureTipElement_와 this.measureTipOverlay_ 멤버변수에 저장되어 있는
	 * 모든 객체 또는 요소가 삭제된다.
	 * 
	 * @type {Function}
	 * @private
	 */
	this.removeMeasureTip_ = function(feature) {
		var element;
		var overlay;
		var length = this.measureTipElement_.length;

		if (feature) {
			element = this.measureTipElement_;
			overlay = this.measureTipOverlay_;

			for (var i = 0; i < length; ++i) {
				if (element[i].id === feature.getId()) {
					element[i].remove();
					element.splice(i, 1);
					break;
				}
			}

			for (var i = 0; i < length; ++i) {
				if (overlay[i].get('id') === feature.getId()) {
					overlay[i].getMap().removeOverlay(overlay[i]);
					overlay.splice(i, 1);
					break;
				}
			}
		} else {
			for (var i = 0; i < length; ++i) {
				element = this.measureTipElement_.pop();
				overlay = this.measureTipOverlay_.pop();

				element.remove();
				overlay.getMap().removeOverlay(overlay);
			}
		}
	};
	
	/**
	 * 인자값으로 주어진 폴리곤 객체에 대한 면적을 계산하여 String 형식으로 반환한다
	 *
	 * @type {Function}
	 * @return {String}
	 * @private
	 */
	this.formatArea_ = function(polygon) {
		var area = polygon.getArea();
		var output;
		if (area > 10000) {
			output = (Math.round(area/1000000*100)/100) + ' ' + 'km<sup>2</sup>';
		} else {
			output = (Math.round(area*100)/100) + ' ' + 'm<sup>2</sup>';
		}
		return output;
	};

	/**
	 * 인자값으로 주어진 라인스트링 객체에 대한 길이를 계산하여 String 형식으로 반환한다 MultiLineString 객체일때에는
	 * getLength() 함수가 적용되지 않으므로 우선 LineString객체들을 추출해낸 다음 각 LineString객체들의 길이를
	 * 더하여 반환한다
	 * 
	 * @type {Function}
	 * @return {String}
	 * @private
	 */
	this.formatLength_ = function(line) {
		var length = 0;
		var output;
		if (line instanceof ol.geom.MultiLineString) {
			var lines = line.getLineStrings();
			for (var i = 0; i < lines.length; ++i) {
				length += Math.round(lines[i].getLength()*100)/100;
			}
		} else if (line instanceof ol.geom.LineString) {
			length = Math.round(line.getLength() * 100) / 100;
		} else {
			console.error('not support type');
			return;
		}

		if (length > 100) {
			output = (Math.round(length/1000*100)/100) + ' ' + 'km';
		} else {
			output = (Math.round(length*100)/100) + ' ' + 'm';
		}
		return output;
	};
	
	var that = this;
	var startEvent = this.on("drawstart", function(evt){
		var feature = evt.feature;
		var measureTip = that.createMeasureTip_();
		var listener = feature.getGeometry().on('change', function(evt) {
			var geom = evt.target;
			var output, tipCoord;

			if (geom instanceof ol.geom.Polygon) {
				output = that.formatArea_(geom);
				tipCoord = geom.getInteriorPoint().getCoordinates();
			} else if (geom instanceof ol.geom.LineString) {
				output = that.formatLength_(geom);
				tipCoord = geom.getLastCoordinate();
			}
			measureTip.element.innerHTML = output;
			measureTip.element.style.position = "relative";
			measureTip.element.style.background = "rgba(0, 0, 0, 0.5)";
			measureTip.element.style.borderRadius = "4px";
			measureTip.element.style.color = "white";
			measureTip.element.style.padding = "4px 8px";
			measureTip.element.style.opacity = "1";
			measureTip.element.style.whiteSpace = "nowrap";
			measureTip.element.style.fontWeight = "bold";
			measureTip.overlay.setPosition(tipCoord);
		});
		that.listener_.push(listener);
		that.vector_.setMap(that.getMap());
	});
	this.listener_.push(startEvent);
	
	var endEvent = this.on("drawend", function(evt){
		// 배열의 맨마지막에 있는 (가장 최근에 생성되어진) element와 overlay를 가져온다
		var element = that.measureTipElement_[that.measureTipElement_.length - 1];
		var overlay = that.measureTipOverlay_[that.measureTipOverlay_.length - 1];
		var geom = evt.feature.getGeometry();

		if (geom instanceof ol.geom.MultiPolygon) {
			overlay.setPosition(geom.getInteriorPoints().getCoordinates()[0]);
		} else if (geom instanceof ol.geom.Polygon) {
			overlay.setPosition(geom.getInteriorPoint().getCoordinates());
		}

		// drawing이 끝나면 element를 고정시킨다
		element.className = 'tip tip-static';
		element.style.backgroundColor = "#ffcc33";
		element.style.color = "black";
		element.style.border = "1px solid white";
		overlay.setOffset([ 0, -7 ]);
	});
	this.listener_.push(endEvent);
	
	var interactions = this.map.getInteractions();
	var listen = interactions.on("remove", function(evt){
		if (evt.element instanceof gb.interaction.MeasureTip) {
			evt.element.removeMeasureTip_();
			that.source_.clear();
			//ol.Observable.unByKey(evt.element.listener_);
			//ol.Observable.unByKey(listen);
		}
	});
	
	this.on("change:active", function(evt){
		if(evt.oldValue){
			evt.target.removeMeasureTip_();
			that.source_.clear();
			if(!!that.snapInteraction){
				that.map.removeInteraction(that.snapInteraction)
			}
		} else {
			if(!!that.snapSource){
				that.map.addInteraction(new ol.interaction.Snap({
					source : that.snapSource
				}));
			}
		}
	});
};
ol.inherits(gb.interaction.MeasureTip, ol.interaction.Draw);

ol.interaction.Draw.prototype.selectedType = function(){
	return this.type_;
}