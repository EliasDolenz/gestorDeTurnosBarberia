package gestionDeTurnosBarberia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gestionDeTurnosBarberia.Domain.Barbero;
import gestionDeTurnosBarberia.Repository.BarberoRepository;
import gestionDeTurnosBarberia.Service.BarberoService;

@ExtendWith(MockitoExtension.class)
public class BarberoServiceTest {
    @Mock
    private BarberoRepository barberoRepository;

    @InjectMocks
    private BarberoService barberoService;

    @Test
    void cuandoGuardoBarberoValido_entoncesRetornabarbero() {
        Barbero barbero = new Barbero();
        barbero.setFirstName("Elias");
        barbero.setSurname("Dolenz");

        when(barberoRepository.save(any(Barbero.class))).thenReturn(barbero);

        Barbero resultado = barberoService.saveBarbero(barbero);

        assertNotNull(resultado);
        assertEquals("Elias", resultado.getFirstName());
        verify(barberoRepository, times(1)).save(barbero);
    }
}
