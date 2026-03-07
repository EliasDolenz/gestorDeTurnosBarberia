package gestionDeTurnosBarberia.Dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record TurnoCreateDTO(
        @NotNull(message = "El ID del cliente es obligatorio") 
        Long idCliente,

        @NotNull(message = "El ID del barbero es obligatorio") 
        Long idBarbero,

        @NotNull(message = "La fecha no puede estar vacia")
        @Future(message = "El turno debe ser para una fecha futura") 
        LocalDateTime fecha) {
}
