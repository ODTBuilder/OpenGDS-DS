var gb;
if (!gb)
	gb = {};
if (!gb.footer)
	gb.footer = {};

(function($){
	/**
	 * feature list table 객체를 정의한다.
	 * 
	 * @author hochul.kim
	 * @date 2018. 06.05
	 * @version 0.01
	 * @class gb.footer.FeatureList
	 * @constructor
	 */
	gb.footer.FeatureList = function(obj) {
		gb.footer.Base.call(this, obj);
		
		var that = this;
		
		var options = obj || {};
		
		this.map = options.map || undefined;
		
		this.wfstURL = options.wfstURL || '';
		
		this.layerInfoURL = options.layerInfoURL || '';
		
		/**
		 * 현재 선택된 레이어의 JSTree ID
		 */
		this.selectedLayer = undefined;
		
		/**
		 * JSTree의 treeid가 key이고 value는 feature의 속성 key값인 col, feature의 속성값인 data로 구성
		 */
		this.attrList = {};
		
		/**
		 * 속성값이 수정된 feature들의 집합
		 */
		this.editedFeature = {};
		
		/**
		 * DataTable ID 고유번호
		 */
		this.countId = 0;
		
		/**
		 * footer content에 생성할 table element의 ID
		 */
		this.tableId = options.tableId || "featureListTable";
		
		this.tableTitles = options.tableTitles || [];
		
		this.tableData = options.tableData || [];
		
		this.content = options.content;
			
		this.titleAreaStyle = {
			"position": "absolute",
			"display": "none",
			"padding": "8px 16px"
		};
		
		this.contentAreaStyle = {
			"height": "100%"
		};
		
		this.backgroundStyle = {
			"display": "none",
			"position": "absolute",
			"z-index": "2",
			"left": "0",
			"top": "0",
			"width": "100%",
			"height": "100%",
			"overflow": "auto",
			"background-color": "rgb(0, 0, 0)",
			"background-color": "rgba(0, 0, 0, 0.4)"
		};
		
		this.tableElement = this.createTableElement();
		
		this.backgroundDiv = $("<div class='footer-background'>");
		this.adjustStyle_(this.backgroundDiv, this.backgroundStyle);
		this.targetElement.append(this.backgroundDiv);
		
		this.dataTable = this.tableElement.DataTable({
			columns: [
				{title: 'Column1'},
				{title: 'Column2'}
			],
			data: [
				[
					'Row 1 Data 1',
					'Row 1 Data 2'
				]
			],
			info: false,
			paging: false,
			scrollX: true,
			scrollY: true,
			scrollCollapse: true
		});
		
		this.tableElement.find("tbody > tr").css("background-color", "transparent");
		this.footerTag.find("label").css("color", "#DDD");
		
		window.addEventListener("resize", function () {
			that.onResize();
		}, false);
		
		$(document).ready(function(){
			that.onResize();
		});
		
		this.footerTag.on("footeropen", function(){
			that.onResize();
		});
		
		this.footerTag.on("footerclose", function(){
			if(!$.isEmptyObject(that.editedFeature)){
				that.openSaveModal();
			}
		});
		
		// Div style change event 추가
		var orig = $.fn.css;
		
		$.fn.css = function() {
			var result = orig.apply(this, arguments);
			$(this).trigger('stylechange');
			return result;
		}
		
		// footer tag의 css style 변경시 실행되는 이벤트 함수
		this.footerTag.on("stylechange", function(e){
			var display = this.style.display;
			
			// feature list footer tag의 display none으로 설정되면 background의 display도 none으로 변경
			if(display === "none"){
				that.backgroundDiv.css("display", "none");
			} else {
				that.backgroundDiv.css("display", "block");
			}
		});
	}
	
	// gb.footer.Base 상속
	gb.footer.FeatureList.prototype = Object.create(gb.footer.Base.prototype);
	gb.footer.FeatureList.prototype.constructor = gb.footer.FeatureList;
	
	gb.footer.FeatureList.prototype.createTableElement = function(){
		this.removeTableElement();
		
		var that = this;
		var num = ++this.countId;
		
		var table = $("<table>").attr("id", this.tableId + num).css({
			width: "100%"
		});
		
		table.on("click", "tr", function(){
			var data = that.dataTable.row(this).data();
			if(data.length === 0){
				return;
			}
			
			that.map.getView().fit(data[0].getGeometry().getExtent(), that.map.getSize());
			that.map.getView().setZoom(14);
		});
		
		this.createFooter({
			title: this.title,
			content: table
		});
		
		return table;
	}
	
	gb.footer.FeatureList.prototype.removeTableElement = function(){
		if(!this.tableElement){
			return;
		}
		this.tableElement.off("click", "tr");
		this.tableElement.remove();
		delete this.tableElement;
		
		$(document).off("click", "#addRowBtn");
		$(document).off("click", "#editRowBtn");
		$(document).off("click", "#deleteRowBtn");
		$("#altEditor-modal").off("edited");
		$("#altEditor-modal").remove();
	}
	
	/**
	 * feature list layout 생성 클릭 이벤트
	 * @param {jquery} target Target
	 */
	gb.footer.FeatureList.prototype.clickEvent = function(target){
		var that = this;
		$(target).click(function(){
			that.createFooter({
				title: that.title,
				content: that.tableElement
			});
		});
	}
	
	/**
	 * 전체 객체 리스트 테이블 크기 재갱신
	 */
	gb.footer.FeatureList.prototype.onResize = function(){
		if(!!this.dataTable){
			this.dataTable.columns.adjust().draw();
			this.resizeTbody();
		}
	}
	
	/**
	 * tbody height 갱신
	 */
	gb.footer.FeatureList.prototype.resizeTbody = function(){
		var a = this.footerTag.find(".footer-content").height();
		var b = $("#" + this.tableId + this.countId + "_wrapper").find(".dt-buttons").height();
		var c = $("#" + this.tableId + this.countId + "_wrapper .dataTables_scrollHead").height();
		
		var height = a - b - c;
		
		this.footerTag.find(".footer-content #" + this.tableId + this.countId + "_wrapper .dataTables_scrollBody").css("max-height", height + "px");
	}
	
	/**
	 * Feature Attribute 편집 저장 여부 알림창을 생성한다.
	 */
	gb.footer.FeatureList.prototype.openSaveModal = function(){
		var that = this;

		var row2 = $("<div>").addClass("row").append(
		"변경사항이 있습니다. 저장하시겠습니까?");

		var well = $("<div>").addClass("well").append(row2);

		var closeBtn = $("<button>").css({
			"float" : "right"
		}).addClass("gb-button").addClass("gb-button-default").text("Cancel");
		var okBtn = $("<button>").css({
			"float" : "right"
		}).addClass("gb-button").addClass("gb-button-primary").text("Save");

		var buttonArea = $("<span>").addClass("gb-modal-buttons").append(okBtn)
				.append(closeBtn);
		var modalFooter = $("<div>").append(buttonArea);

		var gBody = $("<div>").append(well).css({
			"display" : "table",
			"width" : "100%"
		});
		var openSaveModal = new gb.modal.Base({
			"title" : "저장",
			"width" : 540,
			"height" : 250,
			"autoOpen" : true,
			"body" : gBody,
			"footer" : modalFooter
		});
		$(closeBtn).click(function() {
			that.attrList = {};
			that.editedFeature = {};
			openSaveModal.close();
		});
		$(okBtn).click(function() {
			that.sendWFSTTransaction();
		});
	}
	
	/**
	 * DataTable Table 내용 갱신
	 * @param {String} treeid
	 */
	gb.footer.FeatureList.prototype.updateTable = function(treeid){
		var that = this;
		this.selectedLayer = treeid;
		
		var col = this.attrList[treeid].col;
		var features = this.attrList[treeid].features;
		var column = [],
			data= [];
		
		for(var i = 0; i < col.length; i++){
			if(col[i] === "geometry"){
				column.push({
					title: col[i],
					visible: false
				});
			} else {
				column.push({
					title: col[i]
				});
			}
		}
		
		for(var id in features){
			var arr = [];
			for(var i = 0; i < col.length; i++){
				if(col[i] === "geometry"){
					arr.push(features[id]);
				} else {
					arr.push(features[id].get(col[i]));
				}
			}
			data.push(arr);
		}
		
		this.dataTable.clear();
		this.dataTable.destroy();
		delete this.dataTable;
		this.tableElement = this.createTableElement();
		
		this.dataTable = this.tableElement.DataTable({
			columns: column,
			data: data,
			info: false,
			paging: false,
			scrollX: true,
			scrollY: "100%",
			scrollCollapse: true,
			dom: "Bfrtip",
			select: "single",
			responsive: true,
			altEditor: true,
			buttons: [{
				extend: "selected",
				text: "Edit",
				name: "edit"
			}
			/*{
				extend: "selected",
				text: "Delete",
				name: "delete"
			}*/]
		});
		
		$("#altEditor-modal").on("edited", function(e, data){
			var feature, keys;
			var layer = that.attrList[that.selectedLayer];
			var geomKey = that.attrList[that.selectedLayer].geomKey;
				
			for(let i = 0; i < data.length; i++){
				if(data[i] instanceof ol.Feature){
					feature = data[i];
					keys = data[i].getKeys();
				}
			}
			
			for(let i = 0; i < keys.length; i++){
				if(keys[i] === "geometry"){
					continue;
				}
				feature.set(keys[i], data[i]);
			}
			
			if(!that.editedFeature[that.selectedLayer]){
				that.editedFeature[that.selectedLayer] = {};
				that.editedFeature[that.selectedLayer].serverName = layer.serverName;
				that.editedFeature[that.selectedLayer].workspace = layer.workspace;
				that.editedFeature[that.selectedLayer].layerName = layer.layerName;
			}
			
			if(!!geomKey){
				feature.setGeometryName(geomKey);
				feature.set(geomKey, feature.get("geometry"));
				feature.unset("geometry");
			}
			
			that.editedFeature[that.selectedLayer].feature = feature;
		});
		
		this.tableElement.find("tbody > tr").css("background-color", "transparent");
		this.footerTag.find("label").css("color", "#DDD");
		this.onResize();
	}
	
	gb.footer.FeatureList.prototype.updateFeatureList = function(opt){
		var options = opt || {};
		
		var treeid = options.treeid;
		
		if(!!this.attrList[treeid]){
			this.updateTable(treeid)
			return;
		}
		
		var url = options.url;
		if(!url){
			console.error('gb.footer.FeatureList: url is required');
			return;
		}
		
		var geoserver = options.geoserver;
		if(!geoserver){
			console.error('gb.footer.FeatureList: geoserver name is required');
			return;
		}
		
		var workspace = options.workspace;
		if(!workspace){
			console.error('gb.footer.FeatureList: workspace name is required');
			return;
		}
		
		var layerName = options.layerName;
		if(!layerName){
			console.error('gb.footer.FeatureList: layer name is required');
			return;
		}
		
		var list = this.attrList;
		
		var defaultParameters = {
			"serverName": geoserver,
			"workspace": workspace,
			"version" : "1.0.0",
			"typeName" : layerName,
			"outputformat" : "application/json",
		};

		var that = this;
		$.ajax({
			url : url,
			type : "GET",
			contentType : "application/json; charset=UTF-8",
			data : defaultParameters,
			dataType : "JSON",
			success : function(errorData, textStatus, jqXHR) {
				var format = new ol.format.GeoJSON().readFeatures(JSON.stringify(errorData));
				var th = [],
					td = [],
					data = {},
					col = [];
				
				for(var i in format){
					if(!th.length){
						col = format[i].getKeys();
					}
					data[format[i].getId()] = format[i];
				}
				
				list[treeid] = {};
				list[treeid].serverName = geoserver;
				list[treeid].workspace = workspace;
				list[treeid].layerName = layerName;
				list[treeid].col = col;
				list[treeid].features = data;
				
				that.requestLayerInfo(geoserver, workspace, layerName, treeid);
				that.updateTable(treeid);
				//that.setTitle(layerName);
			},
			error: function(jqXHR, textStatus, errorThrown){
				console.log(errorThrown);
			}
		});
	}
	
	gb.footer.FeatureList.prototype.requestLayerInfo = function(serverName, workspace, layer, treeid){
		var list = this.attrList;
		var treeid = treeid;
		var a = {
			serverName: serverName,
			workspace: workspace,
			geoLayerList: [layer]
		};
		
		$.ajax({
			method : "POST",
			url: this.layerInfoURL,
			data: JSON.stringify(a),
			contentType: 'application/json; charset=utf-8',
			success: function(data, textStatus, jqXHR) {
				var geomKey = data[0].geomkey;
				list[treeid].geomKey = geomKey;
			},
			error: function(e) {
				var errorMsg = e? (e.status + ' ' + e.statusText) : "";
				console.log(errorMsg);
			},
		});
	}
	
	gb.footer.FeatureList.prototype.sendWFSTTransaction = function(){
		var featureInfo = this.editedFeature;
		var format = new ol.format.WFS();
		var node, param;
		
		for(var treeid in featureInfo){
			node = format.writeTransaction(null, [featureInfo[treeid].feature], null, {
				"featureNS": featureInfo[treeid].workspace,
				"featurePrefix": featureInfo[treeid].workspace,
				"featureType": featureInfo[treeid].layerName,
				"version": "1.0.0"
			});
			
			param = {
				"serverName": featureInfo[treeid].serverName,
				"workspace": featureInfo[treeid].workspace,
				"wfstXml": new XMLSerializer().serializeToString(node)
			}
			
			$.ajax({
				type: "POST",
				url: this.wfstURL,
				data: JSON.stringify(param),
				contentType: 'application/json; charset=utf-8',
				success: function(data) {
					var result = format.readTransactionResponse(data);
					console.log(result);
				},
				error: function(e) {
					var errorMsg = e? (e.status + ' ' + e.statusText) : "";
					console.log(errorMsg);
				},
				context: this
			});
		}
	}
}(jQuery));