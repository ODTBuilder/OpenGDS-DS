/**
 * Layer Navigator
 * 
 * @author hochul.kim
 * @date 2018. 09. 18
 * @version 0.01
 */
var gb;
if (!gb)
	gb = {};
if (!gb.layer)
	gb.layer = {};

gb.layer.Navigator = function(obj) {
	var options = obj;
	
	this.map = options.map || false;
	if(!this.map){
		console.error("gb.layer.Navigator: ol.map is required");
		return null;
	}
	
	this.token = options.token || "";
	
	this.getWFSFeature = options.getWFSFeature || "geoserver/geoserverWFSGetFeature.ajax";
	
	this.featureList = undefined;
	
	this.count = 0;
	
	this.td2 = $("<div>").css({
		"display" : "inline-block"
	});
	
	this.tbody = $("<tbody>");
	
	this.naviWindow = $("<div>").css({
		"max-width" : "400px",
		"top" : "150px",
		"right" : "5px",
		"position" : "absolute",
		"z-Index" : "999",
	});
	
	this.createNavigator_();
}

gb.layer.Navigator.prototype.setFeatures = function(Layer){
	var layer = Layer;
	
	if(layer instanceof ol.layer.Tile){
		var git = layer.get("git");
		this.requestFeatureList(git.geoserver, git.workspace, layer.get("name"));
	} else if(layer instanceof ol.layer.Vector){
		this.featureList = layer.getSource().getFeatures();
		this.updateNavigator();
	} else {
		console.error("Not supported layer type");
		return;
	}
}

gb.layer.Navigator.prototype.requestFeatureList = function(serverName, workspace, layer){
	var that = this;
	var a = {
		serverName: serverName,
		workspace: workspace,
		typeName: layer,
		version: "1.0.0",
		outputformat: "application/json"
	};
	
	$.ajax({
		url: this.getWFSFeature + this.token,
		type : "GET",
		contentType : "application/json; charset=UTF-8",
		data: a,
		dataType: "JSON",
		success: function(data, textStatus, jqXHR) {
			that.featureList = new ol.format.GeoJSON().readFeatures(JSON.stringify(data));
			that.updateNavigator();
		},
		error: function(e) {
			var errorMsg = e? (e.status + ' ' + e.statusText) : "";
			console.log(errorMsg);
		},
	});
}

gb.layer.Navigator.prototype.updateNavigator = function(){
	var features = this.featureList;
	this.count = 0;
	this.showFeatureInfo(features[this.count]);
	this.open();
}

gb.layer.Navigator.prototype.createNavigator_ = function(){
	var that = this;
	var prevIcon = $("<span>").addClass("glyphicon").addClass("glyphicon-backward"),
		nextIcon = $("<span>").addClass("glyphicon").addClass("glyphicon-forward");
	
	var btnPrev = 
			$("<button>")
				.addClass("gb-navigator-prev")
				.addClass("btn").addClass("btn-default")
				.append(prevIcon), 
		btnNext = 
			$("<button>")
			.addClass("gb-navigator-next")
			.addClass("btn").addClass("btn-default").append(nextIcon);
	
	$(document).on("click", ".gb-navigator-prev", function() {
		that.prev();
	});
	
	$(document).on("click", ".gb-navigator-next", function() {
		that.next();
	});
	
	var td1 = $("<div>").css({
		"width" : "100px",
		"display" : "inline-block"
	}).append(btnPrev), td3 = $("<div>").css({
		"width" : "100px",
		"display" : "inline-block"
	}).append(btnNext);
	var tr1 = $("<div>").addClass("text-center").append(td1).append(this.td2).append(td3);
	var thead = $("<div>").css({
		"margin-bottom" : "10px"
	}).append(tr1);
	var xSpan = $("<span>").attr({
		"aria-hidden" : "true"
	}).append("&times;");
	var xBtn = $("<button>").click(function() {
		$(that.naviWindow).hide();
	}).attr({
		"data-dismiss" : "modal",
		"aria-label" : "Close"
	}).css({
		"display" : "inline-block",
		"float" : "right",
		"padding" : "0",
		"margin" : "0",
		"color" : "#ccc",
		"border" : "none",
		"background-color" : "transparent",
		"cursor" : "pointer",
		"outline" : "none",
		"color" : "#ccc"
	}).append(xSpan);

	var title = $("<span>").text("Feature Navigator");
	var tb = $("<table>").addClass("table").append(this.tbody);
	var pbd = $("<div>").addClass("panel-body").css({
		"max-height" : "500px",
		"overflow" : "auto"
	}).append(thead).append(tb);
	var phd = $("<div>").addClass("panel-heading").append(title).append(xBtn);
	var pdf = $("<div>").addClass("panel").addClass("panel-default").append(phd).append(pbd);
	this.naviWindow.append(pdf);

	$("body").append(this.naviWindow);
	$(this.naviWindow).hide();
}

gb.layer.Navigator.prototype.open = function(){
	$(this.naviWindow).show();
}

gb.layer.Navigator.prototype.close = function(){
	$(this.naviWindow).hide();
}

gb.layer.Navigator.prototype.showFeatureInfo = function(feature) {
	var fid = feature.getId();
	$(this.td2).text(fid);
	var prop = feature.getProperties();
	var keys = Object.keys(prop);
	$(this.tbody).empty();
	for (var i = 0; i < keys.length; i++) {
		var td1 = $("<td>").text(keys[i]);
		var td2 = $("<td>").attr("colspan", 2).text(prop[keys[i]]);
		var tr1 = $("<tr>").append(td1).append(td2);
		$(this.tbody).append(tr1);
	}
	var geom = feature.getGeometry();
	this.map.getView().fit(geom.getExtent(), this.map.getSize());
	this.map.getView().setZoom(16);
}

gb.layer.Navigator.prototype.prev = function(){
	var features = this.featureList;
	if (this.count > 0 && this.count <= features.length) {
		this.count--;
	} else {
		return;
	}
	var feature = features[this.count];
	if (feature) {
		this.showFeatureInfo(feature);
	}
}

gb.layer.Navigator.prototype.next = function(){
	var features = this.featureList;
	if (this.count >= 0 && this.count < features.length) {
		this.count++;
	} else {
		return;
	}
	var feature = features[this.count];
	if (feature) {
		this.showFeatureInfo(feature);
	}
}
