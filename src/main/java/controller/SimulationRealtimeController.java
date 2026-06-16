/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import Invernadero_servidor.invernadero.dto.SimulationRealtimeDTO;
import java.util.Map;
import java.util.Optional;
import model.Invernadero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.DashboardRealtimeService;
import service.InvernaderoService;
import service.MqttCommandPublisher;

/**
 *
 * @author erick
 */
@RestController
@RequestMapping("/api/simulation")
public class SimulationRealtimeController {

    @Autowired
    private DashboardRealtimeService dashboardService;

    @Autowired
    private InvernaderoService invernaderoService;

    @Autowired
    private MqttCommandPublisher mqttPublisher;

    @GetMapping("/realtime/{idInvernadero}")
    public SimulationRealtimeDTO obtenerRealtime(
            @PathVariable Integer idInvernadero
    ) {

        return dashboardService.obtenerRealtime(
                idInvernadero
        );
    }

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarSimulacion(@RequestBody Map<String, Integer> request) throws Exception {
        Integer idInvernadero = request.get("idInvernadero");

        // 1. Handshake: Validar existencia
        Optional<Invernadero> invernadero = invernaderoService.obtenerPorId(idInvernadero);
        if (invernadero.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invernadero no encontrado");
        }

        mqttPublisher.publicarComando(
                idInvernadero,
                "SIMULACION",
                "INICIAR"
        );

        return ResponseEntity.ok(Map.of(
                "status", "CONNECTED",
                "message", "Handshake exitoso",
                "timestamp", System.currentTimeMillis()
        ));
    }

    @PostMapping("/terminar")
    public ResponseEntity<?> terminarSimulacion(@RequestBody Map<String, Integer> request) throws Exception {
        Integer idInvernadero = request.get("idInvernadero");

        mqttPublisher.publicarComando(
                idInvernadero,
                "SIMULACION",
                "DETENER"
        );
        return ResponseEntity.ok(Map.of(
                "status", "DISCONNECTED",
                "message", "Simulación terminada correctamente"
        ));
    }
}
