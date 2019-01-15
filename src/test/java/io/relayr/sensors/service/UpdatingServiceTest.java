package io.relayr.sensors.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;

import io.relayr.sensors.model.Sensor;

@RunWith(MockitoJUnitRunner.class)
public class UpdatingServiceTest {

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private UpdatingService updatingService;

    @Test
    public void shouldSetValueById() {
        doReturn(new Sensor()).when(sensorService).setValueById(anyLong(), anyInt());

        updatingService.setValueById(45L, 5);

        InOrder order = inOrder(sensorService);
        order.verify(sensorService).setValueById(45L, 5);
        order.verify(sensorService).save(isA(Sensor.class));
    }

    @Test
    public void shouldIncreaseValueById() {
        doReturn(new Sensor()).when(sensorService).increaseValueById(anyLong(), anyInt());

        updatingService.increaseValueById(7L, 15);

        InOrder order = inOrder(sensorService);
        order.verify(sensorService).increaseValueById(7L, 15);
        order.verify(sensorService).save(isA(Sensor.class));
    }

    @Test
    public void shouldDecreaseValueById() {
        doReturn(new Sensor()).when(sensorService).decreaseValueById(anyLong(), anyInt());

        updatingService.decreaseValueById(5L, 1);

        InOrder order = inOrder(sensorService);
        order.verify(sensorService).decreaseValueById(5L, 1);
        order.verify(sensorService).save(isA(Sensor.class));
    }

}
