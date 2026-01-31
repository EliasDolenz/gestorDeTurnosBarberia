package gestionDeTurnosBarberia.Domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Disponibilidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private DayOfWeek diaDeLaSemana;
    private LocalTime horarioDeInicio;
    private LocalTime horarioDeFinalizacion;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_barbero")
    private Barbero unBarbero;
}
