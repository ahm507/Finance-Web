<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	
	<title>Export Transactions and Accounts</title>	
</head>
<body>

	<%@include file="header-transaction.jspf" %>

<br>
<br>

<h1>
Exporting Accounts and transactions
</h1>
<br>
<br>

This will export accounts and transactions as comma delimited file in one table. You can keep the file as 
a backup that you can restore later.
<br><br>
To proceed, click Exporting <a href="exporting">Accounts and Transactions</a>
<br>
<br>
<br>
	<%@include file="footer.jspf" %>




</body>
</html>