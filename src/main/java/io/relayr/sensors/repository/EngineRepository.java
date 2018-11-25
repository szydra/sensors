package io.relayr.sensors.repository;

import io.relayr.sensors.model.Engine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EngineRepository extends CrudRepository<Engine, Long> {

    @Query("SELECT DISTINCT e FROM Engine e"
            + " JOIN e.sensors s1 JOIN e.sensors s2"
            + " WHERE s1.type = io.relayr.sensors.model.SensorType.PRESSURE AND s1.value < :pressure"
            + " AND s2.type = io.relayr.sensors.model.SensorType.TEMPERATURE AND s2.value > :temperature")
    List<Engine> findByPressureLowerThanAndTemperatureGreaterThan(Integer pressure, Integer temperature);

}
