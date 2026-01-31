package gestionDeTurnosBarberia.Dto;

import java.time.LocalDateTime;

import lombok.Data;
@Data

public class TurnoRequest {
    private Long idCliente;
    private Long idBarbero;
    private LocalDateTime fecha;
}
