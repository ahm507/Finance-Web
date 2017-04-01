<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<html>
<head>

<!-- CSRF Protection headers used in JSON requests in javascript -->
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>	
	

<!--  Research using Google Charts https://developers.google.com/chart/interactive/docs/quick_start -->

<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="easyui/themes/finance/easyui.css">
<link rel="stylesheet" type="text/css" href="finance.css">
<script type="text/javascript" src="easyui/jquery.min.js">
	
</script>
<!-- jquery-1.10.1 required -->
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="jqwidgets3.0/jqxcore.js"></script>
<script type="text/javascript" src="jqwidgets3.0/jqxchart.js"></script>
<script type="text/javascript" src="jqwidgets3.0/jqxdata.js"></script>
<script type="text/javascript" src="js/rest.js"></script>
<script type="text/javascript" src="js/transactions.js"></script>
<script type="text/javascript" src="js/data.js"></script>
<script type="text/javascript" src="js/charts.js"></script>

<script>
	$(document).ready(function() {
		fillYearsComboBox();
		initSelectedChartTypeAndKind();
		setupChart();
		initTransactionDlgTreeGrids();
		doResize();
		initCallbacks();
	});
</script>

<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	color: #666;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>


<title>Charts</title>
</head>
<body>

	<%@include file="header-charts.jspf"%>

	<br>
	<div id='jqxChart'
		style="width: 99%; height: 300px; position: relative; left: 0px; top: 0px;">
	</div>
	<br>
	<b>
		<div id='eventText' style="width: 600px; height: 30px"></div>
	</b>

	<div id="toolbar">
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true"
			onclick="newExpense()">New</a> <a href="#" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editExpense()">Edit</a> <a
			href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
			onclick="removeExpense()">Remove</a>
	</div>

	<table id="dg" class="easyui-datagrid" fitColumns="true"
		singleSelect="true">
		<thead>
			<tr>
				<th field="description">Description</th>
				<th field="date">Date</th>
				<th field="withdraw">Withdraw From Account</th>
				<th field="deposit">Deposit To Account</th>
				<th field="amountFormated">Amount</th>
				<th field="balanceFormated">Balance</th>
			</tr>
		</thead>
	</table>

	<!-- The dialoge ======================================================== -->

	<div id="dlg" class="easyui-dialog"
		style="width: 550px; height: 320px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<div class="ftitle">Expense Information</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>Description:</label><input id="description"
					name="description">
				<!--  required="true" -->
			</div>
			<div class="fitem">
				<label>Withdraw Account:</label> <select id="withdraw"
					name="withdraw" class="easyui-combotree" style="width: 250px;"
					data-options="required:true">
				</select>
			</div>
			<div class="fitem">
				<label>Deposit Account:</label> <select id="deposit" name="deposit"
					class="easyui-combotree" style="width: 250px;"
					data-options="required:true">
				</select>
			</div>
			<div class="fitem">
				<label>Amount:</label><input name="amount" id="amount" value="0">
			</div>
			<div class="fitem">
				<label>Date:</label> <input class="easyui-datebox"
					data-options="formatter:ymdDateFormatter,parser:ymdDateParser"
					required="true" id="date" name="date">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
			onclick="saveExpense(true)">Save</a> <a href="#"
			class="easyui-linkbutton" id="save_and_add" iconCls="icon-ok"
			onclick="saveExpense(false)">Save & Add New</a> <a href="#"
			class="easyui-linkbutton" iconCls="icon-cancel"
			onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>


</body>
</html>