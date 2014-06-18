<%@ page language="java" import="java.util.Vector,com.mago.bean.PicList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get Project Carousel Session -->
<jsp:include page="GetProjectCarouselAction.do"></jsp:include>
<!-- /Get Project Carousel Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	Vector<PicList> newestPicVector = (Vector<PicList>)session.getAttribute("newestPicVec");
	PicList bean = null;
%>

<div id="project-wrapper" class="clearfix jcarousel-container jcarousel-container-horizontal" style="position: relative; display: block;">
	 <div class="section-title one-fourth">
	 	<h4>最新图文</h4>
	 	<p>这里汇集了我们网站最新的图文页面，你可以点击查阅详细！</p>
	 	<p><a href="javascript:void(0)">查看更多</a></p>
	 	<div class="carousel-nav">
	 		<a id="project-prev" class="jcarousel-prev jcarousel-prev-horizontal jcarousel-prev-disabled jcarousel-prev-disabled-horizontal" href="javascript:void(0)" title="上一页" disabled="disabled" style="display: block;"></a>
	 		<a id="project-next" class="jcarousel-next jcarousel-next-horizontal" href="javascript:void(0)" title="下一页" style="display: block;"></a>
         </div>
      </div>
      
      <div class="jcarousel-clip jcarousel-clip-horizontal" style="position: relative;">
      	<ul class="project-carousel jcarousel-list jcarousel-list-horizontal" style="overflow: hidden; position: relative; top: 0px; margin: 0px; padding: 0px; left: 0px; width: 2020px;">
      		<li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-undefined jcarousel-item-undefined-horizontal jcarousel-item-placeholder jcarousel-item-placeholder-horizontal" style="float: left; list-style: none;"></li><li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-NaN jcarousel-item-NaN-horizontal jcarousel-item-placeholder jcarousel-item-placeholder-horizontal" jcarouselindex="NaN" style="float: left; list-style: none;"></li>
<%
	for(int i = 0; i < newestPicVector.size(); i++){
		bean = newestPicVector.elementAt(i);
%>
			<li class="jcarousel-item jcarousel-item-horizontal jcarousel-item-1 jcarousel-item-1-horizontal" jcarouselindex="1" style="float: left; list-style: none;">
	      		<a href="<%=basePath%>/GetArticleAction.do?articleId=<%=bean.getArticleId() %>" title="<%=bean.getArticleName() %>" class="project-item">
		      		<div class="project-image" style="background-image:url(<%=basePath + bean.getFilePath() %>)" alt="<%=bean.getArticleName() %>"></div>
		      		<div class="overlay">
		      			<h5><p><%=bean.getArticleDescription() %></p></h5>
		      			<p><%=bean.getArticleName() %></p>
		      		</div>
	      		</a>
      		</li>
<%
	}
%> 			
        </ul>
    </div>
    
    <div class="has-line" style="margin-top: 50px;"></div>
    
</div>