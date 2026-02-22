package gestionDeTurnosBarberia.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Exception.ResourceNotFoundException;
import gestionDeTurnosBarberia.Repository.BarberoRepository;

@Service
public class BarberoService {
    private static final Logger logger = LoggerFactory.getLogger(BarberoService.class);
    private final BarberoRepository barberoRepository;

    public BarberoService(BarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    public Barbero saveBarbero(Barbero unBarbero) {
        logger.info("Creando Barbero - Nombre: {}, Apellido: {}", unBarbero.getFirstName(), unBarbero.getSurname());

        if (unBarbero.getFirstName() == null || unBarbero.getFirstName().isBlank()) {
            logger.warn("Validación fallida: nombre vacío.");
            throw new BusinessLogicException("El nombre no puede estar vacío");

        } else if (unBarbero.getSurname() == null || unBarbero.getSurname().isEmpty()) {
            logger.warn("Validación fallida: Apellido vacío.");
            throw new BusinessLogicException("El apellido no puede estar vacío");

        }
        Barbero saved = this.barberoRepository.save(unBarbero);
        logger.info("Barbero guardado exitosamente - ID: {}, Nombre: {}, Apellido: {}", saved.getId(),
                saved.getFirstName(), saved.getSurname());
        return saved;

    }

    public List<Barbero> findAllBarbero() {
        logger.debug("Buscando todos los Barberos");
        return this.barberoRepository.findAll();
    }

    public Barbero findBarberoById(Long idBarbero) {
        logger.debug("Buscando Barbero ID: {}", idBarbero);
        return this.barberoRepository.findById(idBarbero)
                .orElseThrow(() -> {
                    logger.warn("Barbero no encontrado ID: {}", idBarbero);
                    return new ResourceNotFoundException("No se encontro el barbero con el id " + idBarbero);
                });
    }

    public Boolean deleteById(Long idBarbero) {
        logger.info("Eliminando al Barbero ID: {}", idBarbero);
        if (this.barberoRepository.existsById(idBarbero)) {
            this.barberoRepository.deleteById(idBarbero);
            logger.info("Barbero eliminado exitosamente");
            return Boolean.TRUE;
        } else {
            logger.warn("Intento de eliminar barbero inexistente - ID: {}", idBarbero);
            throw new ResourceNotFoundException("No se encontro el barbero con el id " + idBarbero);
        }
    }
}
