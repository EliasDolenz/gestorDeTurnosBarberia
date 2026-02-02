import { useEffect, useState } from 'react';
import './App.css';

function App() {
  // 1. ESTADOS
  const [barberos, setBarberos] = useState([]);
  const [nuevoBarbero, setNuevoBarbero] = useState({ firstName: '', surname: '' });
  const [error, setError] = useState(null);

  // 2. FUNCIONES DE CARGA (FETCH)
  const traerBarberos = () => {
    fetch('http://localhost:8080/api/barberos')
      .then(res => res.json())
      .then(data => {
        setBarberos(data);
        setError(null); // Limpiamos errores si la carga fue exitosa
      })
      .catch(err => setError("No se pudo conectar con el servidor de Java."));
  };

  useEffect(() => {
    traerBarberos();
  }, []);

  // 3. LÓGICA DE ENVÍO (POST)
  const manejarEnvio = (e) => {
    e.preventDefault();

    fetch('http://localhost:8080/api/barberos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(nuevoBarbero)
    })
      .then(async res => {
        const data = await res.json();
        if (!res.ok) {
          // Aquí capturamos tu BusinessLogicException de Java
          throw new Error(data.mensaje || "Error al guardar");
        }
        return data;
      })
      .then(() => {
        traerBarberos(); // Refrescamos la lista
        setNuevoBarbero({ firstName: '', surname: '' }); // Limpiamos formulario
        setError(null);
        alert("¡Barbero registrado con éxito!");
      })
      .catch(err => {
        setError(err.message);
      });
  };

  const borrarBarbero = (id) => {
    if (window.confirm("¿Estás seguro de que querés eliminar a este especialista?")) {
      fetch(`http://localhost:8080/api/barberos/${id}`, {
        method: 'DELETE',
      })
        .then(res => {
          if (res.ok) {
            // Opción A: Volver a traer todos de la DB
            traerBarberos();
            alert("Barbero eliminado");
          } else {
            throw new Error("No se pudo eliminar el barbero.");
          }
        })
        .catch(err => setError(err.message));
    }
  };
  // 4. RENDERIZADO (DISEÑO)
  return (
    <div className="app-wrapper">
      <header className="main-header">
        <h1>Barbería Pro <span className="gold-text">Gestor</span></h1>
      </header>

      <main className="dashboard">
        {/* Lado Izquierdo: Formulario */}
        <section className="form-section">
          <div className="card">
            <h3>Registrar Staff</h3>
            {error && <div className="error-banner">{error}</div>}

            <form onSubmit={manejarEnvio}>
              <div className="input-group">
                <label>Nombre</label>
                <input
                  type="text"
                  placeholder="Ej: Elías"
                  value={nuevoBarbero.firstName}
                  onChange={(e) => setNuevoBarbero({ ...nuevoBarbero, firstName: e.target.value })}
                />
              </div>
              <div className="input-group">
                <label>Apellido</label>
                <input
                  type="text"
                  placeholder="Ej: Dolenz"
                  value={nuevoBarbero.surname}
                  onChange={(e) => setNuevoBarbero({ ...nuevoBarbero, surname: e.target.value })}
                />
              </div>
              <button type="submit" className="btn-gold">Guardar Barbero</button>
            </form>
          </div>
        </section>

        {/* Lado Derecho: Lista de Barberos */}
        <section className="list-section">
          <h3>Nuestros Especialistas</h3>

          {barberos.length === 0 ? (
            <div className="empty-state">
              <p>No hay barberos registrados en el sistema.</p>
            </div>
          ) : (
            <div className="grid-barberos">
              {barberos.map(b => (
                <div key={b.id} className="barbero-card">
                  <div className="avatar">{b.firstName.charAt(0)}</div>
                  <div className="barbero-info">
                    <h4>{b.firstName} {b.surname}</h4>
                    <span className="badge">Staff Senior</span>
                  </div>

                  {/* Nuevo botón de borrar */}
                  <button
                    className="btn-delete"
                    onClick={() => borrarBarbero(b.id)}
                    title="Eliminar Barbero"
                  >
                    🗑️
                  </button>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;