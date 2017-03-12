<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">    
    
	<!-- icon for iphone -->
	<link rel="apple-touch-icon" sizes="57x57" href="images/icon-57.png" />
	<link rel="apple-touch-icon" sizes="80x80" href="images/icon-80.png" />
	<link rel="apple-touch-icon" sizes="120x120" href="images/icon-120.png" />
    
    
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
 	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script> 
	<script type="text/javascript" src="js/rest.js"></script>
	<script type="text/javascript" src="js/data.js"></script>
<!-- 	<script type="text/javascript" src="js/login.js"></script> -->

	<style>
	  p {
	    color: blue;
	    margin: 8px;
	  }
	  b {
	    color: red;
	  }
	  
	  div.body-inner {
			/* Override finance CSS file  */
			width: 400px !important; 
		}
	  
	  
	  </style>
	  
	<script type="text/javascript">
	
		//$ is shorthand to jQuery
		$(document).ready(function() {
			var email = readCookie("email");
			if(email != null && email != undefined) {
				$('#email').val(email);
			}
		});

	</script>

	<title>Login</title>
</head>
<body class="easyui-layout">
	
		<%@include file="header.jspf" %>
		
			
					<h2>Personal Finance Login</h2>
					<%-- Hello <%= request.getRemoteUser() %> --%>
					
					<!-- This is error block = -->					
					<% 
					String msg = request.getParameter("msg");
					if("error".equals(msg)) {
%>											

						<div class="error">Login Error. Please check user name and/or password.</div>
	

<%					} else if("logout".equals(msg)) {   %>

						<div class="msg">You Logout successfully.</div>

						
<%						}  %>
					
				
					<form name='loginForm' method="POST" id="login_form"  action="login">
					<b>
					<div id="error">
					</div>
					</b>
							<table border="0">
								<tr>
									<td align="right">Email:</td>
									<td>
									<input name="username" id="username" style="width: 159px;"></td>
								</tr>
								<tr>
									<td align="right">Password:</td>
									<td><input type="password" id="password" name="password"
										style="width: 160px;"></td>
								</tr>
								<tr>
									<td>
									</td>
									<td>
										<br>
										<input id="login" name="login" id="login" type="submit"
											value="             Login             ">
									</td>
								</tr>
								<tr>
									<td></td>
									<td><a href="register"><br>Register New Account</a></td>
								</tr>
								<tr>
									<td> <br><br></td>
									<td><a href="password-forget">Forget Password</a></td>
								</tr>
							</table>
							
							<%-- 	<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
														 --%>
							
						</form>
		
	

	
	<%@include file="footer.jspf" %>

</body>
</html>

