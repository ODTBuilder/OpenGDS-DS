var gb;
if (!gb)
	gb = {};
if (!gb.footer)
	gb.footer = {};

(function($){
	/**
	 * command 기능
	 * @author hochul.kim
	 * @date 2018.06.05
	 * @version 0.01
	 * @class gb.footer.CommandLine
	 * @constructor
	 */
	gb.footer.CommandLine = function(obj) {
		gb.footer.Base.call(this, obj);
		
		var options = obj || {};
		
		var that = this;
		
		/**
		 * ol.Map 객체
		 * @type {ol.Map}
		 * @private
		 */
		this.map = options.map;
		
		/**
		 * server url
		 * @type {string}
		 * @private
		 */
		this.serverURL = options.serverURL;
		
		/**
		 * JSTree Instance
		 * @type {Object}
		 * @private
		 */
		this.jstree = options.jstree || undefined;
		
		/**
		 * 언어 코드.
		 * ko: 한글, en: 영어
		 * @type {string}
		 * @private
		 */
		this.locale = options.locale || "en";
		
		/**
		 * 번역
		 * @type {Object.<string, Object<string, string>>}
		 * @private
		 */
		this.translator = {
			"history": {
				"en": "History",
				"ko": "이력"
			},
			"log": {
				"en": "Log",
				"ko": "로그"
			},
			"commandLine": {
				"en": "Command line",
				"ko": "명령어 입력"
			}
		}
		
		/**
		 * 명령어 입력 도우미 영역 jquery instance.
		 * input Tag 왼쪽 부분의 영역에 표시될 데이터.
		 * @type {n.fn.init}
		 * @private
		 */
		this.label = undefined;
		
		/**
		 * 작업 이력
		 * @type {Object.<string, string[]>}
		 * @private
		 */
		this.workHistory_ = {};
		
		/**
		 * input tag 입력된 값 임시 저장
		 * @type {string[]}
		 * @private
		 */
		this.inputHistory_ = [];
		
		/**
		 * input에 입력한 모든 값들을 저장
		 * @type {Object.<string, string>}
		 * @private
		 */
		this.params_ = {};
		
		/**
		 * Command 작업 리스트
		 * tip: 명령어 입력 도움말
		 * paramKey: 입력값을 저장하기위한 Key값
		 * before: 다음 입력 단계로 넘어가기전에 수행할 함수. return true일때 다음 작업으로 넘어감. false 일시 재입력
		 * beforeFailLog: before 함수가 false를 return할때의 log
		 * log: 현재 입력 단계 성공시 log
		 * next: 다음 입력 단계
		 * end: 마무리 단계에서 수행할 함수. 현재까지 입력한 값들을 Object에 담아 함수의 인자값으로 전달함
		 * successLog: 작업 성공시 log. end함수가 true 값을 return할때
		 * failLog: 작업 실패시 log. end함수가 false 값을 return할때
		 * @type {Object}
		 * @private
		 */
		this.commandList_ = {
			createLayer: {
				tip: "enter the geoserver",
				paramKey: "geoserver",
				before: function(value){
					if(/[`~!@#$%^&*|\\\'\";:\/?]/gi.test(value)){
						return false;
					} else {
						return true;
					}
				},
				beforeFailLog: "Cannot input special characters",
				next: {
					tip: "enter the workspace",
					paramKey: "workspace",
					before: function(value){
						if(/[`~!@#$%^&*|\\\'\";:\/?]/gi.test(value)){
							return false;
						} else {
							return true;
						}
					},
					beforeFailLog: "Cannot input special characters",
					next: {
						tip: "Enter the datastore",
						paramKey: "datastore",
						before: function(value){
							if(/[`~!@#$%^&*|\\\'\";:\/?]/gi.test(value)){
								return false;
							} else {
								return true;
							}
						},
						beforeFailLog: "Cannot input special characters",
						next: {
							tip: "Enter layer name",
							paramKey: "layerName",
							before: function(value){
								if(/[`~!@#$%^&*|\\\'\";:\/?]/gi.test(value)){
									return false;
								} else {
									return true;
								}
							},
							beforeFailLog: "Cannot input special characters",
							log: "Enter layer name success!",
							next: {
								tip: "Layer type? ( point / lineString / polygon )",
								point: {
									end: function(params){
										createVectorLayer(that.map, params.geoserver, 
												params.workspace, params.datastore, params.layerName, "Point");
									},
									successLog: "create POINT Layer success!",
									failLog: "create POINT Layer falied!"
								},
								lineString: {
									end: function(params){
										createVectorLayer(that.map, params.geoserver, 
												params.workspace, params.datastore, params.layerName, "LineString");
									},
									successLog: "create LINESTRING Layer success!",
									failLog: "create LINESTRING Layer falied!"
								},
								polygon: {
									end: function(params){
										createVectorLayer(that.map, params.geoserver, 
												params.workspace, params.datastore, params.layerName, "Polygon");
									},
									successLog: "create POLYGON Layer success!",
									failLog: "create POLYGON Layer falied!"
								}
							}
						}
					}
				}
			},
			createFeature: {
				tip: "Enter Coodinates (ex.point: [x,y] / lineString & polygon: [[x,y], ...])",
				paramKey: "coordinates",
				before: function(value){
					var layers = that.jstree.getJSTree().get_selected_layer();
					if(layers.length === 1){
						return true;
					} else {
						return false;
					}
				},
				beforeFailLog: "You must select only one layer",
				end: function(params){
					var layers = that.jstree.getJSTree().get_selected_layer();
					var insertCoords = params.coordinates;
					var coords = insertCoords.replace(/[[\]]/g, '').split(",");
					var layer, type, geometry, feature;
					
					if(coords.length === 0 || coords.length % 2 !== 0){
						return false;
					}
					if(layers.length === 1){
						layer = layers[0];
						type = layer.get("git").geometry;
						switch(type){
							case "Point":
								if(!regexCoordinate(coords[0]) || !regexCoordinate(coords[1])){
									return false;
								}
								geometry = new ol.geom.Point([coords[0], coords[1]]);
								break;
							case "LineString":
								var lineCoord = [];
								for(var i = 0; i < coords.length; i = i + 2){
									if(!regexCoordinate(coords[i]) || !regexCoordinate(coords[i+1])){
										return false;
									}
									lineCoord.push([coords[i], coords[i+1]]);
								}
								geometry = new ol.geom.LineString(lineCoord);
								break;
							case "Polygon":
								var polyCoord = [];
								for(var i = 0; i < coords.length; i = i + 2){
									if(!regexCoordinate(coords[i]) || !regexCoordinate(coords[i+1])){
										return false;
									}
									polyCoord.push([coords[i], coords[i+1]]);
								}
								geometry = new ol.geom.Polygon([polyCoord]);
								break;
						}
						feature = new ol.Feature({
							geometry: geometry
						});
						feature.setId(createFeatureId(layer));
						epan.featureRecord.create(layer, feature);
						layer.getSource().addFeature(feature);
					} else {
						return false;
					}
				},
				successLog: "create Feature success!",
				failLog: "Wrong Coordinates"
			},
			moveFeature: {
				tip: "Enter Workspace ID",
				paramKey: "workId",
				next: {
					tip: "Enter Layer ID",
					paramKey: "layerId",
					before: function(value){
						var layer = that.jstree.getJSTree().get_selected_layer();
						if(!layer){
							return false;
						} else {
							return true;
						}
					},
					beforeFailLog: "You must select only one layer",
					next: {
						tip: "Enter feature ID",
						paramKey: "featureId",
						next: {
							tip: "Enter coordinate (ex.[x,y])",
							paramKey: "coordinates",
							end: function(params){
								var layer = that.jstree.getJSTree().get_selected_layer();
								var insertCoords = params.coordinates;
								var coords = insertCoords.replace(/[[\]]/g, '').split(",");
								
								var tempVectorSource = new ol.source.Vector();
								var vectorLayer = new ol.layer.Vector({
									source: tempVectorSource
								});
								
								var layer, feature, geometry, lastCoord, deltaX, deltaY;
								
								if(coords.length !== 2){
									return false;
								}
								
								feature = getFeatureByServer(params.workId, params.layerId, params.featureId);
								if(!feature){
									return false;
								}
								geometry = feature.getGeometry();
								lastCoord = feature.getLastCoordinate();
								
								deltaX = lastCoord[0] - coords[0];
								deltaY = lastCoord[1] - coords[1];
								
								geometry.translate(deltaX, deltaY);
								feature.setGeometry(geometry);
								tempVectorSource.addFeature(feature);
								that.map.addLayer(vectorLayer);
								return true;
							}
						}
					}
				}
			}
		}
		
		/**
		 * command 리스트에서의 현재 작업 위치
		 * @type {Object}
		 * @private
		 */
		this.currentCmd = this.commandList_;
		
		/**
		 * command layout css style
		 * @type {Object}
		 * @private
		 */
		this.elementStyle_ = {
			content: {
				"width": "100%",
				"color": "white",
				"display": "inline-flex"
			},
			commandWrapper: {
				"width": "100%",
				"height": "100%"
			},
			logHistoryWrapper: {
				"width": "100%",
				"height": "80%",
				"display": "inline-flex"
			},
			history: {
				"width": "50%",
				"height": "100%",
				"border-top-left-radius": "10px",
				"border-top-right-radius": "10px",
				"display": "flex",
				"flex-direction": "column",
				"box-shadow": "rgba(0, 0, 0, 0.5) 0px 0px 20px",
				"background-color": "rgba(75, 90, 106, 0.8)"
			},
			historyTitle: {
				"flex": "0 0 auto",
				"padding": "8px 16px",
				"border-bottom": "0.5px solid rgba(255,255,255,.4)"
			},
			historyFunction: {
				"display": "inline-block",
				"float": "right"
			},
			historyContent: {
				"flex": "1 1 auto",
				"overflow": "auto",
			},
			log: {
				"width": "50%",
				"height": "100%",
				"border-top-left-radius": "10px",
				"border-top-right-radius": "10px",
				"display": "flex",
				"flex-direction": "column",
				"box-shadow": "rgba(0, 0, 0, 0.5) 0px 0px 20px",
				"background-color": "rgba(75, 90, 106, 0.8)"
			},
			logTitle: {
				"flex": "0 0 auto",
				"padding": "8px 16px",
				"border-bottom": "0.5px solid rgba(255,255,255,.4)"
			},
			logContent: {
				"flex": "1 1 auto",
				"overflow": "auto",
			},
			lineWrapper: {
				"width": "100%",
				"height": "20%",
				"border-top": "1px solid #838383",
				"display": "flex",
				"position": "relative"
			},
			label: {
				"background-color": "rgba(0, 181, 173, .6)",
				"padding": ".5833em .833em",
				"flex": "0 0 auto"
			},
			line: {
				"padding": ".67857143em 1em",
				"border": "none",
				"color": "rgba(0, 0, 0, .87)",
				"flex": "1 0 auto"
			},
			item: {
				"padding": "0px 16px"
			}
		}
		
		this.createContent();
		this.autocomplete();
	}

	// gb.footer.Base 상속
	gb.footer.CommandLine.prototype = Object.create(gb.footer.Base.prototype);
	gb.footer.CommandLine.prototype.constructor = gb.footer.CommandLine;
	
	/**
	 * command layout안에 내용 element를 생성한다.
	 * @method createContent
	 */
	gb.footer.CommandLine.prototype.createContent = function(){
		var that = this;
		
		this.contentTag.empty();
		this.adjustStyle_(this.contentTag, this.elementStyle_.content);
		
		var commandWrapper = $("<div>").addClass("command-wrapper");
		this.adjustStyle_(commandWrapper, this.elementStyle_.commandWrapper);
		
		var logHistoryWrapper = $("<div>").addClass("log-history-wrapper");
		this.adjustStyle_(logHistoryWrapper, this.elementStyle_.logHistoryWrapper);
		
		var history = $("<div>").addClass("command-history");
		this.adjustStyle_(history, this.elementStyle_.history);
		
		var historyTitle = $("<div>").addClass("history-title");
		this.adjustStyle_(historyTitle, this.elementStyle_.historyTitle);
		historyTitle.text("History");
		
		var historyFunction = $("<div>").addClass("history-function");
		this.adjustStyle_(historyFunction, this.elementStyle_.historyFunction);
		
		this.historyContent = $("<div>").addClass("history-content");
		this.adjustStyle_(this.historyContent, this.elementStyle_.historyContent);
		
		var log = $("<div>").addClass("command-log");
		this.adjustStyle_(log, this.elementStyle_.log);
		
		var logTitle = $("<div>").addClass("log-title");
		this.adjustStyle_(logTitle, this.elementStyle_.logTitle);
		logTitle.text("Log");
		
		this.logContent = $("<div>").addClass("log-content");
		this.adjustStyle_(this.logContent, this.elementStyle_.logContent);
		
		var lineWrapper = $("<div>").addClass("command-line-wrapper");
		this.adjustStyle_(lineWrapper, this.elementStyle_.lineWrapper);
		
		this.label = $("<div>").addClass("command-label");
		this.setLabel();
		this.adjustStyle_(this.label, this.elementStyle_.label);
		
		var line = this.input = $("<input id='commandInput' placeholder='Command Line'>").addClass("command-line");
		this.adjustStyle_(line, this.elementStyle_.line);
		line.keypress(function(e){
			if(e.which === 13){
				that.executeCommand(this.value);
				this.value = "";
			}
		});
		
		// 파일 선택 input
		var fileSelect = 
			$("<input type='file' multiple size='50'>")
				.change(function(){
					that.uploadHistory(this);
				});
		// upload 버튼 추가
		historyFunction.append(
			$("<i>")
				.addClass("fas fa-upload")
				.mouseenter(function(){
					$(this).css("cursor", "pointer");
					$(this).addClass("fa-lg");
				})
				.mouseleave(function(){
					$(this).removeClass("fa-lg");
				})
				.click(function(){
					fileSelect.click();
				})
				.css({"margin-right": "10px"}));
		// download 버튼 추가
		historyFunction.append(
			$("<i>")
				.mouseenter(function(){
					$(this).css("cursor", "pointer");
					$(this).addClass("fa-lg");
				})
				.mouseleave(function(){
					$(this).removeClass("fa-lg");
				})
				.click(function(){
					that.downHistory();
				})
				.addClass("fas fa-download"));
		historyTitle.append(historyFunction);
		
		history.append(historyTitle);
		history.append(this.historyContent);
		
		log.append(logTitle);
		log.append(this.logContent);
		
		logHistoryWrapper.append(history);
		logHistoryWrapper.append(log);
		
		lineWrapper.append(this.label);
		lineWrapper.append(line);
		
		commandWrapper.append(logHistoryWrapper);
		commandWrapper.append(lineWrapper);
		
		this.contentTag.append(commandWrapper);
	}
	
	/**
	 * 명령어 입력 도움글 설정
	 * @method setLabel
	 * @param {string} label 명령어 입력 도움글
	 */
	gb.footer.CommandLine.prototype.setLabel = function(label){
		if(typeof label === "string"){
			this.label.text(label);
		} else if(!label){
			this.label.text(">_");
		}
	}
	
	/**
	 * input에 입력된 모든 값을 저장한 변수를 초기화한다.
	 * @method resetParams
	 */
	gb.footer.CommandLine.prototype.resetParams = function(){
		for(var i in this.params_){
			delete this.params_[i];
		}
	}
	
	/**
	 * input에 입력된 값을 저장
	 * @method pushParam
	 * @param {string} value input에 입력된 값
	 * @return {number}
	 */
	gb.footer.CommandLine.prototype.pushParam = function(value){
		var num = 0;
		var index = [];
		for(var i in this.params_){
			index.push(i);
		}
		while(index.includes(num)){
			num++;
		}
		this.params_[num] = value;
		return num;
	}
	
	/**
	 * 작업 이력을 저장
	 * @method pushWorkHistory
	 * @param {string[]} list - input에 입력된 값들의 배열
	 */
	gb.footer.CommandLine.prototype.pushWorkHistory = function(list){
		var time = getTime();
		if($.isArray(list)){
			this.workHistory_[time.flat] = list.slice();
			this.insertHistoryLayout(time.format, list);
		}
	}
	
	/**
	 * 작업 이력 다운로드
	 * @method downHistory
	 */
	gb.footer.CommandLine.prototype.downHistory = function(){
		var text = "";
		for(let i in this.workHistory_){
			text += this.workHistory_[i].toString();
			text += "\n";
		}
		var file = new Blob([text], {type: "text/plain"});
		var down = document.createElement("a");
		down.setAttribute("href", URL.createObjectURL(file));
		down.setAttribute("download", "history.txt");
		down.click();
	}
	
	/**
	 * 작업 이력 업로드
	 * @method uploadHistory
	 * @param {DOM} input - 파일을 포함하고 있는 DOM 객체
	 */
	gb.footer.CommandLine.prototype.uploadHistory = function(input){
		var that = this;
		
		if("files" in input){
			var files = input.files;
			var r = new FileReader();
			
			r.onloadend = function(e){
				if(e.target.readyState === FileReader.DONE){
					that.parseCmdText(e.target.result);
				}
			}
			
			for(let i = 0; i < files.length; i++){
				r.readAsText(files[i]);
			}
		}
	}
	
	gb.footer.CommandLine.prototype.parseCmdText = function(text){
		var lines, cmds;
		
		if(typeof text === "string"){
			lines = text.split("\n").slice();
			for(let i = 0; i < lines.length; i++){
				if(!!lines[i].trim()){
					cmds = lines[i].split(",");
					for(let j = 0; j < cmds.length; j++){
						this.executeCommand(cmds[j].trim());
					}
				}
			}
		}
	}
	
	gb.footer.CommandLine.prototype.insertHistoryLayout = function(time, list){
		if($.isArray(list)){
			var item = $("<div class='list-item'>");
			this.adjustStyle_(item, this.elementStyle_.item);
			item.text(time + " " + list.join(" > "));
			this.historyContent.append(item);
			this.historyContent.scrollTop(this.historyContent[0].scrollHeight)
		}
	}
	
	gb.footer.CommandLine.prototype.insertLogLayout = function(text){
		if(typeof text === "string"){
			var item = $("<div class='list-item'>");
			this.adjustStyle_(item, this.elementStyle_.item);
			item.text(text);
			this.logContent.append(item);
			this.logContent.scrollTop(this.logContent[0].scrollHeight)
		}
	}
	
	gb.footer.CommandLine.prototype.executeCommand = function(value){
		if(!!value){
			
			if(!!this.currentCmd[value]){
				
				if(!!this.currentCmd[value].tip){
					
					this.setLabel(this.currentCmd[value].tip);
					this.inputHistory_.push(value);
					this.insertLogLayout(this.currentCmd[value].log);
					this.currentCmd = this.currentCmd[value];
					
				} else if(!!this.currentCmd[value].end){
					
					this.currentCmd[value].end(this.params_);
					this.inputHistory_.push(value);
					this.pushWorkHistory(this.inputHistory_);
					this.insertLogLayout(this.currentCmd[value].log);
					this.resetAll();
					
				}
				
			} else if(!!this.currentCmd.end){
				
				if(!!this.currentCmd.before){
					if(!this.currentCmd.before(value)){
						this.insertLogLayout(this.currentCmd.beforeFailLog);
						return;
					}
				}
				
				if(!!this.currentCmd.paramKey){
					this.params_[this.currentCmd.paramKey] = value;
				} else {
					this.pushParam(value);
				}
				
				this.currentCmd.end(this.params_);
				this.inputHistory_.push(value);
				this.pushWorkHistory(this.inputHistory_);
				this.resetAll();
				this.insertLogLayout(this.currentCmd.log);
				
			} else if(!!this.currentCmd.next){
				
				if(!!this.currentCmd.before){
					if(!this.currentCmd.before(value)){
						this.insertLogLayout(this.currentCmd.beforeFailLog);
						return;
					}
				}
				
				this.setLabel(this.currentCmd.next.tip);
				
				if(!!this.currentCmd.paramKey){
					
					this.params_[this.currentCmd.paramKey] = value;
					
				} else {
					
					this.pushParam(value);
				}
				
				this.inputHistory_.push(value);
				this.insertLogLayout(this.currentCmd.log);
				this.currentCmd = this.currentCmd.next;
				
			} else {
				this.insertLogLayout(value + " is not command");
			}
		} else {
			this.resetAll();
		}
	}
	
	gb.footer.CommandLine.prototype.resetAll = function(){
		this.resetCommand();
		this.setLabel();
		this.resetParams();
		this.inputHistory_ = [];
	}
	
	gb.footer.CommandLine.prototype.resetCommand = function(){
		this.currentCmd = this.commandList_;
	}
	
	
	gb.footer.CommandLine.prototype.addCommand = function(cmdName, cmdCallback){
		if(!this.commandList[cmdName]){
			this.commandList[cmdName] = cmdCallback;
		}
	}
	
	gb.footer.CommandLine.prototype.getCommandList = function(){
		var a = [];
		for(name in this.commandList_){
			a.push(name);
		}
		return a;
	}
	
	function createVectorLayer(map, geo, work, store, name, type){
		/*var layer = new gb.layer.LayerInfo({
			format: "shp",
			srs: "EPSG:5186",
			name: name,
			geometry: type,
			isNew: true
		});*/
		var vectorLayer = new ol.layer.Vector({
			source: new ol.source.Vector()
		});
		var groupLayer = new ol.layer.Group();
		var gitLayer = {
			"editable" : true,
			"geometry" : type,
			"validation" : false,
			"geoserver": geo,
			"workspace": work,
			"datastore": store
		};
		vectorLayer.set("git", gitLayer);
		groupLayer.set("name", store);
		vectorLayer.set("name", name);
		var collect = new ol.Collection();
		collect.push(vectorLayer);
		groupLayer.setLayers(collect);
		map.addLayer(groupLayer);
	}
	
	function createFeatureId(layer){
		if(!epan){
			return "null";
		}
		var a = epan.featureRecord.getCreated();
		var b = a[layer.get("id")];
		var fid;
		if(!b){
			fid = layer.get("id") + ".new0";
		} else {
			var keys = Object.Keys(b);
			var count;
			if(keys.length === 0){
				count = 0;
			} else {
				var id = keys[keys.length - 1];
				var nposit = (id.search(".new")) + 4;
				count = (parseInt(id.substr(nposit, id.length)) + 1);
			}
			fid = layer.get("id") + ".new" + count;
		}
		return fid;
	}
	
	function getFeatureByServer(workId, layerId, featureId){
		var params = {
			"service" : "WFS",
			"version" : "1.0.0",
			"request" : "GetFeature",
			"typeName" : layerId,
			"outputformat" : "text/javascript",
			"featureID" : featureId,
			"format_options" : "callback:getJson"
		};
		var feature;
		var url = this.serverURL;
		$.ajax({
			url : url,
			data : params,
			async: false,
			dataType : 'jsonp',
			jsonpCallback : 'getJson',
			success : function(data) {
				feature = new ol.format.GeoJSON().readFeature(JSON.stringify(data.features[0]));
			}
		});
		
		var vectorSource = new ol.source.Vector({
			format: new ol.format.GeoJSON(),
			url: function(extent){
				var url = url + "version=1.0.0&request=GetFeature&typename=" + layerId + "&" +
				"outputFormat=text/javascript&bbox=" + extent.join(",");
			}
		});
		
		return vectorSource;
	}
	
	function regexCoordinate(coord){
		if(typeof coord === "string"){
			return /^-?[0-9]*(?:\.[0-9]*)?$/g.test(coord);
		} else {
			return false;
		}
	}
	
	function getTime(){
		var time = {};
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth() < 9 ? "0" + ( date.getMonth() + 1 ) : ( date.getMonth() + 1 );
		var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
		var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		var min = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
		var sec = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
			
		time.format = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
		time.flat = year + month + day + hour + min + sec;
		return time;
	}
	
	/**
	 * 명령어 자동 완성 기능 활성화
	 * @method autocomplete
	 */
	gb.footer.CommandLine.prototype.autocomplete = function(){
		var that = this;
		var except = ["tip", "paramKey", "before", "beforeFailLog", "log", "next", "end", "successLog", "failLog"];
		/*the autocomplete function takes two arguments,
		the text field element and an array of possible autocompleted values:*/
		var currentFocus;
		/*execute a function when someone writes in the text field:*/
		var inp = this.input[0];
		inp.addEventListener("input", function(e) {
			var a, b, i, val = this.value;
			// 명령어 목록 배열
			var arr = Object.keys(that.currentCmd);
			
			var labelWidth = this.parentNode.getElementsByClassName("command-label")[0].clientWidth;
			/*close any already open lists of autocompleted values*/
			closeAllLists();
			if (!val) { return false;}
			currentFocus = -1;
			/*create a DIV element that will contain the items (values):*/
			a = document.createElement("DIV");
			a.setAttribute("id", this.id + "autocomplete-list");
			a.setAttribute("class", "autocomplete-items");
			a.style["position"] = "absolute";
			a.style["border"] = "1px solid #d4d4d4";
			a.style["border-bottom"] = "none";
			a.style["border-top"] = "none";
			a.style["z-index"] = "99";
			/*position the autocomplete items to be the same width as the container =*/
			a.style["bottom"] = "100%";
			a.style["left"] = labelWidth + "px";
			a.style["right"] = "auto";
			a.style["color"] = "rgba(0, 0, 0, 0.87)";
			/*append the DIV element as a child of the autocomplete container:*/
			this.parentNode.appendChild(a);
			/*for each item in the array...*/
			for (i = 0; i < arr.length; i++) {
				if(except.indexOf(arr[i]) !== -1){
					continue;
				}
				/*check if the item starts with the same letters as the text field value:*/
				if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
					/*create a DIV element for each matching element:*/
					b = document.createElement("DIV");
					/*make the matching letters bold:*/
					b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
					b.innerHTML += arr[i].substr(val.length);
					/*insert a input field that will hold the current array item's value:*/
					b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
					/*execute a function when someone clicks on the item value (DIV element):*/
					b.style["padding"] = "10px";
					b.style["cursor"] = "pointer";
					b.style["background-color"] = "#fff";
					b.style["border-bottom"] = "1px solid #d4d4d4";
					b.addEventListener("click", function(e) {
						/*insert the value for the autocomplete text field:*/
						inp.value = this.getElementsByTagName("input")[0].value;
						/*close the list of autocompleted values,
						(or any other open lists of autocompleted values:*/
						closeAllLists();
					});
					a.appendChild(b);
				}
			}
		});
		
		/*execute a function presses a key on the keyboard:*/
		inp.addEventListener("keydown", function(e) {
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x) x = x.getElementsByTagName("div");
			if (e.keyCode == 40) {
				/*If the arrow DOWN key is pressed,
				increase the currentFocus variable:*/
				currentFocus++;
				/*and and make the current item more visible:*/
				addActive(x);
			} else if (e.keyCode == 38) { //up
				/*If the arrow UP key is pressed,
				decrease the currentFocus variable:*/
				currentFocus--;
				/*and and make the current item more visible:*/
				addActive(x);
			} else if (e.keyCode == 13) {
				/*If the ENTER key is pressed, prevent the form from being submitted,*/
				if (currentFocus > -1) {
					/*and simulate a click on the "active" item:*/
					if (x) x[currentFocus].click();
				}
			}
		});
		
		function addActive(x) {
			/*a function to classify an item as "active":*/
			if (!x) return false;
			/*start by removing the "active" class on all items:*/
			removeActive(x);
			if (currentFocus >= x.length) currentFocus = 0;
			if (currentFocus < 0) currentFocus = (x.length - 1);
			/*add class "autocomplete-active":*/
			x[currentFocus].classList.add("autocomplete-active");
			x[currentFocus].style.backgroundColor = "DodgerBlue";
		}
		
		function removeActive(x) {
			/*a function to remove the "active" class from all autocomplete items:*/
			for (var i = 0; i < x.length; i++) {
				x[i].classList.remove("autocomplete-active");
				x[i].style.backgroundColor = "#fff";
			}
		}
		
		function closeAllLists(elmnt) {
			/*close all autocomplete lists in the document,
			except the one passed as an argument:*/
			var x = document.getElementsByClassName("autocomplete-items");
			for (var i = 0; i < x.length; i++) {
				if (elmnt != x[i] && elmnt != inp) {
					x[i].parentNode.removeChild(x[i]);
				}
			}
		}
	}
}(jQuery));