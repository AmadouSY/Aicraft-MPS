var planeList;


$(function() {
	if(localStorage.getItem("role") == "mcc"){
		getServerData("ws/plane/allPlaneInfos", allPlanes,null)
	}
	else {
		
	}
});

function allPlanes(res){
	planeList = res;
	var line = "";
	for (var i = 0; i < planeList.length; i++) {
		line += printPlane(planeList, i);
	}	
	$('#planeTbody').html(line);
}

function printPlane(planeInfo, i){
	console.log("plane  = "+planeInfo);
	var content="<tr>";
	content+= td(planeInfo[i].plane.planeId);
	content+= td(subFy(planeInfo[i].plane.planeType));
	if(planeInfo.flights!=undefined){
	content+= td(subFy(planeInfo[i].flights[0].departureTime));
	content+= td(subFy(planeInfo[i].flights[0].departureAirport));
	}
	content+=td('<button class="btn icon-btn btn-primary" onclick="viewTaskByPlanes('+planeInfo[i].plane.planeId+')"><span class="glyphicon btn-glyphicon glyphicon-eye-open"></span></button>');
	content+="</tr>";
	return content;
	/*
	//console.log(taskList[i]);
	var but = "<button onclick='sendAlert("
		+  PlaneInfo.flights[i].id//sub(JSON.stringify(PlaneInfo.flights[i].id))
		+ ")' "
		+ "class='btn icon-btn btn-danger'>  "
		+ "<span class='glyphicon glyphicon-alert'></span> "
		+ "</button>";
	var ifMcc="";
	if (localStorage.getItem("role") == "mcc"){
		ifMcc = "<td>"+but+"</td>";
	}
	else {
		$("#sendAlert").hide();
	}
    return "<tr> "
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].commercialId))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].departureAirport))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].arrivalAirport))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].departureTime))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].arrivalTime))
	+ "</td>"
	+ "<td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].planeId))
	+ "</td>"
	+ "<td><button onclick='viewTask("
	+  JSON.stringify(PlaneInfo.flights[i])//sub(JSON.stringify(taskList[i].task.id))
	+ ")' "
	+ "class='btn icon-btn btn-primary'>  "
	+ "<span class='glyphicon btn-glyphicon glyphicon-eye-open'></span> "
	+ "</button></td>"
	+ sub(JSON.stringify(PlaneInfo.flights[i].planeId))
	+ "</td>"
	+ ifMcc
	+ "</tr>";
   */
}

function viewTaskByPlanes(id){
	console.log("id "+id);
}