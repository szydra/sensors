package io.relayr.sensors.repository;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.model.SensorType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static io.relayr.sensors.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EngineRepositoryTest {

    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Engine engine;

    @Before
    public void prepareEngine() {
        engine = createEngine();
    }

    private Engine createEngine() {
        Engine engine = new Engine();
        engine.setId(1L);
        Sensor pressureSensor = createSensor(2L, PRESSURE, 20);
        pressureSensor.setEngine(engine);
        Sensor temperatureSensor1 = createSensor(3L, TEMPERATURE, 30);
        temperatureSensor1.setEngine(engine);
        Sensor temperatureSensor2 = createSensor(4L, TEMPERATURE, 40);
        temperatureSensor2.setEngine(engine);
        engine.setSensors(Arrays.asList(pressureSensor, temperatureSensor1, temperatureSensor2));
        return engine;
    }

    private Sensor createSensor(Long id, SensorType type, Integer value) {
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setType(type);
        sensor.setValue(value);
        sensor.setMinValue(Integer.MIN_VALUE);
        sensor.setMaxValue(Integer.MAX_VALUE);
        return sensor;
    }

    @Test
    public void shouldFindEngineWithMatchingParameters() {
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 35);

        assertThat(engines).containsExactly(engine);
    }

    @Test
    public void shouldNotFindEngineWithoutMatchingPressure() {
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(15, 35);

        assertThat(engines).isEmpty();
    }

    @Test
    public void shouldNotFindEngineWithoutMatchingTemperature() {
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 45);

        assertThat(engines).isEmpty();
    }

    @Test
    public void shouldNotContainDuplicates() {
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 0);

        assertThat(engines).doesNotHaveDuplicates();
    }

    @Test
    public void shouldNotSaveEngineWithoutMasterSensor() {
        List<Sensor> sensorsWithoutMaster = new ArrayList<>(engine.getSensors());
        sensorsWithoutMaster.removeIf(sensor -> sensor.getType() == PRESSURE);
        engine.setSensors(sensorsWithoutMaster);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> entityManager.persistAndFlush(engine))
                .withMessageContaining("invalid number of sensors");
    }

    @Test
    public void shouldNotSaveEngineTwoMasterSensors() {
        List<Sensor> sensorsWithTwoMasters = new ArrayList<>(engine.getSensors());
        sensorsWithTwoMasters.add(createSensor(5L, PRESSURE, 50));
        engine.setSensors(sensorsWithTwoMasters);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> entityManager.persistAndFlush(engine))
                .withMessageContaining("invalid number of sensors");
    }

    @Test
    public void shouldNotSaveEngineWithoutMinorSensor() {
        List<Sensor> sensorsWithoutMinor = new ArrayList<>(engine.getSensors());
        sensorsWithoutMinor.removeIf(sensor -> sensor.getType() == TEMPERATURE);
        engine.setSensors(sensorsWithoutMinor);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> entityManager.persistAndFlush(engine))
                .withMessageContaining("invalid number of sensors");
    }

}
