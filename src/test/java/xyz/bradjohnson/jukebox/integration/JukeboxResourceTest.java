package xyz.bradjohnson.jukebox.integration;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.bradjohnson.jukebox.JukeboxApplication;
import xyz.bradjohnson.jukebox.configuration.JukeboxConfiguration;

import javax.ws.rs.core.Response;

public class JukeboxResourceTest {
    public static final DropwizardAppExtension<JukeboxConfiguration> RULE = new DropwizardAppExtension<>(
            JukeboxApplication.class, ResourceHelpers.resourceFilePath("config-test.yml"));

    @BeforeAll
    public static void before() {
        RULE.getTestSupport().before();
    }

    @AfterAll
    public static void after() {
        RULE.getTestSupport().after();
    }

    @Test
    public void get200WithValidSettingIdTest() {
        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/jukebox")
                .queryParam("settingId", "b43f247a-8478-4f24-8e28-792fcfe539f5")
                .request()
                .get();

        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void get400WithInvalidSettingIdFormatTest() {
        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/jukebox")
                .queryParam("settingId", "invalid_format")
                .request()
                .get();

        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void get404WithUnfoundSettingIdTest() {
        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/jukebox")
                .queryParam("settingId", "b43f247a-8478-4f24-8e28-1234abcd4321")
                .request()
                .get();

        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void get404WithUnfoundModelTest() {
        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/jukebox")
                .queryParam("settingId", "b43f247a-8478-4f24-8e28-792fcfe539f5")
                .queryParam("model", "invalid_model")
                .request()
                .get();

        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
