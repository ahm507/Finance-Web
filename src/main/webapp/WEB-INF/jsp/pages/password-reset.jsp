<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>

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
	
	$(function() { //shorthand document.ready function
	    $('#form').on('submit', function(e) { //use on if jQuery 1.7+
	        e.preventDefault();  //prevent form from submitting
	        var email = $('#email').val();
	        var code = $('#code').val();
	        var pass = $('#password').val();
	        var pass2 = $('#password2').val();
	        if(pass.length == 0) {
				alert("No thing changed");	        	
	        	return; // do nothing
	        } 
	        if(pass != pass2) {
				alert("The two new passwords are not matched, please try again");	        	
	        	return; // do nothing
	        }
	        var resp = null;
	    	$.ajax({
	    		  url: 'rest/users/resetPassword.do?email='+ email + '&code=' + code + '&password='+pass+'&password2='+pass2 ,
	    		  async: false,
	    		  dataType: 'json',
	    		  success: function (response) {
	    			  resp = response;
	    		  }
	    		});        	
	        if(resp.success == true) {
	        	//window.location.href = "http://stackoverflow.com";
	        	$("#status").html("Password is reset successfulyy, please go to <a href='login'>login page</a>");
	        } else {
	        	$("#error").text("Error reseting password, " + resp.error);
	        }
	        
	        
	    });
	});
		
	</script>

	<title>Password Reset</title>

</head>
<body>

	<%@include file="header.jspf" %>

<br>
<br>
	<h1> Password Reset: Change Password</h1>

	<form id="form" method="post">
		
		<b>
		<div id="error">
		</div>
		</b>
			
		<%
		String email = request.getParameter("email");
		String code = request.getParameter("code");
		%>
		<table>
			<tr>
				<td>Email</td>
				<td><input id="email" name="email" READONLY DISABLED value="<%=email %>">
				<input id="code" name="code" type="hidden" value="<%=code %>">
				</td>
			</tr>
			
			<tr>
				<td>New Password</td>
				<td><input id="password" type="password" name="password"></td>
			</tr>
			<tr>
				<td>New Password Again</td>
				<td><input id="password2" type="password" name="password2"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Change Password"></td>
			</tr>
		</table>
	</form>
	
	<h1>
		<div id="status">
		</div>
	</h1>
	
	
	
	<%@include file="footer.jspf" %>
	
	</body>
</html>