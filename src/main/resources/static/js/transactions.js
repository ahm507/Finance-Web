//////////////////////////////////////////////////////////////////////////////////
/// FUNCTIONS ////
//////////////////////////////////////////////////////////////////////////////////

//$("head").append('<script type="text/javascript" src="js/rest.js"></script>');
//$("head").append('<script type="text/javascript" src="js/data.js"></script>');

//linked from the jsp files of transactions and charts
function ymdDateFormatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

//linked from the jsp file
function ymdDateParser(s) {
	if (!s) {
		return new Date();
	}
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	} else {
		return new Date();
	}
}

function loadTransactionsGrid() {
	//initialize data grid from cookies
	var nodeId = readCookie("transactions_selected_account");
	var nodeType = readCookie("transactions_selected_account_type");
	if (nodeId && nodeType) {
		//if the node is deleted somewhere, such if import a whole new records, I should handle it.
		var node = $('#tt').treegrid('find', nodeId);
		if (node) {
			$('#tt').treegrid('select', node.id);
			updateDataGrid(node.id, node.type);
		} else {
			eraseCookie("transactions_selected_account");
			eraseCookie("transactions_selected_account_type");
		}
	}
}

function loadAccountTreeData() {
	//Load Tree data once
	var accountsTreeData = getAccountsTreeRest();
	//Load tree to all elements
	$('#tt').treegrid('loadData', accountsTreeData);
	$('#withdraw').combotree('loadData', accountsTreeData);
	$('#deposit').combotree('loadData', accountsTreeData);
}

function fillYearsDropDownList() {
	$.ajax({
		url : 'rest/transactions/getYearList.do?',
		async : false,
		headers : getCsrfHeaders(),
		dataType : 'json',
		success : function(response) {
			var yearList = response;
			//JSON.parse()//eval()//already done
			var yearsDropDown = $("#transactions_year");
			yearsDropDown.append($("<option />").val("all").text("All"));
			$.each(yearList, function(i) {
				yearsDropDown.append($("<option />").val(yearList[i]).text(yearList[i]));
			});
			//set
			var selectedYear = readCookie("transactions_year");
			if (selectedYear) {
				$("#transactions_year").val(selectedYear);
			} else {
				//select last year
				document.getElementById("transactions_year").selectedIndex = yearList.length - 1;
			}
		}
	});
}

function doResize() {
	var currHeight = $(window).height();
	var bodyHeight = currHeight - 110;
	//              var currWidth = $(window).width();
	//              currWidth = currWidth - 30;
	//              var dg_with = currWidth * 0.75;
	//              var tt_with = currWidth * 0.25;
	//Setting through styles does not work !!!
	//$('#tt, #dg').css('height', currHeight); //works very well
	//Resize through resize function does not work !!!
	//$('dg').datagrid('resize', { height: bodyHeight});
	$('#dg').datagrid({
		height : bodyHeight
	});
	$('#tt').treegrid({
		height : bodyHeight
	});
	$('dg').datagrid('resize');
	//to fit the specified 100%
}

var url;
function openNewExpenseDialog() {
	$('#save_and_add').linkbutton('enable');
	$('#dlg').dialog('open').dialog('setTitle', 'New Transaction');
	$('#fm').form('clear');
	//Set today date
	var dt = new Date();
	var today = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
	$('#date').datebox('setValue', today);// set datebox value

	var row = $('#dg').datagrid('getSelected');
	//alert(acc);
	if (row) { //this is to reuse the current row values
		//For unknown reason, the next line crashes and did not load form correctly
		//$('#fm').form('load', row);
		$('#withdraw').combotree('setValue', row.withdrawId);
		$('#deposit').combotree('setValue', row.depositId);
		$('#description').val(row.description);
		$('#amount').val(row.amountFormated);
	}
	url = 'rest/transactions/saveTransaction.do';
}

//Just open the edit dialogue
function openEditExpenseDialog() {
	$('#save_and_add').linkbutton('disable');
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$('#dlg').dialog('open').dialog('setTitle', 'Edit Expense');
		//For unknown reason, the next line crashes and did not load form correctly
		//$('#fm').form('load', row);
		$('#deposit').combotree('setValue', row.depositId);
		$('#withdraw').combotree('setValue', row.withdrawId);
		$('#description').val(row.description);
		$('#amount').val(row.amountFormated);
		$('#date').datebox('setValue', row.date);
		url = 'rest/transactions/updateTransaction.do?id=' + row.id;
	}
}

