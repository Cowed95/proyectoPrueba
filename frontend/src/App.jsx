// import React desde 'react';  
import { useEffect, useState } from "react";

// Componente principal de la aplicación
export default function App() {
  // Manejo del estado de usuarios y autenticación
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState(localStorage.getItem("token") || "");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); // <-- nuevo estado para mostrar errores al usuario

  // cargar usuarios si hay token
  useEffect(() => {
    if (!token) return;

    // Cargar usuarios desde la API protegida
    setLoading(true);
    setError(""); // limpiar error previo
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
        if (!res.ok) {
          // Si es 401 o 403 mostramos mensaje específico
          if (res.status === 401) {
            throw new Error("Token inválido o expirado (401). Inicia sesión otra vez.");
          }
          if (res.status === 403) {
            throw new Error("No tienes permisos para ver esta información (403).");
          }
          throw new Error(`HTTP ${res.status}`);
        }
        return res.json();
      })
      // Actualizar estado de usuarios con los datos recibidos de la API
      .then((data) => setUsers(data))
      // En caso de error, limpiar usuarios y mostrar error en consola 
      .catch((err) => {
        console.error("Error fetching users:", err);
        setUsers([]);
        setError(err.message || "Error al cargar usuarios");
        // opcional: si el token es inválido, limpiarlo
        if (err.message && err.message.includes("Token inválido")) {
          localStorage.removeItem("token");
          setToken("");
        }
      })
      // Finalmente, limpiar estado de carga 
      .finally(() => setLoading(false));
  }, [token]);

  // Manejo del formulario de login y logout
  // Al hacer login, guardar token en localStorage y estado
  // Al hacer logout, limpiar token y usuarios del estado y localStorage
  // Manejo de errores simples con alertas y estado de carga para deshabilitar botón
  // durante la petición de login
  const handleLogin = async (e) => {
    e.preventDefault();
    // Evitar múltiples envíos mientras se procesa la petición
    setLoading(true);
    setError("");

    // Intentamos primero usar proxy ("/api/..."), si hay error de red intentamos "http://localhost:8080"
    const tryEndpoints = ["/api/auth/login", "http://localhost:8080/api/auth/login"];

    for (const url of tryEndpoints) {
      try {
        // Hacer petición de login a la API y manejar la respuesta y errores comunes de autenticación
        // Si la respuesta no es ok, lanzar un error y mostrar alerta
        // Si es ok, parsear JSON, guardar token en localStorage y actualizar estado
        const res = await fetch(url, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ username, password }),
        });

        // Si la respuesta no es ok, intentar leer el body para dar feedback
        if (!res.ok) {
          let text;
          try {
            text = await res.text();
          } catch (e) {
            text = `HTTP ${res.status}`;
          }

          // Intentamos parsear json de error para obtener mensaje si lo hay
          try {
            const errJson = JSON.parse(text || "{}");
            const msg = errJson.message || errJson.error || JSON.stringify(errJson);
            throw new Error(msg || `HTTP ${res.status}`);
          } catch (parseErr) {
            throw new Error(text || `HTTP ${res.status}`);
          }
        }

        // Si es ok, parseamos JSON y buscamos el token en varias claves posibles
        const data = await res.json().catch(() => null);
        // detectar varios nombres posibles del token
        const foundToken =
          (data && (data.token || data.accessToken || data.access_token || data.jwt || data.authToken)) ||
          (data && data.data && (data.data.token || data.data.accessToken || data.data.access_token));

        if (!foundToken) {
          console.warn("Respuesta de login (sin token detectado):", data);
          throw new Error("La respuesta del servidor no incluye token. Revisa la API.");
        }

        // Guardar token y actualizar estado
        localStorage.setItem("token", foundToken);
        setToken(foundToken);
        setUsername("");
        setPassword("");
        setError("");
        setLoading(false);
        return; // login exitoso, salimos del loop
      } catch (err) {
        // Si este endpoint falló por conexión (ECONNREFUSED) probamos el siguiente
        console.warn(`Intento de login a ${url} falló:`, err.message);
        // Si el error fue de red en la primera URL, el loop probará la segunda. Si ya fue la última, mostramos error.
        if (url === tryEndpoints[tryEndpoints.length - 1]) {
          setError(err.message || "Error en login");
          setLoading(false);
          return;
        }
        // else continuar el loop para intentar siguiente endpoint
      }
    }
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

          {/* mostrar el error si hay */}
          {error && <p style={{ color: "red" }}>{error}</p>}

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
