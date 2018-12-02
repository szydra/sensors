package io.relayr.sensors.service;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@Service
@Validated
public class SensorService {

    private SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Reads sensor from the database.
     *
     * @param id sensor id
     * @return found sensor
     * @throws NoSuchSensorException if sensor with the passed id cannot be found
     */
    Sensor findById(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new NoSuchSensorException("Cannot find sensor with id " + id));
    }

    /**
     * Saves the passed sensor to the database.
     *
     * @param sensor sensor to be saved
     * @return copy of the saved sensor
     * @throws ConstraintViolationException if the value is not between min and max
     */
    public Sensor save(@Valid Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    /**
     * Finds a sensor with the passed id and returns it with the updated value.
     *
     * @param id    sensor id
     * @param value value to set
     * @return updated sensor
     * @throws NoSuchSensorException if sensor with the passed id cannot be found
     */
    public Sensor setValueById(Long id, Integer value) {
        Sensor sensor = findById(id);
        sensor.setValue(value);
        return sensor;
    }

    /**
     * Finds a sensor with the passed id and returns it with the updated value.
     *
     * @param id    sensor id
     * @param value value to add
     * @return updated sensor
     * @throws NoSuchSensorException if sensor with the passed id cannot be found
     */
    public Sensor increaseValueById(Long id, Integer value) {
        Sensor sensor = findById(id);
        int currentValue = sensor.getValue();
        sensor.setValue(currentValue + value);
        return sensor;
    }

    /**
     * Finds a sensor with the passed id and returns it with the updated value.
     *
     * @param id    sensor id
     * @param value value to subtract
     * @return updated sensor
     * @throws NoSuchSensorException if sensor with the passed id cannot be found
     */
    public Sensor decreaseValueById(Long id, Integer value) {
        return increaseValueById(id, -value);
    }

}
