<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>My blog</title> 
    <jsp:include page="head.jsp"></jsp:include>
</head>
<body>
<!-- Navigater-->
<jsp:include page="navigate.jsp"></jsp:include>
<!-- /Navigater -->

<!-- Notification -->
<jsp:include page="header.jsp"></jsp:include>
<!-- /Notification -->

<!-- Main Container -->
<div id="body-wrapper" class="body-wraper-path">
	<!-- Content -->
	<div id="content" class="container clearfix">
		<!-- Slider -->
		<jsp:include page="ads.jsp"></jsp:include>
		<!-- /Slider -->
		<!-- MainServices -->
		<jsp:include page="mainService.jsp"></jsp:include>
		<!-- /MainServices -->
		<!-- ProjectCarousel -->
		<jsp:include page="projectCarousel.jsp"></jsp:include>
		<!-- /ProjectCarousel -->
		<!-- BlogCarousel -->
		<jsp:include page="blogCarousel.jsp"></jsp:include>
		<!-- /BlogCarousel -->
	</div>
	<!-- /Content -->
</div>
<!-- /Main Container -->

<!-- Footer -->
<jsp:include page="footer.jsp"></jsp:include>
<!-- /Footer -->

<!-- To Top -->
<div class="backToTop" title="" style="display: block;"></div>
<!-- /To Top -->
</body>
</html>