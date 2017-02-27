//Charts Scripts

function initTransactionDlgTreeGrids() {
	// fill transaction editor dialog with tree
	// Load Tree data once
	var accountsTreeData = null;
	$.ajax({
		url : 'rest/accounts/getAccounts.do?',
		async : false,
	    type: 'POST',
		dataType : 'json',
		success : function(response) {
			accountsTreeData = response;// already parsed with
			// this api
		}
	});
	
	// Load tree to all elements
	$('#withdraw').combotree('loadData', accountsTreeData);
$('#deposit').combotree('loadData', accountsTreeData);

}

function initSelectedChartTypeAndKind() {
	var selectedType = readCookie("chart_type");
	if (selectedType) {
		$("#chart_type").val(selectedType);
	}
	
	var selectedKind = readCookie("chart_kind");
	if (selectedKind) {
		$("#chart_kind").val(selectedKind);
	}
}

function fillYearsComboBox() {
	$.ajax({
		url : 'rest/transactions/getYearList.do?',
		async : false,
	    type: "GET",
		dataType : 'json',
		success : function(response) {
			var yearList = response;// JSON.parse()//eval()//already
			// done
			var yearsDropDown = $("#chart_year");
			yearsDropDown.append($("<option />").val("all").text("All"));
			$.each(yearList, function(i) {
				yearsDropDown.append($("<option />").val(yearList[i]).text(yearList[i]));
			});
			// set
			var selectedYear = readCookie("chart_year");
			// alert(selectedYear);
			if (selectedYear) {
				$("#chart_year").val(selectedYear);
			} else {
				// select last year
				document.getElementById("chart_year").selectedIndex = yearList.length - 1;
			}
		}
	});
}

function doResize() {
	var currHeight = $(window).height();
	var bodyHeight = (currHeight / 4.5);

	$('#dg').datagrid({
		height : bodyHeight
	});

	$('dg').datagrid('resize'); // to fit the specified 100%
}

function setupChart() {
	var st = getChartSettings();
	if (st != null) {
		$('#jqxChart').jqxChart(st);
	} else {
		alert("No data exist or session expired");
	}
}

function getChartSettings() {
	var type = $('#chart_type').val();
	var year = $('#chart_year').val();
	var chart_kind = $('#chart_kind').val();
	var accounts = null;
	$.ajax({
		url : 'rest/charts/getChartDataFields.do?type=' + type,
		async : false,
        type: "GET",
		dataType : 'json',
		success : function(response) {
			accounts = response;
		}
	});
	// Build dataFields variable
	if (accounts == null || accounts.length == 0) {
		return null;
	}
	var dataFields = [ {
		name : 'Month',
		type : 'date'
	} ];
	for ( var i = 0; i < accounts.length; i++) {
		var acc = {
			name : accounts[i].name
		};
		dataFields = dataFields.concat(acc);
	}
	// Build seriesDef variable
	var seriesDef = [ {
		dataId : accounts[0].id,
		dataField : accounts[0].name,
		displayText : accounts[0].name
	} ];
	for ( var i = 1; i < accounts.length; i++) {
		var acc = {
			dataId : accounts[i].id,
			dataField : accounts[i].name,
			displayText : accounts[i].name
		};
		seriesDef = seriesDef.concat(acc);
	}
	var dataUrl = "rest/charts/getExpensesTrend.do?year=" + year
			+ "&type=" + type;
	var source = {
		datatype : "json",
        type: "GET",
		datafields : dataFields,
		url : dataUrl
	};

	var dataAdapter = new $.jqx.dataAdapter(source, {
		autoBind : true,
		async : false,
		downloadComplete : function() {
		},
		loadComplete : function() {
		},
		loadError : function() {
		}
	});

	/*
	 * unitInterval: Math.round(dataAdapter.records.length /
	 * 6), gridLinesInterval:
	 * Math.round(dataAdapter.records.length / 3),
	 */
	// description: "Show accounts expenses over months for
	// analysis",
	var settings = {
		title : "  ",
		description : " ",
		showLegend : true,
		enableAnimations : true,
		padding : {
			left : 5,
			top : 5,
			right : 5,
			bottom : 5
		},
		titlePadding : {
			left : 90,
			top : 0,
			right : 0,
			bottom : 10
		},
		source : dataAdapter,
		categoryAxis : {
			dataField : 'Month',
			showGridLines : true,
			axisSize : 'auto',
		},
		colorScheme : 'scheme01',
		seriesGroups : [ {
			type : chart_kind,
			columnsGapPercent : 30,
			seriesGapPercent : 0,
			valueAxis : {
				displayValueAxis : true,
				description : 'Money'
			},
			click : chartBarClickHandler,
			series : seriesDef
		} ]
	};
	return settings;
}

