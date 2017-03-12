//Target: Move all rest apis to this file



//Used by mobile only currently
function loginRest(email, pass) {
		var serviceUrl = 'login';
        var resp = null;
    	var data = {
        	email: email,
        	password: pass
        };
        $.post(serviceUrl, data, function (response) {
        	return "success"
        });

        return "error";
        
//        $.ajax({
//    		  url: serviceUrl,
//    		  async: false,
//    		  dataType: 'json',
//    		  success: function (response) {
//    			  resp = response;//JSON.parse()//eval()//already done
//    		  }
//    		});
//    	return resp;
}

function getAccountsTreeRest() {
    var accountsTreeData = "";
   jQuery.ajax({
       url : 'rest/accounts/getAccounts.do?',
       async : false,
       type : 'POST',  
       dataType : 'json',
       success : function(response) {
           accountsTreeData = response;
           //JSON.parse()//eval()//already done
       }
   });

   return accountsTreeData;   
}

function getTransactionsRest(nodeId, nodeType, year) {
    var restUrl = "rest/transactions/getYearTransactions.do?accountId=" + nodeId + "&year=" + year;
    if (nodeType == "asset" || nodeType == "liability") {
    	restUrl = "rest/transactions/getUpToYearTransactions.do?accountId=" + nodeId + "&year=" + year;
    }
    var dataGridData;
    jQuery.ajax({
        url : restUrl,
        type: "GET",
        async : false,
        dataType : 'json',
        success : function(response) {
            dataGridData = response;
        }
    });
    return dataGridData;
}

function getRest(restUrl) {
    var dataGridData;
    jQuery.ajax({
        url : restUrl,
        type: "GET",
        async : false,
        dataType : 'json',
        success : function(response) {
            dataGridData = response;
        }
    });
    return dataGridData;
}

function getTreeRest() {
	var treeData;
	jQuery.ajax({
        url : 'rest/accounts/getAccounts.do?',
        async : false,
        type : 'POST',  
        dataType : 'json',
        success : function(response) {
    		treeData = response;
        }
    });
	return treeData;
}
