google.charts.load('current', { packages: ["orgchart"] });
google.charts.setOnLoadCallback(pruebas);

async function pruebas() {

    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Name');
    data.addColumn('string', 'Manager');
    data.addColumn('string', 'ToolTip');


    var raw = "";

    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    var valor = obtenerValorParametro('idGrupo');
    if (valor) {
        console.log(valor);
    } else {
        console.log("El valor no existe");
    }

    let req = await
        fetch("http://localhost:3040/api/grupo/buscar/"+valor, requestOptions)
    let grupo = await req.json()


    aux="";
    let organi=[];
    for(let user of grupo.data.usuarios){
        aux=user.idsuperiorInmediato;
        if(aux=="-1"){
            aux="";
        }
        organi.push([
            {'v':user.id,'f': user.nombre+'<div style="color:red; font-style:italic">'+user.nombreRol+'</div>'},
            aux,
            user.nombre
        ]);
    }

    data.addRows(organi);
    document.getElementById("nombreOrganigrama").innerHTML = grupo.data.nombre;
    // Create the chart.
    var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
    // Draw the chart, setting the allowHtml option to true for the tooltips.
    chart.draw(data, { 'allowHtml': true });

}
function obtenerValorParametro(sParametroNombre) {
    var sPaginaURL = window.location.search.substring(1);
    var sURLVariables = sPaginaURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParametro = sURLVariables[i].split('=');
        if (sParametro[0] == sParametroNombre) {
            return sParametro[1];
        }
    }
    return null;
}