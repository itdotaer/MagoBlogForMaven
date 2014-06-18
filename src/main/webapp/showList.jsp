<%@ page language="java" import="java.util.Vector,com.mago.bean.Classe,com.mago.base.SessionOper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String classPId = request.getParameter("pId");
	String classSubId = request.getParameter("subId");
	Vector<Classe> navVector = (Vector<Classe>)session.getAttribute("navVec");
	
	String classPStr = "";
	String classSubStr = "";
	if(classPId != null && classPId != ""){
		classPStr = SessionOper.queryClassNameByClassId(navVector, Integer.parseInt(classPId));
	}
	
	if(classSubId != null && classSubId != ""){
		classSubStr = SessionOper.queryClassNameByClassId(navVector, Integer.parseInt(classSubId));
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=classSubStr %></title>
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
		<jsp:include page="list.jsp"></jsp:include>
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
</html>