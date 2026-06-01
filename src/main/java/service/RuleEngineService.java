package service;

import Invernadero_servidor.invernadero.dto.ComandoActuadorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MqttGateway;
import java.util.List;
import model.CatalogoSensor;
import model.Cultivo;
import model.Invernadero;
import model.InvernaderoActuador;
import model.InvernaderoSensor;
import model.LecturaSensor;
import model.Plantacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.InvernaderoActuadorRepository;
import repository.PlantacionRepository;

@Service
public class RuleEngineService {

    @Autowired
    private PlantacionRepository plantacionRepository;

    @Autowired
    private InvernaderoActuadorRepository actuadorRepository;

    @Autowired
    private ActuadorService actuadorService;

    @Autowired
    private MqttGateway mqttGateway;

    private final ObjectMapper mapper = new ObjectMapper();

    public void evaluarLectura(LecturaSensor lectura) {

        System.out.println("🔥 RULE ENGINE EJECUTANDO");

        InvernaderoSensor invSensor =
                lectura.getInvernaderoSensor();

        Invernadero invernadero =
                invSensor.getInvernadero();

        CatalogoSensor tipoSensor =
                invSensor.getSensor();

        Float valorActual =
                lectura.getValor();

        String tipoMedicion =
                tipoSensor.getNombre().toLowerCase();

        System.out.println("Sensor: " + tipoMedicion);
        System.out.println("Valor: " + valorActual);

        // Obtener cultivo activo
        Cultivo cultivo =
                obtenerCultivoActivo(
                        invernadero.getIdInvernadero()
                );

        if (cultivo == null) {
            return;
        }

        // Obtener actuadores
        List<InvernaderoActuador> actuadores =
                actuadorRepository.findAll();

        // ===== REGLAS =====

        if (tipoMedicion.contains("temperatura")) {

            evaluarTemperatura(
                    valorActual,
                    cultivo,
                    actuadores
            );

        } else if (tipoMedicion.contains("humedad")) {

            evaluarHumedad(
                    valorActual,
                    cultivo,
                    actuadores
            );

        } else if (tipoMedicion.contains("luminosidad")) {

            evaluarLuminosidad(
                    valorActual,
                    cultivo,
                    actuadores
            );

        } else if (tipoMedicion.contains("co2")) {

            evaluarCO2(
                    valorActual,
                    cultivo,
                    actuadores
            );
        }
    }

    // =========================================================
    // TEMPERATURA -> VENTILADOR
    // =========================================================
    private void evaluarTemperatura(
            Float tempActual,
            Cultivo cultivo,
            List<InvernaderoActuador> actuadores
    ) {

        InvernaderoActuador ventilador =
                actuadores.stream()
                .filter(a ->
                        a.getActuador()
                        .getNombre()
                        .toLowerCase()
                        .contains("ventilador")
                )
                .findFirst()
                .orElse(null);

        if (ventilador == null) {
            return;
        }

        // ENCENDER
        if (tempActual > cultivo.getTemperaturaMax()) {

            if (!"ON".equals(
                    ventilador.getEstadoOperativo()
            )) {

                actuadorService.cambiarEstado(
                        ventilador.getIdInvActuador(),
                        true
                );

                enviarComandoMQTT(
                        "VENTILADOR",
                        "ON"
                );

                ventilador.setEstadoOperativo("ON");

                actuadorRepository.save(ventilador);

                System.out.println(
                        "🌡️ VENTILADOR ACTIVADO"
                );
            }
        }

        // APAGAR
        else if (
                tempActual <= cultivo.getTemperaturaMax()
                &&
                "ON".equals(
                        ventilador.getEstadoOperativo()
                )
        ) {

            actuadorService.cambiarEstado(
                    ventilador.getIdInvActuador(),
                    false
            );

            enviarComandoMQTT(
                    "VENTILADOR",
                    "OFF"
            );

            ventilador.setEstadoOperativo("OFF");

            actuadorRepository.save(ventilador);

            System.out.println(
                    "🌡️ VENTILADOR DESACTIVADO"
            );
        }
    }

    // =========================================================
    // HUMEDAD -> BOMBA
    // =========================================================
    private void evaluarHumedad(
            Float humActual,
            Cultivo cultivo,
            List<InvernaderoActuador> actuadores
    ) {

        InvernaderoActuador bomba =
                actuadores.stream()
                .filter(a ->
                        a.getActuador()
                        .getNombre()
                        .toLowerCase()
                        .contains("bomba")
                )
                .findFirst()
                .orElse(null);

        if (bomba == null) {
            return;
        }

        // ENCENDER
        if (humActual < cultivo.getHumedadMin()) {

            if (!"ON".equals(
                    bomba.getEstadoOperativo()
            )) {

                actuadorService.cambiarEstado(
                        bomba.getIdInvActuador(),
                        true
                );

                enviarComandoMQTT(
                        "BOMBA",
                        "ON"
                );

                bomba.setEstadoOperativo("ON");

                actuadorRepository.save(bomba);

                System.out.println(
                        "💧 BOMBA ACTIVADA"
                );
            }
        }

        // APAGAR
        else if (
                humActual >= cultivo.getHumedadMax()
                &&
                "ON".equals(
                        bomba.getEstadoOperativo()
                )
        ) {

            actuadorService.cambiarEstado(
                    bomba.getIdInvActuador(),
                    false
            );

            enviarComandoMQTT(
                    "BOMBA",
                    "OFF"
            );

            bomba.setEstadoOperativo("OFF");

            actuadorRepository.save(bomba);

            System.out.println(
                    "💧 BOMBA DESACTIVADA"
            );
        }
    }

