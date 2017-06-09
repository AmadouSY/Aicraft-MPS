$(function() {
	if (localStorage.getItem("mail") == null) {
		localStorage.setItem("needCo", "true");
		document.location.href = "login.html";
	}
});
function getServerData(url, success) {
	$.ajax({
		dataType : "json",
		url : url
	}).done(success);
}
function importMDP(){
		var file = $("#uploadMPD")[0].files[0];
		var reader = new FileReader();
			reader.onload = function(){
				var text = reader.result;
				//var node = document.getElementById('output');
				//node.innerText = text;
				console.log(reader.result.substring(0, 200));
				getServerData("ws/task/addGenericTasks/"+JSON.parse('\"'+text+'\"'),alert("upload done"));
			};
		reader.readAsText($("#uploadMPD")[0].files[0]);
}

$(function() {
	getServerData("ws/task/all", getAllTasks);
});

$(function() {
	getServerData("ws/mro/all", getAllMRO);
});


 var taskList;
 var mroList;
 function callGetAllTasks(){
		getServerData("ws/task/all", getAllTasks);
	}
 
function getAllTasks(result){
	taskList = result;
	var tr = "";
	// console.log("in get all tasks function and tr = "+tr);
	for (var i = 0; i < result.length; i++) {
        tr += printTask(result, i);
		// console.log(tr);
	}
	
	$('#tbody').html(tr);
} 
var select='';
function getAllMRO(result){
	mroList = result;
	
	 
	select+='<select class="form-control" id="sel1">';
	for (var i=0; i<5; i++){
		
		select+='<option>'+JSON.stringify(result[i].name)+'</option>';
	}    
	select+='</select>';

} 

function sub(r) {
	return r.toString().substring(1, r.toString().length - 1);
}

//<0
function taskToAssign(){
	var tr = "";
	var tmp;
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.taskStatus == 1 ){
            tr += printTask(taskList, i);

		}
	}
	$('#tbody').html(tr);
}
//>0 and dateNow < Enddate

function stringToDate(dateStr){
	return new Date(dateStr.substring(0,4),dateStr.substring(5,7),
			dateStr.substring(8,10),dateStr.substring(11,13),
			dateStr.substring(14,16));
	
}

function inProgress(){
	var tr = "";
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			var endTime = stringToDate(taskList[i].task.endTime);
			if(today < endTime && taskList[i].task.taskStatus == 2)
                tr += printTask(taskList, i);
        }
    }
	$('#tbody').html(tr);
}
	

//>0 and dateNow > EndDate
function done(){
	var tr = "";
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			//var endTime = stringToDate(taskList[i].task.endTime);
			if(taskList[i].task.taskStatus == 3)
                tr += printTask(taskList, i);
		}
	}
	$('#tbody').html(tr);
}

function expired(){
	var tr = "";
	for (var i = 0; i < taskList.length; i++) {
		//if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			var endTime = stringToDate(taskList[i].task.endTime);
			if(today > endTime && taskList[i].task.taskStatus != 3)
                tr += printTask(taskList, i);
        //}
    }
	$('#tbody').html(tr);
}

function callShowTask(id) {
	getServerData("ws/task/" + id, showTask)
}
function showTask(task) {
	console.log(task);	
	var res='<div class="col-md-1"></div>';
	res+='<div class="col-md-10">';
	res+='<div>';
	res+='<h3><br>Task n° ' + (sub(JSON.stringify(task.taskGeneric.id)))+' </br></h3>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><br><b> ATA </b></br></div><div class="col-md-10"><br>'+(sub(JSON.stringify(task.taskGeneric.ataCategory)))+'</br></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> Description </br></b></div><div class="col-md-10">'+'<p align="justify"><br>'+(sub(JSON.stringify(task.taskGeneric.description)))+'</br></p></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> Periodicity </br></b></div><div class="col-md-10"><br>'+(sub(JSON.stringify(task.taskGeneric.periodicity)))+'</br></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> Hanger Need </br></b></div><div class="col-md-10"><br>'+(sub(JSON.stringify((task.taskGeneric.hangarNeed == true))) ? 'yes': 'no')+'</br></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> Length </br></b></div><div class="col-md-10"><br>'+(sub(JSON.stringify(task.taskGeneric.duration)))+'</br></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> Plane Type </br></b></div><div class="col-md-10"><br>'+(sub(JSON.stringify(task.taskGeneric.planeType)))+'</br></div>';
	res+='</div>';
	res+='<div class="row">';
	res+='<div class="col-md-2"><b><br> MRO </br></b></div><div class="col-md-10">';
	res+='<div class="form-group">';
	res+=''+select;
	res+='</div>';
	res+='<div class="row" align="center">';
	res+='<br></br>';
	res+='<button class="btn btn-primary" type="submit">Valider</button>';
	res+='<br></br>';
	res+='</div>';
	
	$("#tab-content").html(res);
	//console.log(task.taskGeneric.planeType);

	// $('#viewTaskGen').html('Task n°'+taskId);
}

function printTask(result, i){
	console.log(result[i]);
    return "<tr> "
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].taskGeneric.ataCategory))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].task.startTime))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].task.endTime))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].taskGeneric.planeType))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].task.planeId))
	+ "</td>"
	+ "<td><button onclick='callShowTask("
	+ sub(JSON.stringify(taskList[i].task.id))
	+ ")' "
	+ "class='btn icon-btn btn-primary'>  "
	+ "<span class='glyphicon btn-glyphicon glyphicon-eye-open'></span> "
	+ "</button></td>" + "</tr>";
}