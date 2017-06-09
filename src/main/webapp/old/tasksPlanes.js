$(function() {
	if (localStorage.getItem("mail") == null) {
		localStorage.setItem("needCo", "true");
		document.location.href = "login.html";
	}
});

function pseudo(result) {
	var pseudo = JSON.stringify(result[0]);
	var role = JSON.stringify(result[1]);
	localStorage.setItem("pseudo", pseudo);
	localStorage.setItem("role", role);
	$("#pseudo").html(localStorage.getItem("mail"));
}
function logout() {
	// localStorage.clear();
	localStorage.removeItem("mail");
	localStorage.removeItem("role");
	getServerData("ws/login/logout", null);
	document.location.href = "login.html";
}

function getServerData(url, callback) {
	$.ajax({
		dataType : "json",
		url : url,
		success: callback(data)
	});
}

function getServerDataWithParam(url, callback, param) {
	$.ajax({
		dataType : "json",
		url : url,
		success: function(data)
		{
			callback(data, param);
		}
	});
}

function sub(r) {
	return r.toString().substring(1, r.toString().length - 1);
}

function getSTask(id) {
	var x = parseInt(id, 10);
	document.location.href = "taskView.html?id=" + x;
}

function getAllPlanes(result) {
	console.log(result);
	var tr = "";
	for (var i = 0; i < result.length; i++) {
		tr += "<tr> "
				+ "<td>"
				+ sub(JSON.stringify(result[i]['planeId']))
				+ "</td>"
				+ "<td>"
				+ sub(JSON.stringify(result[i]['planeType']))
				+ "</td>"
				+ "<td><button onclick='getPlane("
				+ sub(JSON.stringify(result[i]['planeId']))
				+ ")' "
				+ "class='btn icon-btn btn-primary'>  "
				+ "<span class='glyphicon btn-glyphicon glyphicon-eye-open'></span> "
				+ "</button></td>" + "</tr>";
	}
	$('#planeTbody').html(tr);
	var planes = $('#planeslist').DataTable({});
}

function formatDate(date) {
	var monthNames = [ "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December" ];

	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();
	return year + ' ' + monthNames[monthIndex] + ' ' + day;
}

var planeList;
function showPlanes(result) {
	console.log(result);
	planeList = result;
	var tr = "";
	for (var i = 0; i < result.length; i++) {
		var str = sub(JSON.stringify(result[i].planeType)); 
		//console.log("str : "+str);
		var id = sub(JSON.stringify(result[i].planeId));
		tr += "<tr> " 
				+ "<td>"+id+ "</td>" 
				+ "<td>"+str+ "</td>"
				+ "<td><button onclick='chooseTasksByPlane(\""+str+"\","+id+")' class='btn icon-btn btn-success'>  "
				+ "<span class='glyphicon glyphicon-ok'></span> "
				+ "</button></td>" + "</tr>";
	}
	$('#planeTbody2').html(tr);
	$('#planeslist2').DataTable({});
}

function chooseTasksByPlane(planetype, planeid) {
	getServerDataWithParam("ws/task/genericByPlane/"+planetype, chooseTasksByPlaneAux, planeid);
	$('#returnCreateTask').append('<li><a onclick="showPlanes('+planeList+')">'+planetype+'</a></li>');
}

