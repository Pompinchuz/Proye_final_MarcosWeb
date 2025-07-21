// src/main/resources/static/js/productos-admin.js

const token = localStorage.getItem('jwt');
if (!token) {
    window.location.href = '/login';
}

let categorias = [];  // Almacena categorías cargadas

// Cargar categorías para selects
$.ajax({
    url: '/api/categorias',
    headers: { 'Authorization': 'Bearer ' + token },
    success: function(data) {
        categorias = data;
        let options = '<option value="">Selecciona una categoría</option>';
        data.forEach(cat => {
            options += '<option value="' + cat.id + '">' + cat.nombre + '</option>';
        });
        $('#categoriaId').html(options);
        $('#editCategoriaId').html(options);
    },
    error: function() {
        alert('Error cargando categorías. Sesión inválida.');
        logout();
    }
});

// Cargar lista de productos para admin
$.ajax({
    url: '/api/productos',
    headers: { 'Authorization': 'Bearer ' + token },
    success: function(data) {
        let productList = '';
        data.forEach(producto => {
            productList += '<li>' + producto.titulo + ' - $' + producto.precio + 
                           ' <button onclick="editarProducto(' + producto.id + ')">Editar</button>' +
                           ' <button onclick="eliminarProducto(' + producto.id + ')">Eliminar</button></li>';
        });
        $('#productosAdmin').html(productList);
    },
    error: function() {
        alert('Error cargando productos. Sesión inválida.');
        logout();
    }
});
function agregarProducto() {
    if (!validateForm('addProductForm')) return;
    const producto = {
        titulo: $('#titulo').val(),
        descripcion: $('#descripcion').val(),
        precio: parseFloat($('#precio').val()),
        valoracion: parseFloat($('#valoracion').val()),
        imagenUrl: $('#imagenUrl').val(),
        categoria: { id: parseInt($('#categoriaId').val()) }
    };
    $.ajax({
        url: '/api/productos',
        type: 'POST',
        headers: { 'Authorization': 'Bearer ' + token },
        contentType: 'application/json',
        data: JSON.stringify(producto),
        success: function() {
            alert('Producto agregado.');
            location.reload();
        },
        error: function(xhr) {
            if (xhr.status === 400) {
                alert('Error: ' + xhr.responseText);  // Muestra mensaje de duplicado
            } else {
                alert('Producto ya existe en la base de datos.');
            }
        }
    });
}
function editarProducto(id) {
    $.ajax({
        url: '/api/productos/' + id,
        headers: { 'Authorization': 'Bearer ' + token },
        success: function(producto) {
            $('#editId').val(producto.id);
            $('#editTitulo').val(producto.titulo);
            $('#editDescripcion').val(producto.descripcion);
            $('#editPrecio').val(producto.precio);
            $('#editValoracion').val(producto.valoracion);
            $('#editImagenUrl').val(producto.imagenUrl);
            $('#editCategoriaId').val(producto.categoria ? producto.categoria.id : '');
            var modal = new bootstrap.Modal(document.getElementById('editProductModal'));
            modal.show();
        },
        error: function() {
            alert('Error cargando producto para edición.');
        }
    });
}

function actualizarProducto() {
    if (!validateForm('editProductForm')) return;
    const id = $('#editId').val();
    const producto = {
        titulo: $('#editTitulo').val(),
        descripcion: $('#editDescripcion').val(),
        precio: parseFloat($('#editPrecio').val()),
        valoracion: parseFloat($('#editValoracion').val()),
        imagenUrl: $('#editImagenUrl').val(),
        categoria: { id: parseInt($('#editCategoriaId').val()) }
    };
    $.ajax({
        url: '/api/productos/' + id,
        type: 'PUT',
        headers: { 'Authorization': 'Bearer ' + token },
        contentType: 'application/json',
        data: JSON.stringify(producto),
        success: function() {
            alert('Producto actualizado.');
            location.reload();
        },
        error: function(xhr) {
            alert('Error actualizando producto: ' + xhr.responseText);
        }
    });
}

function eliminarProducto(id) {
    if (confirm('¿Eliminar producto ID: ' + id + '?')) {
        $.ajax({
            url: '/api/productos/' + id,
            type: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token },
            success: function() {
                alert('Producto eliminado.');
                location.reload();
            },
            error: function() {
                alert('Error eliminando producto.');
            }
        });
    }
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