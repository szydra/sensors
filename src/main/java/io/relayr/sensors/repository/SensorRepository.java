package io.relayr.sensors.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.relayr.sensors.model.Sensor;

public interface SensorRepository extends CrudRepository<Sensor, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Sensor s WHERE s.id = :id")
    Optional<Sensor> findByIdForUpdate(Long id);

}
