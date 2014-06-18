<%@ page language="java" import="java.util.Vector,com.mago.bean.Article,com.mago.base.SessionOper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Article article = (Article)session.getAttribute("article");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=article.getArticleName() %></title>
<jsp:include page="head.jsp"></jsp:include>
</head>
<body>
<!-- Navigate -->
<jsp:include page="navigate.jsp"></jsp:include>
<!-- /Navigate -->

<!-- NavHeader -->
<jsp:include page="showHeaderBar.jsp"></jsp:include>
<!-- /NavHeader -->

<!-- MainContainer -->
<div id="body-wrapper">
	<!-- Content -->
	<div class="clearfix">
		<!-- Main Content -->
		<jsp:include page="detail.jsp"></jsp:include>
		<!-- /Main Content -->
		<!-- Sidebar -->
		<jsp:include page="showLeft.jsp"></jsp:include>
		<!-- /Sidebar -->
	</div>
	<!-- /Content -->	
</div>
<!-- /MainContainer -->

<!-- Footer -->
<jsp:include page="footer.jsp"></jsp:include>
<!-- /Footer -->
<div class="backToTop" title="" style="display: block;"></div>
</body>
