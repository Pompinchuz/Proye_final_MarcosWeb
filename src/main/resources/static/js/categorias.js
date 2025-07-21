// src/main/resources/static/js/categorias.js

let editingId = null;  // Para saber si estamos editando o creando

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    cargarCategorias(token);
});

function cargarCategorias(token) {
    fetch('/api/categorias', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => {
        const tableBody = document.getElementById('categoriasTable');
        tableBody.innerHTML = '';
        data.forEach(categoria => {
            const row = `
                <tr>
                    <td>${categoria.id}</td>
                    <td>${categoria.nombre}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="prepararEdicion(${categoria.id}, '${categoria.nombre}')">Editar</button>
                        <button class="btn btn-danger btn-sm" onclick="eliminarCategoria(${categoria.id}, '${token}')">Eliminar</button>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    })
    .catch(error => {
        console.error('Error cargando categorías:', error);
        alert('Error cargando categorías.');
    });
}

function guardarCategoria() {
    const token = localStorage.getItem('jwt');
    const form = document.getElementById('categoriaForm');

    // Validar el formulario
    if (!validateForm('categoriaForm')) {
        return; // Detiene la ejecución si el formulario no es válido
    }

    const nombre = document.getElementById('nombre').value;
    const categoria = { nombre: nombre };

    let url = '/api/categorias';
    let method = 'POST';
    if (editingId) {
        url += `/${editingId}`;
        method = 'PUT';
    }

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(categoria)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al guardar categoría');
        }
        alert(editingId ? 'Categoría actualizada.' : 'Categoría creada.');
        limpiarFormulario();
        cargarCategorias(token);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error al guardar categoría.');
    });
}

function prepararEdicion(id, nombre) {
    editingId = id;
    document.getElementById('nombre').value = nombre;
    document.querySelector('button[onclick="guardarCategoria()"]').textContent = 'Actualizar Categoría';
}

function eliminarCategoria(id, token) {
    if (confirm('¿Eliminar categoría ID: ' + id + '?')) {
        fetch(`/api/categorias/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al eliminar categoría');
            }
            alert('Categoría eliminada.');
            cargarCategorias(token);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar categoría.');
        });
    }
}

function limpiarFormulario() {
    editingId = null;
    document.getElementById('nombre').value = '';
    document.querySelector('button[onclick="guardarCategoria()"]').textContent = 'Guardar Categoría';
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