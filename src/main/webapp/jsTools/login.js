$(function(){

    $("#btnlogin").click(function () {

        let coduni = $("#coduniversitario").val();
        let passuni = $("#passuniversitario").val();
        let param = {coduni: coduni, passuni: passuni, op: 1};


        $.getJSON("validarusuario", param, function (data) {
            
            console.log(data);
            console.log(data.acceso);
            if (data.acceso === "ok") {
                alert("acceso satisfactorio");
                $(location).attr('href', "login.html");
            } else {
                alert("datos incorrectos");
            }
        });

        
    });
    
});