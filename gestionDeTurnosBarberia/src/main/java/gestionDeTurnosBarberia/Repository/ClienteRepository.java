package gestionDeTurnosBarberia.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gestionDeTurnosBarberia.Domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
