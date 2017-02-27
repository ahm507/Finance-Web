<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	//If forward to login.jsp
	pf.webmvc.JspRequests.processIfNotLoggedUser(request, response);
%>

<!DOCTYPE html>
<html>
    <head>
		<link rel="stylesheet" type="text/css" href="easyui/themes/finance/easyui.css">
		<link rel="stylesheet" type="text/css" href="finance.css">
		<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
		<script type="text/javascript" src="easyui/jquery.min.js"></script>
		<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
        <title>Import Accounts and Transaction</title>
        
		<style>
		  div.warning {
			color: red;
		  }
		  </style>
        
    </head>
    <body>

  	<% String curPage = "import";%>
	<%@include file="header.jspf" %>

   <br>
   <br>
    
    <h1>Import CSV files</h1>
    
	 	<form method="POST" action="upload" enctype="multipart/form-data" >
            <input type="file" name="file" id="file" /> 
            <br>
            <input type="submit" value="     Upload and Import     " name="upload" id="upload" />
        </form>

<hr>
<br>

<h1>Instructions: </h1>

<ol align="left">
	<li><div class="warning">
	All current data will be deleted as import makes clean import, even if import failed, your data will be lost ! </div></li> 

	<li>Comma delimited file, known as CSV <br></li>

	<li>Note that: Account is separated by ":", and only two levels are accepted. The first level 
	must be one of the following names: Assets, Income, Expenses, Liabilities. Second level can be anything given that no 
	two items on the same level has the same name. <br></li>
	
	<li>Here is sample entry: <br> </li>
</ol>

	
	<table width="90%" border="1" cellpadding="4" cellspacing="0" border="0">
	<tr>
		<th>Date</th>
		<th>Description</th>
		<th>Account</th>
		<th>Transfer from/to</th>
		<th>Amount</th>
	</tr>
	<tr bgcolor="#ffffff">
		<td class="text-cell" rowspan="1" colspan="1">17/05/2006</td>
		<td class="text-cell" rowspan="1" colspan="1">Historical Income - Land Purchase</td>
		<td>Assets:Land</td>
		<td>Income:Historical- Untracked</td>
		<td class="number-cell" rowspan="1" colspan="1">
		102,000.00
		</td>
	</tr>
	<tr>
		<td class="text-cell" rowspan="1" colspan="1">10/08/2008</td>
		<td class="text-cell" rowspan="1" colspan="1">Historical Income - Rayan Initial Amount</td>
		<td>Income:Historical- Untracked</td>
		<td>Assets:Rayan Flat Installment</td>
		<td class="number-cell" rowspan="1" colspan="1">
		70,000.00
		</td>
	</tr>
	
	<tr bgcolor="#ffffff">
		<td class="text-cell" rowspan="1" colspan="1">10/08/2008</td>
		<td class="text-cell" rowspan="1" colspan="1">Historical Income - Rayan Initial Amount</td>
		<td>Assets:Rayan Flat Installment</td>
		<td>Income:Historical- Untracked</td>
		<td class="number-cell" rowspan="1" colspan="1">
		70,000.00
		</td>
	
	</tr>
	</table>

	<%@include file="footer.jspf" %>

        
    </body>
</html>