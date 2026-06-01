/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import Invernadero_servidor.invernadero.dto.SimulationRealtimeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.DashboardRealtimeService;

/**
 *
 * @author erick
 */
@RestController
@RequestMapping("/api/simulation")
public class SimulationRealtimeController {
    
    @Autowired
    private DashboardRealtimeService dashboardService;

    @GetMapping("/realtime/{idInvernadero}")
    public SimulationRealtimeDTO obtenerRealtime(
            @PathVariable Integer idInvernadero
    ) {

        return dashboardService.obtenerRealtime(
                idInvernadero
        );
    }
}
