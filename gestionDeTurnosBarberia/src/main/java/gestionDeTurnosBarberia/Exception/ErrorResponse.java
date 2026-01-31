package gestionDeTurnosBarberia.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String mensaje;
    private Integer status;
    private Long timestamp;
}
