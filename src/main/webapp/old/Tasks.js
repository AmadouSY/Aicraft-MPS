/** DECLARE VARIABLE **/
var taskList;

/** END DECLARE VARIABLE **/

$(function() {
	if(localStorage.getItem("role") == "mcc"){
		getServerData("ws/task/all", allTasks,null)
	}
	else {
		$("#body").html('vous êtes un mro');
	}
});


/** FUNCTIONS **/

function allTasks(result){
	taskList = result;
	getAllTasks();
}


/** SORT TASKS FUNCTION **/
function getAllTasks(){
	var line = "";
	for (var i = 0; i < taskList.length; i++) {
		line += printTask(taskList, i);
	}
	$('#tbody').html(line);
}

function getTaskToAssign(){
	var line;
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.taskStatus == 1 ){
            line += printTask(taskList, i);
		}
	}
	$('#tbody').html(line);
}

function getTasksInProgress(){
	var line;
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			var endTime = stringToDate(taskList[i].task.endTime);
			if(today < endTime && taskList[i].task.taskStatus == 2)
                line += printTask(taskList, i);
        }
    }
	$('#tbody').html(line);
}

function getTasksdone(){
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
/** END SORT TASKS FUNCTIONS **/

/** PRINT **/
function printTask(taskList, i){
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
	+ "<td><button onclick='viewTask("
	+  JSON.stringify(taskList[i])//sub(JSON.stringify(taskList[i].task.id))
	+ ")' "
	+ "class='btn icon-btn btn-primary'>  "
	+ "<span class='glyphicon btn-glyphicon glyphicon-eye-open'></span> "
	+ "</button></td>" + "</tr>";
}

/** VIEW TASKS **/

function viewTask(taskSelected){
	$('#tasksmenu').html()
	console.log('taskSelected : '+sub(JSON.stringify(taskSelected.task.id)));
	
}
/** END FUNCTIONS **/