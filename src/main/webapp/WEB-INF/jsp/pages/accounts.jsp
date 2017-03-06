<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    
<%-- <%
    	//If forward to login.jsp
            pf.webmvc.JspRequests.processIfNotLoggedUser(request, response);
%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="jquery,ui,easy,easyui,web">
	<meta name="description" content="easyui help you build your web page easily!">
	<title>Account Editing</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/rest.js"></script>
	<script type="text/javascript" src="js/accounts.js"></script>


	<style type="text/css">
		#fm{
			margin:0;
			padding:10px 30px;
		}
		.ftitle{
			font-size:14px;
			font-weight:bold;
			color:#666;
			padding:5px 0;
			margin-bottom:10px;
			border-bottom:1px solid #ccc;
		}
		.fitem{
			margin-bottom:5px;
		}
		.fitem label{
			display:inline-block;
			width:80px;
		}
	</style>

<script>
    $(document).ready(function () {
        	
    	loadTreeGrid();
        	
    	//On resize of window
		doResize();

    	$(window).resize(function() {
    		doResize();
		});
	
    });	
</script>
	
</head>

<body>

	<%@include file="header-transaction.jspf" %>
<br>

	   <div id="status"></div>


	<div id="toolbar">
		<a href="#" id="toolbar-newAccount" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newAccount()">New</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editAccount()">Edit</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeAccount()">Remove</a>
	</div>		

	<!--
	fitColumns="false" 
	It was true, and was working fine when the html table tag gets data by itself, after moving it to 
	javascript, it became buggy, so we made it false.
	-->

	<table id="dg" title="Accounts Editing" class="easyui-treegrid" width="100%" style="height:450px"
			align="center" 
			toolbar="#toolbar" rownumbers="flase" fitColumns="false" singleSelect="true" idField="id" treeField="text">
		<thead>
			<tr>
				<th field="text" width="300">Account Name</th>
				<th field="description" width="100" align="right">Description</th>
				<th field="currency" width="100" align="right">Currency</th>
				<th field="type" width="100" align="right">Type</th>
				<th field="balanceFormated" width="100" align="right">Balance</th>
				
			</tr>
		</thead>
	</table>

	<div id="dlg" class="easyui-dialog" style="width:500px;height:320px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">Account Information</div>
		<form id="fm" method="post" novalidate>	
			<div class="fitem">
				<label>Account Name:</label><input id="text" name="text" style="width:200px;" required="true">
			</div>
			<div class="fitem">
			<label>Account Type:</label>
			<select class="easyui-combobox" id="type" name="type" style="width:200px;" required="true">
			    <option value="asset">Asset</option>
			    <option value="income">Income</option>
			    <option value="expense">Expense</option>
			    <option value="liability">Liability</option>
			    <option value="other">Other</option>
			</select>
			</div>
			<div class="fitem">
			<label>Currency:</label>
			<select class="easyui-combobox" id="currency" name="currency" style="width:200px;" required="true">
			    <option value="egp">Egyptian Pound</option>
			    <option value="usd">US Dollars</option>
			    <option value="sar">Saudia Riyals</option>
			</select>
			</div>
			<div class="fitem">
				<label>Description:</label><textarea rows="3" style="width:200px;" id="description" name="description"></textarea>
			</div>
			
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" id="save" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAccount()">Save</a>
		<a href="#" id="cancel" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>


</body>
</html>

