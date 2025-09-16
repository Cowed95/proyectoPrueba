// import React desde 'react';
import { useState } from "react";
import LoginPage from "./pages/LoginPage";
import UsersPage from "./pages/UserPage";

// Componente principal de la aplicación
export default function App() {
  // Estado global de la aplicación
  // Token de autenticación, usuarios y control de carga
  const [token, setToken] = useState(localStorage.getItem("token") || "");

  // Función para manejar login exitoso desde LoginPage.jsx
  const handleLogin = (newToken) => {
    // Guardar token en estado global
    setToken(newToken);
  };

  // Función para manejar logout desde UsersPage.jsx
  const handleLogout = () => {
    // Limpiar token del estado y del localStorage
    localStorage.removeItem("token");
    setToken("");
  };

  // Renderizado condicional:
  // - Si hay token, mostrar UsersPage (lista de usuarios y logout)
  // - Si no hay token, mostrar LoginPage (formulario de login)
  // Cada componente maneja su propio estado interno y fetch
  return (
    <div style={{ padding: 24, fontFamily: "Arial, sans-serif" }}>
      {!token ? (
        // LoginPage se encarga de todo el manejo de login y llamadas a authService
        <LoginPage onLogin={handleLogin} />
      ) : (
        // UsersPage se encarga de mostrar usuarios protegidos y manejar logout
        <UsersPage token={token} onLogout={handleLogout} />
      )}
    </div>
  );
}
