
//$("head").append('<script type="text/javascript" src="/js/rest.js"></script>');
//$("head").append('<script type="text/javascript" src="/js/data.js"></script>');

//simple email validation
	function validateEmail(email) {
		var re = /\S+@\S+\.\S+/;
		return re.test(email);
	}	
	
	$(function() { //shorthand document.ready function
		
		//on Submit even handler
	    $('#login_form').on('submit', function(e) { //use on if jQuery 1.7+
	        $('#login').val("     Please Wait...     ");
	    	e.preventDefault();  //prevent form from submitting

	    	var email = $('#email').val();
	        if(validateEmail(email) == false ) {
	        	alert("Invalid email address, please check. ");
		        $('#login').val("   Login   ");
	        	return ;
	        }
	        var pass = $('#password').val();
	        if(pass.length < 1 ) {
	        	alert("Invalid password, please check");
		        $('#login').val("   Login   ");
	        	return ;
	        }
	        
	        var resp = loginRest(email, pass);
	        
	        if(resp.status == "success") {
	        	createCookie("email", email, 365);
	        	$(location).attr('href','transactions'); //.jsp
	        } else {
	        	if(resp.error == "EmailNotVerified") {
	        		$("#error").text("Email exist but not verified yet, please check your email inbox.");
	        	} else if (resp.error == "InvalidEmail") {
	        		$("#error").text("Invalid Email, please check and try again");
	        	} else if (resp.error == "InvalidPassword") {
	        		$("#error").text("Invalid password, please check and try again");
	        	} else if (resp.error == "UserNotExist") {
	        		$("#error").text("User does not exist, please check and try again");
	        	} else {
	        		//alert(resp);
	        		$("#error").text(resp.error);//"Invalid email or password"
	        	}
		        $('#login').val("   Login   ");
	        }
	        
	    });
	});
	
	
	
	