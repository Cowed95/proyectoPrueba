// Servicio para autenticación: login
export async function login(username, password) {
  if (!username || !password) {
    throw new Error("Debes ingresar usuario y contraseña");
  }

  // Realizamos la petición al backend de login y manejo de errores
  // Aceptamos distintas formas de token en la respuesta (JSON)
  // Ejemplo de respuesta esperada: { token : "abc123" } o { access
  // : "abc 123" } o { jwt: "abc 123" } etc.
  try {
    const res = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    // Manejo de errores HTTP y de parseo de JSON
    // Intentamos extraer mensaje de error del cuerpo si es posible
    // Si no, usamos un mensaje genérico con el código HTTP de error
    // Esto cubre errores 4xx y 5xx y también errores de parseo JSON
    // en la respuesta del servidor de error
    if (!res.ok) {
      let text;
      try {
        // Intentamos extraer mensaje de error del cuerpo JSON
        text = await res.text();
        const errJson = JSON.parse(text);
        throw new Error(errJson.message || `Error en login: HTTP ${res.status}`);
      } catch {
        throw new Error(`Error en login: HTTP ${res.status}`);
      }
    }

    // Parseamos la respuesta JSON exitosa y extraemos el token
    // Aceptamos distintas formas de token en la respuesta
    const data = await res.json();

    // Intentamos extraer el token de distintas posibles claves
    // Si no encontramos ninguna, lanzamos un error
    const token = data.token || data.accessToken || data.jwt;
    if (!token) {
      throw new Error("El servidor no devolvió un token válido");
    }

    // Retornamos el objeto completo de datos junto con el token extraído
    // para que el llamador pueda usar otros datos si es necesario
    return { ...data, token };
  } catch (err) {
    console.error("Error en authService.login:", err);
    throw err;
  }
}
