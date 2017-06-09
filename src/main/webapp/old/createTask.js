$(function (){
	if(localStorage.getItem("mail") == null){
		localStorage.setItem("needCo","true");
		document.location.href="login.html";
	}
});



function chooseTask(){
	$("#content").html(

			function getAllTasks(result){
		$('#tasks').DataTable( {
			retrieve: true,
			paging: false,
		    searching: true
		} );
		
		for(var i=0; i<result.length; i++){

				tab.row.add( [
				JSON.stringify(result[i].task.id),
				JSON.stringify(result[i].task.ataCategory),
				//JSON.stringify(result[i].task.description),
				formatDate(new Date(result[i].task.endTime)),
				formatDate(new Date(result[i].flight.departureTime)),
				JSON.stringify(result[i].plane.planeType)
				
					
			] ).draw( false );
			
			
		//}
		}
	});
	
	$('#planes').hide();
	$('#tasks').show();
}
function choosePlane(){
	$("#content").html(
			
		function getAllPlanes(result){
		
		var planes =$('#planes').DataTable( {
			retrieve: true,
			paging: false,
		    searching: true
		} );
		
		for(var i=0; i<result.length; i++){
			planes.row.add( [
				JSON.stringify(result[i].planeId),
				JSON.stringify(result[i].planeType)
			] ).draw( false );
		}
	});
	$('#tasks').hide();
	$('#planes').show();
}

function getServerData(url, success){
    $.ajax({
        dataType: "json",
        url: url
    }).done(success);
}
var task;
var idt;
function getAllTasks(result){
	var tab =$('#tasklist').DataTable( {
	} );
	
	for(var i=0; i<result.length; i++){
			idt=JSON.stringify(result[i].task.id);
			tab.row.add( [
			idt,
			JSON.stringify(result[i].task.ataCategory),
			//JSON.stringify(result[i].task.description),
			formatDate(new Date(result[i].task.endTime)),
			formatDate(new Date(result[i].flight.departureTime)),
			JSON.stringify(result[i].plane.planeType)
			
				
		] ).draw( false );
		
		
	//}
	}
}


function getAllPlanes(result){
	
	var planes =$('#planeslist').DataTable( {
	} );
	
	for(var i=0; i<result.length; i++){
		planes.row.add( [
			JSON.stringify(result[i].planeId),
			JSON.stringify(result[i].planeType)
		] ).draw( false );
	}
}
$(function(){
	getServerData("ws/task/all",getAllTasks);
});
$(function(){
getServerData("ws/plane/all",getAllPlanes);
});