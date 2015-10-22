/*************************************************************/

/* jQuery */

$(document).ready(function () {

    // inicializacion
});

/*************************************************************/

/* funciones */
function setCampos(id_costo_extra, nombre, descripcion, precio, nombre_precio) {

    // busqueda de nombre_tipo_precio
    var select = document.forms[0].id_tipo_precio;
    var index_precio = 0;
    for (i = 0;i < select.length;i++) {
        //alert( select.options[i].innerText + " " + nombre_tipo_precio );
        if (select.options[i].innerText == nombre_precio) {
            index_precio = i;
            break;
        }
    }
    
    document.forms[0].id_costo_extra.value        	= id_costo_extra;
    document.forms[0].nombre.value                  = nombre;
    document.forms[0].descripcion.value             = descripcion;
    document.forms[0].precio.value                  = precio;
    document.forms[0].id_tipo_precio.selectedIndex  = index_precio;
}

function crear() {
    if (document.forms[0].nombre.value == "" || document.forms[0].precio.value == "")
        alert("Todos los campos son obligatorios, favor de informarlos.");
    else {
        document.forms[0].action = urlAlta;
        document.forms[0].submit();
    }
}

function modifica() {
    if (document.forms[0].nombre.value == "" || document.forms[0].precio.value == "")
        alert("Todos los campos son obligatorios, favor de informarlos.");
    else {
        document.forms[0].action = urlModifica;
        document.forms[0].submit();
    }
}

function elimina() {
    if (confirm(String.fromCharCode(191) + "Realmente desea eliminar este registro?")) {
        document.forms[0].action = urlElimina;
        document.forms[0].submit();
    }
}

function limpia() {
    document.forms[0].id_costo_extra.value        	= "";
    document.forms[0].nombre.value                  = "";
    document.forms[0].descripcion.value             = "";
    document.forms[0].precio.value                  = "";
    document.forms[0].id_tipo_precio.selectedIndex  = 0;
}