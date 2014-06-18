<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to login page</title>
</head>
<body>
<p><html:form action="/Admin/login.do">

UserName:<html:text property="userName"></html:text></br>
Password:<html:text property="password"></html:text></br>

<html:submit value="Login"></html:submit>
</html:form></p>
</body>
</html>