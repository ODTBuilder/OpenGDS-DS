var gb;
if (!gb)
	gb = {};
if (!gb.layer)
	gb.layer = {};

(function($){
	
	/**
	 * Add Image Layer
	 * 
	 * @author hochul.kim
	 * @date 2018. 10. 26
	 * @version 0.01
	 */
	gb.layer.ImageLayer = function(obj) {
		var options = obj;
		
		/**
		 * ol.Map 객체
		 * @type {ol.Map}
		 * @private
		 */
		this.map = options.map || undefined;
		
		/**
		 * Image Width
		 * @type {string}
		 * @private
		 */
		this.imageWidth = options.width || undefined;
		
		/**
		 * Image Height
		 * @type {string}
		 * @private
		 */
		this.imageHeight = options.height || undefined;
		
		/**
		 * Image file url
		 * @type {string}
		 * @private
		 */
		this.imageURL = options.url || undefined;
		
		/**
		 * Image Title
		 * 
		 * @type {String}
		 * @private
		 */
		var title = options.title || "New Image";
		
		/**
		 * Image Layer 수정을 위한 메뉴바 생성 위치
		 * 
		 * @type {DOM}
		 * @private
		 */
		this.baseDiv = options.baseDiv ? $(options.baseDiv) : $(".bind > div:last-child");
		
		/**
		 * Image Layer
		 * 
		 * @type {ol.layer.Image}
		 * @private
		 */
		this.imageLayer = undefined;
		
		/**
		 * pointer interaction
		 * image layer scale
		 * 
		 * @type {gb.layer.Pointer}
		 * @private
		 */
		this.scaleInteraction = undefined;
		
		/**
		 * Translate Interaction
		 * 
		 * @type {ol.interaction.Translate}
		 * @private
		 */
		this.translateInteraction = undefined;
		
		/**
		 * listener
		 * 
		 * @type {Array.<ol.events.Event>}
		 * @private
		 */
		this.listener_ = [];
		
		/**
		 * 길이, 면적 측정 피쳐 생성을 위한 임시 vector source
		 * @type {ol.source.Vector}
		 * @private
		 */
		var source = new ol.source.Vector({wrapX: false});
		
		/**
		 * 길이, 면적 측정 피쳐 생성을 위한 임시 vector layer
		 * @type {ol.layer.Vector}
		 * @private
		 */
		this.vector = new ol.layer.Vector({
			source : source,
			style: new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(255, 255, 255, 0)'
				}),
				stroke: new ol.style.Stroke({
					color: 'rgba(255, 255, 255, 0)',
					width: 0
				})
			})
		});
		
		this.vector.set("git", {
			"editable" : true,
			"geometry" : "Polygon",
			"validation" : false
		});
		
		this.vector.setMap(this.map);
		
		ol.interaction.Draw.call(this, {
			type: "Circle",
			geometryFunction: ol.interaction.Draw.createBox(),
			source: source
		});
		
		var that = this;
		var startEvent = this.on("drawstart", function(evt){
			that.vector.setMap(that.getMap());
		});
		this.listener_.push(startEvent);
		
		var endEvent = this.on("drawend", function(evt){
			var feature = evt.feature;
			var geom = feature.getGeometry();
			var extent = geom.getExtent();
			var x = extent[0] + (extent[2] - extent[0]) / 2;
			var y = extent[1] + (extent[3] - extent[1]) / 2;
			
			centerCoord = [x, y];
			
			var projection = new ol.proj.Projection({
				code: 'xkcd-image',
				units: 'pixels',
				extent: extent
			});
			
			this.imageLayer = new ol.layer.Image({
				source: new ol.source.ImageStatic({
					url: that.imageURL,
					imageSize: [that.imageWidth, that.imageHeight],
					projection: projection,
					imageExtent: extent
				}),
				opacity: 1
			});
			
			this.imageLayer.set("name", title);
			this.imageLayer.set("vectorLayer", that.vector);
			this.imageLayer.set("select", this);
			that.map.addLayer(this.imageLayer);
			
			that.map.removeInteraction(that);
		});
		this.listener_.push(endEvent);
		
		this.map.addInteraction(this);
	}
	
	ol.inherits(gb.layer.ImageLayer, ol.interaction.Draw);
	ol.interaction.Draw.prototype.selectedType = function(){
		return this.type_;
	}
	
	gb.layer.ImageLayer.prototype.createMenuBar = function(target){
		if($.find("#imageLayerMenu").length !== 0){
			$("#imageLayerMenu").remove();
		}
		
		var that = this;
		var icon1 = $("<i class='fas fa-vector-square fa-lg'>");
		var item1 = $("<div>").append(icon1).css({
			"text-align": "center",
			"padding": "5px 0",
		}).hover(
			function(){
				$(this).css({
					"cursor": "pointer"
				});
			}, function(){
				$(this).css({
					"cursor": "default"
				});
			}
		).click(function(){
			that.activeEdit("scale");
		});
		
		var icon2 = $("<i class='fas fa-arrows-alt fa-lg'>");
		var item2 = $("<div>").append(icon2).css({
			"text-align": "center",
			"padding": "5px 0",
		}).hover(
			function(){
				$(this).css({
					"cursor": "pointer"
				});
			}, function(){
				$(this).css({
					"cursor": "default"
				});
			}
		).click(function(){
			that.activeEdit("move");
		});
		
		var menu = $("<div id='imageLayerMenu'>").css({
			"position": "absolute",
			"background-color": "#27292a",
			"color": "#fff",
			"text-shadow": "none",
			"top": "5em",
			"left": "0px",
			"width": "40px",
			"height": "auto",
			"white-space": "nowrap",
			"overflow": "hidden",
			"-webkit-transition": "0.3s width ease, 0.5s transform ease",
			"-moz-transition": "0.3s width ease, 0.5s transform ease",
			"-o-transition": "0.3s width ease, 0.5s transform ease",
			"-ms-transition": "0.3s width ease, 0.5s transform ease",
			"transition": "0.3s width ease, 0.5s transform ease",
			"z-index": "2"
		}).append(item1).append(item2);
		
		target.append(menu);
	}
	
	gb.layer.ImageLayer.prototype.removeMenuBar = function(){
		var features = this.vector.getSource().getFeatures();
		for(let i = 0; i < features.length; i++){
			features[i].setStyle([ new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(255, 255, 255, 0)'
				}),
				stroke : new ol.style.Stroke({
					color : 'rgba(255,0,0,0)',
					width : 0
				})
			})]);
		}
		if($.find("#imageLayerMenu").length !== 0){
			$("#imageLayerMenu").remove();
		}
		this.deActiveEdit();
	}
	
	gb.layer.ImageLayer.prototype.activeEdit = function(str){
		var editName = str;
		
		this.deActiveEdit();
		
		if(editName === "move"){
			this.move();
		} else if(editName === "scale"){
			this.scale();
		}
	}
	
	gb.layer.ImageLayer.prototype.deActiveEdit = function(){
		var that = this;
		this.map.getInteractions().forEach(function(interaction) {
			if (interaction === that.scaleInteraction || interaction === that.translateInteraction ) {
				that.map.removeInteraction(interaction);
			}
		});
	}
	
	gb.layer.ImageLayer.prototype.move = function(){
		var features = this.vector.getSource().getFeatures();
		var collection = new ol.Collection();
		
		for(let i = 0; i < features.length; i++){
			features[i].setStyle([ new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(255, 255, 255, 0.2)'
				}),
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
			})]);
			
			collection.push(features[i]);
		}
		
		this.map.getInteractions().forEach(function(interaction) {
			if (interaction instanceof ol.interaction.Translate) {
				that.map.removeInteraction(interaction);
			}
		});
		
		var move = new ol.interaction.Translate({
			features : collection
		});
		
		this.translateInteraction = move;
		
		move.setProperties({"imageLayer":this.imageLayer}, true);
		
		move.on("translating", function(evt) {
			var feature = evt.features.item(0);
			var geom = feature.getGeometry();
			var center = geom.getFlatInteriorPoint();
			
			this.getProperties().imageLayer.getSource().refresh();
			this.getMap().renderSync();
		});
		
		this.map.addInteraction(move);
	}
	
	gb.layer.ImageLayer.prototype.scale = function(){
		var that = this;
		var features = this.vector.getSource().getFeatures();
		var collection = new ol.Collection();
		
		for(let i = 0; i < features.length; i++){
			features[i].setStyle([ new ol.style.Style({
				fill: new ol.style.Fill({
					color: 'rgba(255, 255, 255, 0.2)'
				}),
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
			})]);
			
			collection.push(features[i]);
		}
		
		this.map.getInteractions().forEach(function(interaction) {
			if (interaction instanceof gb.layer.Pointer) {
				that.map.removeInteraction(interaction);
			}
		});
		
		var scale = new gb.layer.Pointer({
			feature: features[0],
			imageLayer: this.imageLayer,
			map: this.map
		});
		
		this.scaleInteraction = scale;
		
		this.map.addInteraction(scale);
	}
	
	/*
	 * Pointer event type
	 */
	gb.layer.PointerEventType = {
		TRANSFORMSTART : 'transformstart',
		TRANSFORMING : 'transforming',
		TRANSFORMEND : 'transformend'
	};
	
	gb.layer.Pointer = function(opt_options){
		var options = opt_options ? opt_options : {};

		ol.interaction.Pointer.call(this, {
			handleDownEvent : gb.layer.Pointer.prototype.handleDownEvent,
			handleDragEvent : gb.layer.Pointer.prototype.handleDragEvent,
			handleMoveEvent : gb.layer.Pointer.prototype.handleMoveEvent,
			handleUpEvent : gb.layer.Pointer.prototype.handleUpEvent
		});
		
		/**
		 * ol.Map 객체
		 * 
		 * @type {ol.Map}
		 * @private
		 */
		this.map = options.map || undefined;
		
		/**
		 * ol.Map 객체
		 * 
		 * @type {ol.layer.Image}
		 * @private
		 */
		this.imageLayer = options.imageLayer || undefined;
		
		/**
		 * 현재 커서의 위치를 저장
		 * 
		 * @type {<Array>}
		 * @private
		 */
		this.cursorCoordinate_ = null;

		/**
		 * 이전 커서의 위치를 저장
		 * 
		 * @type {<Array>}
		 * @private
		 */
		this.prevCursor_ = null;

		/**
		 * 마우스 이벤트를 적용시킬 feature를 저장
		 * 
		 * @type {ol.Feature}
		 * @private
		 */
		this.feature_ = options.feature;
		
		/**
		 * rotate, scale 함수 parameter 값을 위한 feature의 중점좌표
		 * 
		 * @type {<Array>}
		 * @private
		 */
		this.flatInteriorPoint_ = null;
		
		var that = this;
		this.map.on('postcompose', function(evt) {
			that.map.getInteractions().forEach(function(interaction) {
				if (interaction instanceof gb.layer.Pointer) {
					if (interaction.getActive()) {
						interaction.drawMbr(evt);
					}
				}
			});
		});
	}
	ol.inherits(gb.layer.Pointer, ol.interaction.Pointer);
	
	/**
	 * Mouse down 이벤트
	 * 
	 * @return {Boolean} true 반환시 drag squence 시작
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.handleDownEvent = function(evt) {
		var map = evt.map;
		var element = evt.map.getTargetElement();
		var geometry = this.feature_.getGeometry();
		var extent = geometry.getExtent();
		var x = extent[0] + (extent[2] - extent[0]) / 2;
		var y = extent[1] + (extent[3] - extent[1]) / 2;
		
		if(element.style.cursor === "nw-resize" || element.style.cursor === "ne-resize"){
			this.flatInteriorPoint_ = [ x, y ];
			centerCoord = [ x, y ];
			return true;
		}
		this.dispatchEvent(new gb.layer.Pointer.Event(gb.layer.PointerEventType.TRANSFORMSTART, this.feature_, evt));
		return false;
	};

	/**
	 * Mouse Drag 이벤트
	 * 
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.handleDragEvent = function(evt) {
		var cursorPoint = evt.coordinate;
		var magni = this.scaleAlgorithm_(this.feature_, cursorPoint);
		
		if (magni[0] > magni[1]) {
			this.feature_.getGeometry().scale(magni[0], magni[0], this.flatInteriorPoint_);
		} else {
			this.feature_.getGeometry().scale(magni[1], magni[1], this.flatInteriorPoint_);
		}
		
		this.imageLayer.getSource().refresh();
		this.getMap().renderSync();
		
		this.dispatchEvent(new gb.layer.Pointer.Event(gb.layer.PointerEventType.TRANSFORMING, this.feature_, evt));
	};

	/**
	 * Mouse Move 이벤트. drag 실행 중에는 실행되지 않는다.
	 * 
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.handleMoveEvent = function(evt) {
		if (!evt.dragging) {
			var map = evt.map
			var element = evt.map.getTargetElement();
			var task = this.selectTask_(map, this.feature_, evt.pixel);
			
			if (!!task) {
				switch (task) {
				case 'scaleW':
					element.style.cursor = 'nw-resize';
					break;
				case 'scaleE':
					element.style.cursor = 'ne-resize';
					break;
				default:
					element.style.cursor = 'pointer';
				}
			} else {
				if (element.style.cursor !== '') {
					element.style.cursor = '';
				}
			}
			this.cursorCoordinate_ = evt.coordinate;
		}
	};

	/**
	 * Mouse Up 이벤트
	 * 
	 * @return {Boolean} false 반환시 drag squence 종료
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.handleUpEvent = function(evt) {
		var element = evt.map.getTargetElement();
		
		this.prevCursor_ = null;
		this.flatInteriorPoint = null;
		element.style.cursor = '';
		this.dispatchEvent(new gb.layer.Pointer.Event(gb.layer.PointerEventType.TRANSFORMEND, this.feature_, evt));
		return false;
	};
	
	/**
	 * @classdesc Events emitted by {@link gb.interaction.MultiTransform} instances
	 *            are instances of this type.
	 * 
	 * @constructor
	 * @extends {ol.events.Event}
	 * @param {ol.interaction.MultiTransformEventType}
	 *            type Type.
	 * @param {ol.Feature}
	 *            feature The feature modified.
	 * @param {ol.MapBrowserPointerEvent}
	 *            mapBrowserPointerEvent Associated
	 *            {@link ol.MapBrowserPointerEvent}.
	 */
	gb.layer.Pointer.Event = function(type, feature, mapBrowserPointerEvent) {

		ol.events.Event.call(this, type);

		/**
		 * The features being modified.
		 * 
		 * @type {ol.Feature}
		 * @api
		 */
		this.feature = feature;

		/**
		 * Associated {@link ol.MapBrowserEvent}.
		 * 
		 * @type {ol.MapBrowserEvent}
		 * @api
		 */
		this.mapBrowserEvent = mapBrowserPointerEvent;
	};
	ol.inherits(gb.layer.Pointer.Event, ol.events.Event);
	
	/**
	 * 이벤트 영역을 {ol.style.Style}객체로 그려낸다.
	 * 
	 * @param {ol.render.Event}
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.drawMbr = function(evt) {

		var map = this.getMap();

		if (this.feature_ !== undefined) {

			var mbr = null;
			var line = null;
			var circle = null;

			var triangle = [];
			var square = [];
			var point = [];

			var features = [];

			var strokes = {
				'line' : new ol.style.Stroke({
					color : 'rgba(152,152,152,0.6)',
					width : 3,
					lineDash : [ 1, 4 ]
				}),
				'default' : new ol.style.Stroke({
					color : 'rgba(152,152,152,0.8)',
					width : 1.5
				})
			};

			var fill = new ol.style.Fill({
				color : 'rgba(152,152,152,0.9)'
			});

			var styles = {
				'line' : new ol.style.Style({
					stroke : strokes['line'],
					image : new ol.style.Circle({
						radius : 10,
						stroke : strokes['line']
					})
				}),
				'circle' : new ol.style.Style({
					// stroke: strokes['circle'],
					image : new ol.style.Circle({
						radius : 8,
						stroke : strokes['default']
					})
				}),
				'square' : new ol.style.Style({
					image : new ol.style.RegularShape({
						stroke : strokes['default'],
						points : 4,
						radius : 8,
						angle : Math.PI / 4
					})
				}),
				'triangle' : new ol.style.Style({
					image : new ol.style.RegularShape({
						stroke : strokes['default'],
						points : 3,
						radius : 8
					})
				})
			};

			var extent = this.feature_.getGeometry().getExtent();
			var coorX = (extent[0] + extent[2]) / 2;

			var vectorContext = evt.vectorContext;

			mbr = new ol.geom.Polygon([ [ [ extent[0], extent[3] ], [ extent[0], extent[1] ], [ extent[2], extent[1] ],
					[ extent[2], extent[3] ], [ extent[0], extent[3] ] ] ]);

			square.push(new ol.geom.Point([ extent[0], extent[1] ]));
			square.push(new ol.geom.Point([ extent[0], extent[3] ]));
			square.push(new ol.geom.Point([ extent[2], extent[1] ]));
			square.push(new ol.geom.Point([ extent[2], extent[3] ]));

			for ( var i in square) {
				vectorContext.drawFeature(new ol.Feature({
					geometry : square[i],
					name : 'scale'
				}), styles['square']);
			}

			vectorContext.setStyle(styles['line']);
			vectorContext.drawGeometry(mbr);
		}
	};
	
	/**
	 * style로 그려진 버튼들의 영역을 설정하고 cursor가 그 위치에 있을때 해당버튼에 맞는 작업의 이름을
	 * String으로 반환한다. 커서가 버튼 영역에 놓여있지 않다면 null 값을 반환한다.
	 * 
	 * @param {ol.Map}
	 * @param {ol.Feature}
	 * @param {Array}
	 * @return {String}
	 * @this {gb.layer.Pointer}
	 */
	gb.layer.Pointer.prototype.selectTask_ = function(map, feature, cursor) {

		const AREA = 6;

		var extent = feature.getGeometry().getExtent();
		var scale = [];
		var task = null;

		scale.push(map.getPixelFromCoordinate([ extent[0], extent[1] ]));
		scale.push(map.getPixelFromCoordinate([ extent[0], extent[3] ]));
		scale.push(map.getPixelFromCoordinate([ extent[2], extent[3] ]));
		scale.push(map.getPixelFromCoordinate([ extent[2], extent[1] ]));

		for ( var i in scale) {
			if ((cursor[0] >= scale[i][0] - AREA && cursor[0] <= scale[i][0] + AREA)
					&& (cursor[1] >= scale[i][1] - AREA && cursor[1] <= scale[i][1] + AREA)) {
				(i % 2 === 0) ? task = 'scaleE' : task = 'scaleW';
			}
		}

		return task;
	};
	
	/**
	 * 피처 확대, 축소 알고리즘. 선택된 scale버튼의 이전 좌표값과 pointer를 drag함으로서 변경된 커서의 좌표, 두 좌표값사이
	 * 길이의 배율값을 구하여 그 배율의 절대값을 리턴한다.
	 * 
	 * @param {ol.Feature}
	 * @param {Array}
	 *            drag를 통해 변경된 커서의 위치
	 * @return {Array} 늘어난 x좌표, y좌표 배율의 절대값
	 * @this {gb.interaction.MultiTransform}
	 */
	gb.layer.Pointer.prototype.scaleAlgorithm_ = function(feature, currentCursorPoint) {
		var map = this.getMap();
		var extent = this.feature_.getGeometry().getExtent();
		var cursor = currentCursorPoint;
		var center = this.flatInteriorPoint_;
		var result = [];

		var cursorPixel = map.getPixelFromCoordinate(currentCursorPoint);
		var centerPixel = map.getPixelFromCoordinate(center);
		var subX = Math.abs(cursorPixel[0] - centerPixel[0]);
		var subY = Math.abs(cursorPixel[1] - centerPixel[1]);
		var magniX = 0;
		var magniY = 0;

		if (subX < 2 || subY < 2) {
			magniX = 1;
			magniY = 1;
		} else {
			magniX = (cursor[0] - center[0]) / (extent[0] - center[0]);
			magniY = (cursor[1] - center[1]) / (extent[1] - center[1]);
		}

		result.push(Math.abs(magniX), Math.abs(magniY));
		return result;
	};
}(jQuery));