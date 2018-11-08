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
</style>
</head>
<body>
	<script>
		$(document).on("click", "#deactivateUser", function() {
			swal({
				title : "<spring:message code="lang.deactusermsg" />",
				type : "warning",
				showCancelButton : true,
				confirmButtonText : "<spring:message code="lang.confirm" />",
				cancelButtonText : "<spring:message code="lang.cancel" />",
				reverseButtons : true
			}).then(function(isConfirm) {
				if (isConfirm.value) {
					$.ajax({
						"url" : "${pageContext.request.contextPath}/user/deactivateuser.ajax",
						"success" : function(data, textStatus, jqXHR) {
							console.log(data);
							if (data) {
								swal({
									title : "<spring:message code="lang.deactusersuccmsg" />",
									type : "success",
									confirmButtonText : "<spring:message code="lang.confirm" />",
								}).then(function(confirm) {
									if (confirm.value) {
										location.href = "${pageContext.request.contextPath}/user/signout.do";
									}
								});
							} else {
								swal('<spring:message code="lang.fail" />', '<spring:message code="lang.deactuserfailmsg" />', 'error');
							}
						}
					});
				}
			});
		});
	</script>
	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
		<div class="panel panel-default">
			<div class="panel-body">
				<section class="SettingSection">
					<div class="row">
						<div class="col-md-4 col-md-offset-4">
							<div class="row">
								<div class="col-md-4 col-md-offset-2">
									<spring:message code="lang.userId" />
								</div>
								<div class="col-md-6">${username}</div>
							</div>
							<div class="row">
								<div class="col-md-4 col-md-offset-2">
									<spring:message code="lang.name" />
								</div>
								<div class="col-md-6">${fname}&nbsp;${lname}</div>
							</div>
							<div class="row">
								<div class="col-md-4 col-md-offset-2">
									<spring:message code="lang.email" />
								</div>
								<div class="col-md-6">${email}</div>
							</div>
							<div class="row">
								<div class="col-md-4 col-md-offset-2">
									<spring:message code="lang.valauth" />
								</div>
								<div class="col-md-6">${auth}</div>
							</div>
							<div class="col-md-4"></div>
						</div>
					</div>
					<div class="text-right" style="margin-top: 15px;">
						<button id="deactivateUser" class="btn btn-link">
							<spring:message code="lang.deactuser" />
						</button>
					</div>
				</section>
			</div>
		</div>
		<section>
			<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
		</section>
	</div>
</body>
</html>
