package xyz.bradjohnson.jukebox.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class JukeboxConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("jerseyClient")
    private JerseyClientConfiguration jerseyClientConfiguration;

    @NotNull
    @JsonProperty("upstream-jukes")
    private String upstreamJukes;

    @NotNull
    @JsonProperty("upstream-settings")
    private String upstreamSettings;
}
