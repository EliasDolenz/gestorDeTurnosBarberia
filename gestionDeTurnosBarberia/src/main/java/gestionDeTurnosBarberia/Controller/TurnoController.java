package gestionDeTurnosBarberia.Controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gestionDeTurnosBarberia.Domain.Turno;
import gestionDeTurnosBarberia.Dto.TurnoRequest;
import gestionDeTurnosBarberia.Service.TurnoService;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {
    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping
    public ResponseEntity<List<Turno>> getAllTurnos() {
        List<Turno> turnos = this.turnoService.findAllTurnos();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/{idTurno}")
    public ResponseEntity<Turno> getTurnoById(@PathVariable Long idTurno) {
        Turno turno = this.turnoService.findTurnoById(idTurno);
        return ResponseEntity.ok(turno);
    }

    @PutMapping("{idTurno}")
    public ResponseEntity<Turno> updateTurno(@PathVariable Long idTurno, @RequestBody TurnoRequest newRequest) {
        Turno turnoActualizado = this.turnoService.updateTurno(idTurno, newRequest);
        return ResponseEntity.ok(turnoActualizado);

    }

    @PostMapping
    public ResponseEntity<Turno> createTurno(@RequestBody TurnoRequest newRequest) {
        Turno savedTurno = this.turnoService.saveTurno(newRequest.getIdCliente(), newRequest.getIdBarbero(),
                newRequest.getFecha());

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(savedTurno.getId()).toUri();
        return ResponseEntity.created(location).body(savedTurno);

    }

    @GetMapping("/agenda")
    public ResponseEntity<List<Turno>> getAgenda(
            @RequestParam Long idBarbero,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {

        List<Turno> turnos = this.turnoService.findTurnosByBarberoAndFecha(idBarbero, fecha);

        return ResponseEntity.ok(turnos);
    }
}