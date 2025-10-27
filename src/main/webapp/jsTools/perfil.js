$(function () {
    $(document).ready(function () {
        // Cargar datos desde el servidor
        $.getJSON("perfilusuario", { op: 1 }, function (data) {
            // Mostrar en formulario
            $("#txtnombre").val(data.nombre);
            $("#txtapellido").val(data.apellido);
            $("#txtcoduni").val(data.coduni);
            $("#txtcorreo").val(data.correo);
            $("#txtdireccion").val(data.direccion);
            $("#txtcelular").val(data.celular);
            $("#txtfechanac").val(data.fechnaci);
            $("#txtfacultad").val(data.facultad);
            $("#txtescuela").val(data.escuela);
            $("#txtdni").val(data.dni);

            // Si viene foto en base64
            if (data.photo) {
                $('#profileImage').attr('src', data.photo);
                data.photo = data.photo;
            }

            // Guarda perfil completo en sessionStorage
            sessionStorage.setItem('voterProfile', JSON.stringify(data));
        });

        // Habilitar edición
        $('#editBtn').click(function () {
            $('#profileForm input').prop('disabled', false);
            $('#inputImagen').prop('disabled', false);
            $('#editBtn').hide();
            $('#saveBtn').show();
        });

        // Capturar imagen de perfil
        $('#inputImagen').on('change', function (e) {
            const file = e.target.files[0];
            if (!file) return;
            const fr = new FileReader();
            fr.onloadend = () => {
                const profile = JSON.parse(sessionStorage.getItem('voterProfile')) || {};
                profile.photo = fr.result;
                sessionStorage.setItem('voterProfile', JSON.stringify(profile));
                $('#profileImage').attr('src', fr.result);
            };
            fr.readAsDataURL(file);
        });

        // Guardar cambios y redirigir
        $('#profileForm').on('submit', function (e) {
            e.preventDefault();
            const profile = {
                nombre: $('#txtnombre').val().trim(),
                apellido: $('#txtapellido').val().trim(),
                coduni: $('#txtcoduni').val().trim(),
                correo: $('#txtcorreo').val().trim(),
                direccion: $('#txtdireccion').val().trim(),
                celular: $('#txtcelular').val().trim(),
                fechnaci: $('#txtfechanac').val(),
                facultad: $('#txtfacultad').val().trim(),
                escuela: $('#txtescuela').val().trim(),
                dni: $('#txtdni').val().trim(),
                photo: JSON.parse(sessionStorage.getItem('voterProfile')).photo || null
            };
            sessionStorage.setItem('voterProfile', JSON.stringify(profile));
            // Redirecciona a votar
            window.location.href = 'votar.html';
        });
    });
});