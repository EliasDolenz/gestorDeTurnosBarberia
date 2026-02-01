package gestionDeTurnosBarberia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Domain.Cliente;
import gestionDeTurnosBarberia.Domain.Turno;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Repository.TurnoRepository;
import gestionDeTurnosBarberia.Service.BarberoService;
import gestionDeTurnosBarberia.Service.ClienteService;
import gestionDeTurnosBarberia.Service.TurnoService;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private BarberoService barberoService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private TurnoService turnoService;

    @Test
    void rechazarReservaCuandoElBarberoYaTieneUnTurno() {

        Barbero barbero = new Barbero();
        Cliente unCliente = new Cliente();
        LocalDateTime horarioDelTurno = LocalDateTime.parse("2026-02-25T15:30:00");

        when(barberoService.findBarberoById(any())).thenReturn(barbero);
        when(clienteService.findClienteById(any())).thenReturn(unCliente);
        when(turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(barbero, horarioDelTurno)).thenReturn(true);

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            turnoService.saveTurno(1L, 1L, horarioDelTurno);
        });

        assertEquals("El barbero no tiene disponibilidad en ese turno", ex.getMessage());
    }

    @Test
    void cuandoGuardoTurnoValido_entoncesDevuelveElTurno() {
        Long idBarbero = 1L;
        Long idCliente = 1L;
        LocalDateTime horaDelTurno = LocalDateTime.parse("2026-03-20T15:30:00");

        Barbero barberoSimulado = new Barbero();
        Cliente clienteSimulado = new Cliente();

        barberoSimulado.setId(idBarbero);
        clienteSimulado.setId(idCliente);

        Turno turnoEsperado = new Turno();
        turnoEsperado.setDiaYHoraDelTurno(horaDelTurno);
        turnoEsperado.setUnBarbero(barberoSimulado);
        turnoEsperado.setUnCliente(clienteSimulado);

        when(barberoService.findBarberoById(idBarbero)).thenReturn(barberoSimulado);
        when(clienteService.findClienteById(idCliente)).thenReturn(clienteSimulado);

        when(turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(barberoSimulado, horaDelTurno))
                .thenReturn(Boolean.FALSE);

        when(turnoService.saveTurno(idCliente, idBarbero, horaDelTurno)).thenReturn(turnoEsperado);

        Turno resultado = turnoService.saveTurno(idCliente, idBarbero, horaDelTurno);

        assertNotNull(resultado);
        assertEquals(horaDelTurno, resultado.getDiaYHoraDelTurno());
        verify(turnoRepository, times(1)).save(any(Turno.class));
    }

    @Test
    void cuandoElBarberoYaTieneTurno_entoncesLanzaError() {
        LocalDateTime fechaFutura = LocalDateTime.parse("2026-03-19T15:30:00");
        Long idBarbero = 1L;
        Barbero barberoSimulado = new Barbero();

        Long idCliente = 1L;
        Cliente clienteSimulado = new Cliente();
        clienteSimulado.setId(idCliente);
        barberoSimulado.setId(idBarbero);

        when(barberoService.findBarberoById(idBarbero)).thenReturn(barberoSimulado);
        when(turnoRepository.existsByUnBarberoAndDiaYHoraDelTurno(barberoSimulado, fechaFutura))
                .thenReturn(Boolean.TRUE);

        assertThrows(BusinessLogicException.class, () -> {
            turnoService.saveTurno(clienteSimulado.getId(), barberoSimulado.getId(), fechaFutura);
        });
    }

    @Test
    void cuandoLaFechaEsPasada_entoncesLanzaError() {
        LocalDateTime fecha = LocalDateTime.now().minusDays(1);
        Long idCliente = 1L;
        Long idBarbero = 1L;

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            turnoService.saveTurno(idCliente, idBarbero, fecha);
        });

        assertEquals("El día y horario seleccionado corresponde a una fecha que ya paso", ex.getMessage());
    }

    @Test
    void elBarberoNoTieneUnHorarioDisponible_entoncesLanzaError() {
        LocalDateTime fecha = LocalDateTime.of(2025, 2, 5, 10, 0, 0);
        Long idBarbero = 1L;
        Barbero barberoSimulado = new Barbero();
        barberoSimulado.setId(idBarbero);
        when(barberoService.findBarberoById(idBarbero)).thenReturn(barberoSimulado);

        turnoService.findTurnosByBarberoAndFecha(idBarbero, fecha);

        LocalDateTime inicioEsperado = fecha.with(LocalTime.MIN);
        LocalDateTime finEsperado = fecha.with(LocalTime.MAX);

        verify(turnoRepository).findByUnBarberoAndDiaYHoraDelTurnoBetween(
                eq(barberoSimulado),
                eq(inicioEsperado),
                eq(finEsperado));
    }
}
