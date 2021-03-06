package io.relayr.sensors.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.relayr.sensors.model.Sensor;
import io.relayr.sensors.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;
import static io.relayr.sensors.init.GithubUtils.prepareUrl;
import static org.springframework.core.env.Profiles.of;

/**
 * <p>Runs at server startup and loads sensors from the yaml file whose address
 * was passed in the property <code>sensors.yaml.url</code>.</p>
 * <p>In case of passing an invalid address, the server fails to start.
 * If no address is specified, invocation of this class is skipped
 * and the server continues initialization.</p>
 */
@Component
@ConditionalOnProperty("sensors.yaml.url")
public class SensorsLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorsLoader.class);

    @Value("${sensors.yaml.url}")
    private String sensorsYamlUrl;

    private Environment environment;
    private SensorRepository sensorRepository;

    @Autowired
    public SensorsLoader(Environment environment, SensorRepository sensorRepository) {
        this.environment = environment;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Mapper mapper = Mapper.forDtos(readSensors());
        List<Sensor> sensors = mapper.mapToSensors();
        // saveAll() is transactional, so there is no need to annotate with @Transactional
        sensorRepository.saveAll(sensors);
        LOGGER.info("{} sensors saved", sensors.size());
    }

    private List<SensorDto> readSensors() throws IOException {
        ObjectMapper mapper = prepareYamlMapper();
        List<SensorDto> sensors;
        if (environment.acceptsProfiles(of("test"))) {
            File file = new ClassPathResource("sensors.yml").getFile();
            sensors = mapper.readValue(file, new SensorList());
        } else {
            URL url = new URL(prepareUrl(sensorsYamlUrl));
            sensors = mapper.readValue(url, new SensorList());
        }
        return sensors;
    }

    private ObjectMapper prepareYamlMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    private static class SensorList extends TypeReference<List<SensorDto>> {
    }

}
