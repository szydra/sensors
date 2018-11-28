package io.relayr.sensors.service;

import io.relayr.sensors.model.Engine;
import io.relayr.sensors.repository.EngineRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class EngineServiceTest {

    @Mock
    private EngineRepository engineRepository;

    @InjectMocks
    private EngineService engineService;

    @Test
    public void shouldReturnIncorrectlyWorkingEngines() {
        Engine engine = new Engine();
        engine.setId(1L);
        doReturn(Collections.singletonList(engine))
                .when(engineRepository)
                .findByPressureLowerThanAndTemperatureGreaterThan(20, 30);

        List<Engine> actualEngines = engineService.findIncorrectlyWorkingEngines(20, 30);

        assertThat(actualEngines).containsExactly(engine);
    }

    @Test
    public void shouldReturnEmptyListWhenEnginesWorkCorrectly() {
        doReturn(Collections.emptyList())
                .when(engineRepository)
                .findByPressureLowerThanAndTemperatureGreaterThan(anyInt(), anyInt());

        List<Engine> actualEngines = engineService.findIncorrectlyWorkingEngines(20, 30);

        assertThat(actualEngines).isEmpty();
    }

}
