<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp"%>

<%
	//String root = request.getServletContext().getRealPath("/WEB-INF");
	/* String email = request.getParameter("email");
	String code = request.getParameter("code");
	*/ //FIXME: Convert this call to a REST request
		//Boolean success = new pf.web.JspRequests().verifyUserEmail(root, email, code);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="easyui/themes/finance/easyui.css">
<link rel="stylesheet" type="text/css" href="finance.css">
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
<script type="text/javascript" src="easyui/jquery.min.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>

<title>Verify Registration</title>

<script type="text/javascript">
	urlVars = getUrlVars();

	jQuery.ajax({
		type : 'POST',
		url : 'rest/accounts/verifyEmail.do',
		async : false,
		dataType : 'json',
		data : {
			email : urlVars['email'],
			code : urlVars['code']
		},
		success : function(response) {
			if (response.status == 'success') {
				//empty the error-message
				$("#error-message").text(" ");

			} else {
				//empty the success-message
				$("#success-message").text(" ");

			}
		}
	});

	function getUrlVars() {
		var vars = [], hash;
		var hashes = window.location.href.slice(
				window.location.href.indexOf('?') + 1).split('&');
		for (var i = 0; i < hashes.length; i++) {
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = hash[1];
		}
		return vars;
	}
</script>

</head>
<body>

	<%@include file="header.jspf"%>

	<br>
	<br>

	<span id="success-message"> <!-- SUCCESS_CODE_FOR_TESTING  -->
		<h1>Email is verified successfully</h1> You can now proceed to <a
		href="login">Login</a>

	</span>

	<span id="error-message">

		<h1>Email Verification Error</h1> Error verifying email, please make
		sure verification URL is written correctly.
	</span>

	<br>
	<br>

	<hr>
	<%@include file="footer.jspf"%>



</body>
</html>