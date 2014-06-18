<%@ page language="java" import="java.util.Vector,com.mago.bean.ArticleList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Vector<ArticleList> classArticleVector = (Vector<ArticleList>)session.getAttribute("classArticleList");
	ArticleList bean = null;
%>

<div id="main">
	<!-- Simple Post -->
<%
	for(int i = 0; i < classArticleVector.size(); i++){
		bean = classArticleVector.elementAt(i);
%>
	<div id="post_list" class="clearfix">
		<span><a href="" title="">0°</a></span>
		<a href="<%=basePath %>GetArticleAction.do?articleId=<%=bean.getArticleId() %>" title="<%=bean.getArticleName() %>"><h2 class="title"><%=bean.getArticleName() %></h2></a>
		<ul class="post-meta">
			<li class="author">作者：<a href="javascript:void(0)"> <%=bean.getPublishedBy() %></a></li>
            <li class="date"><%=bean.getPublishDate() %></li>
            <li class="comments"><a href="http://dchaser.dearb.me/archives/51.html">被围观 <%=bean.getViewNum() %> 次</a></li>
		</ul>
		<div class="post-entry">
			<div id="post_listl">
				<div class="thumbnail">
					<a "<%=basePath %>GetArticleAction.do?articleId=<%=bean.getArticleId() %>" title="<%=bean.getArticleName() %>" rel="bookmark">
						<img width="150" height="150" src="<%=path %>/static/img/dog.png" class="attachment-thumbnail wp-post-image" alt="slide_02_main">
					</a>
				</div>
			</div>
            <div id="post_listr"><%=bean.getArticleDescription() %></div>
            <div id="post_list_more" class="red">
            	<a href="<%=basePath %>GetArticleAction.do?articleId=<%=bean.getArticleId() %>" rel="bookmark">详细阅读</a>
            </div>
       </div>
	</div>
<%
	}
%>
	<!-- /Simple Post -->  
	
	<!-- Pagination -->
            <ul class="pagination"> </ul>
    <!-- /Pagination -->
</div>