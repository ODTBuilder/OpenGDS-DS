<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<nav class="navbar navbar-default mainHeader">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/main.do"><img
				style="height: 20px; width: auto; display: inline; margin-right: 10px; padding: 0;"
				src="${pageContext.request.contextPath}/resources/img/onlyglobe.png" alt="GeoDT Online">GeoDT Online</a>
		</div>
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="navbar-collapse-1">
			<ul class="nav navbar-nav navbar-left">
				<li><a href="${pageContext.request.contextPath}/ko/locale.do"
					style="display: inline-block; padding-right: 5px;">한국어</a>|<a
					href="${pageContext.request.contextPath}/en/locale.do" style="display: inline-block; padding-left: 5px;">English</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="${pageContext.request.contextPath}/map.do"> <i class="fas fa-edit fa-lg"
						style="color: #344762;"></i> <spring:message code="lang.editor" /></a></li>
				<li><a href="${pageContext.request.contextPath}/validation.do"> <i class="fas fa-clipboard-check fa-lg"
						style="color: #344762;"></i> <spring:message code="lang.validation" />
				</a></li>
				<li><a href="${pageContext.request.contextPath}/list.do"> <i class="fas fa-clipboard-list fa-lg"
						style="color: #344762;"></i> <spring:message code="lang.result" />
				</a></li>
				<li><a href="${pageContext.request.contextPath}/settinglist.do"> <i class="fas fa-cog fa-lg"
						style="color: #344762;"></i> <spring:message code="lang.setting" />
				</a></li>
				<c:choose>
					<c:when test="${username ne null}">
						<li><a href="${pageContext.request.contextPath}/user/userinfo.do"><i class="fas fa-user-circle"></i>&nbsp;${fname}&nbsp;${lname}</a></li>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${username eq null}">
						<li><a href="${pageContext.request.contextPath}/user/signin.do"><span class="glyphicon glyphicon-log-in"></span>
								<spring:message code="lang.signin" /></a></li>
					</c:when>
					<c:when test="${username ne null}">
						<li><a href="${pageContext.request.contextPath}/user/signout.do"><span
								class="glyphicon glyphicon-log-out"></span> <spring:message code="lang.signout" /></a></li>
					</c:when>
				</c:choose>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>