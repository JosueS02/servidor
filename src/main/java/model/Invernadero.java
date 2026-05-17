package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Invernadero")
public class Invernadero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invernadero")
    private Integer idInvernadero;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String ubicacion;

    @Column(length = 50)
    private String estado;

    // Relación para recuperar los sensores vinculados a este invernadero
    @OneToMany(mappedBy = "invernadero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InvernaderoSensor> invernaderoSensores;

    // Relación para recuperar los actuadores vinculados a este invernadero
    @OneToMany(mappedBy = "invernadero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InvernaderoActuador> invernaderoActuadores;

    public Integer getIdInvernadero() {
        return idInvernadero;
    }

    public void setIdInvernadero(Integer idInvernadero) {
        this.idInvernadero = idInvernadero;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<InvernaderoSensor> getInvernaderoSensores() {
        return invernaderoSensores;
    }

    public void setInvernaderoSensores(List<InvernaderoSensor> invernaderoSensores) {
        this.invernaderoSensores = invernaderoSensores;
    }

    public List<InvernaderoActuador> getInvernaderoActuadores() {
        return invernaderoActuadores;
    }

    public void setInvernaderoActuadores(List<InvernaderoActuador> invernaderoActuadores) {
        this.invernaderoActuadores = invernaderoActuadores;
    }
}