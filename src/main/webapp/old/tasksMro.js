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
    getServerData("ws/task/all", getAllTasks);
});
var taskList;

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

function sub(r) {
    return r.toString().substring(1, r.toString().length - 1);
}

//<0
function taskToDo(){
    var tr = "";
    var tmp;
    for (var i = 0; i < taskList.length; i++) {
        if(taskList[i].task.mroId < 0 ){
            tr += printTask(taskList, i);

        }
    }
    $('#tbody').html(tr);
}
//>0 and dateNow < Enddate
function inProgress(){
    var tr = "";
    for (var i = 0; i < taskList.length; i++) {
        if(taskList[i].task.mroId > 0 ){
            var today = new Date();
            var endTime = new Date(taskList[i].task.endTime.substring(0,4),taskList[i].task.endTime.substring(5,7),
                taskList[i].task.endTime.substring(8,10),taskList[i].task.endTime.substring(11,13),
                taskList[i].task.endTime.substring(14,16));
            if(today < endTime)
                tr += printTask(taskList, i);
        }
    }
    $('#tbody').html(tr);
}


function printTask(result, i){
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
        + sub(JSON.stringify(taskList[i].plane.planeType))
        + "</td>"
        + "<td>"
        + sub(JSON.stringify(taskList[i].plane.planeId))
        + "</td>"
        + "<td><button onclick='getSTask("
        + sub(JSON.stringify(taskList[i].task.id))
        + ")' "
        + "class='btn icon-btn btn-primary'>  "
        + "<span class='glyphicon btn-glyphicon glyphicon-eye-open'></span> "
        + "</button></td>" + "</tr>";
}