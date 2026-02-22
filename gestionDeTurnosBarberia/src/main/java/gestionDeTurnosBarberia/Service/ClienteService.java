package gestionDeTurnosBarberia.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gestionDeTurnosBarberia.Domain.Cliente;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Exception.ResourceNotFoundException;
import gestionDeTurnosBarberia.Repository.ClienteRepository;

@Service
public class ClienteService {
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente saveCliente(Cliente unCliente) {
        logger.info("Guardando Cliente ID: {}, Nombre: {}, Apellido: {}, Telefono: {}", unCliente.getId(),
                unCliente.getFirstName(), unCliente.getSurname(), unCliente.getTelefono());

        if (unCliente.getFirstName() == null || unCliente.getFirstName().isBlank()) {
            logger.warn("Validación Fallida: El cliente debe tener nombre");
            throw new BusinessLogicException("El cliente debe tener nombre");
        } else if (unCliente.getSurname() == null || unCliente.getSurname().isBlank()) {
            logger.warn("Validación Fallida: El cliente debe tener Apellido");
            throw new BusinessLogicException("El cliente debe tener apellido");
        }
        if (unCliente.getTelefono() == null || unCliente.getTelefono().length() <= 10) {
            logger.warn("Validación Fallida: El numero de telefono debe tener 10 digitos");
            throw new BusinessLogicException("El numero de telefono ingresado es incorrecto");
        }
        Cliente saved = this.clienteRepository.save(unCliente);
        logger.info("Cliente guardado exitosamente - ID: {}, Nombre: {}, Apellido: {}, Telefono: {}", saved.getId(),
                saved.getFirstName(), saved.getSurname(), saved.getTelefono());
        return saved;
    }

    public List<Cliente> findAllCliente() {
        logger.debug("Obteniendo lista de todos los clientes");
        return this.clienteRepository.findAll();
    }

    public Cliente findClienteById(Long idCliente) {
        logger.debug("Obteniendo cliente con el ID: {}", idCliente);

        return this.clienteRepository.findById(idCliente).orElseThrow(() -> {
            logger.warn("Cliente con ID: {} no encontrado", idCliente);
            return new ResourceNotFoundException("No existe el Id ingresado");
        });
    }

    public Boolean deleteById(Long idCliente) {
        logger.info("Eliminando cliente con ID: {}", idCliente);

        if (clienteRepository.existsById(idCliente)) {
            this.clienteRepository.deleteById(idCliente);
            logger.info("cliente eliminado exitosamente, ID: {}", idCliente);
            return Boolean.TRUE;
        }

        logger.warn("Intento de eliminar cliente inexistente - ID: {}", idCliente);
        throw new ResourceNotFoundException("El cliente indicado no existe");

    }
}