function chooseTasksByPlaneAux(result, planeid) {
	var tr = "";
	for (var i = 0; i < result.length; i++) {
		tr += "<tr> "
				+ "<td>"
				+ sub(JSON.stringify(result[i].id))
				+ "</td>"
				+ "<td>"
				+ sub(JSON.stringify(result[i].ataCategory))
				+ "</td>"
				+ "<td>"
				+ sub(JSON.stringify(result[i].planeType))
				+ "</td>"
				+ "<td><button onclick='callViewTask("+ result[i].id
				+ ")' class='btn icon-btn btn-primary' data-toggle='modal' data-target='#myModal'><span class='glyphicon btn-glyphicon glyphicon-eye-open'></span></button></td>"
				+ "<td><button onclick='chooseStartDate(" + sub(JSON.stringify(result[i].id)) +","+planeid+ ")' "
				+ "class='btn icon-btn btn-success'>  "
				+ "<span class='glyphicon glyphicon-ok'></span> "
				+ "</button></td>" + "</tr>";
		// console.log(tr);
	}

	$('#createTaskBody')
			.html(
					"<table class='table table-striped'>"
							+ "<thead><tr><th>Generic task Id</th>  <th >ATA category</th> <th >Plane type</th> <th> show tasks </th> <th>Choose Task</th> </tr></thead>"
							+ "<tbody id='tbody'>" + tr + "</tbody>"
							+ "</table>");
}

function chooseStartDate(taskId, planeId) {
	console.log('Test'+taskId+ ',' +planeId);
	$('#returnCreateTask').append('<li><a> task n°'+taskId+'</a></li>');
	var res = '<div class="control-group">';
    res+='<label class="control-label">Choose a date</label>'; 
    res+='<div class="controls input-append date form_datetime" data-date-format="yyyy/mm/dd hh:mm"  data-link-field="dtp_input1">';
    res+='<input class="form-control" size="20" id="textDate" type="text" value="" readonly/>';
    res+='<span class="add-on"><i class="icon-remove"></i></span>';
	res+='<span class="add-on"><i class="icon-th"></i></span>';
    res+='</div>';
	res+='<input type="hidden" id="dtp_input1" value="" /><br/></div>';
	res+='<button type="submit" onclick="validTask('+taskId+','+planeId+')" class="btn btn-primary">Submit</button>'
	$("#createTaskBody").html(res);
	$('.form_datetime').datetimepicker({
	    //language:  'fr',
	    weekStart: 1,
	    todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
	    showMeridian: 1
	});
	//$("#createTaskBody").load("chooseStartDate.html");
}


function validTask(genericTaskId, planeId) {
	//var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
	var d = $('#textDate').val();
	 $.ajax({
			contentType: "application/json",
			dataType : "json",
			url: "ws/task/add",
			data: JSON.stringify({
			    "id": -1, 
			    "idTaskGeneric": genericTaskId, 
			    "startTime": d,
			    "endTime": d,
			    "planeId": planeId,
			    "taskStatus": 1, 
			    "mroId": -1,
			    "mccId": -1}),
			type: "PUT",
			processData: true
		    }).done(function() {
		    	document.location.href = "tasksPlanes.html";
				
		    }).fail(function() {
				alert( "An error has occurred. Thank you for re-verifying the information." );
		    });
}

function callViewTask(id) {
	getServerData("ws/task/" + id, viewTask)
}

function viewTask(task) {
	console.log(task);
	$('#viewTaskGen')
			.html('Task n°' + sub(JSON.stringify(task.taskGeneric.id)));
	$('#ata').html(+sub(JSON.stringify(task.taskGeneric.ataCategory)));
	$('#description').html(sub(JSON.stringify(task.taskGeneric.description)));
	$('#periodicity').html(sub(JSON.stringify(task.taskGeneric.periodicity)));
	$('#hangerNeed').html(
			sub(JSON.stringify((task.taskGeneric.hangarNeed == true))) ? 'yes'
					: 'no');
	$('#length').html(sub(JSON.stringify(task.taskGeneric.duration)));
	$('#planeType2').html(sub(JSON.stringify(task.taskGeneric.planeType)));
	console.log(task.taskGeneric.planeType);
	// $('#viewTaskGen').html('Task n°'+taskId);
}
// ----------------------------------------

$(function() {
	getServerData("ws/task/all", getAllTasks);
	getServerData("ws/plane/all", showPlanes);
});

$(function() {
	$("#pseudo").html(localStorage.getItem("mail"));

});


