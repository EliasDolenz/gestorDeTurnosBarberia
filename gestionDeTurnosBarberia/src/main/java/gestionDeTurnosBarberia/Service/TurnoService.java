package gestionDeTurnosBarberia.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TurnoService.class);
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
        logger.info("Creando nuevo turno - Cliente {}, Barbero {}, Fecha y Hora: {}", idCliente, idBarbero,
                diaYHoraDelTurno);

        LocalTime horaDelTurno = diaYHoraDelTurno.toLocalTime();
        LocalTime horaDeApertura = LocalTime.of(9, 0);
        LocalTime horaDeCierre = LocalTime.of(20, 0);
        Barbero unBarbero = barberoService.findBarberoById(idBarbero);
        Cliente unCliente = clienteService.findClienteById(idCliente);

        if (horaDelTurno.isAfter(horaDeCierre) || horaDelTurno.isBefore(horaDeApertura)) {
            logger.warn("Intento de turno fuera de horario: {}", horaDelTurno);
            throw new BusinessLogicException(
                    "En ese horario la barberia se encuentra cerrada. Trabajamos desde las 9:00hs hasta las 20:00hs");
        }

        if (diaYHoraDelTurno.isBefore(LocalDateTime.now())) {
            logger.warn("Intento de turno en fecha pasada: {}", diaYHoraDelTurno);
            throw new BusinessLogicException("El día y horario seleccionado corresponde a una fecha que ya paso");
        }

        if (this.turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(unBarbero, diaYHoraDelTurno)) {
            logger.warn("El Barbero no tiene disponible el día y horario: {}", diaYHoraDelTurno);
            throw new BusinessLogicException("El barbero no tiene disponibilidad en ese turno");
        }

        Turno nuevoTurno = new Turno();

        nuevoTurno.setUnBarbero(unBarbero);
        nuevoTurno.setUnCliente(unCliente);
        nuevoTurno.setDiaYHoraDelTurno(diaYHoraDelTurno);

        Turno saved = this.turnoRepository.save(nuevoTurno);
        logger.info("Turno creado exitosamente - ID: {}", saved.getId());
        return saved;

    }

    public List<Turno> findAllTurnos() {
        logger.debug("Obteniendo todos los turnos");
        return this.turnoRepository.findAll();
    }

    public Turno findTurnoById(Long idTurno) {
        logger.debug("Buscando turno ID: {}", idTurno);
        return this.turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un turno con ese ID"));
    }

    public Boolean deleteById(Long idTurno) {
        logger.info("Eliminando turno Existente - idTurno: {}",
                idTurno);
        if (turnoRepository.existsById(idTurno)) {
            logger.info("Turno eliminado exitosamente.");
            this.turnoRepository.deleteById(idTurno);
            return Boolean.TRUE;
        }
        logger.warn("El turno con el ID: {} no existe.", idTurno);
        throw new ResourceNotFoundException("El turno indicado no existe");
    }

    public Turno updateTurno(Long idTurno, TurnoRequest newRequest) {
        logger.info("Actualizando turno ID: {} - Nueva fecha: {}", idTurno, newRequest.getFecha());

        Barbero unBarbero = barberoService.findBarberoById(newRequest.getIdBarbero());
        Cliente unCliente = clienteService.findClienteById(newRequest.getIdCliente());

        Turno turnoActualizado = this.turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un turno con ese ID"));

        if (newRequest.getFecha().isBefore(LocalDateTime.now())) {
            logger.warn("La fecha seleccionada: {} ya paso", newRequest.getFecha());
            throw new BusinessLogicException("No se puede reprogramar un turno para una fecha pasada");
        }

        Boolean cambioDeFecha = !turnoActualizado.getDiaYHoraDelTurno().equals(newRequest.getFecha());
        Boolean cambioDeBarbero = !turnoActualizado.getUnBarbero().getId().equals(newRequest.getIdBarbero());

        if (cambioDeBarbero || cambioDeFecha) {
            if (this.turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(unBarbero, newRequest.getFecha())) {
                logger.warn("Barbero no disponible para cambio - ID: {}, Nueva fecha: {}", newRequest.getIdBarbero(),
                        newRequest.getFecha());
                throw new BusinessLogicException("El barbero no tiene disponibilidad en ese turno");
            }

        }

        turnoActualizado.setUnBarbero(unBarbero);
        turnoActualizado.setUnCliente(unCliente);
        turnoActualizado.setDiaYHoraDelTurno(newRequest.getFecha());
        logger.info("Turno actualizado - ID: {}", turnoActualizado.getId());
        return this.turnoRepository.save(turnoActualizado);

    }

    public List<Turno> findTurnosByBarberoAndFecha(Long idBarbero, LocalDateTime unaFechaYHora) {
        logger.debug("Buscando turnos - Barbero: {}, Fecha: {}", idBarbero, unaFechaYHora);
        Barbero unBarbero = this.barberoService.findBarberoById(idBarbero);
        LocalDateTime inicio = unaFechaYHora.with(LocalTime.MIN);
        LocalDateTime fin = unaFechaYHora.with(LocalTime.MAX);

        logger.info("Agenda encontrada - Barbero ID: {}", idBarbero);
        return this.turnoRepository.findByUnBarberoAndDiaYHoraDelTurnoBetween(unBarbero, inicio,
                fin);
    }
}
