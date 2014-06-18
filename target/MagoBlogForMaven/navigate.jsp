<%@ page language="java" import="java.util.Vector,com.mago.bean.Classe,com.mago.base.SessionOper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Get Nav List Session -->
<jsp:include page="GetNavAction.do"></jsp:include>
<!-- /Get Nav List Session -->
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Vector<Classe> navVector = (Vector<Classe>)session.getAttribute("navVec");
	Vector<Vector<Classe>> vec = null;
	if(!navVector.isEmpty())
	{
		vec = SessionOper.SortClassById(navVector);
	}
%>

<div id="header" class="container clearfix">
	<a href="<%=basePath %>" id="logo"><img width="80px" height="73px" src="<%=path %>/static/img/dog.png" alt=""></a>
 	<!-- Navigation -->
 	<nav id="main-nav-menu" class="menu-dchaser-container">
 		<ul class="sf-menu sf-js-enabled">
 			<li id="menu-item-4" class="menu-item menu-item-type-custom menu-item-object-custom current-menu-item current_page_item menu-item-home menu-item-4 active"><a href=<%=basePath %>>首页</a></li>
 			
<%
	for(int i = 0; i < vec.size(); i++){
		Vector<Classe> tempSubVec = vec.elementAt(i);
 		String className = tempSubVec.elementAt(0).getClassName();
 		int pId = tempSubVec.elementAt(0).getClassId();
%>
 			<li  class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-12 dropdown" data-dropdown="dropdown">
 				<a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown"><%=new String(className.getBytes("UTF-8"), "UTF-8") %><b class="caret"></b></a>
 <%
 		for(int j = 1; j < tempSubVec.size(); j++){
 			Classe bean = tempSubVec.elementAt(j);
 %>
 				<ul class="dropdown-menu" style="display: none;">
 					<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-13 dropdown-submenu" data-dropdown="dropdown">
 						<a href="<%=basePath %>GetListAction.do?pId=<%=pId %>&subId=<%=bean.getClassId() %>" class="dropdown-toggle" data-toggle="dropdown"><%=new String(bean.getClassName().getBytes("UTF-8"), "UTF-8") %></a>
					</li>
				</ul>
<%
		}
 %>
 			</li>
 <%
 	}
 %>
		</ul>
	</nav>
    <!-- /Navigation -->

    <select id="responsive-main-nav-menu" onchange="javascript:window.location.replace(this.value);">
    </select>
    </div>