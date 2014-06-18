<%@ page language="java" import="java.util.Vector,com.mago.bean.Article,com.mago.base.SessionOper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Article article = (Article)session.getAttribute("article");
	Object viewNum = session.getAttribute("viewNum");
%>

<div class="main">
	<!-- Post -->
	<div class="divleft">
	
		<div class="single_list">
			<span class="comment-count"><a href="http://dchaser.dearb.me/archives/51.html#respond" title="《大气的企业、CMS、个人博客主题-Dchaser》上的评论">0°</a></span>
			<h2 class="title"><%=article.getArticleName() %></h2>
			<ul class="post-meta">
				<li class="author">作者：<a href="javascript:void(0)"><%=article.getPublishedBy() %></a></li>
				<li class="date"><%=article.getPublishDate() %></li>
				<li class="comments"><a href="javascript:void(0)">被围观 <%=viewNum %>次</a></li>
			</ul>
			
			<div class="post-entry">
				<%=article.getArticleContent() %>
			</div>
         </div>
         
	</div>
	
	<div class="divleft">
		<div class="single_list">本文链接：<a href="<%=basePath + "GetArticleAction.do?articleId=" + article.getArticleId() %>" title="大气的企业、CMS、个人博客主题-Dchaser" rel="bookmark"><%=basePath + "GetArticleAction.do?articleId=" + article.getArticleId() %></a></div></div>
			<div class="divleft">
                <div class="single_list">
                	<div class="content_tx">
                		<img alt="" src="http://0.gravatar.com/avatar/04a08ef88fd9d58f5ca76fd840c0165c?s=80&amp;d=http%3A%2F%2F0.gravatar.com%2Favatar%2Fad516503a11cd5ca435acc9bb6523536%3Fs%3D80&amp;r=G" class="avatar avatar-80 photo" height="80" width="80">
                	</div>
                	<div class="content_sm">本文章由 
                		<a href="http://dchaser.dearb.me/"><strong>admin</strong></a> 于2013年03月13日发布在
                		<a href="http://dchaser.dearb.me/archives/category/product-show" title="查看产品展示中的全部文章" rel="category tag">产品展示</a>分类下，您可以
                		<a href="#respond">发表评论</a>，并在保留
                		<a href="http://dchaser.dearb.me/archives/51.html" rel="bookmark">原文地址</a>及作者的情况下
                		<a href="http://dchaser.dearb.me/archives/51.html/trackback" rel="trackback">引用</a>到你的网站或博客。<br> 转载请注明：
                		<a href="http://dchaser.dearb.me/archives/51.html" rel="bookmark" title="本文固定链接 http://dchaser.dearb.me/archives/51.html">大气的企业、CMS、个人博客主题-Dchaser-Dream Chaser</a><br>关键字：
                		<a href="http://dchaser.dearb.me/archives/tag/cms%e4%b8%bb%e9%a2%98" rel="tag">CMS主题</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/wordpress%e4%b8%bb%e9%a2%98" rel="tag">wordpress主题</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e4%b8%aa%e4%ba%ba%e5%8d%9a%e5%ae%a2%e4%b8%bb%e9%a2%98" rel="tag">个人博客主题</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e4%ba%91%e5%95%86%e5%ba%97" rel="tag">云商店</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e4%bc%81%e4%b8%9a%e4%b8%bb%e9%a2%98" rel="tag">企业主题</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e5%90%be%e7%88%b1it%e7%bd%91" rel="tag">吾爱IT网</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e5%bc%80%e6%ba%90%e4%b8%bb%e9%a2%98" rel="tag">开源主题</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e5%bf%ab%e9%80%9f%e5%bb%ba%e7%ab%99" rel="tag">快速建站</a>, 
                		<a href="http://dchaser.dearb.me/archives/tag/%e6%96%b0%e6%b5%aa%e4%ba%91" rel="tag">新浪云</a>
                	</div>
                </div>
            </div>
            <!-- 
            <div class="divleft">
                <div class="single_list">
                	<div class="single_listl"><a href="http://dchaser.dearb.me/archives/60.html" rel="next">无懈可击之高手如林-汤七七</a></div>
                	<div class="single_listr"><a href="http://dchaser.dearb.me/archives/47.html" rel="prev">自适应响应式布局wordpress主题</a></div>
                </div>
            </div>
            -->
            
            <div class="divleft">
                <div class="single_list">
                	<h2 class="bdshare-title">好文章就要一起分享！</h2>
                	<!-- Baidu Button BEGIN -->

	                <div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare">
	                    <a class="bds_tsina" title="分享到新浪微博" href="#"></a>
	                    <a class="bds_qzone" title="分享到QQ空间" href="#"></a>
	                    <a class="bds_tqq" title="分享到腾讯微博" href="#"></a>
	                    <a class="bds_renren" title="分享到人人网" href="#"></a>
	                    <a class="bds_ty" title="分享到天涯社区" href="#"></a>
	                    <a class="bds_ff" title="分享到饭否" href="#"></a>
	                    <a class="bds_fbook" title="分享到Facebook" href="#"></a>
	                    <a class="bds_baidu" title="分享到百度搜藏" href="#"></a>
	                    <a class="bds_hi" title="分享到百度空间" href="#"></a>
	                    <a class="bds_zx" href="#"></a>
	                    <a class="bds_douban" title="分享到豆瓣网" href="#"></a>
	                    <a class="bds_t163" title="分享到网易微博" href="#"></a>
	                    <a class="bds_xg" title="分享到鲜果" href="#"></a>
	                    <a class="bds_qq" title="分享到QQ收藏" href="#"></a>
			            <a class="bds_tieba" title="分享到百度贴吧" href="#"></a>
	                    <span class="bds_more">更多</span>
	                    <a class="shareCount" href="#" title="累计分享4次">4</a>
	                </div>
                	<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=730973" src="http://bdimg.share.baidu.com/static/js/bds_s_v2.js?cdnversion=389429"></script>
                
                	<script type="text/javascript">
	               		document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + new Date().getHours();
                	</script>
                	<!-- Baidu Button END -->
                </div>
            </div>
			<!--            
            <div class="rand-article">
            	<div class="textlist_s"><h2>真的，我想您也会喜欢</h2>
                    <div class="hr-line"></div>
                    	<ul>
                    		<li><a href="http://dchaser.dearb.me/archives/47.html" rel="bookmark" title="自适应响应式布局wordpress主题">自适应响应式布局wordpress主题</a></li>
							<li><a href="http://dchaser.dearb.me/archives/43.html" rel="bookmark" title="DChaser-The Best Business Theme">DChaser-The Best Business Theme</a></li>
						</ul>
				</div>
			</div>
			
			<div class="rand-article">
				<div class="textlist_s"><h2>随便找了点看您喜欢不</h2>
					<div class="hr-line"></div>
					<ul>
						<li><a href="http://dchaser.dearb.me/archives/39.html" title="下载最新版本">下载最新版本</a></li>
						<li><a href="http://dchaser.dearb.me/archives/43.html" title="DChaser-The Best Business Theme">DChaser-The Best Business Theme</a></li>
						<li><a href="http://dchaser.dearb.me/archives/60.html" title="无懈可击之高手如林-汤七七">无懈可击之高手如林-汤七七</a></li>
						<li><a href="http://dchaser.dearb.me/archives/47.html" title="自适应响应式布局wordpress主题">自适应响应式布局wordpress主题</a></li>
						<li><a href="http://dchaser.dearb.me/archives/68.html" title="我们约会吧-曹苑">我们约会吧-曹苑</a></li>
					</ul>
				</div>
			</div>
			-->
	<!-- /Post -->
</div>