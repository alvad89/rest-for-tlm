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
        "oLanguage":{"sSearch": "Поиск:",
            "sLengthMenu": 'Показывать по <select>'+
                '<option value="50">50</option>'+
                '<option value="100">100</option>'+
                '<option value="-1">All</option>'+
                '</select> записей',
            "sZeroRecords": "Ничего не найдено",
            "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
            "sInfoEmpty": "Показано 0 to 0 of 0 записей",
            "sInfoFiltered": "(Поиск по _MAX_ записей)"},

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
        "oLanguage":{"sSearch": "Поиск:",
            "sLengthMenu": 'Показывать по <select>'+
                '<option value="50">50</option>'+
                '<option value="100">100</option>'+
                '<option value="-1">All</option>'+
                '</select> записей',
            "sZeroRecords": "Ничего не найдено",
            "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
            "sInfoEmpty": "Показано 0 to 0 of 0 записей",
            "sInfoFiltered": "(Поиск по _MAX_ записей)"},

        "iDisplayLength": 50,
        "sScrollY": 480,
        "bJQueryUI": true,
        "bProcessing": true,
        "bDestroy": true,
        "sPaginationType": "full_numbers",

        "sAjaxDataProp":"response",
        "sAjaxSource": "services/hourlVol/"+idEnt.childNodes[0].id+"?dateBegin="+dateBegin+"&dateEnd="+dateEnd

    } );
};


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
        "oLanguage":{"sSearch": "Поиск:",
            "sLengthMenu": 'Показывать по <select>'+
                '<option value="50">50</option>'+
                '<option value="100">100</option>'+
                '<option value="-1">All</option>'+
                '</select> записей',
            "sZeroRecords": "Ничего не найдено",
            "sInfo": "Показано _START_ to _END_ of _TOTAL_ записей",
            "sInfoEmpty": "Показано 0 to 0 of 0 записей",
            "sInfoFiltered": "(Поиск по _MAX_ записей)"},

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


//search
var ex1,  ex2, blist, filterval, fworking=false;
function listsearch(){
    ex1 = document.getElementById("listEnterprise");
    ex2 = ex1.cloneNode(true);
    ex2.setAttribute("id","links");
    ex1.parentNode.insertBefore(ex2,ex1);
    ex2.style.display="none";
    blist = ex2.getElementsByTagName("A");
    list2select(blist, "rubrika", setCookie);

}
onload=listsearch;
function pickup(obj) {
    if (obj.value.length<2) {
        /*если ещё не набрал 2 буквы или стёр до одной*/
        obj.style.color="#000";
        ex1.style.display="block" ;
        ex2.style.display="none";
        filterval=obj.value;
        c.firstChild.nodeValue=0
        return
    }
    /*если не отработал предыдущий поиск,
     или поле поиска не изменилось (например, нажали стрелку Left)*/
    if (fworking || filterval==obj.value) return;
    var fflagg=false, i=0, str="", el, val, q, qr;
    filterval=obj.value; fworking=true;
    ex1.style.display="none"; /*резервный список скрываем*/
    q=obj.value.toLocaleLowerCase();
    qr=q.substr(1);

    for (var s=0; s<blist.length; s++) {
        el=blist[s];
      //  if ("_top"!=el.target) continue; /*так, лишняя проверка*/
        val=el.firstChild.nodeValue.toLocaleLowerCase();
        if ((0==q.indexOf(" ") && val.indexOf(qr)>-1) || 0==val.indexOf(q)) {
            fflagg=true; el.style.display="block"; i++;
        } /*каждый элемент списка поиска отображаем или скрываем*/
        else el.style.display="none";
    }

    if (!fflagg) { /*если ничего не найдено*/
        obj.style.color="#c00";
        ex1.style.display="block"; /*отображаем резерв целиком*/
        ex2.style.display="none"; /*список поиска скрываем*/
    }
    else { /*если найден хоть один элемент*/
        obj.style.color="#000";
        ex1.style.display="none";
        ex2.style.display="block";
    }
   // c.firstChild.nodeValue=i
    fworking=false;
}
function list2select(alist, inputname) {

    var aa, id, tx, o, inputcoo=getCookie(inputname);
    if (!alist) return "alist";
    var el=document.createElement("SELECT");
    el.name=inputname;
    el.onchange= $.cookie();

    for (var a=0; a<alist.length; a++) {
        if (1==alist[a].nodeType){ /*можно выбирать ссылки по разным признакам*/
            aa=alist[a];
            id = /(\d+)$/.exec(aa.href);
            id=(id)?id[0]:0;
            tx=document.createTextNode(aa.firstChild.nodeValue);
            o=document.createElement("OPTION");
            if (id==inputcoo) o.selected=true;
            o.value=id; o.appendChild(tx);
            el.appendChild(o);
        }
    }
    document.forms[0].appendChild(el);
}





