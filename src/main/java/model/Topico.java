package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Topico")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topico")
    private Integer idTopico;

    @Column(nullable = false, unique = true, length = 255)
    private String ruta;

    @OneToOne
    @JoinColumn(name = "id_inv_sensor")
    private InvernaderoSensor invernaderoSensor;

    @OneToOne
    @JoinColumn(name = "id_inv_actuador")
    private InvernaderoActuador invernaderoActuador;

    public Integer getIdTopico() {
        return idTopico;
    }

    public void setIdTopico(Integer idTopico) {
        this.idTopico = idTopico;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public InvernaderoSensor getInvernaderoSensor() {
        return invernaderoSensor;
    }

    public void setInvernaderoSensor(InvernaderoSensor invernaderoSensor) {
        this.invernaderoSensor = invernaderoSensor;
    }

    public InvernaderoActuador getInvernaderoActuador() {
        return invernaderoActuador;
    }

    public void setInvernaderoActuador(InvernaderoActuador invernaderoActuador) {
        this.invernaderoActuador = invernaderoActuador;
    }

}
