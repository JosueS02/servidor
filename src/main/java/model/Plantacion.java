package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Plantacion")
public class Plantacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantacion")
    private Integer idPlantacion;

    @ManyToOne
    @JoinColumn(name = "id_invernadero", nullable = false)
    private Invernadero invernadero;

    @ManyToOne
    @JoinColumn(name = "id_cultivo", nullable = false)
    private Cultivo cultivo;

    @Column(name = "fecha_plantado", nullable = false)
    private LocalDate fechaPlantado;

    @Column(name = "fecha_finalizacion")
    private LocalDate fechaFinalizacion;

    @Column(length = 20)
    private String estado = "Activo";

    public Integer getIdPlantacion() {
        return idPlantacion;
    }

    public void setIdPlantacion(Integer idPlantacion) {
        this.idPlantacion = idPlantacion;
    }

    public Invernadero getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public LocalDate getFechaPlantado() {
        return fechaPlantado;
    }

    public void setFechaPlantado(LocalDate fechaPlantado) {
        this.fechaPlantado = fechaPlantado;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
