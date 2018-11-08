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

.SettingSection {
	margin-top: 10px;
	margin-bottom: 10px;
}

.Message {
	position: fixed;
	top: 0;
	left: 0;
	/* 	width: 100%; */
}

.row {
	margin-top: 5px;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<script>
	var locale = '<spring:message code="lang.localeCode" />';
	var pid = undefined;
	var title = undefined;
	var name = undefined;
	var layer = undefined;
	var option = undefined;		
	<c:if test="${pid ne null}">
	pid=${pid};
	</c:if>
	<c:if test="${title ne null}">
	title="${title}";
	</c:if>
	<c:if test="${name ne null}">
	name="${name}";
	</c:if>
	<c:if test="${layer ne null}">
	layer=${layer};
	</c:if>
	<c:if test="${option ne null}">
	option =${option};
	</c:if>
		$(document).ready(function() {

			var token = "${_csrf.token}";
			var header = "${_csrf.parameterName}";
			$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
			});

			$(".QA-Type-Sect").tooltip({
				"html" : true,
				"placement" : "right"
			});
			
			$(".QA-detail-sect").tooltip({
				"html" : true,
				"placement" : "right"
			});
			
			$(".MoveToStep1").click(function() {
				$(".Step1").show();
				$(".Step2").hide();
				$(".Step3").hide();
				$(".Step4").hide();
			});
			$(".MoveToStep2").click(function() {
				$(".Step1").hide();
				$(".Step2").show();
				$(".Step3").hide();
				$(".Step4").hide();

				tempLayerDef = JSON.stringify(layerDef.getStructure());
			});
			$(".MoveToStep3").click(function() {
				$(".Step1").hide();
				$(".Step2").hide();
				$(".Step3").show();
				$(".Step4").hide();

				if (tempLayerDef != JSON.stringify(layerDef.getStructure())) {
					optionDef.clearStructure();
					gitrnd.alert("warning", " <spring:message code="lang.alertValidateItemReset" />");
				}
				optionDef.init();
			});
			$(".MoveToStep4").click(function() {
				$(".Step1").hide();
				$(".Step2").hide();
				$(".Step3").hide();
				$(".Step4").show();

				tempLayerDef = JSON.stringify(layerDef.getStructure());
			});

			$("#btnAddCategory").click(function() {
				layerDef.addCategory();
			});

			$("#btnGetLayerJSON").click(function() {
				layerDef.getJSONFile();
			});

			$("#btnInitOption").click(function() {
				optionDef.init();
			});

			$("#btnGetOptionJSON").click(function() {
				optionDef.getJSONFile();
			});

			$("#save-def").click(function() {
				var data = new FormData();

				data.append("category", $("input[type=radio][name=qacat2]:checked").val());
				data.append("version", $("input[type=radio][name=qacat1]:checked").val());

				data.append("name", $("#def-name").val());
				data.append("layer", JSON.stringify(layerDef.getStructure()));
				data.append("option", JSON.stringify(optionDef.getStructure()));

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "${pageContext.request.contextPath}/option/createpreset.ajax?${_csrf.parameterName}=${_csrf.token}");
				xhr.onload = function() {
					console.log('DONE', xhr.readyState); // readyState will be 4
					window.location = "${pageContext.request.contextPath}/settinglist.do";
				};
				xhr.send(data);
			});
			
			$("#update-def").click(function() {
				var data = new FormData();

				data.append("category", $("input[type=radio][name=qacat2]:checked").val());
				data.append("version", $("input[type=radio][name=qacat1]:checked").val());
				data.append("pid", pid);
				data.append("name", $("#def-name").val());
				data.append("layer", JSON.stringify(layerDef.getStructure()));
				data.append("option", JSON.stringify(optionDef.getStructure()));

				var xhr = new XMLHttpRequest();
				xhr.open("POST", "${pageContext.request.contextPath}/option/updatePreset.ajax?${_csrf.parameterName}=${_csrf.token}");
				xhr.onload = function() {
					console.log('DONE', xhr.readyState); // readyState will be 4
					window.location = "${pageContext.request.contextPath}/settinglist.do";
				};
				xhr.send(data);
			});

			$("input[type=radio][name=qacat2]").change(function() {
				if (this.value === "forest") {
					$(".VersionArea").hide();
				} else if (this.value === "numetrical" || this.value === "underground") {
					$(".VersionArea").show();
				}
				var ldef = layerDef.getStructure();
				if (ldef.length > 0) {
					layerDef.clearStructure();
					layerDef.updateStructure();
					gitrnd.alert("warning", " <spring:message code="lang.initLaOptionChng" />");
				}
				optionDef.setQACategory(this.value);
			});

			$("input[type=radio][name=qacat1]").change(function() {
				if (this.value === "qa1" || this.value === "qa2") {
					var ldef = layerDef.getStructure();
					if (ldef.length > 0) {
						layerDef.clearStructure();
						layerDef.updateStructure();
						gitrnd.alert("warning", " <spring:message code="lang.initLaOptionChng" />");
					}
					optionDef.setQAVersion(this.value);
				}
			});

			var tempLayerDef = "";

			var layerDef = new gb.validation.LayerDefinition({
				"append" : ".LayerDefinitionArea",
				"fileClass" : "layerSetting",
				"msgClass" : "Message",
				"locale" : locale
			});

			var optionDef = new gb.validation.OptionDefinition({
				"layerDefinition" : layerDef,
				"append" : ".OptionDefinitionArea",
				"fileClass" : "optionSetting",
				"msgClass" : "Message",
				"locale" : locale
			});

			<c:choose>
		    <c:when test="${title eq 'Digital Map 1.0'}">
		    $("#qacat21").prop("checked", true);
		    $("#qacat21").trigger("change");
		    
		    $("#qacat11").prop("checked", true);
		    $("#qacat11").trigger("change");
		    </c:when>
		    <c:when test="${title eq 'Digital Map 2.0'}">
		    $("#qacat21").prop("checked", true);
		    $("#qacat21").trigger("change");
		    
		    $("#qacat12").prop("checked", true);
		    $("#qacat12").trigger("change");
		    </c:when>
		    <c:when test="${title eq 'Underground Map 1.0'}">
		    $("#qacat22").prop("checked", true);
		    $("#qacat22").trigger("change");
		    
		    $("#qacat11").prop("checked", true);
		    $("#qacat11").trigger("change");
		    </c:when>
		    <c:when test="${title eq 'Underground Map 2.0'}">
		    $("#qacat22").prop("checked", true);
		    $("#qacat22").trigger("change");
		    
		    $("#qacat12").prop("checked", true);
		    $("#qacat12").trigger("change");
		    </c:when>
		    <c:when test="${title eq 'Forest Map'}">
		    $("#qacat23").prop("checked", true);
		    $("#qacat23").trigger("change");
		    </c:when>
		    <c:otherwise>
		    $("#qacat21").prop("checked", true);
		    $("#qacat21").trigger("change");
		    
		    $("#qacat11").prop("checked", true);
		    $("#qacat11").trigger("change");
			</c:otherwise>
		</c:choose>
			
			console.log(optionDef.getQACategory());
			console.log(optionDef.getQAVersion());
			
			if (layer !== undefined) {
				layerDef.setStructure(layer);
				layerDef.updateStructure();
			}
			if (option !== undefined) {
				optionDef.setStructure(option);
				optionDef.updateStructure();
				optionDef.init();
			}
			
			$("#def-name").val(typeof name === "string" ? name : "");
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
				var head = $("<strong>").text("<spring:message code="lang.notice" />");
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
				<section class="SettingSection Step1">
					<div class="row text-right" style="margin-top: 10px; margin-bottom: 10px;">
						<div class="col-md-12">
							<button class="btn btn-default MoveToStep2">
								<spring:message code="lang.next" />
							</button>
						</div>
					</div>
					<h3>
						1.
						<spring:message code="lang.validationType" />
					</h3>
					<div class="row QA-Type-Sect" style="margin-bottom: 20px;" title="some tips">
						<div class="col-md-4">
							<input type="radio" class="radio" name="qacat2" id="qacat21" value="numetrical" checked /> <label for="qacat21">
								<spring:message code="lang.digitalMap" />
							</label>
						</div>
						<div class="col-md-4">
							<input type="radio" class="radio" name="qacat2" id="qacat22" value="underground" /> <label for="qacat22"><spring:message
									code="lang.underFacility" /></label>
						</div>
						<div class="col-md-4">
							<input type="radio" class="radio" name="qacat2" id="qacat23" value="forest" /> <label for="qacat23"><spring:message
									code="lang.forestMap" /> </label>
						</div>
					</div>
					<div class="row VersionArea QA-detail-sect" title="some tips">
						<div class="col-md-6">
							<input type="radio" class="radio" name="qacat1" id="qacat11" value="qa1" checked /> <label for="qacat11">
								<spring:message code="lang.exactPosition" />
							</label>
						</div>
						<div class="col-md-6">
							<input type="radio" class="radio" name="qacat1" id="qacat12" value="qa2" /> <label for="qacat12"><spring:message
									code="lang.structuring" /> </label>
						</div>
					</div>
				</section>
				<section class="SettingSection Step2" style="display: none;">
					<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
						<div class="col-md-6 text-left">
							<button class="btn btn-default MoveToStep1">
								<spring:message code="lang.prev" />
							</button>
						</div>
						<div class="col-md-6 text-right">
							<button class="btn btn-default MoveToStep3">
								<spring:message code="lang.next" />
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<h3>
								2.
								<spring:message code="lang.layerDefinition" />
							</h3>
						</div>
						<div class="col-md-2">
							<!-- <button id="btnSetLayerJSON" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">설정
								파일 업로드</button> -->
							<label class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;"><spring:message
									code="lang.importConfig" /><input type="file" class="layerSetting" style="display: none;"> </label>
						</div>
						<div class="col-md-2">
							<button id="btnGetLayerJSON" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">
								<spring:message code="lang.exportConfig" />
							</button>
						</div>
						<div class="col-md-2">
							<button id="btnAddCategory" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">
								<spring:message code="lang.addCategory" />
							</button>
						</div>
					</div>
					<div class="LayerDefinitionArea"></div>
				</section>
				<section class="SettingSection Step3" style="display: none;">
					<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
						<div class="col-md-6 text-left">
							<button class="btn btn-default MoveToStep2">
								<spring:message code="lang.prev" />
							</button>
						</div>
						<div class="col-md-6 text-right">
							<button class="btn btn-default MoveToStep4">
								<spring:message code="lang.next" />
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-8">
							<h3>
								3.
								<spring:message code="lang.validateItemDefinition" />
							</h3>
						</div>
						<div class="col-md-2">
							<!-- <button id="btnSetLayerJSON" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">설정
								파일 업로드</button> -->
							<label class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;"><spring:message
									code="lang.importConfig" /><input type="file" class="optionSetting" style="display: none;" /> </label>
						</div>
						<div class="col-md-2">
							<button id="btnGetOptionJSON" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">
								<spring:message code="lang.exportConfig" />
							</button>
						</div>
						<!-- 						<div class="col-md-2">
							<button id="btnInitOption" class="btn btn-default" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">레이어
								정의 불러오기</button>
						</div> -->
					</div>
					<div class="OptionDefinitionArea"></div>
				</section>
				<section class="SettingSection Step4" style="display: none;">
					<div class="row text-right" style="margin-top: 10px; margin-bottom: 10px;">
						<div class="col-md-12">
							<button class="btn btn-default MoveToStep3">
								<spring:message code="lang.prev" />
							</button>
							<c:if test="${pid ne null}">
								<button id="update-def" class="btn btn-default">
									<spring:message code="lang.save" />
								</button>
							</c:if>
							<c:if test="${pid eq null}">
								<button id="save-def" class="btn btn-default">
									<spring:message code="lang.save" />
								</button>
							</c:if>
						</div>
					</div>
					<h3>
						4.
						<spring:message code="lang.saveConfig" />
					</h3>
					<div class="row">
						<div class="col-md-2">
							<spring:message code="lang.name" />
						</div>
						<div class="col-md-10">
							<input type="text" id="def-name" class="form-control">
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
