package io.relayr.sensors.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
class SensorUpdateDto {

    @NotNull
    private Operation operation;

    @NotNull
    private Integer value;

}