//save Expense in case of New, New & Add, and Edit
function saveExpense(close) {

	//FIXME: depositVal is poor name. Amboiguous, if it is ID or text.
	var depositVal = $('#deposit').combotree('getText');
	var withdrawVal = $('#withdraw').combotree('getText');
	var amount = $('#amount').val();
	//0 amounts transfer is allowed as a mark for Credit card statement as example.
	//Still not allowed to transfer amounts from-to the same account.
	if (depositVal == withdrawVal && amount != 0) {
		alert("Please select different deposit account than withdraw account, or zero amount.");
		return false;
	}
	var rootElements = "Assets;Income;Expenses;Liabilities";
	if (rootElements.indexOf(depositVal) != -1) {
		alert("Please select leaf account.");
		return false;
	}
	if (rootElements.indexOf(withdrawVal) != -1) {
		alert("Please select leaf account.");
		return false;
	}

	var depositId = $('#deposit').combotree('getValue');
	var withdrawId = $('#withdraw').combotree('getValue');

//	$.post(url, {
//		amount : amount,
//		date : $('#date').datebox('getValue'),
//		description : $('#description').val(),
//		withdraw : withdrawId,
//		deposit : depositId
//
//	}, function(data, status) {
//		//alert("Data: " + data + "\nStatus: " + status);
//
//		if (close) {
//			$('#dlg').dialog('close');
//			// close the dialog
//		}
//		//The following line did not update data grid. seems it POST, so it does not work with REST
//		// $('#dg').datagrid('reload');
//		loadTransactionsGrid();
//
//	});

	jQuery.ajax({
		url : url,
		async : false,
		data : {
			amount : amount,
			date : $('#date').datebox('getValue'),
			description : $('#description').val(),
			withdraw : withdrawId,
			deposit : depositId
		},
		headers : getCsrfHeaders(),
		type : 'POST',
		dataType : 'json',
		success : function(result) {
			//alert("Data: " + data + "\nStatus: " + status);
			if (close) {
				$('#dlg').dialog('close');
				// close the dialog
			}
			//The following line did not update data grid. seems it POST, so it does not work with REST
			// $('#dg').datagrid('reload');
			loadTransactionsGrid();
		}
	});

	//
	//    $('#fm').form('submit', {
	//        url : url,
	//        onSubmit : function() {
	//
	//
	//            //return $(this).form('validate');
	//
	//            var depositId = $('#deposit').combotree('getValue');
	//            var withdrawId = $('#withdraw').combotree('getValue');
	//
	//           $.post(url,
	//                {
	//                    amount:  amount,
	//                    date:  $('#date').datebox('getValue'),
	//                    description:  $('#description').val(),
	//                    withdraw: withdrawId,
	//                    deposit: depositId
	//
	//                },
	//                function(data, status){
	//                    //alert("Data: " + data + "\nStatus: " + status);
	//
	//                    if (close) {
	//                        $('#dlg').dialog('close');
	//                        // close the dialog
	//                    }
	//                    //The following line did not update data grid. seems it POST, so it does not work with REST
	//                    // $('#dg').datagrid('reload');
	//                    loadTransactionsGrid();
	//
	//                });
	//
	//            return false; // DO NOT VALIDATE
	//
	//        },
	//        success : function(result) {
	//            var result = eval('(' + result + ')');
	//            if (result.success) {
	//                if (close) {
	//                    $('#dlg').dialog('close');
	//                    // close the dialog
	//                }
	//                //The following line did not update data grid. seems it POST, so it does not work with REST
	//                // $('#dg').datagrid('reload');
	//                loadTransactionsGrid();
	//            } else {
	//                $.messager.show({
	//                    title : 'Error',
	//                    msg : result.msg
	//                });
	//            }
	//        },
	//        error : function (result) {
	//                $.messager.show({
	//                    title : 'Error',
	//                    msg : result.msg
	//                    });
	//        }
	//    });
}

function openRemoveExpenseDialog() {
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$.messager.confirm('Confirm', 'Are you sure you want to remove this record?', function(answeredYes) {
			if (answeredYes) { //confirmed to delete
				jQuery.ajax({
					url : 'rest/transactions/removeTransaction.do?id=' + row.id,
					type : "DELETE",
					headers : getCsrfHeaders(),
					async : false,
					dataType : 'json',
					success : function(response) {
						if (response.success) {
							//The following line did not update data grid. seems it POST, so it does not work with REST
							//$('#dg').datagrid('reload');
							loadTransactionsGrid();
						} else {
							$.messager.show({// show error message
								title : 'Error',
								msg : response.msg
							});
						}
					}
				// error: function(xhr,status,error) {
				// } 
				});

			}
		});
	}
}

function updateDataGrid(nodeId, nodeType) {
	var year = $('#transactions_year').val();
	var gridData = getTransactionsRest(nodeId, nodeType, year);
	$('#dg').datagrid({
		data : gridData
	});
}

function initEventHandlers() {

	$('#tt').treegrid({
		onSelect : function(node) {
			createCookie("transactions_selected_account", node.id);
			createCookie("transactions_selected_account_type", node.type);
			updateDataGrid(node.id, node.type);
		}
	});

	//Hnadler for chaning date combo
	$("#transactions_year").change(function() {
		createCookie("transactions_year", $('#transactions_year').val(), 30);
		//reload the chart
		var node = $('#tt').treegrid('getSelected');
		if (node) {
			updateDataGrid(node.id, node.type);
		}
	});

	//Select last row in the grid after loading
	$('#dg').datagrid({
		onLoadSuccess : function(data) {
			$('#dg').datagrid('selectRow', data.total - 1);
		}
	});

	//On resize of window
	$(window).resize(function() {
		//GET NEW HEIGHT
		//var currHeight = $(window).height();
		//RESIZE BOTH ELEMENTS TO NEW HEIGHT
		//$('#tt, #dg').css('height', currHeight);
		//var size2 = {'height':currHeight};
		//$('dg').datagrid('resize', { height: 333, width:333});
		//$('#logAgents').datagrid('resize',{
		//    width: 600
		//  });
		doResize();
	});

}
