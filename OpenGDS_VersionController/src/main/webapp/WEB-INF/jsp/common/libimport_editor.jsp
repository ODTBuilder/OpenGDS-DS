<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 제이쿼리 -->
<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-2.2.2.min.js"></script>
<!-- 부트스트랩 -->
<script src="${pageContext.request.contextPath}/resources/js/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/bootstrap/css/bootstrap.min.css">
<!-- 폰트어썸(아이콘) -->
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/fontawesome/css/fontawesome-all.min.css" /> --%>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/all.css"
	integrity="sha384-lKuwvrZot6UHsBSfcMvOkWwlCMgc0TaWr+30HWe3a4ltaBwTZhyTEggF5tJv8tbt" crossorigin="anonymous">

<c:choose>
	<c:when test="${browser == 'MSIE'}">
		<!-- 스윗얼럿 익스플로러 지원을 위한 코어js -->
		<script src='https://cdnjs.cloudflare.com/ajax/libs/core-js/2.5.5/core.min.js'></script>
	</c:when>
</c:choose>
<!-- 스윗얼럿(알림) -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/sweetalert2/sweetalert2.css">
<script src="${pageContext.request.contextPath}/resources/js/sweetalert2/sweetalert2.js"></script>
<!-- 드롭존(파일업로드) -->
<script src="${pageContext.request.contextPath}/resources/js/dropzone/dropzone.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/dropzone/basic.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/dropzone/dropzone.css">
<!-- 다운로드 js-->
<script src="${pageContext.request.contextPath}/resources/js/download/download.js"></script>
<!-- proj4js -->
<script src="${pageContext.request.contextPath}/resources/js/proj4js/dist/proj4-src.js"></script>
<%-- 오픈 레이어스3 --%>
<script src="${pageContext.request.contextPath}/resources/js/ol3/ol-debug.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/ol3/ol.css">
<%-- jsTree--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jsTree/jstree.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/jsTree/themes/default/style.css" />
<%-- jsTree geoserver plugin--%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-geoserver/jstree-geoserver.js"></script>
<%-- jsTree geogig plugin--%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-geoserver/jstree-geogigfunction.js"></script>
<%-- jsTree openlayers3--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/jstree.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/themes/default/style.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/jstree-visibility.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/jstree-layerproperties.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/jstree-legends.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/jsTree-openlayers3/jstree-functionmarker.js"></script>
<%-- 데이터 테이블 --%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/datatables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/datatables/js/button/dataTables.buttons.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/datatables/js/select/dataTables.select.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/datatables/js/responsive/dataTables.responsive.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/datatables/js/dataTables.altEditor.free.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/datatables/css/jquery.dataTables.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/datatables/css/button/buttons.dataTables.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/datatables/css/select/select.dataTables.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/js/datatables/css/responsive/responsive.dataTables.css" />

<!-- shp2geojson -->
<script src="${pageContext.request.contextPath}/resources/js/shp2geojson/preview.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/shp2geojson/preprocess.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/shp2geojson/jszip.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/shp2geojson/jszip-utils.js"></script>
<!-- gb CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/gb/css/gb.css">
<!-- gb namespace -->
<script src="${pageContext.request.contextPath}/resources/js/gb/gb_debug.js"></script>
<!-- gb map -->
<script src="${pageContext.request.contextPath}/resources/js/gb/map/map.js"></script>
<!-- gb.modal -->
<script src="${pageContext.request.contextPath}/resources/js/gb/modal/base.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/modal/modifylayerprop.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/validation/validation.js"></script>
<!-- gb panel  base -->
<script src="${pageContext.request.contextPath}/resources/js/gb/panel/base.js"></script>
<!-- gb basemap -->
<script src="${pageContext.request.contextPath}/resources/js/gb/style/basemap.js"></script>
<!-- gb layerstyle -->
<script src="${pageContext.request.contextPath}/resources/js/gb/style/layerstyle.js"></script>
<!-- gb layerstyle -->
<script src="${pageContext.request.contextPath}/resources/js/spectrum/spectrum.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/js/spectrum/spectrum.css" />
<!-- gb.crs.BaseCRS -->
<script src="${pageContext.request.contextPath}/resources/js/gb/crs/basecrs.js"></script>
<!-- gb.tree.geoserver -->
<script src="${pageContext.request.contextPath}/resources/js/gb/tree/geoserver.js"></script>
<!-- gb.tree.openlayers -->
<script src="${pageContext.request.contextPath}/resources/js/gb/tree/openlayers.js"></script>
<!-- gb.versioning.Repository  -->
<script src="${pageContext.request.contextPath}/resources/js/gb/versioning/repository.js"></script>
<!-- gb.versioning.Feature -->
<script src="${pageContext.request.contextPath}/resources/js/gb/versioning/feature.js"></script>
<!-- gb.edit -->
<script src="${pageContext.request.contextPath}/resources/js/gb/edit/edithistory.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/edit/undo.js"></script>
<!-- gb.footer -->
<script src="${pageContext.request.contextPath}/resources/js/gb/footer/base.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/footer/featureList.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/footer/command.js"></script>
<!-- gb.header -->
<script src="${pageContext.request.contextPath}/resources/js/gb/header/base.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/header/editingtool.js"></script>
<!-- gb.interaction -->
<script src="${pageContext.request.contextPath}/resources/js/gb/overriding/olinteractiondraw.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/interaction/multitransform.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/interaction/copy-paste.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/interaction/measuretip.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/interaction/holedraw.js"></script>
<!-- gb.geocoder -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/js/gb/css/ol-geocoder.css">
<script src="${pageContext.request.contextPath}/resources/js/gb/geocoder/ol-geocoder.js"></script>
<!-- gb.map.MousePosition -->
<script src="${pageContext.request.contextPath}/resources/js/gb/map/mouseposition.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/geocoder/ol-geocoder.js"></script>
<!-- gb.geoserver.ImportSHP-->
<script src="${pageContext.request.contextPath}/resources/js/gb/geoserver/uploadshp.js"></script>
<!-- gb.layer-->
<script src="${pageContext.request.contextPath}/resources/js/gb/layer/navigator.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/gb/layer/imageLayer.js"></script>
