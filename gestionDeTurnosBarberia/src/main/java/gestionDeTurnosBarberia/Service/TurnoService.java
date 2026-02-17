package gestionDeTurnosBarberia.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Domain.Cliente;
import gestionDeTurnosBarberia.Domain.Turno;
import gestionDeTurnosBarberia.Dto.TurnoRequest;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Exception.ResourceNotFoundException;
import gestionDeTurnosBarberia.Repository.TurnoRepository;

@Service
public class TurnoService {
    private final BarberoService barberoService;
    private final ClienteService clienteService;
    private final TurnoRepository turnoRepository;

    public TurnoService(BarberoService barberoService, ClienteService clienteService,
            TurnoRepository turnoRepository) {
        this.barberoService = barberoService;
        this.clienteService = clienteService;
        this.turnoRepository = turnoRepository;
    }

    public Turno saveTurno(Long idCliente, Long idBarbero, LocalDateTime diaYHoraDelTurno) {
        LocalTime horaDelTurno = diaYHoraDelTurno.toLocalTime();
        LocalTime horaDeApertura = LocalTime.of(9, 0);
        LocalTime horaDeCierre = LocalTime.of(20, 0);
        Barbero unBarbero = barberoService.findBarberoById(idBarbero);
        Cliente unCliente = clienteService.findClienteById(idCliente);

        if (horaDelTurno.isAfter(horaDeCierre) || horaDelTurno.isBefore(horaDeApertura)) {
            throw new BusinessLogicException(
                    "En ese horario la barberia se encuentra cerrada. Trabajamos desde las 9:00hs hasta las 20:00hs");
        }

        if (diaYHoraDelTurno.isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException("El día y horario seleccionado corresponde a una fecha que ya paso");
        }

        if (this.turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(unBarbero, diaYHoraDelTurno)) {
            throw new BusinessLogicException("El barbero no tiene disponibilidad en ese turno");
        }

        Turno nuevoTurno = new Turno();

        nuevoTurno.setUnBarbero(unBarbero);
        nuevoTurno.setUnCliente(unCliente);
        nuevoTurno.setDiaYHoraDelTurno(diaYHoraDelTurno);

        return this.turnoRepository.save(nuevoTurno);

    }

    public List<Turno> findAllTurnos() {
        return this.turnoRepository.findAll();
    }

    public Turno findTurnoById(Long idTurno) {
        return this.turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un turno con ese ID"));
    }

    public Boolean deleteById(Long idTurno) {
        if (turnoRepository.existsById(idTurno)) {
            this.turnoRepository.deleteById(idTurno);
            return Boolean.TRUE;
        }
        throw new ResourceNotFoundException("El turno indicado no existe");
    }

    public Turno updateTurno(Long idTurno, TurnoRequest newRequest) {
        Barbero unBarbero = barberoService.findBarberoById(newRequest.getIdBarbero());
        Cliente unCliente = clienteService.findClienteById(newRequest.getIdCliente());

        Turno turnoActualizado = this.turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un turno con ese ID"));

        if (newRequest.getFecha().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException("No se puede reprogramar un turno para una fecha pasada");
        }

        Boolean cambioDeFecha = !turnoActualizado.getDiaYHoraDelTurno().equals(newRequest.getFecha());
        Boolean cambioDeBarbero = !turnoActualizado.getUnBarbero().getId().equals(newRequest.getIdBarbero());

        if (cambioDeBarbero || cambioDeFecha) {
            if (this.turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(unBarbero, newRequest.getFecha())) {
                throw new BusinessLogicException("El barbero no tiene disponibilidad en ese turno");
            }

        }

        turnoActualizado.setUnBarbero(unBarbero);
        turnoActualizado.setUnCliente(unCliente);
        turnoActualizado.setDiaYHoraDelTurno(newRequest.getFecha());

        return this.turnoRepository.save(turnoActualizado);

    }

    public List<Turno> findTurnosByBarberoAndFecha(Long idBarbero, LocalDateTime unaFechaYHora) {
        Barbero unBarbero = this.barberoService.findBarberoById(idBarbero);
        LocalDateTime inicio = unaFechaYHora.with(LocalTime.MIN);
        LocalDateTime fin = unaFechaYHora.with(LocalTime.MAX);

        return this.turnoRepository.findByUnBarberoAndDiaYHoraDelTurnoBetween(unBarbero, inicio,
                fin);
    }
}
