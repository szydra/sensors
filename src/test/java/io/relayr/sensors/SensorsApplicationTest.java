package io.relayr.sensors;

import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.relayr.sensors.model.SensorType.TEMPERATURE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SensorsApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger("TestLogger");

    @Autowired
    private SensorRepository sensorRepository;

    @Test
    public void shouldLoadContextWithoutAnyError() {
        LOGGER.info("Context loaded!");
    }

    @Test
    public void shouldInvokeCommandLineRunnerAndSaveSensors() {
        Iterable<Sensor> allSensors = sensorRepository.findAll();

        assertThat(allSensors).hasSize(5)
                .filteredOn(sensor -> sensor.getType() == TEMPERATURE)
                .hasSize(3);
    }

}
