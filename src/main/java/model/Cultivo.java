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
@Table(name = "Cultivo")
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cultivo")
    private Integer idCultivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "temperatura_min")
    private Float temperaturaMin;
    @Column(name = "temperatura_max")
    private Float temperaturaMax;
    @Column(name = "humedad_min")
    private Float humedadMin;
    @Column(name = "humedad_max")
    private Float humedadMax;
    @Column(name = "luz_min")
    private Float luzMin;
    @Column(name = "luz_max")
    private Float luzMax;

    public Integer getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(Integer idCultivo) {
        this.idCultivo = idCultivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(Float temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public Float getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(Float temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public Float getHumedadMin() {
        return humedadMin;
    }

    public void setHumedadMin(Float humedadMin) {
        this.humedadMin = humedadMin;
    }

    public Float getHumedadMax() {
        return humedadMax;
    }

    public void setHumedadMax(Float humedadMax) {
        this.humedadMax = humedadMax;
    }

    public Float getLuzMin() {
        return luzMin;
    }

    public void setLuzMin(Float luzMin) {
        this.luzMin = luzMin;
    }

    public Float getLuzMax() {
        return luzMax;
    }

    public void setLuzMax(Float luzMax) {
        this.luzMax = luzMax;
    }

}
