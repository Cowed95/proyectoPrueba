import { useEffect, useState } from "react";

export default function App() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token"); // Recuperar el token guardado

    if (!token) {
      console.warn("No hay token guardado en localStorage");
      setLoading(false);
      return;
    }

    fetch("/api/users", {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data) => setUsers(data))
      .catch((err) => {
        console.error("Error fetching users:", err);
        setUsers([]);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div style={{ padding: 24, fontFamily: "Arial, sans-serif" }}>
      <h1>Usuarios</h1>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <ul>
          {users.length === 0 ? (
            <li>No hay usuarios (o la API no respondió)</li>
          ) : (
            users.map((u) => (
              <li key={u.id}>
                {u.username} — {u.email}
              </li>
            ))
          )}
        </ul>
      )}
    </div>
  );
}
