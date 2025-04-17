// Verificar autenticación
function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token && window.location.pathname !== '/login.html') {
        window.location.href = '/login.html';
        return;
    }
}

// Agregar token a las peticiones
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
}

// Cerrar sesión
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login.html';
}

// Verificar autenticación al cargar la página
document.addEventListener('DOMContentLoaded', checkAuth);

// Modificar todas las peticiones fetch para incluir el token
const originalFetch = window.fetch;
window.fetch = function () {
    let [resource, config] = arguments;

    // Si no es la petición de login, agregar el token
    if (!resource.includes('/api/auth/login')) {
        config = {
            ...config,
            headers: {
                ...config?.headers,
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        };
    }

    return originalFetch(resource, config);
};