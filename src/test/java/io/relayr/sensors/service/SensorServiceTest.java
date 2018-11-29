package io.relayr.sensors.service;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.model.Engine;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static io.relayr.sensors.model.SensorType.PRESSURE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SensorService.class, MethodValidationPostProcessor.class})
public class SensorServiceTest {

    @Autowired
    private SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    @Captor
    private ArgumentCaptor<Sensor> sensorArgumentCaptor;

    private Sensor sensor;

    @Before
    public void setup() {
        sensor = createSensorForTest();
        doReturn(Optional.of(sensor)).when(sensorRepository).findById(1L);
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
        doReturn(Optional.empty()).when(sensorRepository).findById(17L);

        assertThatExceptionOfType(NoSuchSensorException.class)
                .isThrownBy(() -> sensorService.findById(17L))
                .withMessageContaining("Cannot find sensor with id 17");
    }

    @Test
    public void shouldReturnSensorForGivenId() {
        Sensor foundSensor = sensorService.findById(1L);

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
    public void shouldSaveSensorWithSetValue() {
        sensorService.setValueById(1L, 15);

        verify(sensorRepository).save(sensorArgumentCaptor.capture());
        Sensor savedSensor = sensorArgumentCaptor.getValue();
        assertThat(savedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 15);
    }

    @Test
    public void shouldSaveSensorWithIncreasedValue() {
        sensorService.increaseValueById(1L, 5);

        verify(sensorRepository).save(sensorArgumentCaptor.capture());
        Sensor savedSensor = sensorArgumentCaptor.getValue();
        assertThat(savedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 25);
    }

    @Test
    public void shouldSaveSensorWithDecreaseValue() {
        sensorService.decreaseValueById(1L, 8);

        verify(sensorRepository).save(sensorArgumentCaptor.capture());
        Sensor savedSensor = sensorArgumentCaptor.getValue();
        assertThat(savedSensor)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("value", 12);
    }

}
