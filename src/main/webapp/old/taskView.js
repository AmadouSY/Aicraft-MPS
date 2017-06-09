function getServerData(url, success) {
	$.ajax({
		dataType : "json",
		url : url
	}).done(success);
}
var task;
var s;
function getTask(taskInfo) {
	$('#taskId').html("Task n° " + taskInfo.task.id);
	$('#ata').html(JSON.stringify(taskInfo.task.ataCategory));
	$('#startDate').html(JSON.stringify(taskInfo.task.startTime));
	$('#endDate').html(JSON.stringify(taskInfo.task.endTime));
	$('#description').html(JSON.stringify(taskInfo.task.description));
	$('#periodicity').html(JSON.stringify(taskInfo.task.periodicity));
	$('#hangarNeed').html(taskInfo.task.hangarNeed);
	$('#mro').html(taskInfo.mro.name);
}

/*$(document).ready(function () {
    var task = new Object();
    task.

    $("#Submit").click(function () {
        $.ajax({
            url: 'http://localhost:8082/task_view_mro.html?id=',
            type: 'PUT',
            dataType: 'json',
            data: taskInfo,
            success: function (data, textStatus, xhr) {
                console.log(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log('Error in Operation');
            }
        });
    });*/
    
function getAllMro(result) {
	//var cpt = [ result.length ];
	var tab = $('#mroList').DataTable({
		"searching": false
	});
	for (var i = 0; i < result.length; i++) {
		//cpt[i] = i;
		tab.row.add(
				[

				JSON.stringify(result[i].id),
				JSON.stringify(result[i].name),
				JSON.stringify(result[i].qualification) ]).draw(false);
	}
	

	$('#mroList tbody')
			.on(
					'click',
					'tr',
					function() {
						if ($(this).hasClass('selected')) {
							var table = document.getElementById('mroList');   
							var rows = table.getElementsByClassName("selected");
							rows[0].style.backgroundColor = "white";
							$(this).removeClass('selected');
							
						} else {
							var table = document.getElementById('mroList');   
							var rows = table.getElementsByClassName("selected");
							if(rows[0]!=undefined){
							rows[0].style.backgroundColor = "white";
						}
							tab.$('tr.selected').removeClass('selected');
							$(this).addClass('selected');
							table = document.getElementById('mroList'); 
							rows = table.getElementsByClassName("selected");
							rows[0].style.backgroundColor = "yellow";
						}
						var data = tab.row(this).data();

						
						document.getElementById("mro").innerHTML = ""
								+ data[1] ;

					});
}

var getUrlParameter = function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
			.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
};

$(function() {
	var x = getUrlParameter('id');
	getServerData("ws/task/" + x, getTask);
	getServerData("ws/mro/all", getAllMro);
});

/* DATAPICKER */
var nowTemp = new Date();
var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
 
var checkin = $('#dpd1').datepicker({
  onRender: function(date) {
    return date.valueOf() < now.valueOf() ? 'disabled' : '';
  }
}).on('changeDate', function(ev) {
  if (ev.date.valueOf() > checkout.date.valueOf()) {
    var newDate = new Date(ev.date)
    newDate.setDate(newDate.getDate() + 1);
    checkout.setValue(newDate);
  }
  checkin.hide();
  $('#dpd2')[0].focus();
}).data('datepicker');
var checkout = $('#dpd2').datepicker({
  onRender: function(date) {
    return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
  }
}).on('changeDate', function(ev) {
  checkout.hide();
}).data('datepicker');