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

import gestionDeTurnosBarberia.Domain.Cliente;
import gestionDeTurnosBarberia.Service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = this.clienteService.findAllCliente();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long idCliente) {
        Cliente cliente = this.clienteService.findClienteById(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long idCliente, @RequestBody Cliente detailCliente) {
        Cliente clienteExisting = this.clienteService.findClienteById(idCliente);

        clienteExisting.setFirstName(detailCliente.getFirstName());
        clienteExisting.setSurname(detailCliente.getSurname());
        clienteExisting.setTelefono(detailCliente.getTelefono());
        clienteExisting.setTurnos(detailCliente.getTurnos());

        Cliente clienteUpdate = clienteService.saveCliente(clienteExisting);
        return ResponseEntity.ok(clienteUpdate);
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> deleteClienteById(@PathVariable Long idCliente) {
        this.clienteService.deleteById(idCliente);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente newCliente) {
        Cliente savedCliente = this.clienteService.saveCliente(newCliente);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(savedCliente.getId()).toUri();
        return ResponseEntity.created(location).body(savedCliente);
    }
}
