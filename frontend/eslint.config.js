// importaciones necesarias para la configuración de ESLint y plugins relevantes para un proyecto React moderno con Vite

import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import { defineConfig, globalIgnores } from 'eslint/config'

// Configuración de ESLint para un proyecto React moderno con Vite y manejo de hooks
// Incluye reglas recomendadas para JavaScript, React Hooks y Vite con React Refresh
// Ignora la carpeta dist que es generada por el build
// Configura el parser para entender JSX y las últimas características de ECMAScript
// Define algunas reglas personalizadas, como ignorar variables no usadas que empiecen con mayúscula o guion bajo
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{js,jsx}'],
    extends: [
      // Extensiones recomendadas para JavaScript, React Hooks y Vite con React Refresh
      js.configs.recommended,
      // Usar la configuración recomendada más reciente para React Hooks
      reactHooks.configs['recommended-latest'],
      // Usar la configuración recomendada para Vite con React Refresh
      reactRefresh.configs.vite,
    ],
    // Configuración del entorno para navegador y ES2021 (modern JS)
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      // Configuración del parser para entender JSX y las últimas características de ECMAScript
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },

    // Reglas personalizadas
    rules: {
      'no-unused-vars': ['error', { varsIgnorePattern: '^[A-Z_]' }],
    },
  },
])
