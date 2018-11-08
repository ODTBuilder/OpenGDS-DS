<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
.error-template {
	padding: 40px 15px;
	text-align: center;
}

.error-actions {
	margin-top: 15px;
	margin-bottom: 15px;
}

.error-actions .btn {
	margin-right: 10px;
}
</style>
</head>
<body>
	<div class="container">
		<div>
			<section>
				<div class="well">
					<div class="row">
						<div class="col-md-12">
							<div class="error-template">
								<h2>${errorcode}</h2>
								<div class="error-details">${errormessage}</div>
								<div class="error-actions">
									<a href="${pageContext.request.contextPath}/main.do" class="btn btn-primary btn-lg"><span
										class="glyphicon glyphicon-home"></span> 홈으로 가기</a><a href="mailto:monkey1221@git.co.kr"
										class="btn btn-default btn-lg"><span class="glyphicon glyphicon-envelope"></span> 메일 보내기</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
			<section>
				<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
			</section>
		</div>
	</div>
</body>
</html>
