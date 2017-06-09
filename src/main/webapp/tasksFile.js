/** DECLARE VARIABLE **/
var taskList;
var planeList;
var mroChosen;
var listMros;
/** END DECLARE VARIABLE **/
$('#example').DataTable({});

$(function() {
	if(localStorage.getItem("role") == "mcc"){
		getServerData("ws/task/all", allTasks,null)
		getServerData("ws/mro/all", allMros,null)
		getServerData("ws/plane/all", allPlanes,null)
	}
	else {
		$("#createTaskToggle").hide();
		$("#uploadTaskToggle").hide();
		$("#tasksToAssign").hide();
		var im = localStorage.getItem("idMRO");
		console.log('im = '+im);
		getServerData("ws/task/mro/"+1, allTasks,null)
		getServerData("ws/plane/all", allPlanes,null)
		taskList = null;
		
		//$("#body").html('vous êtes un mro');
	}
	
});


/** FUNCTIONS **/

function allTasks(result){
	taskList = result;
	console.log(taskList);
	//getAllTasks();
	if(localStorage.getItem("role") == "mcc"){
		getTaskToAssign();
	}
	else getTasksInProgress();
}

function allPlanes(result){
	planeList = result;
	
	getAllPlanes();
}

function allMros(result){
	listMros = result;
	//getAllTasks();
}


/** SORT TASKS FUNCTION **/
function getAllTasks(){
	if(localStorage.getItem("role") == "mro"){
		$("#sendAlert").hide();
	}
	else $("#sendAlert").show();
	var line = "";
	for (var i = 0; i < taskList.length; i++) {
		line += printTask(taskList, i);
	}
	
	$('#tbody').html(line);
}

function getTaskToAssign(){
	$("#sendAlert").hide();
	var line;
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.taskStatus == 0 ){
            line += printTask(taskList, i);
		}
	}
	$('#tbody').html(line);
}

function getTasksInProgress(){
	console.log(localStorage.getItem("role"));
	if(localStorage.getItem("role") == "mcc"){
		$("#sendAlert").show();
	}
	else $("#sendAlert").hide();
	
	var line;
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.taskStatus == 1){
			line += printTask(taskList, i);
		}
		/*if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			var endTime = stringToDate(taskList[i].task.endTime);
			if(today < endTime && taskList[i].task.taskStatus == 2)
                line += printTask(taskList, i);
        }*/
    }
	$('#tbody').html(line);
}

function getTasksdone(){
	$("#sendAlert").hide();
	var tr = "";
	for (var i = 0; i < taskList.length; i++) {
		if(taskList[i].task.mroId > 0 ){
			var today = new Date();
			//var endTime = stringToDate(taskList[i].task.endTime);
			if(taskList[i].task.taskStatus == 2)
                tr += printTask(taskList, i);
		}
	}
	$('#tbody').html(tr);
}

function getAllPlanes(){
	var line = "";
	for (var i = 0; i < planeList.length; i++) {
		line += printPlane(i);
	}
	
	$('#planeTbody').html(line);
}

/** END SORT TASKS FUNCTIONS **/

/** PRINT **/
function printTask(taskList, i){
	//console.log(taskList[i]);
	var but = "<button onclick='sendAlert("
		+  taskList[i].task.id//sub(JSON.stringify(taskList[i].task.id))
		+ ")' "
		+ "class='btn icon-btn btn-danger'>  "
		+ "<span class='glyphicon glyphicon-alert'></span> "
		+ "</button>";
	var ifMcc="";
	//console.log(localStorage.getItem("role")+" "+taskList[i].task.taskStatus);
	if (localStorage.getItem("role") == "mcc" && taskList[i].task.taskStatus == 1){
		ifMcc = "<td>"+but+"</td>";
	}
	else {
		
	}
    return "<tr> "
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].taskGeneric.ataCategory))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].task.startTime))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(taskList[i].taskGeneric.duration))
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
	+ "</button></td>"
	+ sub(JSON.stringify(taskList[i].task.planeId))
	+ "</td>"
	+ ifMcc
	+ "</tr>";
   
}

function printPlane(i){
	var content="";
	content+="<tr> ";
	content += td(sub(JSON.stringify(planeList[i].planeId)));
	content += td(sub(JSON.stringify(planeList[i].planeType)));
	//content += td(subFy(planeList[i].flighs[0].departureAirport));
	content += "</tr>";
	return content;
}

/** SEND MESSAGES TO MRO **/
function sendAlert(id){
	postServerData("ws/task/alert/"+id,alert('Task n°'+id+'send'),null);
	//alert('id'+id);
}

/** VIEW AND VALID TASKS **/
function callGetlistMros(){
	getServerData("ws/mro/all",getlistMros,null);
}
function getlistMros(){
	//listMros = res;
	console.log('rf');
	var mroOption='<select id="sel" class="form-control">';
	for(var j = 0; j < listMros.length; j++){
		mroOption+="<option>"+listMros[j].id+"- "+subFy(listMros[j].email) +"</option>";
	}
	mroOption+='</select>';
	console.log(mroOption);
	return mroOption;
	
}
function choseMro(id){
	mroChosen = id;
	console.log(id);
}
function viewTask(taskSelected){
	
	var task;
	task = dl("Ata Category",subFy(taskSelected.taskGeneric.ataCategory) );
	task+= dl("Description ",subFy(taskSelected.taskGeneric.description) );
	task+= dl("Periodicity", subFy(taskSelected.taskGeneric.periodicity) );
	task+= dl("Hangar Need", stringify(taskSelected.taskGeneric.hangarNeed) );
	task+= dl("Duration"   , taskSelected.taskGeneric.duration );
	task+= dl("Plane Type",subFy(taskSelected.taskGeneric.planeType    ) );
	console.log("status "+taskSelected.task.taskStatus);
	if(localStorage.getItem("role") == "mcc"){
		if(taskSelected.task.taskStatus == 0){
			task+= dl("Mro list", getlistMros());
			task+='<center><button class="btn icon-btn btn-primary" onclick="validTask('+taskSelected.task.id+')"> submit </button></center>';
		}
		else if(taskSelected.task.taskStatus == 1) {
			task+= dl("Mro ", taskSelected.task.mroId);
			task+= dl("Mro list", getlistMros());
			task+='<center><button class="btn icon-btn btn-primary" onclick="validTask('+taskSelected.task.id+')"> edit </button></center>';
		}
		else {
			task+= dl("Mro ", taskSelected.task.mroId);
			task+='<center><button class="btn icon-btn btn-primary" onclick="location.reload()"> Return </button></center>';
		}
	}
	else {
		task+='<center><button class="btn icon-btn btn-primary" onclick="mroValidTask('+taskSelected.task.id+')"> Task Done </button></center>';
	}
	
	
	
	/** add Panel **/  
	var panel;
	panel = '<div class="panel panel-primary">';
	panel += '<div class="panel-heading">Task n° '+ sub(JSON.stringify(taskSelected.task.id));
	panel += '<span class="navbar-right " onclick="location.reload()" style ="margin-right : 10px; cursor: pointer">TaskList</span></div>';
	panel += '<div class="panel-body">'+task+'</div></div>';

	$('#tasksmenu').html(panel);
}

function mroValidTask(id){
	//alert('Task Done');
	postServerData('ws/task/done/'+id,location.reload(),null);
}

function validTask(id){
	var m = split0($('#sel option:selected').val());
	console.log("m "+m);
	getServerData("ws/task/mro/"+m+"/"+id,location.reload(),null);
	//console.log("mroChosen : "+$('#sel option:selected').val() +" id : "+id);
}
/** END FUNCTIONS **/