<%@ page language="java" import="java.util.Vector,com.mago.bean.ArticleList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get Blog Carousel Session -->
<jsp:include page="GetBlogCarouselAction.do"></jsp:include>
<!-- /Get Blog Carousel Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Vector<ArticleList> mostViewedArticleVector = (Vector<ArticleList>)session.getAttribute("mostViewedArticleVec");
	ArticleList bean = null;
%>

<div id="blog-wrapper" class="clearfix jcarousel-container jcarousel-container-horizontal" style="position: relative; display: block;">
	<div class="section-title one-fourth">
		<h4>热评文章</h4>
		<p>最受欢迎的文章当然有它的出彩之处，点进去看看吧！</p>
		<p><a href="javascript:void(0)">查看更多</a></p>
		<div class="carousel-nav">
			<a id="blog-prev" class="jcarousel-prev jcarousel-prev-horizontal jcarousel-prev-disabled jcarousel-prev-disabled-horizontal" href="javascript:void(0)" title="上一页" disabled="disabled" style="display: block;"></a>
			<a id="blog-next" class="jcarousel-next jcarousel-next-horizontal" href="javascript:void(0)" title="下一页" style="display: block;"></a>
		</div>
	</div>
	
	<div class="jcarousel-clip jcarousel-clip-horizontal" style="position: relative;">
		<ul class="blog-carousel jcarousel-list jcarousel-list-horizontal" style="overflow: hidden; position: relative; top: 0px; margin: 0px; padding: 0px; left: 0px; width: 2500px;">
			<li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-undefined jcarousel-item-undefined-horizontal jcarousel-item-placeholder jcarousel-item-placeholder-horizontal" style="float: left; list-style: none;"></li>		
			<li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-NaN jcarousel-item-NaN-horizontal jcarousel-item-placeholder jcarousel-item-placeholder-horizontal" jcarouselindex="NaN" style="float: left; list-style: none;"></li>
<%
	for(int i = 0; i < mostViewedArticleVector.size(); i++){
		bean = mostViewedArticleVector.elementAt(i);
%>		
	        <li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-1 jcarousel-item-1-horizontal" jcarouselindex="<%=i %>>" style="float: left; list-style: none;">
		        <a href="<%=basePath%>GetArticleAction.do?articleId=<%=bean.getArticleId()%>" title="<%=bean.getArticleName() %>"><h4><%=bean.getArticleName() %></h4></a>
		        <span class="date"><%=bean.getPublishDate() %>&nbsp;被围观<%=bean.getViewNum() %>&nbsp;次</span>
		        <p><%=bean.getArticleDescription() %></p>
	        </li>
<%
	}
%>
		</ul>
	</div>
</div>