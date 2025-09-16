// import React desde 'react'; 
import { useEffect, useState } from "react";

export default function App() {
  // Manejo del estado de usuarios y autenticación
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // cargar usuarios si hay token
  useEffect(() => {
    if (!token) return;

    // Cargar usuarios desde la API protegida
    setLoading(true);
    fetch("/api/users", {
      // Agregar el token en el header Authorization Bearer
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })

    // Manejo de la respuesta de la API y errores comunes de autenticación 
    // como token expirado o inválido (401 Unauthorized) o falta de permisos (403 Forbidden)
    // Si la respuesta no es ok, lanzar un error y limpiar usuarios en catch
    // Si es ok, parsear JSON y actualizar estado de usuarios 
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      // Actualizar estado de usuarios con los datos recibidos de la API
      .then((data) => setUsers(data))
      // En caso de error, limpiar usuarios y mostrar error en consola 
      .catch((err) => {
        console.error("Error fetching users:", err);
        setUsers([]);
      })
      // Finalmente, limpiar estado de carga 
      .finally(() => setLoading(false));
  }, [token]);

  // Manejo del formulario de login y logout
  // Al hacer login, guardar token en localStorage y estado
  // Al hacer logout, limpiar token y usuarios del estado y localStorage
  // Manejo de errores simples con alertas y estado de carga para deshabilitar botón
  // durante la petición de login
  const handleLogin = (e) => {
    e.preventDefault();
    // Evitar múltiples envíos mientras se procesa la petición
    setLoading(true);

    // Hacer petición de login a la API y manejar la respuesta y errores comunes de autenticación
    // Si la respuesta no es ok, lanzar un error y mostrar alerta
    // Si es ok, parsear JSON, guardar token en localStorage y actualizar estado
    // Finalmente, limpiar estado de carga
    fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    })
    // Manejo de la respuesta de la API y errores comunes de autenticación
    // como credenciales inválidas (401 Unauthorized)
    // Si la respuesta no es ok, lanzar un error y mostrar alerta
    // Si es ok, parsear JSON, guardar token en localStorage y actualizar estado
      .then((res) => {
        if (!res.ok) throw new Error("Credenciales inválidas");
        return res.json();
      })
      // Actualizar estado con el token recibido de la API
      // Guardar token en localStorage para persistencia entre recargas
      // Limpiar campos de usuario y contraseña del formulario
      .then((data) => {
        localStorage.setItem("token", data.token);
        setToken(data.token);
      })
      // En caso de error, mostrar alerta con el mensaje del error
      .catch((err) => alert(err.message))
      .finally(() => setLoading(false));
  };

  // Cerrar sesión limpiando token y usuarios del estado y localStorage
  // Esto también hará que el efecto de useEffect limpie la lista de usuarios
  // al no haber token válido en el estado de la aplicación
  // y evitará que se intente cargar usuarios protegidos sin autenticación
  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken("");
    setUsers([]);
  };

  // Renderizado condicional basado en si hay token (usuario autenticado) o no
  // Si no hay token, mostrar formulario de login
  // Si hay token, mostrar lista de usuarios y botón de logout
  // Manejo de estado de carga para mostrar mensaje mientras se cargan usuarios
  // Manejo de caso donde no hay usuarios (lista vacía) o la API no respondió
  // mostrando un mensaje adecuado en lugar de una lista vacía o un error

  return (
    <div style={{ padding: 24, fontFamily: "Arial, sans-serif" }}>
      {!token ? (
        <form onSubmit={handleLogin}>
          <h1>Login</h1>
          <div>
            <label>Usuario:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Contraseña:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" disabled={loading}>
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>
      ) : (
        <div>
          <h1>Usuarios</h1>
          <button onClick={handleLogout}>Cerrar sesión</button>
          {loading ? (
            <p>Cargando...</p>
          ) : (
            <ul>
              {users.length === 0 ? (
                <li>No hay usuarios (o la API no respondió)</li>
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
      )}
    </div>
  );
}
