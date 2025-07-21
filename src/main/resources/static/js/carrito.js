// src/main/resources/static/js/carrito.js

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    fetch('/api/carrito', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(carrito => {
        const itemsList = document.getElementById('carritoTable');
        let total = 0;
        carrito.items.forEach(item => {
            const producto = item.producto;
            const subtotal = producto.precio * item.cantidad;
            itemsList.innerHTML += `<tr><td>${producto.titulo}</td><td>${item.cantidad}</td><td>$${producto.precio.toFixed(2)}</td><td>$${subtotal.toFixed(2)}</td></tr>`;
            total += subtotal;
        });
        document.getElementById('total').textContent = total.toFixed(2);

        if (carrito.items.length === 0) {
            document.getElementById('procederPagoButton').disabled = true;  // Deshabilita botón si vacío (agrega id="procederPagoButton" al botón)
            alert('Carrito vacío. Agrega productos primero.');
        }
    })
    .catch(error => {
        console.error('Error cargando carrito:', error);
        alert('Error cargando carrito.');
    });
});

function procederPago() {
    const modal = new bootstrap.Modal(document.getElementById('pagoModal'));
    modal.show();
}

// src/main/resources/static/js/carrito.js (actualiza success handler para no depender de full Pedido if needed, but with fix it should work)

function finalizarCompra() {
    const datosPago = document.getElementById('datosPago').value;
    const token = localStorage.getItem('jwt');
    fetch('/api/pedidos', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'text/plain'
        },
        body: datosPago
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al finalizar compra');
        }
        return response.json();
    })
    .then(pedido => {
        alert('Compra finalizada. Pedido ID: ' + pedido.id);
        const modal = bootstrap.Modal.getInstance(document.getElementById('pagoModal'));
        modal.hide();
        location.reload();  // Recarga para mostrar carrito vacío
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error en el pago: ' + error.message);
    });
}