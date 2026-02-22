package gestionDeTurnosBarberia.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "barberos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del barbero es requerido")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido del barbero es requerido")
    @Column(nullable = false)
    private String surname;

    @OneToMany(mappedBy = "unBarbero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // El barbero "administra" la relación
    private List<Turno> listaTurnos;

}
