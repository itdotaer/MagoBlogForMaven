<%@ page language="java" import="java.util.Vector,com.mago.bean.ArticleList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get Newest articles Session -->
<jsp:include page="GetNewestArticleAction.do"></jsp:include>
<!-- /Get Newest articles Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Vector<ArticleList> newestArticleVector = (Vector<ArticleList>)session.getAttribute("newestArticleVec");
	ArticleList bean = null;
%>

<div id="sidebar">
	<div id="search-2" class="widget well widget_search">
		<form method="get" id="searchform" class="form-search" action="">
    		<h3>搜索</h3>
    		<div class="input-append">
        		<input id="s" class="search" value="Search" default-value="Search" type="text" name="s" placeholder="Search">
        		<input class="red search-btn" name="submit" id="searchsubmit" type="submit" value="Go">
    		</div>
		</form>
	</div>
	<!--
	<div class="weiboshow">
    	<iframe width="220" height="123" class="share_self" frameborder="0" scrolling="no" src="http://widget.weibo.com/weiboshow/index.php?language=&amp;width=220&amp;height=123&amp;fansRow=2&amp;ptype=1&amp;speed=0&amp;skin=1&amp;isTitle=1&amp;noborder=1&amp;isWeibo=0&amp;isFans=0&amp;uid=2101557455&amp;verifier=ea02d6eb&amp;colors=F5F5F5,ffffff,666666,666666,6DA336&amp;dpc=1"></iframe>
	</div>
	-->
	<div class="four-widget">
    	<ul id="subnavs">
	        <li>
	        	<a href="javascript:void(0)" class="zy_subnavs" onclick="javascript:window.external.AddFavorite('<%=basePath %>','MagoBlog')" title="MagoBlog">收藏本站</a>
	        </li>
	        <li><a href="javascript:void(0)" onclick="this.style.behavior='url(#default#homepage)';this.setHomePage('<%=basePath %>');" class="zy_subnavs">设为首页</a></li>
	        <li><a href="http://dchaser.dearb.me/sitemap.xml" class="zy_subnavs" target="_blank">站点地图</a></li>
	        <li><a href="http://dchaser.dearb.me/sitemap.html" class="zy_subnavs" target="_blank">百度地图</a></li>
    	</ul>
	</div>
	
	<div id="recent-posts-2" class="widget well widget_recent_entries">
        <h3 class="right_title">近期文章</h3>
        <ul id="accordion">
<%
	for(int i = 0;i < newestArticleVector.size(); i++){
		bean = newestArticleVector.elementAt(i);
%>
			<li>
        		<a class="accordion-button" href="javascript:void(0)"><span><i class="accordion-btn accordion-close">关闭</i></span><%=bean.getArticleName() %></a>
                <ul class="accordion-content">
                	<li>
                		<a href="<%=basePath + "GetArticleAction.do?articleId=" + bean.getArticleId() %>" title="<%=bean.getArticleName() %>">
                			<p><%=bean.getArticleDescription() %></p>
                		</a>
                	</li>
                </ul>
            </li>
<%		
	}
%>
		</ul>
	</div>               
</div>