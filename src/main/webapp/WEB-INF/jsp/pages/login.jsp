<!doctype html>
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
	<script type="text/javascript" src="js/login.js"></script>

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
	
	  	<% String curPage = "login";%>
		<%@include file="header.jspf" %>
		
			<table border="0">
			<tr>
				<td> 	
					<h2>Personal Finance</h2>
				</td>
				<td>
					<h2>
					<a href="mobile-login.html">Mobile Access</a>
					</h2> 
				</td>
			</tr>
				<tr> 
				<td width="60%"> 
			
					Login:<form method="POST" id="login_form">
					<b>
					<div id="error">
					</div>
					</b>
							<table>
								<tr>
									<td align="right">Email:</td>
									<td><input id="email" style="width: 159px;"></td>
								</tr>
								<tr>
									<td align="right">Password:</td>
									<td><input type="password" id="password"
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
									<td><a href="register.jsp"><br>Register New Account</a></td>
								</tr>
								<tr>
									<td> <br><br></td>
									<td><a href="password-forget.jsp">Forget Password</a></td>
								</tr>
							</table>
						</form>
		
		</td>
				
				</tr>
			</table>


	
	<%@include file="footer.jspf" %>

</body>
</html>

