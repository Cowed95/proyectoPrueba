// importaciones necesarias de React y servicios y manejo de usuarios

import { useEffect, useState } from "react";
import { getUsers } from "../services/UserService";

// Componente de la página de usuarios protegida por token
// Recibe token y onLogout como props desde App.jsx
// Maneja su propio estado para usuarios y loading
// Al montar, si hay token, carga usuarios desde el servicio userService
// Si no hay token, no hace nada (no debería montarse sin token)
// Muestra lista de usuarios o mensaje de carga
// Botón para cerrar sesión que llama a onLogout
export default function UsersPage({ token, onLogout }) {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  // Cargar usuarios al montar si hay token y manejar errores
  useEffect(() => {
    if (!token) return;
    setLoading(true);
    getUsers(token)

    // Manejo de la respuesta y errores comunes de autenticación
    // Si la respuesta no es ok, lanzar un error y limpiar usuarios en catch
    // Si es ok, parsear JSON y actualizar estado de usuarios
      .then((data) => setUsers(data))
      .catch((err) => {
        console.error("Error:", err);
        setUsers([]);
      })

      // Finalmente, limpiar estado de carga
      .finally(() => setLoading(false));
  }, [token]);

  // Renderizado de la lista de usuarios o mensaje de carga y botón de logout
  // Si loading es true, mostrar "Cargando..."
  // Si no, mostrar lista de usuarios o "No hay usuarios" si está vacío
  // Botón para cerrar sesión que llama a onLogout
  return (
    <div>
      <h1>Usuarios</h1>
      <button onClick={onLogout}>Cerrar sesión</button>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <ul>
          {users.length === 0 ? (
            <li>No hay usuarios</li>
          ) : (
            users.map((u) => (
              <li key={u.id}>
                {u.username} — {u.email}
              </li>
            ))
          )}
        </ul>
      )}
    </div>
  );
}
