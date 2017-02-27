<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>

<%
	String root = request.getServletContext().getRealPath("/WEB-INF");
	String email = request.getParameter("email");
	String code = request.getParameter("code");
	Boolean success = new pf.webmvc.JspRequests().verifyUserEmail(root, email, code);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>

	<title>Verify Registration</title>
</head>
<body>

  	<% String curPage = "register-verify";%>
	<%@include file="header.jspf" %>

	<br>
	<br>

	<% if(success) { %>
		<!-- SUCCESS_CODE_FOR_TESTING  -->
		<h1>Email is verified successfully</h1>
		You can now proceed to <a href="login">Login</a>
		
	<% } else { 
		
	%>
	
		<h1>Email Verification Error</h1>
		Error verifying email, please make sure verification URL is written correctly.
		 
	<% } %>

	<br>
	<br>

	<hr>
	<%@include file="footer.jspf" %>



</body>
</html>