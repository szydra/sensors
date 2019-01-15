package io.relayr.sensors.controller;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.relayr.sensors.exception.NoSuchSensorException;
import io.relayr.sensors.service.UpdatingService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SensorController.class)
public class SensorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UpdatingService updatingService;

    @Test
    public void shouldHandleNoSuchSensorException() throws Exception {
        doThrow(NoSuchSensorException.class).when(updatingService).setValueById(eq(123L), anyInt());

        mvc.perform(post("/sensors/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"set\",\"value\":\"5\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldHandleConstraintViolationException() throws Exception {
        doThrow(ConstraintViolationException.class).when(updatingService).setValueById(123L, 5);

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
        verifyZeroInteractions(updatingService);
    }

    @Test
    public void shouldSetSensorValue() throws Exception {
        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"set\",\"value\":\"5\"}"))
                .andExpect(status().isOk());
        verify(updatingService).setValueById(45L, 5);
    }

    @Test
    public void shouldIncreaseSensorValue() throws Exception {
        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"increment\",\"value\":\"1\"}"))
                .andExpect(status().isOk());
        verify(updatingService).increaseValueById(45L, 1);
    }

    @Test
    public void shouldDecreaseSensorValue() throws Exception {
        mvc.perform(post("/sensors/45")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"operation\":\"decrement\",\"value\":\"8\"}"))
                .andExpect(status().isOk());
        verify(updatingService).decreaseValueById(45L, 8);
    }

}
