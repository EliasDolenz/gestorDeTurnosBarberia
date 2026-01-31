package gestionDeTurnosBarberia.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import gestionDeTurnosBarberia.Domain.Cliente;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Exception.ResourceNotFoundException;
import gestionDeTurnosBarberia.Repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente saveCliente(Cliente unCliente) {

        if (unCliente.getFirstName() == null || unCliente.getFirstName().isBlank()) {
            throw new BusinessLogicException("El cliente debe tener nombre");
        } else if (unCliente.getSurname() == null || unCliente.getSurname().isBlank()) {
            throw new BusinessLogicException("El cliente debe tener apellido");
        }
        if (unCliente.getTelefono() == null || unCliente.getTelefono().length() <= 10) {
            throw new BusinessLogicException("El numero de telefono ingresado es incorrecto");
        }
        return this.clienteRepository.save(unCliente);
    }

    public List<Cliente> findAllCliente() {
        return this.clienteRepository.findAll();
    }

    public Cliente findClienteById(Long idCliente) {
        return this.clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el Id ingresado"));
    }

    public Boolean deleteById(Long idCliente) {
        if (clienteRepository.existsById(idCliente)) {
            this.clienteRepository.deleteById(idCliente);
            return Boolean.TRUE;
        }
        throw new ResourceNotFoundException("El cliente indicado no existe");

    }
}
