function getServerData(url, success) {
	$.ajax({
		dataType : "json",
		url : url
	}).done(success);
}
var task;
var s;
function getTask(taskInfo) {
	task = taskInfo;
	
	$('#taskId').html("Task n° "+taskInfo.task.id);
	$('#ata').html("Ata Category        : "+JSON.stringify(taskInfo.task.ataCategory));
	$('#startDate').html("Start date    : "+JSON.stringify(taskInfo.task.startTime));
	$('#endDate').html("End date : "+JSON.stringify(taskInfo.task.endTime));
	$('#description').html("Description : "+JSON.stringify(taskInfo.task.description));
	$('#periodicity').html("Periodicity : "+JSON.stringify(taskInfo.task.periodicity));
	$('#hangarNeed').html("Hangar Need 	: "+taskInfo.task.hangarNeed);
	$('#mro').html("Mro :  "+taskInfo.mro.name);
	
	});
function getAllMro(result) {
	var cpt=[result.length];
	var tab = $('#mroList').DataTable({});
	for (var i = 0; i < result.length; i++) {
		cpt[i]=i;
		tab.row
				.add(
						[
							
								JSON.stringify(result[i].id),
								JSON.stringify(result[i].name),
								JSON.stringify(result[i].qualification),
								"<button type='button' class='btn btn-primary' id='button"
										+ i
										+ "'>"
										+ "<span class='glyphicon glyphicon-ok'></span>"
										+ "</button>"

						]).draw(false);
	}