// importaciones necesarias de React y servicios

import { useState } from "react";
import { login } from "../services/authService";

// Componente de la página de login
// Recibe onLogin como prop para actualizar el estado del token en App.jsx
// Maneja su propio estado para username, password y loading del formulario
// Al enviar el formulario, llama a login del servicio authService y maneja la respuesta
// Si el login es exitoso, guarda el token en localStorage y llama a onLogin con el token
// Si hay un error, muestra una alerta con el mensaje de error
export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  // Manejo del envío del formulario de login y llamadas al servicio de autenticación
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    // Limpiar error previo (no usamos estado de error aquí, solo alerta)
    try {
      const data = await login(username, password);
      localStorage.setItem("token", data.token);
      onLogin(data.token);

      // Limpiar formulario después de login exitoso (no es obligatorio)
    } catch (err) {
      alert(err.message);
      // opcional: limpiar formulario en caso de error
    } finally {
      setLoading(false);
    }
  };

  // Renderizado del formulario de login con campos controlados y botón de envío
  // El botón muestra "Ingresando..." y se deshabilita mientras loading es true
  // No mostramos errores inline, solo alertas en catch
  // No hay manejo de estado de error aquí, solo en App.jsx
  // El formulario es simple y directo
  return (
    <form onSubmit={handleSubmit}>
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
  );
}
