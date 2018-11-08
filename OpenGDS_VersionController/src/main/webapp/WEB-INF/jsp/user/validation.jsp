<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>GeoDT Online</title>
<jsp:include page="/WEB-INF/jsp/common/libimport.jsp" />
<style>
/* .navbar-brand { */
/* 	background-image: url(resources/img/onlyglobe.png); */
/* 	width: 284px; */
/* } */
.crsitem {
	cursor: pointer;
}

.file-area {
	width: 100%;
	min-height: 100px;
}

select.QATypeSelect {
	visibility: hidden;
	display: none;
}

label.QATypeSelect {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: normal;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #333;
	background-color: #fff;
	border-color: #ccc;
	width: 100%;
}

label.QATypeSelect-Selected {
	border-color: #4cae4c;
}

div.QATypeSelect-Selected::before {
	font-family: "Font Awesome 5 Free";
	font-weight: 900;
	content: "\f00c";
	color: #5cb85c;
}

li.QATypeSelect {
	padding: 5px;
}

li.QATypeSelect:hover {
	background-color: #ededed;
}

.SettingSection {
	margin-top: 10px;
	margin-bottom: 10px;
}

input.radio {
	visibility: hidden;
	display: none;
}

input.radio+label {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: normal;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #333;
	background-color: #fff;
	border-color: #ccc;
	width: 100%;
}

input.radio:checked+label {
	border-color: #4cae4c;
}

input.radio:checked+label::before {
	font-family: "Font Awesome 5 Free";
	font-weight: 900;
	content: "\f00c";
	color: #5cb85c;
}

.DropArea {
	/* 	text-align: center; */
	/* 	display: table-cell; */
	min-height: 300px;
	width: 100%;
	display: table;
	cursor: pointer;
	/* 	vertical-align: middle; */
}

.BeforeDrop {
	border: 2px dashed #9f9f9f;
	border-radius: 8px;
}

.DefaultMessage {
	display: table-cell;
	vertical-align: middle;
	text-align: center;
}

.DefaultMessage>h3 {
	color: #747474;
}
/* Mimic table appearance */
div.table {
	display: table;
}

div.table .file-row {
	display: table-row;
}

div.table .file-row>div {
	display: table-cell;
	vertical-align: middle;
	border-top: 1px solid #ddd;
	padding: 8px;
}

div.table .file-row:nth-child(odd) {
	background: #f9f9f9;
}

