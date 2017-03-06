<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
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
		
		
	
		
		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>

		
			<table border="0">
			<tr>
				<td> 	
					<h2>Personal Finance</h2>
					Hello <%= request.getRemoteUser() %>
					<br>
					<br>
					
				</td>
				<td>
					<h2>
					<a href="mobile-login.html">Mobile Access</a>
					</h2> 
				</td>
			</tr>
				<tr> 
				<td width="60%"> 
 				
 				
 				
 			<%-- 	<c:url value='/login' /> 
 				<hr> 
				<c:url value='j_spring_security_check' />
			 --%>		
					
					
					Login:
					<form name='loginForm' method="POST" id="login_form"  action="<c:url value='login' />">
					<b>
					<div id="error">
					</div>
					</b>
							<table>
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
							
								<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
														
							
						</form>
		
		</td>
				
				</tr>
			</table>


	
	<%@include file="footer.jspf" %>

</body>
</html>

