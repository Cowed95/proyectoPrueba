// Componente de Login que maneja el formulario de autenticación y la lógica asociada

import { useState } from "react";

// onLogin es una función pasada desde el componente padre (App) para actualizar el estado de autenticación allí
// cuando el login es exitoso y se guarda el token en localStorage
export default function Login({ onLogin }) {

  // Manejo del estado del formulario y errores simples
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // Manejo del envío del formulario de login
  // Al hacer submit, hacer la petición a la API y manejar la respuesta y errores comunes de autenticación
  // Si la respuesta no es ok, lanzar un error y mostrar mensaje
  // Si es ok, parsear JSON, guardar token en localStorage y avisar al padre que ya está logeado
  // Manejo de errores simples con estado de error
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Hacer petición de login a la API y manejar la respuesta y errores comunes de autenticación
    // Si la respuesta no es ok, lanzar un error y mostrar mensaje
    // Si es ok, parsear JSON, guardar token en localStorage y avisar al padre que ya está logeado
    // Finalmente, limpiar estado de carga y errores simples
    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      // Manejo de errores comunes de autenticación como credenciales inválidas (401 Unauthorized)
      // Si la respuesta no es ok, lanzar un error y mostrar mensaje
      // Si es ok, parsear JSON, guardar token en localStorage y avisar al padre que ya está logeado
      // Finalmente, limpiar estado de carga y errores simples
      if (!res.ok) {
        throw new Error("Credenciales inválidas");
      }

      // Si el login es exitoso, parsear JSON, guardar token en localStorage y avisar al padre que ya está logeado
      // Finalmente, limpiar estado de carga y errores simples 
      // onLogin en el padre actualizará el estado del token y disparará la carga de usuarios
      // Si hay un error en cualquier parte del proceso, capturarlo y mostrar mensaje
      // Finalmente, limpiar estado de carga y errores simples
      // Manejo de la respuesta exitosa
      const data = await res.json();
      localStorage.setItem("token", data.token);
      onLogin();
    } catch (err) {
      setError(err.message);
    }
  };

  // Renderizado del formulario de login con manejo simple de errores y estilos básicos
  // Al hacer submit, llamar a handleSubmit
  // Mostrar mensaje de error si existe
  // Usar estilos inline simples para centrar y espaciar elementos
  return (
    <div style={{ padding: 24, fontFamily: "Arial, sans-serif" }}>
      <h1>Iniciar Sesión</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 12 }}>
          <label>
            Usuario:
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              style={{ marginLeft: 8 }}
            />
          </label>
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>
            Contraseña:
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              style={{ marginLeft: 8 }}
            />
          </label>
        </div>
        <button type="submit">Ingresar</button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
