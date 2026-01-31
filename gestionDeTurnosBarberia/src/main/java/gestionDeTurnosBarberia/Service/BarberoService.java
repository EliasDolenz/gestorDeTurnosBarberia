package gestionDeTurnosBarberia.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Exception.BusinessLogicException;
import gestionDeTurnosBarberia.Exception.ResourceNotFoundException;
import gestionDeTurnosBarberia.Repository.BarberoRepository;

@Service
public class BarberoService {
    private final BarberoRepository barberoRepository;

    public BarberoService(BarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    public Barbero saveBarbero(Barbero unBarbero) {
        if (unBarbero.getFirstName() == null || unBarbero.getFirstName().isBlank()) {

            throw new BusinessLogicException("El nombre no puede estar vacío");

        } else if (unBarbero.getSurname() == null || unBarbero.getSurname().isEmpty()) {
            throw new BusinessLogicException("El apellido no puede estar vacío");

        }
        return this.barberoRepository.save(unBarbero);
    }

    public List<Barbero> findAllBarbero() {
        return this.barberoRepository.findAll();
    }

    public Barbero findBarberoById(Long idBarbero) {
        return this.barberoRepository.findById(idBarbero)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el barbero con el id " + idBarbero));
    }

    public Boolean deleteById(Long idBarbero) {
        if (this.barberoRepository.existsById(idBarbero)) {
            this.barberoRepository.deleteById(idBarbero);
            return Boolean.TRUE;
        } else {
            throw new ResourceNotFoundException("No se encontro el barbero con el id " + idBarbero);
        }
    }
}
