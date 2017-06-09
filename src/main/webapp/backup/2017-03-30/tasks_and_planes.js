
function getServerData(url, success){
    $.ajax({
        dataType: "json",
        url: url
    }).done(success);
}
var task;


//GET ALL TASK
function getAllTasks(result){
	var tab =$('#example').DataTable( {
	} );
	console.log(result[1].plane.planeType);
	for(var i=0; i<result.length; i++){
		for(var j=0 ; j<result[i].tasklist.length ; j++ ){
		console.log(result[i].tasklist[j].id);
		tab.row.add( [
			JSON.stringify(result[i].tasklist[j].id),
			JSON.stringify(result[i].tasklist[j].ataCategory),
			JSON.stringify(result[i].tasklist[j].description),
			formatDate(new Date(result[i].tasklist[j].endTime)),
			formatDate(new Date(result[i].flight.departureTime)),
			JSON.stringify(result[i].plane.planeType)
		] ).draw( false );
		
		
		/* We did not delete this part because we still need it */
		//var templateExample = _.template($('#taskTemp').html());
		/*var html = templateExample({
			"starttimetask":JSON.stringify(result[i].startTime),
			"endtimetask":JSON.stringify(result[i].endTime),
			"descriptiontask":JSON.stringify(result[i].description),
			"periodicitytask":JSON.stringify(result[i].periodicity),
			"atatask":JSON.stringify(result[i].ataCategory),
			"hangartask":JSON.stringify(result[i].hangarNeed),
		
		});
		$('#tabb').append(html);*/
	}
	}
	$('#example tbody').on('click','tr', function(){
		var data = tab.row( this ).data();
		var x = parseInt(data[0],10);
		document.location.href="taskView_mro.html?id="+x;
		//alert ('you clicked on '+data[0]+'\'s row');
	});
}


function getAllPlanes(result){
	
	var planes =$('#planeslist').DataTable( {
	} );
	
	//console.log(result[1].plane.planeType);
	for(var i=0; i<result.length; i++){
		planes.row.add( [
			JSON.stringify(result[i].planeId),
			JSON.stringify(result[i].planeType)
		] ).draw( false );
	}
}

function formatDate(date) {
	  var monthNames = [
	    "January", "February", "March",
	    "April", "May", "June", "July",
	    "August", "September", "October",
	    "November", "December"
	  ];

	  var day = date.getDate();
	  var monthIndex = date.getMonth();
	  var year = date.getFullYear();

	  return year + ' ' + monthNames[monthIndex] + ' ' + day;
	}




$(function(){
		getServerData("ws/task/all2",getAllTasks);
});
$(function(){
	getServerData("ws/plane/all",getAllPlanes);
});
