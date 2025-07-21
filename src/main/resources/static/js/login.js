document.addEventListener('DOMContentLoaded', () => {
    // Optional: Add form submission on Enter key
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        login();
    });
});

function login() {
    const email = $('#email').val();
    const password = $('#password').val();
    $.ajax({
        url: '/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: email, password: password }),
        success: function(token) {
            localStorage.setItem('jwt', token); // Store token in LocalStorage
            window.location.href = '/'; // Redirect to dashboard
        },
        error: function() {
            alert('Error en login. Verifica tus credenciales.');
        }
    });
}

