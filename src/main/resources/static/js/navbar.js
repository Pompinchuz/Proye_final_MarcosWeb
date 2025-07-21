// src/main/resources/static/js/navbar.js

$(document).ready(function() {
    const token = localStorage.getItem('jwt');

    if (token) {
        // Usuario autenticado
        $('#loginLink').hide();
        $('#logoutLink').show();
        $('#protectedContent').show();
        $('#anonymousContent').hide();
        $('#carritoLink').show();  // Muestra carrito si logueado

        // Cargar datos del usuario desde /me
        $.ajax({
            url: '/me',
            headers: { 'Authorization': 'Bearer ' + token },
            success: function(data) {
                $('#nombreUsuario').text(data.nombre);
                if (data.rol && data.rol.nombre === 'ADMIN') {
                    $('#adminLink').show();
                    $('#categoriasLink').show();
                } else {
                    $('#adminLink').hide();
                    $('#categoriasLink').hide();
                }
            },
            error: function() {
                console.error('Error cargando datos del usuario. Sesión inválida.');
                logout();  // Logout si falla la auth
            }
        });
    } else {
        // Usuario no autenticado
        $('#loginLink').show();
        $('#logoutLink').hide();
        $('#adminLink').hide();
        $('#categoriasLink').hide();
        $('#carritoLink').hide();
        $('#protectedContent').hide();
        $('#anonymousContent').show();
        $('#nombreUsuario').text('');  // Limpiar nombre
    }
});

function logout() {
    localStorage.removeItem('jwt');
    window.location.href = '/login';
}


// Función para validar form (client-side)
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (form.checkValidity() === false) {
        form.classList.add('was-validated');  // Muestra errores Bootstrap
        return false;
    }
    form.classList.remove('was-validated');
    return true;
}