$(".alert").hide();
if(localStorage.getItem("mail") != null){
	localStorage.clear(); //Clean localStorage when recall login.html in order to re-loggin
	$('#ifConnect').html("<h1> You are already logged in. </h1>");
}

function postLogin(pseudo, pass, success){
	$.ajax({
        dataType: "json",
        url: "ws/login/"+pseudo+"/"+pass,
        type: "POST",
    }).done(success)
	.fail(success);
}

function login(result){
	var r =  JSON.stringify(result[0]);
	var role = r.toString().substring(1,r.toString().length-1);
	var idMRO="";
	if(role != "mcc" && role != "incorrect"){
		console.log("role :"+role);
		idMRO = role.split("-")[1];
		role = role.split("-")[0];
		console.log("id MRO : "+idMRO+"\n role : "+role);
		
	}//alert(role);
	console.log("role in login "+role)
	if(role === "incorrect"){
		
		$(".alert").html("Wrong Username or Password");
		$(".alert").show();
		
	}
	else {
		
		var pseudo =localStorage.getItem("tmp");
		localStorage.setItem("mail",pseudo);
		localStorage.setItem("role",role);
		localStorage.setItem("idMRO",idMRO);
		document.location.href="tasksPlanes.html";
	}	
}

$("#ok").click(function (){
	var pseudo =$('#username').val();
	var pass = $('#password').val();
	if(pseudo == ""){
		$(".alert").html("<a class='close' data-dismiss='alert' href='#'>×</a>Username required");
		$(".alert").show();
	}
	else if (pass == "") {
		$(".alert").html("<a class='close' data-dismiss='alert' href='#'>×</a>password required");
		$(".alert").show();
	}
	else {
		localStorage.setItem("tmp",pseudo);
		
		var jqxhr = $.post( "ws/login/"+pseudo+"/"+pass, function() {
			  //alert( "success" );
			})
			  .done(login)
			  .fail(function() {
			    alert( "error" );
			  });
		//postLogin(pseudo,pass,login);
	}
});



