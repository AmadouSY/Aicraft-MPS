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

$(function() {
	getServerData("ws/plane/all", getAllPlanes);
});


var planeList;
//CREATE TASK MENU JS
function getAllPlanes(result) {
	console.log(result);
	planeList = result;
	//
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
function choosePlane() {

}
var idPlane;
function chooseTasksByPlane(planetype,planeId) {
	console.log("plane type "+planetype+", plane id "+planeId);
	idPlane=planeId;
	getServerData("ws/task/genericByPlane/"+planetype, chooseTasksByPlaneAux);
	$('#returnCreateTask').append('<li><a onclick="showPlanes('+planeList+')">'+planetype+'</a></li>');
}

var tab;

function chooseTasksByPlaneAux(result) {
	
	var tr = "";
	// console.log("in get all tasks function and tr = "+tr);
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
				+ "<td><button onclick='callViewTask("
				+ result[i].id
				+ ")' class='btn icon-btn btn-primary' data-toggle='modal' data-target='#myModal'><span class='glyphicon btn-glyphicon glyphicon-eye-open'></span></button></td>"
				+ "<td><button onclick='chooseStartDate(" + sub(JSON.stringify(result[i].id)) + ")' "
				+ "class='btn icon-btn btn-success'>  "
				+ "<span class='glyphicon glyphicon-ok'></span> "
				+ "</button></td>" + "</tr>";
		// console.log(tr);
	}
}