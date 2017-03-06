
//	$("head").append('<script type="text/javascript" src="/js/rest.js"></script>');
    

    function loadTreeGrid() {
    	var treeData = getTreeRest();
 		$('#dg').treegrid('loadData', treeData);
    }   

    function doResize() {
		var currHeight = $(window).height();
		var bodyHeight = currHeight - 100;
//     				//Setting through styles does not work !!!
//     				//$('#tt, #dg').css('height', currHeight); //works very well
//     				//Resize through resize function does not work !!!
//     				//$('dg').datagrid('resize', { height: bodyHeight});
		$('#dg').datagrid({
			height: bodyHeight
		});
		$('dg').treegrid('resize'); //to fit the specified 100%
	}        
        
	var url;
	function newAccount(){
		$('#dlg').dialog('open').dialog('setTitle','New Account');
		$('#fm').form('clear');
		url = 'rest/accounts/saveAccount.do';
		//set the parent combo tree to current node
		var acc = $('#dg').datagrid('getSelected');
		if(acc) {
			$('#parent').combotree('setValue', acc.id);
			$('#type').combobox('setValue', acc.type);
		} else {
			$('#parent').combotree('setValue', '0'); //select top level first element
			$('#type').combobox('setValue', 'expense');
			$('#currency').combobox('setValue', 'egp');

		}
	}
	
	//Just open the edit dialogue
	function editAccount(){
		var row = $('#dg').datagrid('getSelected');
		if(row && row.parent=="0") {
			alert("Editing is not allowed for root elements.");
			return; // do not allow edinting the titles of root accounts
		}
		if (row){
			$('#dlg').dialog('open').dialog('setTitle','Edit Account');
			$('#fm').form('load',row);
			url = 'rest/accounts/updateAccount.do?id='+row.id;
		}
	}
	
	function saveAccount(){

	 var data = {

            text: $('#text').val(),
            description: $('#description').val(),
            type: $('#type').combobox('getValue'),
            currency: $('#currency').combobox('getValue')
	    };
	 $.post(url, data, function(data, status){
//                          alert("Data: " + data + "\nStatus: " + status);
                            var accName = $('#text').val();
                            $('#status').text(accName);

                            $('#dlg').dialog('close');		// close the dialog
                            $('#dg').treegrid('reload');	// reload the user data
                            location.reload(true); //true to force not get from cache

                          }
             );


//
//		$('#fm').form('submit',{
//			url: url,
//			onSubmit: function(){
//				return $(this).form('validate');
//			},
//			success: function(result){
//				var result = eval('('+result+')');
//				if (result.success){
//					var accName = $('#text').val();
//					$('#status').text(accName);
//
//					$('#dlg').dialog('close');		// close the dialog
//					$('#dg').treegrid('reload');	// reload the user data
//					location.reload(true); //true to force not get from cache
////					loadTreeGrid();
//				} else {
//					$.messager.show({
//						title: 'Error',
//						msg: result.msg
//					});
//				}
//			}
//		});
	}

	function removeAccount(){
		var row = $('#dg').datagrid('getSelected');
		if(row && row.type==0)
			return; // do not allow deleting root accounts
		if (row){
			$.messager.confirm('Confirm','Are you sure you want to remove this record?',function(r){
				if (r){
					$.post('rest/accounts/removeAccount.do',
							{
								id:row.id
							},
							function(result){
						if (result.success){
							$('#dg').datagrid('reload');	// reload the user data
							location.reload(true); //true to force not get from cache
						} else {
							$.messager.alert('Error', 'Error removing account, please ensure to remove all linked transactions first.');
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.msg
							});
						}
					},'json');
				}
			});
		}
	}
