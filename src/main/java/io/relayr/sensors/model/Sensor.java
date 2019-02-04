package io.relayr.sensors.model;

import io.relayr.sensors.validation.ValueInRange;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@ValueInRange
public class Sensor {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SensorType type;

    @NotNull
    private Integer value;

    @NotNull
    private Integer minValue;

    @NotNull
    private Integer maxValue;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @NotNull
    private Engine engine;

    @Override
    public String toString() {
        return "Sensor{" + "id=" + id
                + ", type=" + type
                + ", value=" + value
                + ", engine=" + engine + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(id, sensor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
