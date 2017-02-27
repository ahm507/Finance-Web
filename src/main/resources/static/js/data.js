//Cookies and Browser data storage APIs

function createCookie(name, value, days) {
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    } else
        var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
        c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0)
            return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, "", -1);
}


//Browser storage data
function storageSetValue(name, value) {
	window.localStorage.setItem(name, value);
}

function storageGetValue(name) {
	return window.localStorage.getItem(name);
}

function storageRemoveValue(name) {
	window.localStorage.removeItem(name);	
}

//Transactions Handlers
function storeTransaction(recordObj) {
	var gridDataJson = storageGetValue("grid-data");
	if(gridDataJson == null) {
		var gridDataArr = [recordObj];
		gridDataJson = JSON.stringify(gridDataArr);
	} else {
		var gridDataObj = jQuery.parseJSON(gridDataJson);
		gridDataObj.push(recordObj);
		gridDataJson = JSON.stringify(gridDataObj);
	}
	storageSetValue("grid-data", gridDataJson);
}

function removeTransaction(selectedIndex) {
	var gridData = storageGetValue("grid-data");
	var gridDataObject = jQuery.parseJSON(gridData);
	gridDataObject.splice(selectedIndex, 1); //remove
	gridDataJson = JSON.stringify(gridDataObject);
	storageSetValue("grid-data", gridDataJson);
}

function readTransactions() {
	var gridData = storageGetValue("grid-data");
	return jQuery.parseJSON(gridData);
}        

function storeEmailAndPassword(email, password) {
	storageSetValue("email", email);
	storageSetValue("password", password);
	storageSetValue("loggedIn", "true");
}

function readEmail() {
	return storageGetValue("email");
}

function readPassword() {

	return storageGetValue("password");
}

function readIsLoggedIn() {
	return storageGetValue("loggedIn");

}

function storeLogout() {
//	storageRemoveValue("email"); //keep it to set editor of login
	storageRemoveValue("password");
	storageRemoveValue("loggedIn");
}

//class AccountsTreeData
var AccountsTreeData = function () {
};

AccountsTreeData.prototype.save = function(treeDataObject) {
	treeDataJson = JSON.stringify(treeDataObject);
	storageSetValue("tree-data", treeDataJson);
};

AccountsTreeData.prototype.load = function() {
	var gridData = storageGetValue("tree-data");
	return jQuery.parseJSON(gridData); // as an objects
};

