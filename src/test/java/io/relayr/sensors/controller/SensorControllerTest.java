package io.relayr.sensors.controller;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.service.SensorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SensorController.class)
public class SensorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SensorService sensorService;

    @Test
    public void shouldHandleNoSuchSensorException() throws Exception {
        doThrow(NoSuchSensorException.class).when(sensorService).setValueById(eq(123L), anyInt());

        mvc.perform(post("/sensors/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"set\",\"value\":\"5\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldHandleConstraintViolationException() throws Exception {
        doThrow(ConstraintViolationException.class).when(sensorService).save(isA(Sensor.class));
        doReturn(new Sensor()).when(sensorService).setValueById(123L, 5);

        mvc.perform(post("/sensors/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"set\",\"value\":\"5\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("Validation errors")));
    }

    @Test
    public void shouldRejectBadRequest() throws Exception {
        mvc.perform(post("/sensors/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"delete\",\"value\":\"5\"}"))
                .andExpect(status().isBadRequest());
        verifyZeroInteractions(sensorService);
    }

    @Test
    public void shouldSetSensorValue() throws Exception {
        doReturn(new Sensor()).when(sensorService).setValueById(45L, 5);

        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"set\",\"value\":\"5\"}"))
                .andExpect(status().isOk());
        InOrder order = inOrder(sensorService);
        order.verify(sensorService).setValueById(45L, 5);
        order.verify(sensorService).save(isA(Sensor.class));
    }

    @Test
    public void shouldIncreaseSensorValue() throws Exception {
        doReturn(new Sensor()).when(sensorService).increaseValueById(45L, 1);

        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"increment\",\"value\":\"1\"}"))
                .andExpect(status().isOk());
        InOrder order = inOrder(sensorService);
        order.verify(sensorService).increaseValueById(45L, 1);
        order.verify(sensorService).save(isA(Sensor.class));
    }

    @Test
    public void shouldDecreaseSensorValue() throws Exception {
        doReturn(new Sensor()).when(sensorService).decreaseValueById(45L, 8);

        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"decrement\",\"value\":\"8\"}"))
                .andExpect(status().isOk());
        InOrder order = inOrder(sensorService);
        order.verify(sensorService).decreaseValueById(45L, 8);
        order.verify(sensorService).save(isA(Sensor.class));
    }

}
