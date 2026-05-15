package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author josue
 */
@Entity
@Table(name = "Catalogo_Actuador")
public class CatalogoActuador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actuador")
    private Integer idActuador;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_accion", length = 100)
    private String tipoAccion;

    @Column(name = "tiempo_activacion", length = 100)
    private String tiempoActivacion;

    public Integer getIdActuador() {
        return idActuador;
    }

    public void setIdActuador(Integer idActuador) {
        this.idActuador = idActuador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getTiempoActivacion() {
        return tiempoActivacion;
    }

    public void setTiempoActivacion(String tiempoActivacion) {
        this.tiempoActivacion = tiempoActivacion;
    }

}
