/*var userName = "clientID";
var passWord = "secretKey";

function connection() {
	
	var user = document.forms["myForm"]["user"].value;
    var password = document.forms["myForm"]["pass"].value;
    if (user == "") {
    	alert('please enter your user name');
    	//$("#divAlert").html('<div class="alert alert-danger" role="alert"></div>');
    	//return null;
    }
    else if (password == "") {
    	//$("#divAlert").html("please enter a username");
    	alert('please enter your password');
    }
    else{
    	//document.location.href="tasks_and_planes.html";
    	//alert(user+','+password);
    	CallWebAPI(user,password);
    }
    
}


function authenticateUser(user, password)
{
    var token = user + ":" + password;

    // Should i be encoding this value????? does it matter???
    // Base64 Encoding -> btoa
    var hash = btoa(token); 

    return "Basic " + hash;
}

function CallWebAPI(userName,passWord) {

    // New XMLHTTPRequest
    var request = new XMLHttpRequest();
    request.open("POST", "https://xxx123.caspio.com/oauth/token", false);
    request.setRequestHeader("Authorization", authenticateUser(userName, passWord));  
    request.send();
    // view request status
    alert(request.status);
    response.innerHTML = request.responseText;
	document.location.href="tasks_and_planes.html";
}

function getServerData(url, success){
    $.ajax({
        dataType: "json",
        url: url
    }).done(success);
}
$(function(){
	//getServerData("ws/login/connectMcc",null);
});*/
var username = document.forms["myForm"]["user"].value;
var password = document.forms["myForm"]["pass"].value; 

function make_base_auth(user, password) {
  var tok = user + ':' + password;
  var hash = btoa(tok);
  return "Basic " + hash;
}
function connection(){
$.ajax
  ({
    type: "GET",
    //url: "index1.php",
    dataType: 'json',
    async: false,
    data: '{}',
    beforeSend: function (xhr){ 
        xhr.setRequestHeader('Authorization', make_base_auth(username, password)); 
    }
}).done(alert('Success'))
.fail(alert('failure'));

;
document.location.href="tasks_and_planes.html";
}