<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>GeoDT Online</title>
<jsp:include page="/WEB-INF/jsp/common/libimport.jsp" />
<script>
	function checkPassword(input) {
		if (input.value != document.getElementById('password1').value) {
			input.setCustomValidity('<spring:message code="lang.passwordNotMatch" />');
		} else {
			// input is valid -- reset the error message
			input.setCustomValidity('');
		}
	}

	function checkId(input) {
		$.ajax({
			"url" : "${pageContext.request.contextPath}/user/idcheck.ajax",
			"data" : {
				"id" : input.value
			},
			"success" : function(data, textStatus, jqXHR) {
				if (data) {
					var idInput = $("#userId")[0];
					idInput.setCustomValidity('');
				} else {
					var idInput = $("#userId")[0];
					idInput.setCustomValidity('<spring:message code="lang.idInUse" />');
				}
			}
		});
	}

	function checkEmail(input) {
		$.ajax({
			"url" : "${pageContext.request.contextPath}/user/emailcheck.ajax",
			"data" : {
				"email" : input.value
			},
			"success" : function(data, textStatus, jqXHR) {
				if (data) {
					var idInput = $("#userEmail")[0];
					idInput.setCustomValidity('');
				} else {
					var idInput = $("#userEmail")[0];
					idInput.setCustomValidity('<spring:message code="lang.mailInUse" />');
				}
			}
		});
	}

	function switchJoinForm(view) {
		if (view === "join") {
			$('#loginbox').hide();
			$('#signupbox').show();
			$("#signupform").find(".join-form").val("");
		} else if (view === "login") {
			$('#signupbox').hide();
			$('#loginbox').show();
		}
	}
	$(document).ready(function() {
		var idInput = $("#userId")[0];
		idInput.setCustomValidity('<spring:message code="lang.idCheck" />');

		var emailInput = $("#userEmail")[0];
		emailInput.setCustomValidity('<spring:message code="lang.mailCheck" />');
	});
</script>
</head>
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
		<div>
			<section>
				<div id="loginbox" style="margin-top: 50px; margin-bottom: 30px;"
					class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
					<div class="panel panel-info">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lang.signin" />
							</div>
							<!-- <div style="float: right; font-size: 80%; position: relative; top: -10px">
								<a href="#">Forgot password?</a>
							</div> -->
						</div>

						<div style="padding-top: 30px" class="panel-body">

							<div style="display: none" id="login-alert" class="alert alert-danger col-sm-12"></div>

							<form id="loginform" class="form-horizontal" role="form"
								action="${pageContext.request.contextPath}/user/signinProcess.do" method="post">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> <input id="login-username"
										type="text" class="form-control" name="username" value="" placeholder="User ID">
								</div>

								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span> <input id="login-password"
										type="password" class="form-control" name="password" placeholder="Password">
								</div>

								<!-- <div class="input-group">
									<div class="checkbox">
										<label> <input id="login-remember" type="checkbox" name="remember" value="1"> Remember me
										</label>
									</div>
								</div> -->


								<div style="margin-top: 10px" class="form-group">
									<!-- Button -->
									<div class="col-sm-12 controls">
										<!-- 										<a id="btn-login" href="#" class="btn btn-success">Login </a> -->
										<button type="submit" class="btn btn-success">
											<spring:message code="lang.signin" />
										</button>
									</div>
								</div>


								<div class="form-group">
									<div class="col-md-12 control">
										<div style="border-top: 1px solid #888; padding-top: 15px; font-size: 85%">
											<spring:message code="lang.noAccount" />
											<a href="#" onClick="switchJoinForm('join')"> <spring:message code="lang.join" />
											</a>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</section>
			<section>
				<div id="signupbox" style="display: none; margin-top: 50px"
					class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
					<div class="panel panel-info">
						<div class="panel-heading">
							<div class="panel-title">
								<spring:message code="lang.signup" />
							</div>
							<div style="float: right; font-size: 85%; position: relative; top: -10px">
								<a id="signinlink" href="#" onclick="switchJoinForm('login')"><spring:message code="lang.signin" /></a>
							</div>
						</div>
						<div class="panel-body">
							<form id="signupform" class="form-horizontal" role="form"
								action="${pageContext.request.contextPath}/user/signup.do" method="post">
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<div id="signupalert" style="display: none" class="alert alert-danger">
									<p>Error:</p>
									<span></span>
								</div>
								<div class="form-group">
									<label for="userid" class="col-md-3 control-label"><spring:message code="lang.userId" /></label>
									<div class="col-md-9">
										<input type="text" id="userId" class="form-control join-form" name="userid" placeholder=""
											oninput="checkId(this)" required>
									</div>
								</div>
								<div class="form-group">
									<label for="email" class="col-md-3 control-label"><spring:message code="lang.email" /></label>
									<div class="col-md-9">
										<input type="email" id="userEmail" class="form-control join-form" name="email" placeholder=""
											oninput="checkEmail(this)" required>
									</div>
								</div>
								<div class="form-group">
									<label for="firstname" class="col-md-3 control-label"><spring:message code="lang.fname" /></label>
									<div class="col-md-9">
										<input type="text" class="form-control join-form" name="firstname" placeholder="" required>
									</div>
								</div>
								<div class="form-group">
									<label for="lastname" class="col-md-3 control-label"><spring:message code="lang.lname" /></label>
									<div class="col-md-9">
										<input type="text" class="form-control join-form" name="lastname" placeholder="" required>
									</div>
								</div>
								<div class="form-group">
									<label for="password" class="col-md-3 control-label"><spring:message code="lang.pass1" /></label>
									<div class="col-md-9">
										<input type="password" id="password1" class="form-control join-form" name="password" placeholder="" required>
									</div>
								</div>

								<div class="form-group">
									<label for="passwordcheck" class="col-md-3 control-label"><spring:message code="lang.pass2" /></label>
									<div class="col-md-9">
										<input type="password" id="password2" class="form-control join-form" name="passwordcheck" placeholder=""
											required oninput="checkPassword(this)">
									</div>
								</div>
								<div class="form-group">
									<!-- Button -->
									<div style="width: 84px; margin: 0 auto;">
										<button id="btn-signup" type="submit" class="btn btn-info">
											<i class="icon-hand-right"></i>
											<spring:message code="lang.signupReq" />
										</button>
									</div>
								</div>
								<div style="border-top: 1px solid #999; padding-top: 20px" class="form-group">
									<!-- 									<div class="form-group"> -->
									<!-- Button -->
									<!-- 									<div class="col-sm-12"> -->
									<!-- 										<button id="btn-signup" type="button" class="btn btn-info"> -->
									<!-- 											<i class="icon-hand-right"></i> &nbsp Sign Up -->
									<!-- 										</button> -->
									<!-- 									</div> -->
									<!-- 									</div> -->
								</div>
							</form>
						</div>
					</div>
				</div>
			</section>
		</div>
	</div>
</body>
</html>
