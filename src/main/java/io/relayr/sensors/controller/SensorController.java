package io.relayr.sensors.controller;

import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping("/{sensorId}")
    public void updateSensorValue(@PathVariable Long sensorId,
                                  @RequestBody @Valid SensorUpdateDto updateDto) {
        Sensor updatedSensor = getUpdatedSensor(sensorId, updateDto);
        sensorService.save(updatedSensor);
    }

    private Sensor getUpdatedSensor(Long sensorId, SensorUpdateDto updateDto) {
        Operation operation = updateDto.getOperation();
        int value = updateDto.getValue();
        switch (operation) {
            case SET:
                return sensorService.setValueById(sensorId, value);
            case INCREMENT:
                return sensorService.increaseValueById(sensorId, value);
            case DECREMENT:
                return sensorService.decreaseValueById(sensorId, value);
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
    }

}
