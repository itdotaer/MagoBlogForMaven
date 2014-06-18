<%@ page language="java" import="java.util.Vector,com.mago.bean.PicList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get Ad list Session -->
<jsp:include page="GetAdAction.do"></jsp:include>
<!-- /Get Ad list Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	Vector<PicList> adVector = (Vector<PicList>)session.getAttribute("adVec");
	PicList bean = null;
%>

<div id="main-slider" class="flexslider">
	<ul class="slides">
<%
	for(int i = 0; i < adVector.size(); i++){
		bean = adVector.elementAt(i);
%>
		<li class="" style="width: 100%; float: left; margin-right: -100%; position: relative; display: none;">
			<img src="<%=basePath + bean.getFilePath() %>" alt="<%=bean.getArticleName() %>">
			<div class="flex-caption">
				<h3><%=bean.getArticleName() %></h3> 
				<p><%=bean.getArticleDescription() %></p>
				<input type="button" value="查看详情" class="red" onclick="location.href='<%=basePath + "/GetArticleAction.do?articleId=" + bean.getArticleId() %>'">
			</div>
		</li>
<%
	}
%>
	</ul>
	<!-- 
	<ol class="flex-control-nav flex-control-paging">
<%
	for(int j = 1; j <= adVector.size(); j++){
		if(j == 1){
%>
		<li><a class="flex-active"><%=j %></a></li>
<%			
		}else{
%>
		<li><a class=""><%=j %></a></li>
<%	
		}
	}
%>
	</ol>
	 -->
	<ul class="flex-direction-nav">
		<li><a class="flex-prev" href="#">Previous</a></li>
		<li><a class="flex-next" href="#">Next</a></li>
	</ul>
</div>
        
<div class="has-line" style="margin-top: 45px;"></div>