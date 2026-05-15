package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Lectura_Sensor")
public class LecturaSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lectura")
    private Long idLectura;

    @ManyToOne
    @JoinColumn(name = "id_inv_sensor", nullable = false)
    private InvernaderoSensor invernaderoSensor;

    @Column(nullable = false)
    private Float valor;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    public Long getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(Long idLectura) {
        this.idLectura = idLectura;
    }

    public InvernaderoSensor getInvernaderoSensor() {
        return invernaderoSensor;
    }

    public void setInvernaderoSensor(InvernaderoSensor invernaderoSensor) {
        this.invernaderoSensor = invernaderoSensor;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

}
