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
@Table(name = "Invernadero_Actuador")

public class InvernaderoActuador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inv_actuador")
    private Integer idInvActuador;

    @ManyToOne
    @JoinColumn(name = "id_invernadero", nullable = false)
    private Invernadero invernadero;

    @ManyToOne
    @JoinColumn(name = "id_actuador", nullable = false)
    private CatalogoActuador actuador;

    @Column(name = "estado_operativo", length = 50)
    private String estadoOperativo;

    public Integer getIdInvActuador() {
        return idInvActuador;
    }

    public void setIdInvActuador(Integer idInvActuador) {
        this.idInvActuador = idInvActuador;
    }

    public Invernadero getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
    }

    public CatalogoActuador getActuador() {
        return actuador;
    }

    public void setActuador(CatalogoActuador actuador) {
        this.actuador = actuador;
    }

    public String getEstadoOperativo() {
        return estadoOperativo;
    }

    public void setEstadoOperativo(String estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }

}
