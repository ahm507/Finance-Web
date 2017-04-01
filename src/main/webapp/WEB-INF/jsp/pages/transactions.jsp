<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- CSRF Protection headers used in JSON requests in javascript -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>	
	
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">	
	<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
	<link rel="stylesheet" type="text/css" href="finance.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/rest.js"></script>
	<script type="text/javascript" src="js/data.js"></script>
	<script type="text/javascript" src="js/transactions.js"></script>

	<script>
	
	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		window.location.href = "mobile-transactions.html";
	}
/* 	if (typeof window.orientation != 'undefined') { //mobile device
 			window.location.href = "mobile-transactions.html";
	 	}
 */ 
	
		//$ is shorthand to jQuery
		$(document).ready(function() {

    		fillYearsDropDownList();
		    loadAccountTreeData();
		    doResize();
		    loadTransactionsGrid();
		    initEventHandlers();

		});
	
	</script> 	
	
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


<!-- Google Analytics -->

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
  ga('create', 'UA-50335967-1', 'salarycontrol.com');
  ga('send', 'pageview');
</script>




	<title>Transactions Management</title>
</head>
<body>

<%-- 	<% String curPage = "transactions";%>
 --%>
 
 <%@include file="header-transaction.jspf" %>
	
	
	<table border="0" width="99%">
	<tr>
		
<!-- Accounts Tree Start +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
		<td  valign="top" width="25%">
<!--   style="height:500px"   -->
			<table id="tt" title="Accounts Tree" class="easyui-treegrid"  
				data-options="idField:'id',treeField:'text'" showHeader="false" width="100%">
			    <thead>
			        <tr>
			            <th data-options="field:'text',width:280">Accounts Tree</th>
			        </tr>
			    </thead>
			</table>			
			
		</td>

	<!-- Expense Grid Start ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->	
		<td valign="top" width="70%">
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openNewExpenseDialog()">New</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openEditExpenseDialog()">Edit</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="openRemoveExpenseDialog()">Remove</a>
			</div>
								
			<table id="dg" width="100%" class="easyui-datagrid" fitColumns="true" singleSelect="true" 
					title="Transactions" toolbar="#toolbar">						
				<thead>
					<tr>
						<th field="description" width="100">Description</th>
						<th field="date" width="30">Date</th>						
						<th field="withdraw" width="50">Withdraw From Account</th>
	 					<th field="deposit" width="50">Deposit To Account</th>
						<th field="amountFormated" width="30">Amount</th>
						<th field="balanceFormated" width="30">Balance</th>
												
					</tr>
				</thead>
			</table>
	</td>
	</tr>
	</table>
	
	<div id="dlg" class="easyui-dialog" style="width:550px;height:320px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">Expense Information</div>
		<form id="fm" method="post" novalidate>	
			<div class="fitem">
				<label>Description:</label><input id="description" name="description"> <!--  required="true" -->
			</div>		
			<div class="fitem">
				<label>Withdraw Account:</label> 
				<select id="withdraw" name="withdraw" class="easyui-combotree" style="width:250px;" 
			        data-options="required:true">
			    </select>
			</div>
			<div class="fitem">
				<label>Deposit Account:</label> 
				<select id="deposit" name="deposit" class="easyui-combotree" style="width:250px;"  
			        data-options="required:true">
			    </select>
			</div>
			<div class="fitem">
				<label>Amount:</label><input name="amount" id="amount" value="0">
			</div>
			<div class="fitem">
				<label>Date:</label>
				<input class="easyui-datebox" data-options="formatter:ymdDateFormatter,parser:ymdDateParser" required="true" id="date" name="date" >
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a id="save" href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveExpense(true)">Save</a>
		<a href="#" class="easyui-linkbutton" id="save_and_add" iconCls="icon-ok" onclick="saveExpense(false)">Save & Add New</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	
	<!-- Expense Grid Start +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->	
	
	<!--  No footer -->
	
</body>
</html>
