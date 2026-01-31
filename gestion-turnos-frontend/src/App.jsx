import { useEffect, useState } from 'react'

function App() {
  const [barberos, setBarberos] = useState([])
  // Estado para el nuevo barbero
  const [nuevoBarbero, setNuevoBarbero] = useState({ firstName: '', surname: '' })

  // Función para obtener barberos (la sacamos del useEffect para poder reutilizarla)
  const traerBarberos = () => {
    fetch('http://localhost:8080/api/barberos')
      .then(res => res.json())
      .then(data => setBarberos(data))
  }

  useEffect(() => {
    traerBarberos()
  }, [])

  // Función para enviar el POST
  const manejarEnvio = (e) => {
  e.preventDefault();

  fetch('http://localhost:8080/api/barberos', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(nuevoBarbero)
  })
  .then(async res => {
    const data = await res.json(); // Leemos el JSON (sea el barbero o el error)
    
    if (!res.ok) {
      // Si el status no es 2xx (ej: 400 o 404), lanzamos el mensaje que viene de Java
      throw new Error(data.mensaje || "Error desconocido");
    }
    
    return data;
  })
  .then(() => {
    alert("¡Barbero agregado con éxito!");
    traerBarberos();
    setNuevoBarbero({ firstName: '', surname: '' });
  })
  .catch(error => {
    // Aquí es donde mostramos el mensaje de tu BusinessLogicException
    alert("Error: " + error.message);
  });
}

  return (
    <div style={{ padding: '20px' }}>
      <h1>Gestión de Barberos</h1>
      
      {/* Formulario de Carga */}
      <form onSubmit={manejarEnvio} style={{ marginBottom: '20px' }}>
        <input 
          placeholder="Nombre" 
          value={nuevoBarbero.firstName}
          onChange={(e) => setNuevoBarbero({...nuevoBarbero, firstName: e.target.value})} 
        />
        <input 
          placeholder="Apellido" 
          value={nuevoBarbero.surname}
          onChange={(e) => setNuevoBarbero({...nuevoBarbero, surname: e.target.value})} 
        />
        <button type="submit">Agregar Barbero</button>
      </form>

      <hr />

      {/* Lista de Barberos */}
      <ul>
        {barberos.map(b => (
          <li key={b.id}>{b.firstName} {b.surname}</li>
        ))}
      </ul>
    </div>
  )
}

export default App