package io.relayr.sensors.validation;

import io.relayr.sensors.model.Sensor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueInRangeValidator implements ConstraintValidator<ValueInRange, Sensor> {

    @Override
    public boolean isValid(Sensor sensor, ConstraintValidatorContext context) {
        try {
            return sensor.getValue() >= sensor.getMinValue()
                    && sensor.getValue() <= sensor.getMaxValue();
        } catch (Exception e) {
            return false;
        }
    }

}
