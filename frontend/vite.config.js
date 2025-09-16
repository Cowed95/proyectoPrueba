// Importaciones necesarias para la configuración de Vite y el plugin de React

import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// Configuración de Vite para un proyecto React con proxy para la API backend
// Usa el plugin de React para manejar JSX y otras características de React
// Configura un proxy para redirigir las llamadas a /api al servidor backend en localhost:8080
// Esto permite evitar problemas de CORS durante el desarrollo
// y facilita la comunicación entre el frontend y backend en desarrollo
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
