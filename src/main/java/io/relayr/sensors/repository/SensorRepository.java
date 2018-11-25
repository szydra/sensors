package io.relayr.sensors.repository;

import io.relayr.sensors.model.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, Long> {

}
