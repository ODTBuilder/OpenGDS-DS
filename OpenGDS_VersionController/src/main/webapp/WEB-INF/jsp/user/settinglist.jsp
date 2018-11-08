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

.SettingRow {
	cursor: pointer;
}
</style>
</head>
<body>
	<script>
		$(document).ready(function() {

			var token = "${_csrf.token}";
			var header = "${_csrf.parameterName}";

			$(document).on("click", ".SettingSection > table > tbody > tr", function() {
				$(this).toggleClass("info");
			});

			$(document).on("click", "#all-select", function() {
				$(".SettingSection > table > tbody > tr").addClass("info");
			});

			$(document).on("click", "#all-deselect", function() {
				$(".SettingSection > table > tbody > tr").removeClass("info");
			});

			$(document).on("click", "#select-delete", function() {
				swal({
					title : "<spring:message code="lang.delOptionQ" />",
					type : "info",
					showCancelButton : true,
					confirmButtonColor : "#3085d6",
					confirmButtonText : "<spring:message code="lang.confirm" />",
					cancelButtonColor : "#d33",
					cancelButtonText : "<spring:message code="lang.cancel" />"
				}).then(function(result) {
					if (result.value) {
						var selectList = "";

						// 선택된 행들에 대하여 반복문 실행
						$("tr.info").each(function(index) {
							var pid = $(this).find("a").attr("pid");

							// 변수에 병합
							selectList += pid + " ";
						});

						// trim 함수 실행 후 white space를 기준으로 분리. 배열값 도출
						var data = selectList.trim().split(" ");

						// 선택한 행이 없을 시 리턴
						if (selectList === "") {
							return;
						}

						$.post("${pageContext.request.contextPath}/option/deletePresets.ajax?${_csrf.parameterName}=${_csrf.token}", {
							pids : data.join(",")
						}, function(data, status) {
							// data<Boolean>. true: 성공, false: 실패
							if (data) {
								// page reload
								window.location.href = "${pageContext.request.contextPath}/settinglist.do";
							} else {
								alert("Delete Fail");
							}
						});
					}
				});
			});

			gitrnd.retrieveSettingList();
		});

		var gitrnd = {
			"alert" : function(type, message) {
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
			},
			"retrieveSettingList" : function() {
				$.ajax({
					url : "${pageContext.request.contextPath}/option/retrievePresetByUidx.ajax",
					method : "GET",
					success : function(data, textStatus, jqXHR) {
						console.log(data);
						for (var i = 0; i < data.length; i++) {
							var numTd1 = $("<td>").append(i + 1);
							var atag = $("<a>").attr({
								"href" : "${pageContext.request.contextPath}/setting.do?pid=" + data[i].pid,
								"pid" : data[i].pid
							}).css({
								"padding" : 0,
								"margin" : 0
							}).append(data[i].name);
							var settingTd1 = $("<td>").append(atag);
							var catTd1 = $("<td>").append(data[i].title);
							var setrow1 = $("<tr>").append(numTd1).append(settingTd1).append(catTd1);
							$("#settingListArea").append(setrow1);
						}
					}
				});
			},
			"deleteSetting" : function() {

			},
			"retrieveSetting" : function() {

			}
		};
	</script>

	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

		<div class="panel panel-default">
			<div class="panel-body">
				<section>
					<div class="row">
						<div class="col-md-5">
							<div class="btn-group btn-group-justified" role="group" aria-label="..."
								style="margin-top: 15px; margin-bottom: 10px;">
								<div class="btn-group" role="group">
									<a class="btn btn-default" href="${pageContext.request.contextPath}/setting.do"> <i
										class="far fa-plus-square"></i> <spring:message code="lang.newSetting" />
									</a>
								</div>
								<div class="btn-group" role="group">
									<button id="all-deselect" class="btn btn-default">
										<i class="far fa-square"></i>
										<spring:message code="lang.deselall" />
									</button>
								</div>
								<div class="btn-group" role="group">
									<button id="all-select" class="btn btn-info">
										<i class="far fa-check-square"></i>
										<spring:message code="lang.selall" />
									</button>
								</div>
							</div>
						</div>
						<div class="col-md-2 col-md-offset-5">
							<button id="select-delete" class="btn btn-danger" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">
								<i class="far fa-trash-alt"></i>
								<spring:message code="lang.delsel" />
							</button>
						</div>
					</div>
				</section>
				<section class="SettingSection">
					<table class="table table-striped table-hover text-center">
						<thead>
							<tr>
								<td><spring:message code="lang.no" /></td>
								<td><spring:message code="lang.settingTitle" /></td>
								<td><spring:message code="lang.settingCat" /></td>
							</tr>
						</thead>
						<tbody id="settingListArea">
						</tbody>
					</table>
					<!-- <div class="row SettingSection">
						<div class="col-md-12">
							<button class="btn btn-default" style="width: 100%;">더 보기</button>
						</div>
					</div> -->
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
