package io.relayr.sensors.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.relayr.sensors.service.UpdatingService;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private UpdatingService updatingService;

    @Autowired
    public SensorController(UpdatingService updatingService) {
        this.updatingService = updatingService;
    }

    @PostMapping("/{sensorId}")
    public void updateSensorValue(@PathVariable Long sensorId,
            @RequestBody @Valid SensorUpdateDto updateDto) {
        updateSensor(sensorId, updateDto);
    }

    private void updateSensor(Long sensorId, SensorUpdateDto updateDto) {
        Operation operation = updateDto.getOperation();
        int value = updateDto.getValue();
        switch (operation) {
            case SET:
                updatingService.setValueById(sensorId, value);
                break;
            case INCREMENT:
                updatingService.increaseValueById(sensorId, value);
                break;
            case DECREMENT:
                updatingService.decreaseValueById(sensorId, value);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
    }

}
