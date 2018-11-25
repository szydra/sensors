package io.relayr.sensors.init;

import org.junit.Test;

import static io.relayr.sensors.init.GithubUtils.isGithubUrl;
import static io.relayr.sensors.init.GithubUtils.prepareUrl;
import static org.assertj.core.api.Assertions.assertThat;

public class GithubUtilsTest {

    @Test
    public void urlStartingWithHttpShouldMatch() {
        String url = "http://github.com/relayr/pdm-test/blob/master/sensors.yml";

        assertThat(isGithubUrl(url)).isTrue();
    }

    @Test
    public void urlStartingWithHttpsShouldMatch() {
        String url = "https://github.com/relayr/pdm-test/blob/master/sensors.yml";

        assertThat(isGithubUrl(url)).isTrue();
    }

    @Test
    public void nonGithubUrlShouldNotMatch() {
        String url = "https://www2.relayr.io/pl/";

        assertThat(isGithubUrl(url)).isFalse();
    }

    @Test
    public void shouldChangeGithubUrl() {
        String url = "https://github.com/relayr/pdm-test/blob/master/sensors.yml";

        String rawUrl = prepareUrl(url);

        assertThat(rawUrl).isEqualTo("https://raw.githubusercontent.com/relayr/pdm-test/master/sensors.yml");
    }

    @Test
    public void shouldNotChangeNonGithubUrl() {
        String url = "https://www2.relayr.io/pl/";

        String rawUrl = prepareUrl(url);

        assertThat(rawUrl).isEqualTo(url);
    }

}
