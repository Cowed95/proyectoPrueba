// Servicio para obtener usuarios (requiere token)
export async function getUsers(token) {
  const res = await fetch("/api/users", {
    headers: { Authorization: `Bearer ${token}` },
  });

  // Si hay error en la respuesta, lanzamos excepción
  if (!res.ok) {
    if (res.status === 401) throw new Error("Token inválido o expirado");
    if (res.status === 403) throw new Error("No tienes permisos");
    throw new Error(`HTTP ${res.status}`);
  }

  // Retornamos JSON con la lista de usuarios
  return res.json();
}
