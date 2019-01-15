package io.relayr.sensors.service;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SensorService.class, MethodValidationPostProcessor.class })
public class SensorServiceTest {

    @Autowired
    private SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    private Sensor sensor;

    @Before
    public void setup() {
        sensor = createSensorForTest();
        doReturn(Optional.of(sensor)).when(sensorRepository).findByIdForUpdate(1L);
    }

    private Sensor createSensorForTest() {
        Sensor sensorForTest = new Sensor();
        sensorForTest.setId(1L);
        sensorForTest.setType(PRESSURE);
        sensorForTest.setValue(20);
        sensorForTest.setMinValue(10);
        sensorForTest.setMaxValue(30);
        sensorForTest.setEngine(new Engine());
        return sensorForTest;
    }

    @Test
    public void shouldThrowExceptionWhenSensorCannotBeFound() {
        doReturn(Optional.empty()).when(sensorRepository).findByIdForUpdate(17L);

        assertThatExceptionOfType(NoSuchSensorException.class)
                .isThrownBy(() -> sensorService.findByIdForUpdate(17L))
                .withMessageContaining("Cannot find sensor with id 17");
    }

    @Test
    public void shouldReturnSensorForGivenId() {
        Sensor foundSensor = sensorService.findByIdForUpdate(1L);

        assertThat(foundSensor).isEqualTo(sensor)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void shouldSaveValidSensorWithoutException() {
        assertThatCode(() -> sensorService.save(sensor))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldNotSaveSensorWithTooLargeValue() {
        sensor.setValue(40);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> sensorService.save(sensor))
                .withMessageContaining("value out of range");
    }

    @Test
    public void shouldNotSaveSensorWithTooLowValue() {
        sensor.setValue(5);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> sensorService.save(sensor))
                .withMessageContaining("value out of range");
    }

    @Test
    public void shouldSetSensorValue() {
        Sensor updatedSensor = sensorService.setValueById(1L, 15);

        assertThat(updatedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 15);
    }

    @Test
    public void shouldIncreaseSensorValue() {
        Sensor updatedSensor = sensorService.increaseValueById(1L, 5);

        assertThat(updatedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 25);
    }

    @Test
    public void shouldDecreaseSensorValue() {
        Sensor updatedSensor = sensorService.decreaseValueById(1L, 8);

        assertThat(updatedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 12);
    }

}
