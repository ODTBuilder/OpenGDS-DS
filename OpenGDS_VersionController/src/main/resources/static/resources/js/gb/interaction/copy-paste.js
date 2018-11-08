var gb;
if (!gb)
	gb = {};
if (!gb.interaction.Copypaste)
	gb.interaction.Copypaste = {};

/**
 * Copy, Paste 기능 Class. Shift+C 또는 Shift+Y 키이벤트를 통해 Copy, Paste 기능을 실행할 수 있다.
 * 
 * @author hochul.kim
 * @date 2018. 07. 19
 * @version 0.01
 * @class
 * @constructor
 * @param {Object} obj - gb.interaction.Copypaste 생성 기본 옵션
 * @extends {ol.interaction.Interaction}
 */
gb.interaction.Copypaste = function(obj) {
	
	ol.interaction.Interaction.call(this, {
		handleEvent: gb.interaction.Copypaste.prototype.handleEvent
	});
	
	var that = this;
	
	var options = obj || {};
	
	/**
	 * 선택된 feature들의 집합
	 * @type {ol.Collection.<ol.Feature>}
	 * @private
	 */
	this.features_ = options.features;
	
	/**
	 * 선택된 Vector Source들의 집합
	 * @type {ol.Collection.<ol.source.Vector>}
	 * @private
	 */
	this.sources_ = options.sources;
	
	/**
	 * feature 편집 이력 관리
	 * @type {ol.Collection.<ol.source.Vector>}
	 * @private
	 */
	this.record_ = options.featureRecord;
	
	/**
	 * 선택된 features
	 * @type {ol.Collection}
	 * @private
	 */
	this.clipboard_ = new ol.Collection();
	
	this.setActive(true);
	
	// Shift+C, Shift+V Key Event 생성
	$(window).keypress(function(e){
		if(!that.getActive()){
			return;
		}
		
		if((e.keyCode === 67 || e.which === 67) && e.shiftKey){
			// Shift+C
			that.clip();
			console.log("clip");
		} else if((e.keyCode === 86 || e.which === 86) && e.shiftKey){
			// Shift+V
			that.paste();
			console.log("paste");
		}
	});
}
ol.inherits(gb.interaction.Copypaste, ol.interaction.Interaction);

gb.interaction.Copypaste.prototype.handleEvent = function(evt) {
	return true;
}

/**
 * copy. 현재 선택된 feature 저장
 * @return {ol.Collection}
 */
gb.interaction.Copypaste.prototype.clip = function(){
	var that = this;
	this.clipboard_.clear();
	this.features_.forEach(function(feature){
		that.clipboard_.push(feature);
	});
	return this.clipboard_;
}

/**
 * paste. 현재 저장된 feature들을 복사하여 선택된 layer에 추가
 * @param {ol.source.Vector} source - 붙여넣기를 수행할 vector source
 */
gb.interaction.Copypaste.prototype.paste = function(){
	var vectorSource = this.sources_.item(0);
	var arr = this.clipboard_.getArray();
	var feature;
	for(let i in arr){
		if(arr[i] instanceof ol.Feature &&
				vectorSource instanceof ol.source.Vector){
			if(this.record_ instanceof gb.edit.FeatureRecord){
				feature = arr[i].clone();
				feature.setId(gb.interaction.Copypaste.createFeatureId(vectorSource));
				this.record_.create(vectorSource.get("git").tempLayer, feature);
				vectorSource.addFeature(feature);
			}
		}
	}
}

/**
 * @param {ol.source.Vector} source - Feature ID를 생성할 vector source
 * @return {String}
 */
gb.interaction.Copypaste.createFeatureId = function(source){
	var vectorSource = source;
	var features = vectorSource.getFeatures();
	var count = 0;
	var newId = source.get("git") ? source.get("git").layerID + ".new" :
		source.ol_uid + ".new";
	var ids = [];
	
	features.forEach(function(feature){
		ids.push(feature.getId());
	});
	
	while(ids.indexOf(newId + count) !== -1){
		count++;
	}
	
	newId += count;
	return newId;
}