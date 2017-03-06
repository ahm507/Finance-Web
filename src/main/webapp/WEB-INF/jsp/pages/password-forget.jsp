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
	
	<script type="text/javascript">
		//simple email validation
		function validateEmail(email) {
			var re = /\S+@\S+\.\S+/;
			return re.test(email);
		}	
	
	</script>
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
	    $('#form1').on('submit', function(e) { //use on if jQuery 1.7+
	        e.preventDefault();  //prevent form from submitting
	        var email = $('#email').val();
	        if(validateEmail(email) == false) {
	        	alert("Invalid email, please check!");
	        	return;
	        }
	        var resp = null;
	    	$.ajax({
	    		  url: 'rest/users/sendResetEMail.do?email='+ email ,
	    		  async: false,
	    		  dataType: 'json',
	    		  success: function (response) {
	    			  resp = response;//already parsed
	    		  }
	    		});        	
	        if(resp.success == true) {
	        	$("#status").text("Reset password email is sent successfully, please check your inbox.");
	        } else {
	        	$("#error").text("Error sending email, " + resp.error);
	        }
	        
	        
	    });
	});
		
	</script>
	<title>Forget Password</title>
</head>
	<body>

		<%@include file="header.jspf" %>

		<br> <br>
		
		<h2> Forget Password</h2>
		This form will send you email with reset password instructions. 

		<br>
		<br>

		<form id="form1" method="post">
			<b>
			<div id="error">
			</div>
			</b>	
				<table>
					<tr>
						<td>Email</td>
						<td><input id="email" name="email" value=""></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" value="Send Password Reset Email"></td>
					</tr>
				</table>
				<br>
			
				<br>
				<h1> &nbsp; &nbsp; &nbsp;
					<div id="status">
					</div>
				</h1>

				&nbsp;
		</form>
		

		<%@include file="footer.jspf" %>

	
	</body>
</html>