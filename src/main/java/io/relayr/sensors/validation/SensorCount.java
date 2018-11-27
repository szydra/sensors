package io.relayr.sensors.validation;

import io.relayr.sensors.model.SensorType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = SensorCountValidator.class)
@Documented
@Repeatable(SensorsCount.class)
public @interface SensorCount {

    String message() default "invalid number of sensors";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    SensorType type();

    int min() default 0;

    int max() default Integer.MAX_VALUE;

}
