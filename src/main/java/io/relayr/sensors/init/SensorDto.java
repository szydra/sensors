package io.relayr.sensors.init;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.relayr.sensors.model.SensorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class SensorDto {

    private Long id;

    @JsonProperty("engine")
    private Long engineId;

    @JsonProperty("name")
    private String engineName;

    @JsonProperty("master-sensor-id")
    private Long masterSensorId;

    private SensorType type;

    private Integer value;

    @JsonProperty("min_value")
    private Integer minValue;

    @JsonProperty("max_value")
    private Integer maxValue;

}
