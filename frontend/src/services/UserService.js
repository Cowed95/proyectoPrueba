// Servicio para obtener usuarios (requiere token)
export async function getUsers(token) {
  if (!token) {
    throw new Error("Token no proporcionado. No se puede acceder a los usuarios.");
  }

  try {
    const res = await fetch("/api/users", {
      headers: { Authorization: `Bearer ${token}` },
    });

    // Manejo de errores comunes
    if (!res.ok) {
      if (res.status === 401) throw new Error("Token inválido o expirado");
      if (res.status === 403) throw new Error("No tienes permisos");
      throw new Error(`Error en la petición: HTTP ${res.status}`);
    }

    // Retornamos JSON con la lista de usuarios
    const data = await res.json();

    // Validamos que recibimos un array
    if (!Array.isArray(data)) {
      throw new Error("La respuesta del servidor no es una lista de usuarios válida.");
    }

    return data;
  } catch (err) {
    // Propagamos el error para manejarlo en la página
    console.error("Error en userService.getUsers:", err);
    throw err;
  }
}
