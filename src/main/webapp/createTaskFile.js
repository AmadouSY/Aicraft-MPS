/** DECLARE VARIABLE **/
var planesForCreation;
var genericTaskForCreation;
var sptmp;

/** END DECLARE VARIABLE **/

$(function() {
	getServerData("ws/plane/allPlaneInfos",getPlanesForCreateTask,null);
});

/** FUNCTIONS **/
function td(res){
	return "<td>"+res+"</td>";
}
function option(res){
	return '<option>'+res+'</option>';
}
function ok(){
	return '<span class="glyphicon btn-glyphicon glyphicon-ok"></span> ';
}
function getPlanesForCreateTask(planes){
	//console.log(planes);
	planesForCreation = planes;
	var lines= '<select id="selectPLanes" class="form-control" >';
	for(var i=0; i<planesForCreation.length; i++){
		lines+= option(planesForCreation[i].plane.planeId+"- "+subFy(planesForCreation[i].plane.planeType)) ;
	}
	lines+='</select>';
	
	var but = '<button class="btn icon-btn btn-primary" onclick="showPlaneSelected()"><span class="glyphicon btn-glyphicon glyphicon-ok"></span></button>';
	
	$('#createChose').html(dl2("Chose a plane",lines,but));
	
	//$('#planeslist2').DataTable({});
}



function showPlaneSelected(){
	//sptmp = $('#createChose').val();
	//var e = document.getElementById("selectPLanes");
	var id = document.getElementById("selectPLanes").selectedIndex; //e.options[e.selectedIndex].value;
	//var id= $('#selectPLanes option:selected').val();
	console.log("id select "+id);
	var pt = subFy(planesForCreation[id].plane.planeType);
	console.log(pt);
	var content = dl("plane Type ",pt);
	var airport = subFy(planesForCreation[id].flighs[0].departureAirport);
	console.log(airport);
	content += dl("AirPort",airport);
        planesForCreation[id].flighs.sort(function(a, b) {
            return (a.date < b.date) ? -1 : ((a.date > b.date) ? 1 : 0);
        });
	content += dl("Next Departure ",stringToDate(subFy(planesForCreation[id].flighs[0].departureTime))); 
        
        

        
	$('#createShow').html("<h3>Plane n° "+planesForCreation[id].plane.planeId+"</h3>"+content);
	getServerData("ws/task/genericByPlane/"+pt,getGenericTasks,null);
	
	
	//alert(id+" "+pt);
	
}
function getGenericTasks(genericTasks){
	genericTaskForCreation = genericTasks;
	var lines= '<select id="selectGenericTask" class="form-control" >';
	for(var i=0; i<genericTasks.length; i++){
		lines+= option(genericTasks[i].id+"-"+subFy(genericTasks[i].ataCategory));
	}
	lines+='</select>';
	var but = '<button class="btn icon-btn btn-primary" onclick="showGenericTaskSelected()"><span class="glyphicon btn-glyphicon glyphicon-ok"></span></button>';
	var fullSel = '<dl id="toRemove" class="row"><dt class="col-sm-3">Chose a Task </dt><dd class="col-sm-6">'+lines+'</dd><dd class="col-sm-3">'+but+'</dd></dl>';
		
	$("#toRemove").remove();
	$("#toRemove2").remove();
	$('#createChose').append(fullSel);
	//console.log(genericTasks);
}
function showGenericTaskSelected(){
	var j = document.getElementById("selectGenericTask").selectedIndex;
	//alert('cool'+genericTaskForCreation[j].id);
	//console.log()
	var r = printGenericTask(genericTaskForCreation[j]);
	$('#createShow').html("<h3>Task Generic n° "+genericTaskForCreation[j].id+"</h3>"+r);
	
	var res = '<div class="control-group">';
    //res+='<label class="control-label">Choose a date</label>'; 
    res+='<div class="controls input-append date form_datetime" data-date-format="yyyy/mm/dd hh:mm"  data-link-field="dtp_input1">';
    res+='<input class="form-control" size="20" id="textDate" type="text" value="" readonly/>';
    res+='<span class="add-on"><i class="icon-remove"></i></span>';
	res+='<span class="add-on"><i class="icon-th"></i></span>';
    res+='</div>';
	res+='<input type="hidden" id="dtp_input1" value="" /><br/></div>';
	//res+='<button type="submit" onclick="createTask()" class="btn btn-primary">Submit</button>'
	var but = '<button class="btn icon-btn btn-success" onclick="createTask()"><span class="glyphicon btn-glyphicon glyphicon-ok"></span></button>';
	var fullSel = '<dl id="toRemove2" class="row"><dt class="col-sm-3">Chose a Date </dt><dd class="col-sm-6">'+res+'</dd><dd class="col-sm-3">'+but+'</dd></dl>';

	$("#toRemove2").remove();
	$('#createChose').append(fullSel);
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
	
	
	//res+='<button type="submit" onclick="createTask()" class="btn btn-primary">Submit</button>'
}
function createTask(){
	var newGenericTask = split0($('#selectGenericTask option:selected').val());
	var newPlane = split0($('#selectPLanes option:selected').val());
	var newDate = $('#textDate').val();
	
	//var newData =  {"id": -1,"idTaskGeneric": newGenericTask,"startTime": newDate,"endTime": newDate,"planeId": newPlane,"taskStatus": 1,"mroId": -1,"mccId": -1};
	console.log(newDate);
	postServerData("ws/task/create/"+newGenericTask+"/"+newPlane+"/"+encodeURIComponent(newDate),location.reload(),null);
	//alert('task generic id'+newTask+' plane id '+newPlane+' date '+newDate);
	
}




/** END FUNCTIONS **/