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
<div class="heading-top">当前位置：
	<a href="<%=basePath %>" title="返回首页">首页</a> &gt; 
	<a href="<%=basePath %>#" title=""><%=classPStr %></a> &gt; <%=classSubStr %>
</div>