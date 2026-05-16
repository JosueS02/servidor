package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Invernadero_Sensor")
public class InvernaderoSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inv_sensor")
    private Integer idInvSensor;

    @ManyToOne
    @JoinColumn(name = "id_invernadero", nullable = false)
    private Invernadero invernadero;

    @ManyToOne
    @JoinColumn(name = "id_sensor", nullable = false)
    private CatalogoSensor sensor;

    @Column(name = "estado_operativo", length = 50)
    private String estadoOperativo;

    public Integer getIdInvSensor() {
        return idInvSensor;
    }

    public void setIdInvSensor(Integer idInvSensor) {
        this.idInvSensor = idInvSensor;
    }

    public Invernadero getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
    }

    public CatalogoSensor getSensor() {
        return sensor;
    }

    public void setSensor(CatalogoSensor sensor) {
        this.sensor = sensor;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

}
