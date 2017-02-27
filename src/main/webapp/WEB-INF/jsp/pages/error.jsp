<%@ page contentType="text/html; charset=Cp1256" pageEncoding="Cp1256"%>
<%@ page isErrorPage="true" %>

<%
if(exception == null)
	exception = (Exception)request.getAttribute("exception");
%>

<html>
<head>


	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
    <script type="text/javascript" src="easyui/jquery.min.js"></script>  <!-- jquery-1.10.1 required -->	
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>

<title>Error Page</title>
</head>
<body>

  	<% String curPage = "error";%>
	<%@include file="header.jspf" %>

	<br>
	<br>

	<h1> Application Error </h1>
	
	<font color=red>
	<%
		if(exception != null) {
			String message = exception.getMessage();
			if(message != null) {
				out.println(message);
			}
		}
	%>
	</font>

		<br>
			
				Internal application error, please contact support. 
				
	<font color=red>
<%
	if(exception != null) { 
			out.print("\r\n<!-- ");
		out.print(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ \r\n\r\n");
		String error = exception.getMessage();
		if(error == null)
			error = "";
		out.print(exception.getMessage());
		out.print("\r\n");
		out.flush();
		exception.printStackTrace(response.getWriter());
		out.print("-->");
	}
	%>
	</font>
		
	<br>
	<br>
	<br>
	<br>

	<hr>

	<%@include file="footer.jspf" %>



</body>

</html>
