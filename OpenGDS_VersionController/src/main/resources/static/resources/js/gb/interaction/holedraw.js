var gb;
if (!gb)
	gb = {};
if (!gb.interaction)
	gb.interaction = {};

(function($){
	/**
	 * Hole Polygon 그리기 기능
	 * @author hochul.kim
	 * @date 2018. 08. 06
	 * @version 0.01
	 * @class gb.interaction.HoleDraw
	 * @constructor
	 * @param {Object} opt_options - gb.interaction.HoleDraw 생성 옵션
	 */
	gb.interaction.HoleDraw = function(opt_options) {
		var that = this;
		var options = opt_options ? opt_options : {};
		
		this.selected = options.selected;
		
		/**
		 * 임시 vector source
		 * @type {ol.source.Vector}
		 * @private
		 */
		this.source_ = new ol.source.Vector();
		
		ol.interaction.Draw.call(this, {
			type: "Polygon",
			source: this.source_
		});
		
		this.on("drawstart", function(e){
			var feature = e.feature;
			var ringAdded = false;
			
			if(!that.checkSelected()){
				that.setActive(false);
				return;
			}
			feature.on("change", function(e){
				var drawCoords = feature.getGeometry().getCoordinates(false)[0];
				if(drawCoords.length > 2){
					if(ringAdded === false){
						var testCoords = feature.getGeometry().getCoordinates(false)[0];
						var testCoordsLength = testCoords.length;
						if(testCoords.length === 3 && 
								testCoords[testCoordsLength - 1][0] === testCoords[testCoordsLength - 2][0] && 
								testCoords[testCoordsLength - 1][1] === testCoords[testCoordsLength - 2][1]){
							console.log("do nothing!!");
							return;
						} else {
							that.selected.getArray()[0].getGeometry().appendLinearRing(
								new ol.geom.LinearRing(
									pushFirstElement(feature.getGeometry().getCoordinates()[0])
								)
							);
							ringAdded = true;
						}
					} else {
						var coordElemntLength = that.selected.getArray()[0].getGeometry().getCoordinates().length;
						var coordsExt = that.selected.getArray()[0].getGeometry().getCoordinates()[0];
						var coordsInter = [];
						if (coordElemntLength > 2) {
							for (var i = 1; i < coordElemntLength - 1; i++) {
								coordsInter.push(that.selected.getArray()[0].getGeometry().getCoordinates()[i]);
							}
						}
						var setCoords = [];
	
						if (coordsInter.length > 0) {
							setCoords.push(coordsExt);
							for (var z = 0; z < coordsInter.length; z++) {
								setCoords.push(coordsInter[z]);
							}
	
							setCoords.push(pushFirstElement(feature.getGeometry().getCoordinates(false)[0]));
						} else {
							setCoords = [coordsExt, pushFirstElement(feature.getGeometry().getCoordinates(false)[0])];
						}
	
						that.selected.getArray()[0].getGeometry().setCoordinates(setCoords);
					}
				}
			});
		});
		
		this.on("drawend", function(e){
			console.log(e);
		});
	};
	ol.inherits(gb.interaction.HoleDraw, ol.interaction.Draw);

	gb.interaction.HoleDraw.prototype.checkSelected = function(){
		if(!(this.selected instanceof ol.Collection)){
			alert("There is no object of ol.interaction.Select");
			return false;
		} else {
			if(this.selected.getArray().length !== 1){
				alert("You must select only one feature");
				return false;
			} else {
				if(this.selected.getArray()[0].getGeometry().getType() !== "Polygon"){
					alert("Only Polygon geometry selections.");
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	gb.interaction.HoleDraw.prototype.setSelectFeatures = function(sel){
		if(sel instanceof ol.Collection){
			this.selected = sel;
			return true;
		} else {
			return false;
		}
	}
	function pushFirstElement(arr){
		var retArray = new Array()
		for (var i = 0; i < arr.length; i++) {
			retArray.push(arr[i])
		}
		retArray.push(arr[0])
		return retArray;
	}
}(jQuery));