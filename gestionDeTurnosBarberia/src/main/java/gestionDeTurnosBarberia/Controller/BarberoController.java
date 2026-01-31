package gestionDeTurnosBarberia.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Service.BarberoService;

@RestController
@RequestMapping("/api/barberos")
public class BarberoController {
    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @GetMapping
    public ResponseEntity<List<Barbero>> getAllBarbero() {
        List<Barbero> barberos = this.barberoService.findAllBarbero();
        return ResponseEntity.ok(barberos);
    }

    @GetMapping("/{idBarbero}")
    public ResponseEntity<Barbero> getBarberoById(@PathVariable Long idBarbero) {
        Barbero barbero = this.barberoService.findBarberoById(idBarbero);
        return ResponseEntity.ok(barbero);
    }

    @PutMapping("/{idBarbero}")
    public ResponseEntity<Barbero> updateBarbero(@PathVariable Long idBarbero, @RequestBody Barbero detailBarbero) {
        Barbero barberoExisting = this.barberoService.findBarberoById(idBarbero);

        barberoExisting.setFirstName(detailBarbero.getFirstName());
        barberoExisting.setSurname(detailBarbero.getSurname());

        Barbero barberoUpdate = barberoService.saveBarbero(barberoExisting);
        return ResponseEntity.ok(barberoUpdate);

    }

    @DeleteMapping("/{idBarbero}")
    public ResponseEntity<Void> deleteBarberoById(@PathVariable Long idBarbero) {
        this.barberoService.deleteById(idBarbero);
        return ResponseEntity.noContent().build();

    }

    @PostMapping
    public ResponseEntity<Barbero> createBarbero(@RequestBody Barbero newBarbero) {
        Barbero savedBarbero = this.barberoService.saveBarbero(newBarbero);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(savedBarbero.getId()).toUri();
        return ResponseEntity.created(location).body(savedBarbero);
    }
}
