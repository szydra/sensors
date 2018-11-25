package io.relayr.sensors.repository;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.model.SensorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static io.relayr.sensors.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EngineRepositoryTest {

    @Autowired
    private EngineRepository engineRepository;

    @Test
    public void shouldFindEngineWithMatchingParameters() {
        Engine engine = prepareEngine();
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 35);

        assertThat(engines).containsExactly(engine);
    }

    @Test
    public void shouldNotFindEngineWithoutMatchingPressure() {
        Engine engine = prepareEngine();
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(15, 35);

        assertThat(engines).isEmpty();
    }

    @Test
    public void shouldNotFindEngineWithoutMatchingTemperature() {
        Engine engine = prepareEngine();
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 45);

        assertThat(engines).isEmpty();
    }

    @Test
    public void shouldNotContainDuplicates() {
        Engine engine = prepareEngine();
        engineRepository.save(engine);

        List<Engine> engines = engineRepository.findByPressureLowerThanAndTemperatureGreaterThan(25, 0);

        assertThat(engines).doesNotHaveDuplicates();
    }

    private Engine prepareEngine() {
        Engine engine = new Engine();
        engine.setId(1L);
        Sensor pressureSensor = prepareSensor(2L, PRESSURE, 20);
        pressureSensor.setEngine(engine);
        Sensor temperatureSensor1 = prepareSensor(3L, TEMPERATURE, 30);
        temperatureSensor1.setEngine(engine);
        Sensor temperatureSensor2 = prepareSensor(4L, TEMPERATURE, 40);
        temperatureSensor2.setEngine(engine);
        engine.setSensors(Arrays.asList(pressureSensor, temperatureSensor1, temperatureSensor2));
        return engine;
    }

    private Sensor prepareSensor(Long id, SensorType type, Integer value) {
        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setType(type);
        sensor.setValue(value);
        sensor.setMinValue(Integer.MIN_VALUE);
        sensor.setMaxValue(Integer.MAX_VALUE);
        return sensor;
    }

}
