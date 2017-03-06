<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact us</title>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
 	<link rel="stylesheet" type="text/css" href="finance.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	
	<style>
	  p {
	    color: black;
	    margin: 8px;
		font-size:12px;
	    font-family:helvetica,tahoma,verdana,sans-serif;
	    
	  }
	  b {
	    color: red;
	  }
	  </style>
	  	
	<script type="text/javascript">
	
	//simple email validation
	function validateEmail(email) {
		var re = /\S+@\S+\.\S+/;
		return re.test(email);
	}	
	
	$(function() { //shorthand document.ready function
	    $('#contact_us_form').on('submit', function(e) { //use on if jQuery 1.7+
//	        $('#login').val("     Please Wait...     ");
	    	e.preventDefault();  //prevent form from submitting
	        var email = $('#email').val();
	        if(validateEmail(email) == false ) {
	        	alert("Invalid email address, please check. ");
		        //$('#login').val("   Login   ");
	        	return ;
	        }
	        var name = $('#name').val(); //optional
	        var title = $('#title').val(); //optional
	        var comments = $('#comments').val();
	        
	        if(comments.length < 1) {
	        	alert("Empty comment, please write some feedback and submit again");
//		        $('#login').val("   Login   ");
	        	return ;
	        }
	        var resp = null;
	    	$.ajax({
	    		  url: 'rest/users/contactus.do?email='+ email + '&name='+name + '&title='+title + '&comments='+comments,
	    		  async: false,
	    		  dataType: 'json',
	    		  success: function (response) {
	    			  resp = response;//JSON.parse()//eval()//already done
	    		  }
	    		});        	
	        if(resp.status == "success") {
	        	$(location).attr('href','transactions');
	        } else {
	        		$("#error").text(resp.error + ": Error sending feedback.");
	        }
	        
	    });
	});
				
	</script>

</head>
<body>

    <% if (request.getRemoteUser() == null) { %>

	        <%@include file="header.jspf" %>

	<% } else { %>

	        <%@include file="header-transaction.jspf" %>
	<% } %>

	
	<br><br><br>


<h2>Contact Us</h2>


Send us your comments, suggestions, and feedback to support@SalaryControl.com.  

<br>
<br>
<br>
<br>

&nbsp;
<!-- 

<form method="GET" id="contact_us_form" action="rest/users/contactus.do">
	<b>
	<div id="error">
	</div>
	</b>
		<table>
			<tr>
				<td align="right">Name</td>
				<td><input id="name" size="25"></td>
			</tr>
			<tr>
				<td align="right">Email</td>
				<td><input id="email" size="25"></td>
			</tr>
			<tr>
				<td align="right">Title</td>
				<td><input type="title" id="title" size="50"></td>
			</tr>
			
			<tr>
				<td align="right">Comments</td>
				<td>
					<textarea id="comments" type="text" rows="10" cols="80"></textarea>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input name="login" id="login" type="submit" value="   Submit   "></td>
			</tr>
			
		</table>
		
</form>

 -->
 
	<%@include file="footer.jspf" %>



</body>
</html>