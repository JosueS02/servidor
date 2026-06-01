/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import Invernadero_servidor.invernadero.dto.SimulationRealtimeDTO;
import java.util.List;
import java.util.Optional;
import model.InvernaderoActuador;
import model.LecturaSensor;
import model.Plantacion;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import repository.InvernaderoActuadorRepository;
import repository.LecturaSensorRepository;
import repository.PlantacionRepository;

/**
 *
 * @author erick
 */
@Service
public class DashboardRealtimeService {    
    
    @Autowired
    private LecturaSensorRepository lecturaRepository;

    @Autowired
    private InvernaderoActuadorRepository actuadorRepository;

    @Autowired
    private PlantacionRepository plantacionRepository;

    public SimulationRealtimeDTO obtenerRealtime(
            Integer idInvernadero
    ) {

        SimulationRealtimeDTO dto =
                new SimulationRealtimeDTO();

        cargarSensores(dto);

        cargarActuadores(
                dto,
                idInvernadero
        );

        cargarCultivo(
                dto,
                idInvernadero
        );

        return dto;
    }

    private void cargarSensores(
            SimulationRealtimeDTO dto
    ) {

        cargarTemperatura(dto);

        cargarHumedad(dto);

        cargarLuminosidad(dto);

        cargarCO2(dto);
    }

    private void cargarTemperatura(
            SimulationRealtimeDTO dto
    ) {

        Optional<LecturaSensor> lectura =
                lecturaRepository.findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(1);

        lectura.ifPresent(l ->
                dto.setTemperatura(
                        l.getValor()
                )
        );
    }

    private void cargarHumedad(
            SimulationRealtimeDTO dto
    ) {

        Optional<LecturaSensor> lectura =
                lecturaRepository.findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(4);

        lectura.ifPresent(l ->
                dto.setHumedad(
                        l.getValor()
                )
        );
    }

    private void cargarLuminosidad(
            SimulationRealtimeDTO dto
    ) {

        Optional<LecturaSensor> lectura =
                lecturaRepository.findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(2);

        lectura.ifPresent(l ->
                dto.setLuminosidad(
                        l.getValor()
                )
        );
    }

    private void cargarCO2(
            SimulationRealtimeDTO dto
    ) {

        Optional<LecturaSensor> lectura =
                lecturaRepository.findTopByInvernaderoSensor_IdInvSensorOrderByIdLecturaDesc(3);

        lectura.ifPresent(l ->
                dto.setCo2(
                        l.getValor()
                )
        );
    }

    private void cargarActuadores(
            SimulationRealtimeDTO dto,
            Integer idInvernadero
    ) {

        List<InvernaderoActuador> actuadores =
                actuadorRepository
                .findByInvernadero_IdInvernadero(
                        idInvernadero
                );

        for (
                InvernaderoActuador actuador
                : actuadores
        ) {

            String nombre =
                    actuador
                    .getActuador()
                    .getNombre()
                    .toLowerCase();

            boolean activo =
                    "ON".equalsIgnoreCase(
                            actuador
                            .getEstadoOperativo()
                    );

            if (
                    nombre.contains(
                            "ventilador"
                    )
            ) {

                dto.setVentilador(
                        activo
                );

            } else if (
                    nombre.contains(
                            "bomba"
                    )
            ) {

                dto.setBomba(
                        activo
                );

            } else if (
                    nombre.contains(
                            "extractor"
                    )
            ) {

                dto.setExtractor(
                        activo
                );

            } else if (
                    nombre.contains(
                            "luz"
                    )
            ) {

                dto.setLuz(
                        activo
                );

            } else if (
                    nombre.contains(
                            "malla"
                    )
            ) {

                dto.setMalla(
                        activo
                );
            }
        }
    }

    private void cargarCultivo(
            SimulationRealtimeDTO dto,
            Integer idInvernadero
    ) {

        Plantacion plantacion =
                plantacionRepository
                .findByInvernadero_IdInvernaderoAndEstado(
                        idInvernadero,
                        "ACTIVO"
                )
                .orElse(null);

        if (plantacion == null) {
            return;
        }

        dto.setCultivo(
                plantacion
                .getCultivo()
                .getNombre()
        );

        dto.setInvernadero(
                plantacion
                .getInvernadero()
                .getNombre()
        );
    }
            
}