.Message {
	position: fixed;
	top: 0;
	left: 0;
	/* 	width: 100%; */
}
</style>
</head>
<body>
	<script>
		$(document).ready(
				function() {
					console.log("ready!");

					// Tooltip template 함수
					// options : object 또는 string.
					// options.title : 툴팁의 가장 상단 부분 내용
					// options.img : 이미지가 포함된 div 생성. object 또는 Array
					// options.img.url : 이미지 local 경로
					// options.img.title : 이미지 title
					function template(options) {
						// 최상단 element 생성
						var template = $('<div class="popover" role="tooltip">');
						// 툴팁 element의 화살표 생성
						template.append($('<div class="arrow">'));

						// options가 object일때 실행
						if (typeof options === 'object') {

							// title 부분 생성
							template.append($('<div class="popover-content">').append(options.title));

							// image에 대한 파라미터값이 있을때 실행
							if (!!options.img) {
								var imgArr = [];
								if (Array.isArray(options.img)) {
									imgArr = options.img;
								} else {
									// options.img 가 object일 때 imgArr 변수에 push
									imgArr.push(options.img);
								}

								var row;
								var column;
								var thumb;

								// 부트스트랩 thumbnail 생성. imgArr 배열의 개수만큼 vertical 형식으로 thumbnail을 생성
								for ( var i in imgArr) {
									row = $('<div class="row">');
									column = $('<div class="col-md-12">');
									thumb = $('<div class="thumbnail">');

									thumb.append($('<img class="img-responsive" src="' + imgArr[i].url + '">'));
									thumb.append($('<div class="caption">').append($('<p>' + imgArr[i].title + '</p>')));
									column.append(thumb);
									row.append(column);

									template.append(row);
								}
							}
						} else {
							// options가 string일때
							template.append($('<div class="popover-content">').append(options));
						}

						return template;
					}

					$(".QA-User-Setting").tooltip({
						"html" : true,
						"template" : template('<spring:message code="lang.hintUserSettings" />'),
						"title" : "tooltip",
						"placement" : "right"
					});

					$(".QA-type-sect").tooltip({
						"html" : true,
						"template" : template('<spring:message code="lang.hintValidationType" />'),
						"title" : "tooltip",
						"placement" : "right"
					});

					$(".QA-detail-sect").tooltip({
						"html" : true,
						"template" : template('<spring:message code="lang.hintValidationEdit" />'),
						"title" : "tooltip",
						"placement" : "right"
					});

					$(".QA-sub-options").tooltip({
						"html" : true,
						"template" : template('<spring:message code="lang.hintValidationFormatAndCoordSys" />'),
						"title" : "tooltip",
						"placement" : "right"
					});

					var fileToolTip = template({
						title : $("<span>").html('<spring:message code="lang.hintValidationFileStructure" />'),
						img : [ {
							url : "${pageContext.request.contextPath}/resources/img/forest_file_tree.png",
							title : '<spring:message code="lang.exForestMapFileStruct" />'
						}, {
							url : "${pageContext.request.contextPath}/resources/img/underline_file_tree.png",
							title : '<spring:message code="lang.exUndergroundFileStruct" />'
						} ]
					})

					$(".QA-Upload-Area").tooltip({
						"html" : true,
						"template" : fileToolTip,
						"title" : "tooltip",
						"placement" : "right"
					});

					var fileFormat = {
						sel1 : {
							qacat1 : "dxf",
							qacat2 : "shp;ngi"
						},
						sel2 : {
							qacat1 : "dxf",
							qacat2 : "shp"
						},
						sel3 : {
							qacat1 : "shp",
							qacat2 : "shp"
						}
					}

					function updateFileFormatSeletor(list) {
						// file format 선택자 초기화
						$("#suboption1").empty();

						// file format 선택자에 option 태그 생성
						for ( var i in list) {
							var option = $("<option value='" + list[i] + "'>" + list[i] + "</option>")
							$("#suboption1").append(option);
						}
					}
					// file format 선택자 초기 설정
					updateFileFormatSeletor(fileFormat.sel1.qacat1.trim().split(";"));

					// hochul.kim
					// radio 버튼 클릭 이벤트 함수
					$(".QA-detail-sect .radio").click(function() {
						// 선택된 검수 대상 종류
						var type = $("label.QATypeSelect-Selected").attr("for");

						// 선택된 검수 대상의 데이터 타입
						var detail = $(".QA-detail-sect input.radio:checked").attr("id");
						var formatList = fileFormat[type][detail].trim().split(";");
						updateFileFormatSeletor(formatList);
					});

					$("#quicksetting").change(function() {
						var formatList;

						if ($(this).find("option:selected").val() === "nonset") {
							$(".QA-type-sect").show();
							$(".QA-detail-sect").show();

							// 선택된 검수 대상 종류
							var type = $("label.QATypeSelect-Selected").attr("for");

							// 선택된 검수 대상의 데이터 타입
							var detail = $(".QA-detail-sect input.radio:checked").attr("id");

							formatList = fileFormat[type][detail].trim().split(";");
						} else {

							$(".QA-type-sect").hide();
							$(".QA-detail-sect").hide();

							// 선택된 setting의 파일 지원 형식을 배열로 저장
							formatList = $(this).find("option:selected").data("support").trim().split(";");
						}

						// file format 선택자에 option 태그 생성
						updateFileFormatSeletor(formatList);
					});

					$("label.QATypeSelect").hover(function() {
						var nlist = $(this).prev().children();
						for (var i = 0; i < nlist.length; i++) {
							var attr = $(nlist[i]).attr("disabled");
							if (typeof attr === "undefined") {
								var li = $("<li>").text($(nlist[i]).text()).addClass("QATypeSelect");
								$(this).find("ul").append(li);
							}
						}
						$(this).find("ul").css("display", "block");
					}, function() {
						$(this).find("ul").empty();
						$(this).find("ul").css("display", "none");
					});

					$(document).on("click", "li.QATypeSelect", function() {
						var selected = $(this).parent().parent().prev().find("option:contains('" + $(this).text() + "')").val();
						$(this).parent().parent().prev().val(selected).trigger('change');
						$(this).parent().empty();
						$(this).parent().css("display", "none");
					});

					$("select.QATypeSelect").change(
							function() {
								var children = $("label.QATypeSelect").children();
								var label = $("label.QATypeSelect");
								for (var i = 0; i < label.length; i++) {
									if ($(label[i]).find("div").text().indexOf('<spring:message code="lang.digitalMap" />') !== -1) {
										$(label[i]).find("div").text('<spring:message code="lang.digitalMap" />');
									}
									if ($(label[i]).find("div").text().indexOf('<spring:message code="lang.underFacility" />') !== -1) {
										$(label[i]).find("div").text('<spring:message code="lang.underFacility" />');
									}
									if ($(label[i]).find("div").text().indexOf('<spring:message code="lang.forestMap" />') !== -1) {
										$(label[i]).find("div").text('<spring:message code="lang.forestMap" />');
									}
								}
								if ($("label.QATypeSelect").children().filter("div").hasClass("QATypeSelect-Selected")) {
									$("label.QATypeSelect").children().filter("div").removeClass("QATypeSelect-Selected");
								}
								if ($("label.QATypeSelect").hasClass("QATypeSelect-Selected")) {
									$("label.QATypeSelect").removeClass("QATypeSelect-Selected");
								}
								$(this).next().addClass("QATypeSelect-Selected");
								$(this).next().find("div.QATypeSelect").empty().append($(this).children().filter("option:selected").text())
										.addClass("QATypeSelect-Selected");
								$("#qatype").val($(this).children().filter("option:selected").val());
								console.log($(':radio[name="qaver"]:checked').val());
								console.log($("#qatype").val());

								if ($(this).attr("id") === "sel3") {
									$("#qacat1").parent().parent().hide();
								} else {
									$("#qacat1").parent().parent().show();
								}

								// hochul.kim
								// 선택된 검수 대상 종류
								var type = $("label.QATypeSelect-Selected").attr("for");
								// 선택된 검수 대상의 데이터 타입
								var detail = $(".QA-detail-sect input.radio:checked").attr("id");
								var formatList = fileFormat[type][detail].trim().split(";");
								// file format 선택자에 option 태그 생성
								updateFileFormatSeletor(formatList);
							});

					//해당 셀렉트 박스 이벤트 실행
					$("#sel1").trigger('change');

					var previewNode = document.querySelector("#previewTemplate");
					previewNode.id = "";
					var previewTemplate = previewNode.parentNode.innerHTML;
					previewNode.parentNode.removeChild(previewNode);

					Dropzone.autoDiscover = false;

					var dropzone = new Dropzone("div#dropzone", {
						url : "${pageContext.request.contextPath}/upload.do?${_csrf.parameterName}=${_csrf.token}",
						method : "post",
						maxFiles : 1,
						maxFilesize : 10240,
						autoProcessQueue : false,
						thumbnailWidth : 60,
						thumbnailHeight : 60,
						acceptedFiles : ".zip",
						clickable : ".FileBrowse",
						dictDefaultMessage : "Drag and drop files here",
						previewTemplate : previewTemplate,
						previewsContainer : "#previewContainer",
						init : function() {

							document.querySelector(".TotalProgress .progress-bar").style.width = "0%";

							this.on("addedfile", function(file) {
								$(".dz-message").hide();
								if ($("#UploadPanel").hasClass("BeforeDrop")) {
									$("#UploadPanel").removeClass("BeforeDrop");
								}
								if (!file.type.match(/image.*/)) {
									var beforestr = file.name.toLocaleLowerCase();
									if (/dxf$/.test(beforestr)) {
										this.emit("thumbnail", file, "${pageContext.request.contextPath}/resources/img/dxf-icon.png");
									}
									if (/ngi$/.test(beforestr)) {
										this.emit("thumbnail", file, "${pageContext.request.contextPath}/resources/img/ngi-icon.png");
									}
									if (/nda$/.test(beforestr)) {
										this.emit("thumbnail", file, "${pageContext.request.contextPath}/resources/img/nda-icon.png");
									}
									if (/shp$/.test(beforestr)) {
										this.emit("thumbnail", file, "${pageContext.request.contextPath}/resources/img/shp-icon.png");
									}
									if (/zip$/.test(beforestr)) {
										this.emit("thumbnail", file, "${pageContext.request.contextPath}/resources/img/zip-icon.png");
									}
								}
							});

							this.on("reset", function() {
								$(".dz-message").show();
								if (!$("#UploadPanel").hasClass("BeforeDrop")) {
									$("#UploadPanel").addClass("BeforeDrop");
								}
								document.querySelector(".TotalProgress .progress-bar").style.width = "0%";
								$(".StartUp").removeAttr("disabled");
							});

							this.on("removedfile", function() {
								document.querySelector(".TotalProgress .progress-bar").style.width = "0%";
							});

							this.on("success", function(file) {
								gitrnd.alert("success", " " + file.name + '<spring:message code="lang.successRequestValidation" />');
							});

							this.on("error", function(file, errorMessage) {
								gitrnd.alert("danger", " " + errorMessage.message);
							});

							this.on("queuecomplete", function() {
								this.removeAllFiles(true);
								$(".StartUp").removeAttr("disabled");
								document.querySelector(".TotalProgress .progress-bar").style.width = "0%";
							});

							this.on("sending", function(file, xhr, formData) {
								$(".StartUp").attr("disabled", "disabled");
								var qaVer = $(':radio[name="qaver"]:checked').val();
								if (typeof qaVer !== "string") {
									console.error("not found qaVer");
									return;
								}
								formData.append("qaver", qaVer); // Append all the additional input data of your form here!

								var qaType = $("#qatype").val();
								if (typeof qaType !== "string") {
									console.error("not found qatype");
									return;
								}
								formData.append("qatype", qaType); // Append all the additional input data of your form here!

								var pid = $("#quicksetting").find("option:selected").val();
								if (typeof qaType !== "string") {
									console.error("not found qatype");
									return;
								}
								formData.append("pid", pid); // Append all the additional input data of your form here!

								var fileFormat = $("#suboption1").find("option:selected").val();
								if (typeof fileFormat !== "string") {
									console.error("not found file format");
									return;
								}
								formData.append("fileformat", fileFormat);

								var crs = $("#suboption2").find("option:selected").val();
								if (typeof crs !== "string") {
									console.error("not found CRS");
									return;
								}
								formData.append("crs", crs);
							});

							this.on("totaluploadprogress", function(progress) {
								document.querySelector(".TotalProgress .progress-bar").style.width = progress + "%";
							});

						}
					});

					$(".ClearList").click(function() {
						dropzone.removeAllFiles(true);
					});

					$(".StartUp").click(function() {
						dropzone.processQueue();
					});

				});

		var gitrnd = {
			alert : function(type, message) {
				var alert = "alert-";
				switch (type) {
				case "success":
					alert = alert + "success";
					break;
				case "info":
					alert = alert + "info";
					break;
				case "warning":
					alert = alert + "warning";
					break;
				case "danger":
					alert = alert + "danger";
					break;
				default:
					alert = alert + "info";
					break;
				}
				var span = $("<span>").attr("aria-hidden", "true").html("&times;");
				var xbtn = $("<button>").addClass("close").attr("type", "button").attr("data-dismiss", "alert").attr("aria-label", "Close")
						.append(span);
				var head = $("<strong>").text("알림");
				var div = $("<div>").addClass("alert").addClass(alert).addClass("alert-dismissible").attr("role", "alert").append(xbtn)
						.append(head).append(message);
				$(".Message").append(div);
			}
		};
	</script>

	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

		<div class="panel panel-default">
			<div class="panel-body">
				<section class="SettingSection QA-User-Setting" title="some tips">
					<div class="row">
						<div class="col-md-2 text-center">
							<label for="quicksetting" class="control-label" style="margin-top: 5px;"><spring:message code="lang.userSetting" /></label>
						</div>
						<div class="col-md-10">
							<select id="quicksetting" class="form-control">
								<option value="nonset"><spring:message code="lang.notSet" /></option>
								<c:forEach var="item" items="${presets}" varStatus="status">
									<option value="${item.pid}" data-support="${item.support}">${item.name}</option>
								</c:forEach>
							</select>
						</div>
						<!-- 						<div class="col-md-2">
							<button class="btn btn-default" style="width: 100%;">추가</button>
						</div> -->
					</div>
				</section>
				<section class="SettingSection QA-type-sect" title="some tips">
					<div class="row">
						<div class="col-md-4">
							<select id="sel1" class="QATypeSelect">
								<option value="nm1" disabled="disabled"><spring:message code="lang.digitalMap" /> 1:1000</option>
								<option value="nm5" selected><spring:message code="lang.digitalMap" /> 1:5000</option>
								<option value="nm25" disabled="disabled"><spring:message code="lang.digitalMap" /> 1:25000</option>
							</select> <label for="sel1" class="QATypeSelect"><div class="QATypeSelect" style="padding: 2px;"><spring:message code="lang.digitalMap" /></div>
								<ul style="display: none; list-style-type: none; padding: 0; margin: 0;" class="text-center"></ul> </label>
						</div>
						<div class="col-md-4">
							<select id="sel2" class="QATypeSelect">
								<option value="ug1" disabled="disabled"><spring:message code="lang.underFacility" /> 1:1000</option>
								<option value="ug5"><spring:message code="lang.underFacility" /> 1:5000</option>
								<option value="ug25" disabled="disabled"><spring:message code="lang.underFacility" /> 1:25000</option>
							</select> <label for="sel2" class="QATypeSelect"><div class="QATypeSelect" style="padding: 2px;"><spring:message code="lang.underFacility" /></div>
								<ul style="display: none; list-style-type: none; padding: 0; margin: 0;" class="text-center"></ul> </label>
						</div>
						<div class="col-md-4">
							<select id="sel3" class="QATypeSelect">
								<option value="fr1" disabled="disabled"><spring:message code="lang.forestMap" /> 1:1000</option>
								<option value="fr5"><spring:message code="lang.forestMap" /> 1:5000</option>
								<option value="fr25" disabled="disabled"><spring:message code="lang.forestMap" /> 1:25000</option>
							</select> <label for="sel3" class="QATypeSelect"><div class="QATypeSelect" style="padding: 2px;"><spring:message code="lang.forestMap" /></div>
								<ul style="display: none; list-style-type: none; padding: 0; margin: 0;" class="text-center"></ul> </label>
						</div>
					</div>
				</section>
				<section class="SettingSection QA-detail-sect" title="some tips">
					<div class="row">
						<div class="col-md-6">
							<input type="radio" class="radio" name="qaver" id="qacat1" value="qa1" checked /> <label for="qacat1">
								<spring:message code="lang.exactPosition" /> </label>
						</div>
						<div class="col-md-6">
							<input type="radio" class="radio" name="qaver" id="qacat2" value="qa2" /> <label for="qacat2"><spring:message code="lang.structuring" /> </label>
						</div>
					</div>
					<input type="hidden" id="qatype" value="" />
				</section>
				<section class="SettingSection QA-sub-options" title="some tips">
					<div class="row">
						<div class="col-md-6">
							<select id="suboption1" class="form-control"></select>
						</div>
						<div class="col-md-6">
							<select id="suboption2" class="form-control">
								<option value="5186" selected>중부원점 투영좌표계(Central Belt 2010)</option>
								<option value="4737">GRS80 경위도 좌표계(Korean 2000)</option>
								<option value="4326">WGS84 경위도 좌표계(전지구 좌표계)</option>
								<option value="5185">서부원점 투영좌표계(West Belt 2010)</option>
								<option value="5187">동부원점 투영좌표계(East Belt 2010)</option>
								<option value="5188">동해원점 투영좌표계(East Sea Belt 2010)</option>
								<option value="32652">WGS84 UTM52N 투영좌표계</option>
							</select>
						</div>
					</div>
					<input type="hidden" id="qatype" value="" />

				</section>
				<section class="SettingSection QA-Upload-Area" title="some tips">
					<div class="well">
						<div class="row">
							<div class="col-md-2">
								<button class="btn btn-success FileBrowse" style="width: 100%">
									<i class="fas fa-search"></i> <spring:message code="lang.browse" />
								</button>
							</div>
							<div class="col-md-2">
								<button class="btn btn-warning ClearList" style="width: 100%">
									<i class="fas fa-trash-alt"></i> <spring:message code="lang.deleteAll" />
								</button>
							</div>
							<div class="col-md-8">
								<div class="progress TotalProgress" style="height: 34px; margin-bottom: 12px;">
									<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="0" aria-valuemin="0"
										aria-valuemax="100" style="width: 0%"></div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div id="UploadPanel" class="panel panel-default BeforeDrop" style="max-height: 348px; overflow-y: auto;">
									<div class="panel-body" style="padding: 0;">
										<div id="dropzone" class="DropArea FileBrowse">
											<div class="dz-message DefaultMessage" data-dz-message>
												<h3><spring:message code="lang.hintFileDragDrop" /></h3>
											</div>
											<div id="previewContainer" class="table">
												<div id="previewTemplate" class="file-row" style="cursor: default;">
													<div class="row">
														<div class="col-md-1 text-center">
															<span><img data-dz-thumbnail /></span>
														</div>
														<div class="col-md-8">
															<p data-dz-name></p>
															<strong class="error text-danger" data-dz-errormessage></strong>
														</div>
														<div class="col-md-2 text-center">
															<p data-dz-size></p>
															<div class="progress" style="margin-bottom: 0;">
																<div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
																	style="width: 0%" data-dz-uploadprogress></div>
															</div>
														</div>
														<div class="col-md-1 text-center">
															<button class="btn btn-danger" data-dz-remove style="margin-top: 10px;">
																<i class="fas fa-trash-alt"></i>
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</section>
				<section class="SettingSection">
					<div class="row">
						<div class="col-md-12">
							<button type="button" class="btn btn-primary StartUp" style="width: 100%"><spring:message code="lang.requestValidation" /></button>
						</div>
					</div>
				</section>
			</div>
		</div>
		<section class="SettingSection">
			<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
		</section>
		<div class="Message"></div>
	</div>
</body>
</html>
