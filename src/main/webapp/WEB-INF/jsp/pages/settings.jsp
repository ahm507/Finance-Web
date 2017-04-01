<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<!-- CSRF Protection headers used in JSON requests in javascript -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>	
	
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">	
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/rest.js"></script>	
	
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
	$(function() { //shorthand to document.ready function

		var oldUsdRate = $('#usd_rate').val();
		var oldSarRate = $('#sar_rate').val();

		setFormFields();

	    $('#change_settings_form').on('submit', function(e) { //use on if jQuery 1.7+
	        e.preventDefault();  //prevent form from submitting
	        var email = $('#email').val();
	        var oldPass = $('#oldPassword').val();
	        var pass = $('#password').val();
	        var pass2 = $('#password2').val();
	        var usdRate = $('#usd_rate').val();
	        var sarRate = $('#sar_rate').val();
	        if(pass.length == 0 && usdRate == oldUsdRate && sarRate == oldSarRate) {
				alert("No thing changed");	        	
	        	return; // do nothing
	        }

	        if(pass != pass2) {
				alert("The two new passwords are not matched, please try again");	        	
	        	return; // do nothing
	        }
	        if(usdRate <= 0) {
				alert("The USD rate should be a number greater than zero");
	        	return; // do nothing
	        }

	        if(sarRate <= 0) {
				alert("The SAR rate should be a number greater than zero");
	        	return; // do nothing
	        }

	        var resp = null;  //register-ok.jsp
	    	$.ajax({
	    		  url: 'rest/users/changeSettings.do?email='+ email + '&oldPassword=' + oldPass +
	    		  		'&password='+pass+'&password2='+pass2 + '&usd_rate=' + usdRate +
	    		  		'&sar_rate=' + sarRate,
	    		  async: false,
	    			headers : getCsrfHeaders(),

            type: "GET",
	    		  dataType: 'json',
	    		  success: function (response) {
	    			  resp = response;  //JSON.parse()//eval()//already done
	    		  }
	    		});        	
	        if(resp.status == 'success') {
	        	$("#error").text("Settings are updated successfully.");
	        } else {
	        	$("#status").text("Settings update error, " + resp.error);
	        	$("#error").text("");
	        }

	    });
	});

	function setFormFields() {
		$.ajax({
		  url: 'rest/users/getExchangeRates.do?',
		  async: true,
		  type: "GET",
		  dataType: 'json',
		  success: function (response) {
			$('#usd_rate').val(response['usd_rate']);  //text
			$('#sar_rate').val(response['sar_rate']);  //text

		  }
		});






	}


	</script>

<title>Update Settings</title>
</head>
<body>

	<%@include file="header-transaction.jspf" %>

	<br>
	<br>

<form id="change_settings_form" method="post">
		
<!-- Error -->
	<b>
	<div id="error">
	</div>
	</b>
		<h1> Edit User Settings </h1>
		
		<table>
			<tr>
				<td>Email</td>
				<td><input id="email" name="email" READONLY value="<%=request.getRemoteUser()%>"></td>
			</tr>

			<tr>
				<td>1 USD = </td>
				<td>
					<input id="usd_rate" name="usd_rate"> EGP
				</td>
			</tr>

			<tr>
				<td>1 SAR = </td>
				<td>
					<input id="sar_rate" name="sar_rate"> EGP
				</td>
			</tr>


			<tr>
				<td>Old Password</td>
				<td><input id="oldPassword" type="password" name="oldPassword"></td>
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
				<td>
					<br>
					<br>

					<input type="submit" value="Update Setting"></td>
				<td></td>
			</tr>
			<tr>
				<td>
					<div id="status">
					</div>
				
				</td>
			</tr>
		</table>
	</form>
	<br><br>
	
	<%@include file="footer.jspf" %>
	
	</body>
</html>