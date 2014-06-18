<%@ page language="java" import="java.util.Vector,com.mago.bean.MainService,com.mago.base.SessionOper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get MainService Session -->
<jsp:include page="GetMainServiceAction.do"></jsp:include>
<!-- /Get MainService Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Vector<MainService> mainServiceVec = (Vector<MainService>)session.getAttribute("mainServiceVec");
%>
<div class="main-services">
	<ul>
<%
	for(int i = 0; i< mainServiceVec.size(); i++){
		MainService bean = mainServiceVec.elementAt(i);
		String imgPath = "static/img/1_A.png";
		
		switch(i){
			case 0:
				break;
			case 1:
				imgPath = "static/img/2_A.png";
				break;
			case 2:
				imgPath = "static/img/3_A.png";
				break;
			case 3:
				imgPath = "static/img/4_A.png";
				break;
		}	
%>
		<li class="bt-30">
			<img src="<%=imgPath %>" alt=<%=bean.getArtitlceName() %> title=<%=bean.getArtitlceName() %>>
			<a href="<%=basePath %>GetArticleAction.do?articleId=<%=bean.getArtitleId() %>">
				<h4 title="阅读更多"><%=bean.getArtitlceName() %></h4>
			</a>
			<p><%=bean.getArtitleDescription() %></p>
			<a href="<%=basePath %>GetArticleAction.do?articleId=<%=bean.getArtitleId() %>">- 阅读更多 -</a>
		</li>
<%		
	}
%>
	</ul>	
	<div class="has-line"></div>
</div>