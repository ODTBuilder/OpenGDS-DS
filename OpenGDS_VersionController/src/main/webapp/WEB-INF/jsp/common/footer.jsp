<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div>
	<address>
		<img src="${pageContext.request.contextPath}/resources/img/git_new_logo.png" /> <strong style="font-size: 1.1em;">
			<spring:message code="lang.comName" />
		</strong><br>
		<spring:message code="lang.comaddr" />
		<br> <abbr title="Phone">P:</abbr> 82-31-622-3826
	</address>
</div>