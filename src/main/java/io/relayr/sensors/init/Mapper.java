package io.relayr.sensors.init;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.relayr.sensors.model.SensorType.PRESSURE;

class Mapper {

    private final List<SensorDto> sensorDtos;
    private Map<Long, Engine> masterIdEngineMap;

    private Mapper(List<SensorDto> sensorDtos) {
        this.sensorDtos = sensorDtos;
    }

    static Mapper forDtos(List<SensorDto> sensorDtos) {
        return new Mapper(sensorDtos);
    }

    List<Sensor> mapToSensors() {
        masterIdEngineMap = sensorDtos.stream()
                .filter(sensor -> sensor.getType() == PRESSURE)
                .collect(Collectors.toMap(SensorDto::getId, this::mapToEngine));
        return mapToSensorsLinkedWithEngines();
    }

    private Engine mapToEngine(SensorDto sensorDto) {
        Engine engine = new Engine();
        engine.setId(sensorDto.getEngineId());
        engine.setName(sensorDto.getEngineName());
        engine.setSensors(new ArrayList<>());
        return engine;
    }

    private List<Sensor> mapToSensorsLinkedWithEngines() {
        return sensorDtos.stream()
                .map(this::mapToSensorAndLinkWithEngine)
                .collect(Collectors.toList());
    }

    private Sensor mapToSensorAndLinkWithEngine(SensorDto sensorDto) {
        Sensor sensor = new Sensor();
        sensor.setId(sensorDto.getId());
        sensor.setType(sensorDto.getType());
        sensor.setValue(sensorDto.getValue());
        sensor.setMinValue(sensorDto.getMinValue());
        sensor.setMaxValue(sensorDto.getMaxValue());
        Engine engine = getEngineForSensor(sensorDto);
        sensor.setEngine(engine);
        engine.getSensors().add(sensor);
        return sensor;
    }

    private Engine getEngineForSensor(SensorDto sensorDto) {
        Long masterId;
        if (sensorDto.getMasterSensorId() != null) {
            masterId = sensorDto.getMasterSensorId();
        } else {
            masterId = sensorDto.getId();
        }
        return masterIdEngineMap.get(masterId);
    }

}
