var tableHolder = $('#tableHolder');
var holder = $('#holder');
//var buttonTable = $('#tableButton');
//var buttonGraph = $('#graphButton');

function isTable(){
tableHolder = $('#tableHolder');
var buttonTable = $('#tableButton');
buttonTable.click(function(){
holder.hide();
tableHolder.show();

});}

//buttonGraph
function isGraph(){
holder = $('#holder');
var buttonGraph = $('#graphButton');
buttonGraph.click(function(){
tableHolder.hide();

holder.show();
});}



var dayVal = function(id, dateBegin, dateEnd){
    var dannie = $('#danDay');
    dannie.empty();
    $.ajax( 'services/dayVol/'+id, {
        dataType:'json',
        type:'GET',
        data: {dateBegin:dateBegin, dateEnd:dateEnd},
        success:function ( data1 ) {
            dannie.prepend(function(j){
                var res = '';
                for(var i=0; i<data1.resultDay.length; i++){
                    res +='<tr class="gradeA odd"><td>'+data1.resultDay[i].dateBegin+
                        '</td><td>'+data1.resultDay[i].dayWork+'</td><td>'+data1.resultDay[i].dayNormal+
                        '</td><td>'+data1.resultDay[i].dayPress+'</td><td>'+data1.resultDay[i].dayTemp+'</td></tr>';
                }
                console.log(res);

                return res;
            });
            }
    }).error( function()  {
            console.log("error");
        }); };
var showOnlyDay = function(){
    $('#current').hide();
    $('#hourTab').hide();
    $('#attenTab').hide();
    $('#dayTab').show();
    day();
}
var day = function(){

    var dateBegin = document.getElementById("datepickerBegin").value;
    var dateEnd = document.getElementById("datepickerEnd").value;
    dateBegin = dateBegin +" 12:00:00";
    dateEnd = dateEnd + " 11:59:59";
    var idEnt = document.getElementById("titleEnterprise");
    //$('dayTable').empty();
    //dayVal(idEnt.childNodes[0].id, dateBegin, dateEnd);
    $('#dayTable').dataTable( {
        "sLengthMenu": "Показывать по _MENU_ записей",
        "sZeroRecords": "Ничего не найдено",
        "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
        "sInfoEmpty": "Показано 0 to 0 of 0 записей",
        "sInfoFiltered": "(Поиск по _MAX_ записей)",

        "iDisplayLength": 50,
        "sScrollY": 480,
        "bJQueryUI": true,
        "bProcessing": true,
        "bDestroy": true,
        "sPaginationType": "full_numbers",

        "sAjaxDataProp":"response",
        "sAjaxSource": "services/dayVol/"+idEnt.childNodes[0].id+"?dateBegin="+dateBegin+"&dateEnd="+dateEnd

} );

};
var hourlVal = function(id, dateBegin, dateEnd){
    var dannie = $('#dannie');
    dannie.empty();
    $.ajax("services/hourlVal/"+id,{
        dataType:'json',
        type:'GET',
        data: {dateBegin:dateBegin, dateEnd:dateEnd},
        success:function ( data1 ) {
            dannie.prepend(function(j){
                var res = '';
                for(var i=0; i<data1.workVol.length; i++){
                    res +='<tr class="gradeA odd"><td>'+data1.workVol[i].timeCorrector+
                        '</td><td>'+data1.workVol[i].current+'</td><td>'+data1.normalVol[i].current+
                        '</td><td>'+data1.pressure[i].current+'</td><td>'+data1.temper[i].current+'</td></tr>';
                }
                return res;
            });
        }
    }).error(function(){
            console.log("error");
        });
};
var showOnlyHour = function(){
    $('#hourTab').show();
    $('#dayTab').hide();
    $('#current').hide();
    $('#attenTab').hide();
    hour();
}
var hour = function(){
    var dateBegin = document.getElementById("datepickerBeginHourly").value;
    var dateEnd = document.getElementById("datepickerEndHourly").value;
    var v = document.getElementById("spinBegin").value;
    var v2 = document.getElementById("spinEnd").value;
    dateBegin = dateBegin +" "+v+":00:00";
    dateEnd = dateEnd + " "+v2+":59:59";
    var idEnt = document.getElementById("titleEnterprise");
    //hourlVal(idEnt.childNodes[0].id, dateBegin, dateEnd);
    $('#hourTable').dataTable( {
        "sLengthMenu": "Показывать по _MENU_ записей",
        "sZeroRecords": "Ничего не найдено",
        "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
        "sInfoEmpty": "Показано 0 to 0 of 0 записей",
        "sInfoFiltered": "(Поиск по _MAX_ записей)",

        "iDisplayLength": 50,
        "sScrollY": 480,
        "bJQueryUI": true,
        "bProcessing": true,
        "bDestroy": true,
        "sPaginationType": "full_numbers",

        "sAjaxDataProp":"response",
        "sAjaxSource": "services/hourlVol/"+idEnt.childNodes[0].id+"?dateBegin="+dateBegin+"&dateEnd="+dateEnd

    } );
}

window.onload = function() {//onload begin
    var inp = document.getElementById('searchInput');

    var find = function() {//find begin
        var parent = document.getElementById('listEnterprise');
        var divs = parent.getElementsByTagName('a');
        var len = divs.length;
        for (var i = 0; i < len; i++) {//for begin
       //     if($("a:contains("+inp.value+")") && inp.value != '') {console.log("aaaaaaaaaaaaaa");
       //             divs[i].style.display = 'block';}
           if (inp.value != divs[i].innerHTML && inp.value != '') {
                divs[i].style.display = 'none';}
             else if (divs[i].style.display != 'block' && inp.value.length<2) {
                divs[i].style.display = 'block';
            }
        }//for end
    }//find end
    inp.onkeyup = function() {
        find();
    };

}//onload end
var showOnlyAttention = function(){
    $('#hourTab').hide();
    $('#dayTab').hide();
    $('#current').hide();
    $('#attenTab').show();
    attention();
};
var attention = function(){

    var dateBegin = document.getElementById("datepickerBegin").value;
    var dateEnd = document.getElementById("datepickerEnd").value;
    dateBegin = dateBegin +" 12:00:00";
    dateEnd = dateEnd + " 11:59:59";
    var idEnt = document.getElementById("titleEnterprise");
    $('#attenTable').dataTable( {
        "sLengthMenu": "Показывать по _MENU_ записей",
        "sZeroRecords": "Ничего не найдено",
        "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
        "sInfoEmpty": "Показано 0 to 0 of 0 записей",
        "sInfoFiltered": "(Поиск по _MAX_ записей)",

        "iDisplayLength": 50,
        "sScrollY": 480,
        "bJQueryUI": true,
        "bProcessing": true,
        "bDestroy": true,
        "sPaginationType": "full_numbers",

        "sAjaxDataProp":"response",
        "sAjaxSource": "services/attenVol/"+idEnt.childNodes[0].id+"?dateBegin="+dateBegin+"&dateEnd="+dateEnd

    } );
};
$(function() {
    var now = new Date();
    var month = (now.getMonth() + 1);
    var day = now.getDate();
    if(month < 10)
        month = "0" + month;
    if(day < 10)
        day = "0" + day;
    var today = now.getFullYear() +'-'+month+'-'+'01';
    $("#datepickerBeginAtten").val(today);
    $( "#datepickerBeginAtten" ).datepicker();
    today = now.getFullYear() +'-'+month+'-'+day;
    $("#datepickerEndAtten").val(today);
    $( "#datepickerEndAtten" ).datepicker();
});




