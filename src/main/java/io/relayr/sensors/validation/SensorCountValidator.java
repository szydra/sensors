package io.relayr.sensors.validation;

import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.model.SensorType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class SensorCountValidator implements ConstraintValidator<SensorCount, Collection<Sensor>> {

    private SensorType type;
    private int min;
    private int max;

    @Override
    public void initialize(SensorCount constraintAnnotation) {
        this.type = constraintAnnotation.type();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Collection<Sensor> sensors, ConstraintValidatorContext context) {
        try {
            int sensorCount = (int) sensors.stream()
                    .filter(sensor -> sensor.getType() == type)
                    .count();
            return sensorCount >= min && sensorCount <= max;
        } catch (Exception e) {
            return false;
        }
    }

}
