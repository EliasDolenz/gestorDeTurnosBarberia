package gestionDeTurnosBarberia.Domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del Cliente es requerido")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido del Cliente es requerido")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotBlank(message = "El telefono del Cliente es requerido")
    @Pattern(regexp = "\\d{10,}", message = "El télefono del cliente debe tener 10 digitos")
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "unCliente")
    @JsonBackReference
    private List<Turno> turnos = new ArrayList<>();
}
