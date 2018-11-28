package io.relayr.sensors.controller;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/engines")
public class EngineController {

    private EngineService engineService;

    @Autowired
    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @GetMapping(params = {"pressure_threshold", "temp_threshold"})
    public List<String> getIncorrectlyWorkingEngineIds(@RequestParam("pressure_threshold") Integer pressureThreshold,
                                                       @RequestParam("temp_threshold") Integer temperatureThreshold) {
        return engineService.findIncorrectlyWorkingEngines(pressureThreshold, temperatureThreshold)
                .stream()
                .map(Engine::getId)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}
