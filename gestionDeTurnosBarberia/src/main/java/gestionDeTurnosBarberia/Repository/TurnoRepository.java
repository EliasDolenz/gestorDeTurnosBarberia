package gestionDeTurnosBarberia.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Domain.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    Boolean existsByUnBarberoAndDiaYHoraDelTurno(Barbero unBarbero, LocalDateTime diaYHoraDelTurno);

    List<Turno> findByUnBarberoAndDiaYHoraDelTurnoBetween(Barbero unBarbero, LocalDateTime inicio, LocalDateTime fin);
}
