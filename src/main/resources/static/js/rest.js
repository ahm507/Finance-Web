//Target: Move all rest apis to this file



//Used by mobile only currently//NOT USED ANYMORE
//function loginRest(email, pass) {
//		var serviceUrl = 'login';
//        var resp = null;
//    	var data = {
//        	email: email,
//        	password: pass
//        };
//        $.post(serviceUrl, data, function (response) {
//        	return "success"
//        });
//
//        return "error";
//}

function getAccountsTreeRest() {
	var accountsTreeData = "";
   jQuery.ajax({
       url : 'rest/accounts/getAccounts.do?',
       async : false,
       type : 'POST',
       headers: getCsrfHeaders(),
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
        headers: getCsrfHeaders(),
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
        headers: getCsrfHeaders(),
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
        headers: getCsrfHeaders(),
        dataType : 'json',
        success : function(response) {
    		treeData = response;
        }
    });
	return treeData;
}

function getCsrfHeaders() {
	//You must injext the below meta headers in jsp files
	//<meta name="_csrf" content="${_csrf.token}"/>
	//<meta name="_csrf_header" content="${_csrf.headerName}"/>	
	var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
//	var data = {};
//	data[csrfParameter] = csrfToken;
	// using JQuery to send a non-x-www-form-urlencoded request
	var headers = {};
	headers[csrfHeader] = csrfToken;
	return headers;

}
