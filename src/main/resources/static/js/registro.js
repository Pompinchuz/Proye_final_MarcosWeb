$(document).ready(function() {
    // Add form submission on Enter key
    document.getElementById('registroForm').addEventListener('submit', function(e) {
        e.preventDefault();
        registrar();
    });

    // Function to clear previous error messages
    function clearErrors() {
        $('.error-message').remove();
    }

    // Function to display error message
    function showError(inputId, message) {
        $(`#${inputId}`).after(`<div class="error-message text-danger mt-1">${message}</div>`);
    }

    // Validate name
    function validateName(name) {
        const nameRegex = /^[a-zA-Z\s]{2,}$/;
        if (!name) {
            return "El nombre es obligatorio.";
        }
        if (!nameRegex.test(name)) {
            return "El nombre debe tener al menos 2 caracteres y solo contener letras y espacios.";
        }
        return "";
    }

    // Validate email
    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email) {
            return "El email es obligatorio.";
        }
        if (!emailRegex.test(email)) {
            return "Por favor, ingrese un email válido.";
        }
        return "";
    }

    // Validate password
    function validatePassword(password) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!password) {
            return "La contraseña es obligatoria.";
        }
        if (!passwordRegex.test(password)) {
            return "La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un carácter especial.";
        }
        return "";
    }

    // Validate confirm password
    function validateConfirmPassword(password, confirmPassword) {
        if (!confirmPassword) {
            return "La confirmación de la contraseña es obligatoria.";
        }
        if (password !== confirmPassword) {
            return "Las contraseñas no coinciden.";
        }
        return "";
    }

    // Registrar function
    window.registrar = function() {
        clearErrors();

        const nombre = $('#nombre').val().trim();
        const email = $('#email').val().trim();
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();

        let isValid = true;

        // Validate each field
        const nombreError = validateName(nombre);
        if (nombreError) {
            showError('nombre', nombreError);
            isValid = false;
        }

        const emailError = validateEmail(email);
        if (emailError) {
            showError('email', emailError);
            isValid = false;
        }

        const passwordError = validatePassword(password);
        if (passwordError) {
            showError('password', passwordError);
            isValid = false;
        }

        const confirmPasswordError = validateConfirmPassword(password, confirmPassword);
        if (confirmPasswordError) {
            showError('confirmPassword', confirmPasswordError);
            isValid = false;
        }

        if (isValid) {
            // Proceed with AJAX request if validations pass
            $.ajax({
                url: '/registro',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ nombre: nombre, email: email, password: password }),
                success: function(token) {
                    localStorage.setItem('jwt', token); // Store token in LocalStorage
                    window.location.href = '/'; // Redirect to dashboard
                },
                error: function(xhr, status, error) {
                    console.error('Error en registro:', error);
                    showError('registroForm', 'Error en registro: ' + (xhr.responseText || 'Inténtalo de nuevo'));
                }
            });
        }
    };
});