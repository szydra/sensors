package io.relayr.sensors.repository;

import java.util.Arrays;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static io.relayr.sensors.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SensorRepositoryTest {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Sensor validSensor;
    private Engine engine;

    @Before
    public void prepareSensor() {
        validSensor = createValidSensor();
        engine = new Engine();
        engine.setId(2L);
        validSensor.setEngine(engine);
    }

    private Sensor createValidSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(10L);
        sensor.setType(TEMPERATURE);
        sensor.setValue(10);
        sensor.setMinValue(5);
        sensor.setMaxValue(15);
        return sensor;
    }

    private Sensor createSensorForTestWithValue(int value) {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setType(PRESSURE);
        sensor.setValue(value);
        sensor.setMinValue(20);
        sensor.setMaxValue(30);
        return sensor;
    }

    @Test
    public void shouldNotSaveSensorWithTooLowValue() {
        Sensor sensor = createSensorForTestWithValue(10);
        sensor.setEngine(engine);
        engine.setSensors(Arrays.asList(sensor, validSensor));

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    sensorRepository.save(sensor);
                    entityManager.flush();
                }).withMessageContaining("value out of range");
    }

    @Test
    public void shouldNotSaveSensorWithTooLargeValue() {
        Sensor sensor = createSensorForTestWithValue(40);
        sensor.setEngine(engine);
        engine.setSensors(Arrays.asList(sensor, validSensor));

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> {
                    sensorRepository.save(sensor);
                    entityManager.flush();
                }).withMessageContaining("value out of range");
    }

    @Test
    public void shouldSaveSensorWithValidValue() {
        Sensor sensor = createSensorForTestWithValue(25);
        sensor.setEngine(engine);
        engine.setSensors(Arrays.asList(sensor, validSensor));

        assertThatCode(() -> {
            sensorRepository.save(sensor);
            entityManager.flush();
        }).doesNotThrowAnyException();
    }

    @Test
    public void shouldFindSensorForUpdate() {
        Sensor sensor = createSensorForTestWithValue(25);
        sensor.setEngine(engine);
        engine.setSensors(Arrays.asList(sensor, validSensor));

        sensorRepository.save(sensor);
        Optional<Sensor> foundSensor = sensorRepository.findByIdForUpdate(1L);

        assertThat(foundSensor).contains(sensor);
    }

    @Test
    public void shouldNotFindSensorForUpdate() {
        Sensor sensor = createSensorForTestWithValue(25);
        sensor.setEngine(engine);
        engine.setSensors(Arrays.asList(sensor, validSensor));

        sensorRepository.save(sensor);
        Optional<Sensor> foundSensor = sensorRepository.findByIdForUpdate(139L);

        assertThat(foundSensor).isEmpty();
    }

}