function chartBarClickHandler(e) {
	var y = $('#chart_year').val();
	// Set grid to current transactions
	var type = $('#chart_type').val();
	var transUrl;
	if (type == "asset" || type == "liability") {
		transUrl = "rest/transactions/getUpToMonthTransactions.do?accountId="
				+ e.serie.dataId + "&year=" + y + "&month=" + e.elementIndex;
	} else if (type == "income" || type == "expense") {
		transUrl = "rest/transactions/getMonthTransactions.do?accountId="
				+ e.serie.dataId + "&year=" + y + "&month=" + e.elementIndex;
	} else {
		transUrl = "rest/transactions/getMonthTransactions.do?";
	}
//	loadExpensesGridWithRest('#dg', transUrl);
	$('#dg').datagrid({
		data:getRest(transUrl)
		});
	
	// Select last row in the grid after loading
	$('#dg').datagrid({
		onLoadSuccess : function(data) {
			$('#dg').datagrid('selectRow', data.total - 1);
		}
	});
	var value = e.elementValue;
	var formattedValue = value.toFixed(2).replace(
			/(\d)(?=(\d{3})+\.)/g, "$1,");

	// + ' is ' + e.elementValue + ' LE'
	var eventData = formattedValue + ' LE for '
			+ e.serie.dataField + ' in '
			+ getMonthName(e.elementIndex);
	$('#eventText').text(eventData);
}

function getMonthName(m) {
	switch (m) {
	case 0:
		return "Jan";
	case 1:
		return "Feb";
	case 2:
		return "Mar";
	case 3:
		return "Apr";
	case 4:
		return "May";
	case 5:
		return "Jun";
	case 6:
		return "Jul";
	case 7:
		return "Aug";
	case 8:
		return "Sep";
	case 9:
		return "Oct";
	case 10:
		return "Nov";
	case 11:
		return "Dec";
	default:
	    return m; //in case of year charts
	}
}

var url;
function newExpense() {
	$('#save_and_add').linkbutton('enable');
	$('#dlg').dialog('open').dialog('setTitle', 'New Transaction');
	$('#fm').form('clear');
	// Set today date
	var dt = new Date();
	var today = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-"
			+ dt.getDate();
	$('#date').datebox('setValue', today); // set datebox value
	// var v = $('#dd').datebox('getValue'); // get datebox value

	var acc = $('#tt').tree('getSelected');
	// alert(acc);
	if (acc) {
		// if you want to fine tune:
		// Income type -> withdraw only
		// Expense type -> deposit only
		// Asset and Liability could be both
		// $('#expense').tree('select', node.target);
		// alert(acc.id);
		$('#deposit').combotree('setValue', acc.id);
		$('#withdraw').combotree('setValue', acc.id);
		// var node = $('#withdraw').tree('find', acc.id);
		// alert(node.id);
	}

	url = 'rest/transactions/saveTransaction.do';
}
// Just open the edit dialogue
function editExpense() {
	$('#save_and_add').linkbutton('disable');

	var row = $('#dg').datagrid('getSelected');
	// alert(row);
	if (row) {
		$('#dlg').dialog('open').dialog('setTitle', 'Edit Expense');
		// For unknown reason, the next line crashes and did not load form
		// correctly
		// $('#fm').form('load',row);
		$('#deposit').combotree('setValue', row.depositId);
		$('#withdraw').combotree('setValue', row.withdrawId);
		$('#description').val(row.description);
		$('#amount').val(row.amount);
		$('#date').datebox('setValue', row.date);
		url = 'rest/transactions/updateTransaction.do?id=' + row.id;
	}
}

function saveExpense(close) {
	$('#fm').form( 'submit',
		{
			url : url,
			onSubmit : function() {
				var depositVal = $('#deposit').combotree('getText');
				var withdrawVal = $('#withdraw').combotree('getText');
				if (depositVal == withdrawVal) {
					alert("Please select different deposit account than withdraw account.");
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
				return $(this).form('validate');
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					if (close) {
						$('#dlg').dialog('close'); // close the
					}
					updateGridAndChart();
				} else {
					$.messager.show({
						title : 'Error',
						msg : result.msg
					});
				}
			}
		});
}

function removeExpense() {
	var row = $('#dg').datagrid('getSelected');
	if (row) {
		$.messager.confirm('Confirm',
				'Are you sure you want to remove this record?', function(r) {
					if (r) {
						$.post('rest/transactions/removeTransaction.do', {
							id : row.id
						}, function(result) {
							if (result.success) {
								// $('#dg').datagrid('reload'); // reload the
								// user data
								updateGridAndChart();

							} else {
								$.messager.show({ // show error message
									title : 'Error',
									msg : result.msg
								});
							}
						}, 'json');
					}
				});
	}
}

function updateGridAndChart() {
	// $('#dg').datagrid('reload'); //does not work
	// update chart
	// $('#jqxChart').jqxChart('refresh'); //does not work actually
	// just a workaround until a better option is researched
	location.reload(true); // true to force not get from cache
}

function initCallbacks() {
	// On resize of window
	$(window).resize(function() {
		doResize();
	});
	$("#chart_year").change(function() {
		createCookie("chart_year", $('#chart_year').val(), 30);
		setupChart();
	});
	$("#chart_type").change(function() {
		createCookie("chart_type", $('#chart_type').val(), 30);
		setupChart();
	});
	$("#chart_kind").change(function() {
		createCookie("chart_kind", $('#chart_kind').val(), 30);
		setupChart();
	});
}
