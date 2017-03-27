<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
		<link rel="stylesheet" type="text/css" href="finance.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="easyui/jquery.min.js"></script>
		<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	
		<style>
		  div.error {
			color: red;
		  }
		  div.status {
		  	color: green; font-size:large;
		  }
		  </style>
	  
		<script type="text/javascript">
	
		//simple email validation
		function validateEmail(email) {
			var re = /\S+@\S+\.\S+/;
			return re.test(email);
		}	
	
		$(function() { //shorthand document.ready function
			$("#status").hide();

			$('#resend_form').on('submit', function(e) { //use on if jQuery 1.7+
				e.preventDefault();  //prevent form from submitting
				$('#submit').val("     Please Wait...     ");
				var email = $('#email').val();
				$.ajax({
					  url: 'rest/users/resendVerifyEmail.do?email='+ email,
					  async: false,
					  dataType: 'json',
					  success: function (response) {
						  resp = response;//JSON.parse()//eval()//already done
					  }
					});        	
				if(resp.status == "success") {
					alert("The verification email is sent successfully, please check your inbox.");
					$('#submit').val("Register");
					
				} else {
					alert("Error resending verification email.");
					$('#submit').val("Register");
				}
				
			});

		
			$('#register_form').on('submit', function(e) { //use on if jQuery 1.7+
				e.preventDefault();  //prevent form from submitting
				var email = $('#email').val();
				var pass = $('#password').val();
				var pass2 = $('#password2').val();
				if(validateEmail(email) == false ) {
					alert("Invalid email address, please check. ");
					return ;
				}
				var pass = $('#password').val();
				if(pass.length < 1 ) {
					alert("Invalid password, please check.");
					return ;
				}

				if(pass != pass2) {
					alert("The two passwords are not matched.");
					return ;
				}
				
				var resp = null;
				$.ajax({
					  url: 'rest/users/register.do?email='+ email + '&password='+pass+'&password2='+pass2 ,
					  async: false,
					  dataType: 'json',
					  success: function (response) {
						 resp = response;//JSON.parse()//eval()//already done
                         if(resp.status == "success") {
					        $("#status").show();
				         } else {
					        $("#error").text("Registeration error, " + resp.error);
				         }
					  }
					});        	
                    //if(resp.status == "success") {
                    //	$("#status").show();
                    //} else {
                    //	$("#error").text("Registeration error, " + resp.error);
                    //}
			
			
			});
		});
		
		</script>
		<title>Register New User Account</title>

	</head>
	
	<body>

		<%@include file="header.jspf" %>

		<br><br>

			<h2> Register New Account </h2>

			<form id="register_form" method="post">
		
				<div class="error" id="error">
				</div>

				<table>
					<tr>
						<td>Email</td>
						<td><input id="email" name="email"> * </td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input id="password" type="password" name="password"> * </td>
					</tr>
					<tr>
						<td>Password again</td>
						<td><input id="password2" type="password" name="password2"> * </td>
					</tr>
					<tr>
						<td></td>
						<td><input id="submit" type="submit" value="Register"></td>
					</tr>
				</table>
				
				<br>
				<br>

				* This field is required.
				
			</form>
			<br><br>

			
			<div class="status" id="status">
				We sent you an email to verify your email address, please follow up instructions on the email to finish your registration.
				<br>
				If you did not receive the email within a couple of minutes, you can resend the email, click the following form
				<br>  
				<br>
				<br>
				<br>
				<form id="resend_form" method="post">
					<input type="submit" value="              Resend Email              ">
				</form>
				
			</div>
			
		
		<%@include file="footer.jspf" %>
			
		</body>
</html>