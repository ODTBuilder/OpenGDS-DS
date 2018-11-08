/**
 * 드로우 인터렉션에 그리는중에 타입을 바꾸는 함수 추가5
 */
ol.interaction.Draw.prototype.switchType_ = function() {
	this.type_ = this.selectedType();
	/**
	 * Drawing mode (derived from geometry type.
	 * 
	 * @type {ol.interaction.Draw.Mode_}
	 * @private
	 */
	this.mode_ = ol.interaction.Draw.getMode_(this.type_);
	/**
	 * The number of points that must be drawn before a polygon ring or line
	 * string can be finished. The default is 3 for polygon rings and 2 for line
	 * strings.
	 * 
	 * @type {number}
	 * @private
	 */
	if (this.mode_ === ol.interaction.Draw.Mode_.POLYGON) {
		this.minPoints_ = 3;
	} else {
		this.minPoints_ = 2;
	}

	var geometryFunction = this.geometryFunction_ || undefined;
	if (!geometryFunction) {
		if (this.type_ === ol.geom.GeometryType.CIRCLE) {
			/**
			 * @param {ol.Coordinate|Array.
			 *            <ol.Coordinate>|Array.<Array.<ol.Coordinate>>}
			 *            coordinates The coordinates.
			 * @param {ol.geom.SimpleGeometry=}
			 *            opt_geometry Optional geometry.
			 * @return {ol.geom.SimpleGeometry} A geometry.
			 */
			geometryFunction = function(coordinates, opt_geometry) {
				var circle = opt_geometry ? /** @type {ol.geom.Circle} */
				(opt_geometry) : new ol.geom.Circle([ NaN, NaN ]);
				var squaredLength = ol.coordinate.squaredDistance(coordinates[0], coordinates[1]);
				circle.setCenterAndRadius(coordinates[0], Math.sqrt(squaredLength));
				return circle;
			};
		} else {
			var Constructor;
			var mode = this.mode_;
			if (mode === ol.interaction.Draw.Mode_.POINT) {
				Constructor = ol.geom.Point;
			} else if (mode === ol.interaction.Draw.Mode_.LINE_STRING) {
				Constructor = ol.geom.LineString;
			} else if (mode === ol.interaction.Draw.Mode_.POLYGON) {
				Constructor = ol.geom.Polygon;
			}
			/**
			 * @param {ol.Coordinate|Array.
			 *            <ol.Coordinate>|Array.<Array.<ol.Coordinate>>}
			 *            coordinates The coordinates.
			 * @param {ol.geom.SimpleGeometry=}
			 *            opt_geometry Optional geometry.
			 * @return {ol.geom.SimpleGeometry} A geometry.
			 */
			geometryFunction = function(coordinates, opt_geometry) {
				var geometry = opt_geometry;
				if (geometry) {
					if (mode === ol.interaction.Draw.Mode_.POLYGON) {
						geometry.setCoordinates([ coordinates[0].concat([ coordinates[0][0] ]) ]);
					} else {
						geometry.setCoordinates(coordinates);
					}
				} else {
					geometry = new Constructor(coordinates);
				}
				return geometry;
			};
		}
	}

	/**
	 * @type {ol.DrawGeometryFunctionType}
	 * @private
	 */
	this.geometryFunction_ = geometryFunction;
	console.log(this.sketchCoords_);
	console.log(this.sketchLineCoords_);
}
/**
 * 드로우 인터렉션 핸들다운 이벤트 오버라이드
 * 
 * @param {ol.MapBrowserPointerEvent}
 *            event Event.
 * @return {boolean} Start drag sequence?
 * @this {ol.interaction.Draw}
 * @private
 */
ol.interaction.Draw.handleDownEvent_ = function(event) {
	this.switchType_();
	this.shouldHandle_ = !this.freehand_;

	if (this.freehand_) {
		this.downPx_ = event.pixel;
		if (!this.finishCoordinate_) {
			this.startDrawing_(event);
		}
		return true;
	} else if (this.condition_(event)) {
		this.downPx_ = event.pixel;
		return true;
	} else {
		return false;
	}
};

//=============================== hochul area =========================================
/**
 * ol.interaction.Draw.handleUpEvent_ 오버라이드
 * @param {ol.MapBrowserPointerEvent} event Event.
 * @return {boolean} Stop drag sequence?
 * @this {ol.interaction.Draw}
 * @private
 */
ol.interaction.Draw.handleUpEvent_ = function(event) {
	var pass = true;

	this.handlePointerMove_(event);

	var circleMode = this.mode_ === ol.interaction.Draw.Mode_.CIRCLE;

	if (this.shouldHandle_) {
		if (!this.finishCoordinate_) {
			this.startDrawing_(event);
			if (this.mode_ === ol.interaction.Draw.Mode_.POINT) {
				this.finishDrawing();
			} else {
				this.stackStart();
			}
		} else if (this.freehand_ || circleMode) {
			this.finishDrawing();
		} else if (this.atFinish_(event)) {
			if (this.finishCondition_(event)) {
				this.finishDrawing();
				this.stackEnd();
			}
		} else {
			this.addToDrawing_(event);
			this.stackPush_(event);
		}
		pass = false;
	} else if (this.freehand_) {
		this.finishCoordinate_ = null;
		this.abortDrawing_();
	}
	if (!pass && this.stopClick_) {
		event.stopPropagation();
	}
	return pass;
};

