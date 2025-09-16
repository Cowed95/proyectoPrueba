// Servicio para autenticación: login
export async function login(username, password) {
  // Hacemos petición a la API de login
  const res = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  // Si hay error en la respuesta, lanzamos excepción
  if (!res.ok) {
    let text;
    try {
      text = await res.text();
      const errJson = JSON.parse(text);
      throw new Error(errJson.message || "Error en login");
    } catch {
      throw new Error(`HTTP ${res.status}`);
    }
  }

  // Retornamos JSON con datos (esperamos que incluya token)
  const data = await res.json();

  // Validamos que venga token
  if (!data.token) {
    throw new Error("El servidor no devolvió token. Revisa tu API.");
  }

  return data;
}
