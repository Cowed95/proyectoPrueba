// importaciones necesarias para React y el renderizado

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

// Renderizado del componente principal App dentro de StrictMode para ayudar a detectar problemas potenciales en la aplicación
// El componente App maneja el estado global de la aplicación, incluyendo autenticación y datos de usuarios
// Se asume que hay un div con id 'root' en el HTML donde se monta la aplicación React 
// (normalmente en public/index.html)
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
