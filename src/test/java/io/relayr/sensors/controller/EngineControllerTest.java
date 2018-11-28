package io.relayr.sensors.controller;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.service.EngineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EngineControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EngineService engineService;

    @Test
    public void shouldReturnEmptyListWhenNoResultsAvailable() throws Exception {
        doReturn(Collections.emptyList())
                .when(engineService)
                .findIncorrectlyWorkingEngines(80, 120);

        mvc.perform(get("/engines")
                .param("pressure_threshold", "120")
                .param("temp_threshold", "80"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void shouldReturnNonEmptyListWhenResultsAvailable() throws Exception {
        Engine engine1 = new Engine();
        engine1.setId(1459L);
        Engine engine2 = new Engine();
        engine2.setId(6703L);
        doReturn(Arrays.asList(engine1, engine2))
                .when(engineService)
                .findIncorrectlyWorkingEngines(80, 120);

        mvc.perform(get("/engines")
                .param("pressure_threshold", "80")
                .param("temp_threshold", "120"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"1459\",\"6703\"]"));
    }

}
