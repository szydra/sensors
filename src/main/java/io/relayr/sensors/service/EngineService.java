package io.relayr.sensors.service;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.repository.EngineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineService {

    private EngineRepository engineRepository;

    @Autowired
    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    /**
     * Finds all engines whose pressure is lower than the passed pressure threshold
     * and temperature on at least one sensor is greater than the passed temperature threshold.
     *
     * @param pressureThreshold    minimal value of pressure
     * @param temperatureThreshold maximal value of temperature
     * @return list of engines, not null
     */
    public List<Engine> findIncorrectlyWorkingEngines(int pressureThreshold, int temperatureThreshold) {
        return engineRepository
                .findByPressureLowerThanAndTemperatureGreaterThan(pressureThreshold, temperatureThreshold);
    }

}