/**
 * undo 작업을 수행한다. 현재 작업 위치(ol.interaction.Draw.current_)가 1 감소한다
 */
ol.interaction.Draw.prototype.undo = function(){
	console.log("draw undo");
	
	var perform;
	
	if(this.current_ >= 0){
		this.removeLastPoint();
		this.current_--;
	} else {
		throw new Error("Already at oldest change");
	}
}

/**
 * redo 작업을 수행한다. 현재 작업 위치(ol.interaction.Draw.current_)가 1 증가한다
 */
ol.interaction.Draw.prototype.redo = function(){
	console.log("draw redo");
	
	if(!this.sketchFeature_){
		return;
	}
	
	var perform = this.stack_[this.current_ + 1];
	var geometry = this.sketchFeature_.getGeometry();
	var coordinates, sketchCoords_, finishCoord;
	
	if(perform){
		if (this.mode_ === ol.interaction.Draw.Mode_.LINE_STRING) {
			coordinates = this.sketchCoords_;
			finishCoord = coordinates.splice(-1, 1);
			coordinates.push(perform.redoCoord.slice());
			coordinates.push(finishCoord[0]);
			this.geometryFunction_(this.sketchCoords_, geometry);
			
			if (coordinates.length >= 2) {
				this.finishCoordinate_ = coordinates[coordinates.length - 2].slice();
			}
			
		} else if (this.mode_ === ol.interaction.Draw.Mode_.POLYGON) {
			coordinates = this.sketchCoords_[0];
			finishCoord = coordinates.splice(-1, 1);
			coordinates.push(perform.redoCoord.slice());
			coordinates.push(finishCoord[0]);
			sketchLineGeom = /** @type {ol.geom.LineString} */ (this.sketchLine_.getGeometry());
			sketchLineGeom.setCoordinates(coordinates);
			this.geometryFunction_(this.sketchCoords_, geometry);
		}
		
		if (coordinates.length === 0) {
			this.finishCoordinate_ = null;
		}
		
		this.updateSketchFeatures_();
		this.current_++;
	} else {
		throw new Error("Already at newest change");
	}
}

/**
 * undo, redo 기능 수행을 위한 정보를 가지고 있는 ol.interaction.Draw.history 객체를 저장
 * @type {Array.<Object>}
 * @private
 */
ol.interaction.Draw.prototype.stack_ = [];

/**
 * 멤버 변수 stack_의 index. undo 수행시 -1, redo 수행시 +1, push 수행시 +1
 * @type {Array.<Object>}
 * @private
 */
ol.interaction.Draw.prototype.current_ = -1;

ol.interaction.Draw.prototype.stackPush_ = function(event){
	var coord = event.coordinate.slice();
	var coordinates, unCoord;
	
	if (this.mode_ === ol.interaction.Draw.Mode_.LINE_STRING) {
		coordinates = this.sketchCoords_;
		unCoord = coordinates[coordinates.length - 3];
	} else if (this.mode_ === ol.interaction.Draw.Mode_.POLYGON) {
		coordinates = this.sketchCoords_[0];
		unCoord = coordinates[coordinates.length - 3];
	}
	this.current_++;
	this.stack_.splice(this.current_);
	this.stack_.push(new ol.interaction.Draw.history(unCoord, coord));
}

/**
 * draw 작업이 시작되었을때 key event를 생성한다
 */
ol.interaction.Draw.prototype.stackStart = function(){
	var that = this;
	$(window).bind("keypress.draw", function(e){
		if((e.keyCode === 26 || e.which === 26) && e.ctrlKey){
			that.undo();
		} else if((e.keyCode === 25 || e.which === 25) && e.ctrlKey){
			that.redo();
		}
	});
}

/**
 * draw 작업을 끝마쳤을때 수행할 함수들을 실행한다. key event를 해제한다
 */
ol.interaction.Draw.prototype.stackEnd = function(){
	$(window).unbind("keypress.draw");
	this.invalidateAll();
}

/**
 * stack(ol.interaction.Draw.stack_)에 저장되어 있는 모든 작업을 초기화한다
 */
ol.interaction.Draw.prototype.invalidateAll = function () {
	this.stack_ = [];
	this.current_ = -1;
}

/**
 * @classdesc
 * undo, redo에 대한 data를 담는 객체를 생성
 * stack에 저장되며 필요에 따라 호출되어 undo, redo 작업 수행에 필요한 data를 제공한다
 * @param {Array} undo - undo 작업을 수행할 좌표
 * @param {Array} redo - redo 작업을 수행할 좌표
 */
ol.interaction.Draw.history = function(undo, redo){
	this.undoCoord = undo;
	this.redoCoord = redo;
}