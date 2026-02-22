package gestionDeTurnosBarberia.Dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class TurnoRequest {
    @NotNull(message = "El ID del cliente es requerido")
    private Long idCliente;

    @NotNull(message = "El ID del barbero es requerido")
    private Long idBarbero;

    @NotNull(message = "La fecha del turno es requerida")
    @FutureOrPresent(message = "La fecha del turno debe ser en el futuro o presente")
    private LocalDateTime fecha;
}
