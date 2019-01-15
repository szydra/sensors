package io.relayr.sensors.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.relayr.sensors.model.Sensor;

/**
 * A proxy for {@link SensorService} that triggers validation and makes updating
 * sensor value transactional.
 */
@Service
@Transactional
public class UpdatingService {

    private SensorService sensorService;

    @Autowired
    public UpdatingService(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public void setValueById(Long id, Integer value) {
        Sensor updatedSensor = sensorService.setValueById(id, value);
        sensorService.save(updatedSensor);
    }

    public void increaseValueById(Long id, Integer value) {
        Sensor updatedSensor = sensorService.increaseValueById(id, value);
        sensorService.save(updatedSensor);
    }

    public void decreaseValueById(Long id, Integer value) {
        Sensor updatedSensor = sensorService.decreaseValueById(id, value);
        sensorService.save(updatedSensor);
    }

}
