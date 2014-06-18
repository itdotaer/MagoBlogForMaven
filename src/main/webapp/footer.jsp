<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div id="footer">
	<div class="container clearfix">
            <div class="footer-content"> 
                <!-- About -->
                <div class="one-fourth last link-path" style="width:auto;height:auto;">
                    <h5>关于我们</h5>
                    <p>You have a dream, you got to protect it. People can't do something by themselves; they wanna tell you you can not do it.</p>
                </div>
                <!-- /About -->
            </div>

            <div class="info clearfix link-path">
                <!-- Copyright -->
                <ul class="copyright">
                    <li>Copyright © 2014 <strong><a href="<%=basePath %>">Mago Blog</a></strong> . All rights reserved</li>
                    <li>Powered by Mago Blog Web System </li>
                    <li>Designed by <a href="<%=basePath %>" target="_blank">Harry Hu</a></li>
                    <li><a href="<%=basePath %>" target="_blank" title="备案信息">xxx-xxxx-xxxx</a></li>
                </ul>
                <!-- /Copyright -->
            </div>
        </div>

    </div>