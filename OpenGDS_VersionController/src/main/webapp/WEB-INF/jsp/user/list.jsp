<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>GeoDT Online</title>
<jsp:include page="/WEB-INF/jsp/common/libimport.jsp" />
<style>
</style>
</head>
<body>
	<script>
		var detail = new gb.modal.DetailInformation({
			"target" : ".gb-detailinformation-btn"
		});

		$(document).on("click", ".gb-detailinformation-btn", function(e) {
			e.stopPropagation();
			e.preventDefault();
		});

		$(document).on("click", ".gb-download-btn", function(e) {
			e.stopPropagation();
		});

		$(document).on("click", "#serverDataTable tbody tr", function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
			} else {
				$(this).addClass('selected');
			}
		});

		$(document).on("click", "#select-delete", function() {
			swal({
				title : "<spring:message code="lang.delitem" />",
				type : "info",
				showCancelButton : true,
				confirmButtonColor : "#3085d6",
				confirmButtonText : "<spring:message code="lang.confirm" />",
				cancelButtonColor : "#d33",
				cancelButtonText : "<spring:message code="lang.cancel" />"
			}).then(function(result) {

				// 확인버튼 클릭시 아래의 구문 실행
				if (result.value) {
					var selectList = "";
					var fileNameList = "";

					// 선택된 행들에 대하여 반복문 실행
					$("#serverDataTable tbody tr.selected").each(function(index) {

						if ($(this).data("state") === 1 || $(this).data("state") === 2) {

							alert("<spring:message code="lang.delfail" />");

							// 삭제 항목 초기화
							selectList = "";
							fileNameList = "";

							// each 구문 탈출
							return false;
						}
						// 변수에 병합
						selectList += $(this).data("value") + " ";
						fileNameList += $(this).data("filename") + " ";
					});

					// trim 함수 실행 후 white space를 기준으로 분리. 배열값 도출
					var data = selectList.trim().split(" ");
					var file = fileNameList.trim().split(" ");

					// 선택한 행이 없을 시 리턴
					if (selectList === "") {
						return;
					}

					$.post("${pageContext.request.contextPath}/deleteList.ajax?${_csrf.parameterName}=${_csrf.token}", {
						list : data.join(","), // 분리된 데이터들을 ','를 사이에 두고 병합. String값 도출
						file : file.join(",")
					}, function(data, status) {
						// data<Boolean>. true: 성공, false: 실패
						if (data) {
							// page reload
							window.location.href = "${pageContext.request.contextPath}/list.do";
						} else {
							alert("Delete Fail");
						}
					});
				}
			});
		});

		$(document).on("click", "#all-select", function() {
			$("#serverDataTable tbody tr").addClass("selected");
		});

		$(document).on("click", "#all-deselect", function() {
			$("#serverDataTable tbody tr").removeClass("selected");
		});

		$(document).ready(function() {
			$("#serverDataTable").DataTable({
				"serverSide" : true,
				"searching" : false,
				"processing" : true,
				"ajax" : {
					"url" : "${pageContext.request.contextPath}/result/getValidationResult.ajax?${_csrf.parameterName}=${_csrf.token}",
					"data" : function(d) {
						console.log(d);
					},
					"error" : function(e) {
						console.log(e);
					}
				},
				"order" : [ 0, "asc" ],
				"select" : true,
				"displayStart" : 0,
				"pageLength" : 10,
				"lengthMenu" : [ [ 5, 10, 25, 50 ], [ 5, 10, 25, 50 ] ],
				"columns" : [ {
					"data" : "no",
					"title" : "No."
				}, {
					"data" : "zipName",
					"title" : "Original file"
				}, {
					"data" : "createTime",
					"title" : "Create time"
				}, {
					"data" : "endTime",
					"title" : "End time"
				}, {
					"data" : "qaType",
					"title" : "Validation type"
				}, {
					"data" : "format",
					"title" : "File format"
				}, {
					"data" : "state",
					"title" : "Status"
				}, {
					"data" : "download",
					"title" : "Download"
				}, {
					"data" : "comment",
					"title" : "Notes"
				} ]
			});
		});
	</script>
	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
		<div class="panel panel-default">
			<div class="panel-body">
				<section class="SettingSection">
					<div class="row">
						<div class="col-md-3">
							<div class="btn-group btn-group-justified" role="group" aria-label="..."
								style="margin-top: 15px; margin-bottom: 10px;">
								<div class="btn-group" role="group">
									<button id="all-deselect" class="btn btn-default">
										<i class="far fa-square"></i>&nbsp;
										<spring:message code="lang.deselall" />
									</button>
								</div>
								<div class="btn-group" role="group">
									<button id="all-select" class="btn btn-info">
										<i class="far fa-check-square"></i>&nbsp;
										<spring:message code="lang.selall" />
									</button>
								</div>
							</div>
						</div>
						<div class="col-md-2 col-md-offset-7">
							<button id="select-delete" class="btn btn-danger" style="margin-top: 15px; margin-bottom: 10px; width: 100%;">
								<i class="far fa-trash-alt"></i>&nbsp;
								<spring:message code="lang.delsel" />
							</button>
						</div>
					</div>
				</section>
				<section class="SettingSection">
					<table class="table table-bordered" id="serverDataTable" width="100%" cellspacing="0" style="text-align: center">
					</table>
				</section>
			</div>
		</div>
		<section>
			<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
		</section>
	</div>
</body>
</html>
