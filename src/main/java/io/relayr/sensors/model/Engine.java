package io.relayr.sensors.model;

import io.relayr.sensors.validation.SensorCount;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static io.relayr.sensors.model.SensorType.TEMPERATURE;

@Entity
@Getter
@Setter
public class Engine {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "engine", cascade = CascadeType.ALL)
    @SensorCount(type = PRESSURE, min = 1, max = 1)
    @SensorCount(type = TEMPERATURE, min = 1)
    private List<Sensor> sensors;

    @Override
    public String toString() {
        return "Engine " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        return Objects.equals(id, engine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
