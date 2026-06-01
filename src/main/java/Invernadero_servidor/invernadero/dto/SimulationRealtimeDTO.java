package Invernadero_servidor.invernadero.dto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author erick
 */
public class SimulationRealtimeDTO {
    
    private Float temperatura;

    private Float humedad;

    private Float luminosidad;

    private Float co2;

    private Boolean ventilador;

    private Boolean bomba;

    private Boolean extractor;

    private Boolean luz;

    private Boolean malla;

    private String cultivo;

    private String invernadero;

    public SimulationRealtimeDTO() {
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getHumedad() {
        return humedad;
    }

    public void setHumedad(Float humedad) {
        this.humedad = humedad;
    }

    public Float getLuminosidad() {
        return luminosidad;
    }

    public void setLuminosidad(Float luminosidad) {
        this.luminosidad = luminosidad;
    }

    public Float getCo2() {
        return co2;
    }

    public void setCo2(Float co2) {
        this.co2 = co2;
    }

    public Boolean getVentilador() {
        return ventilador;
    }

    public void setVentilador(Boolean ventilador) {
        this.ventilador = ventilador;
    }

    public Boolean getBomba() {
        return bomba;
    }

    public void setBomba(Boolean bomba) {
        this.bomba = bomba;
    }

    public Boolean getExtractor() {
        return extractor;
    }

    public void setExtractor(Boolean extractor) {
        this.extractor = extractor;
    }

    public Boolean getLuz() {
        return luz;
    }

    public void setLuz(Boolean luz) {
        this.luz = luz;
    }

    public Boolean getMalla() {
        return malla;
    }

    public void setMalla(Boolean malla) {
        this.malla = malla;
    }

    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public String getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(String invernadero) {
        this.invernadero = invernadero;
    }
}
