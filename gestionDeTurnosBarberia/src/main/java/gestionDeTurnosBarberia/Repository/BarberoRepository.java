package gestionDeTurnosBarberia.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gestionDeTurnosBarberia.Domain.Barbero;

public interface BarberoRepository extends JpaRepository<Barbero, Long> {

}