    // =========================================================
    // LUMINOSIDAD -> LUZ Y MALLA
    // =========================================================
    private void evaluarLuminosidad(
            Float luzActual,
            Cultivo cultivo,
            List<InvernaderoActuador> actuadores
    ) {

        InvernaderoActuador luz =
                actuadores.stream()
                .filter(a ->
                        a.getActuador()
                        .getNombre()
                        .toLowerCase()
                        .contains("luz")
                )
                .findFirst()
                .orElse(null);

        InvernaderoActuador malla =
                actuadores.stream()
                .filter(a ->
                        a.getActuador()
                        .getNombre()
                        .toLowerCase()
                        .contains("malla")
                )
                .findFirst()
                .orElse(null);

        // =========================
        // LUZ
        // =========================
        if (luz != null) {

            if (luzActual < cultivo.getLuzMin()) {

                if (!"ON".equals(
                        luz.getEstadoOperativo()
                )) {

                    actuadorService.cambiarEstado(
                            luz.getIdInvActuador(),
                            true
                    );

                    enviarComandoMQTT(
                            "LUZ",
                            "ON"
                    );

                    luz.setEstadoOperativo("ON");

                    actuadorRepository.save(luz);

                    System.out.println(
                            "💡 LUZ ACTIVADA"
                    );
                }

            } else if (
                    luzActual >= cultivo.getTemperaturaMin()
                    &&
                    "ON".equals(
                            luz.getEstadoOperativo()
                    )
            ) {

                actuadorService.cambiarEstado(
                        luz.getIdInvActuador(),
                        false
                );

                enviarComandoMQTT(
                        "LUZ",
                        "OFF"
                );

                luz.setEstadoOperativo("OFF");

                actuadorRepository.save(luz);

                System.out.println(
                        "💡 LUZ DESACTIVADA"
                );
            }
        }

        // =========================
        // MALLA
        // =========================
        if (malla != null) {

            if (luzActual > cultivo.getLuzMax()) {

                if (!"ON".equals(
                        malla.getEstadoOperativo()
                )) {

                    actuadorService.cambiarEstado(
                            malla.getIdInvActuador(),
                            true
                    );

                    enviarComandoMQTT(
                            "MALLA",
                            "ON"
                    );

                    malla.setEstadoOperativo("ON");

                    actuadorRepository.save(malla);

                    System.out.println(
                            "☀️ MALLA ACTIVADA"
                    );
                }

            } else if (
                    luzActual <= cultivo.getLuzMax()
                    &&
                    "ON".equals(
                            malla.getEstadoOperativo()
                    )
            ) {

                actuadorService.cambiarEstado(
                        malla.getIdInvActuador(),
                        false
                );

                enviarComandoMQTT(
                        "MALLA",
                        "OFF"
                );

                malla.setEstadoOperativo("OFF");

                actuadorRepository.save(malla);

                System.out.println(
                        "☀️ MALLA DESACTIVADA"
                );
            }
        }
    }

    // =========================================================
    // CO2 -> EXTRACTOR
    // =========================================================
    private void evaluarCO2(
            Float co2Actual,
            Cultivo cultivo,
            List<InvernaderoActuador> actuadores
    ) {

        InvernaderoActuador extractor =
                actuadores.stream()
                .filter(a ->
                        a.getActuador()
                        .getNombre()
                        .toLowerCase()
                        .contains("extractor")
                )
                .findFirst()
                .orElse(null);

        if (extractor == null) {
            return;
        }

        // ENCENDER
        if (co2Actual > cultivo.getCo2Max()) {

            if (!"ON".equals(
                    extractor.getEstadoOperativo()
            )) {

                actuadorService.cambiarEstado(
                        extractor.getIdInvActuador(),
                        true
                );

                enviarComandoMQTT(
                        "EXTRACTOR",
                        "ON"
                );

                extractor.setEstadoOperativo("ON");

                actuadorRepository.save(extractor);

                System.out.println(
                        "🌫️ EXTRACTOR ACTIVADO"
                );
            }
        }

        // APAGAR
        else if (
                co2Actual <= cultivo.getCo2Max()
                &&
                "ON".equals(
                        extractor.getEstadoOperativo()
                )
        ) {

            actuadorService.cambiarEstado(
                    extractor.getIdInvActuador(),
                    false
            );

            enviarComandoMQTT(
                    "EXTRACTOR",
                    "OFF"
            );

            extractor.setEstadoOperativo("OFF");

            actuadorRepository.save(extractor);

            System.out.println(
                    "🌫️ EXTRACTOR DESACTIVADO"
            );
        }
    }

    // =========================================================
    // MQTT
    // =========================================================
    private void enviarComandoMQTT(
            String actuador,
            String accion) {

        try {

            ComandoActuadorDTO dto =
                    new ComandoActuadorDTO(
                            actuador,
                            accion
                    );

            String payload =
                    mapper.writeValueAsString(dto);

            mqttGateway.enviarComando(
                    "invernadero/1/comandos",
                    payload
            );

            System.out.println(
                    "[RULE ENGINE MQTT] "
                    + payload
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CULTIVO ACTIVO
    // =========================================================
    private Cultivo obtenerCultivoActivo(
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

            System.out.println(
                    "⚠️ No hay plantación activa"
            );

            return null;
        }

        System.out.println(
                "🌱 Cultivo activo: "
                + plantacion
                .getCultivo()
                .getNombre()
        );

        return plantacion.getCultivo();
    }
}