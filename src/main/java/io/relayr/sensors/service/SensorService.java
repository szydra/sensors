package io.relayr.sensors.service;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class SensorService {

    private SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    Sensor findById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new NoSuchSensorException("Cannot find sensor with id " + id));
    }

    public Sensor save(@Valid Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Sensor setValueById(Long id, Integer value) {
        Sensor sensor = findById(id);
        sensor.setValue(value);
        return sensor;
    }

    public Sensor increaseValueById(Long id, Integer value) {
        Sensor sensor = findById(id);
        int currentValue = sensor.getValue();
        sensor.setValue(currentValue + value);
        return sensor;
    }

    public Sensor decreaseValueById(Long id, Integer value) {
        return increaseValueById(id, -value);
    }

}
