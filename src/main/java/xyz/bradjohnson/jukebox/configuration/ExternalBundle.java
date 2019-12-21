package xyz.bradjohnson.jukebox.configuration;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistryListener;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Loads mock upstream into the application
 */
public class ExternalBundle implements ConfiguredBundle<JukeboxConfiguration> {
    private String name;
    @Getter
    private WebTarget jukeboxTarget;
    @Getter
    private WebTarget settingsTarget;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        this.name = bootstrap.getApplication().getName();
    }

    @Override
    public void run(JukeboxConfiguration configuration, Environment environment) {
        Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build(this.name);

        this.jukeboxTarget = client.target(configuration.getUpstreamJukes());
        this.settingsTarget = client.target(configuration.getUpstreamSettings());

        environment.healthChecks().register("jukebox target", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                if(ExternalBundle.this.jukeboxTarget.request().get().getStatus() == 200) {
                    return Result.healthy();
                }
                return Result.unhealthy("jukebox target is not responding");
            }
        });

        environment.healthChecks().register("settings target", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                if(ExternalBundle.this.settingsTarget.request().get().getStatus() == 200) {
                    return Result.healthy();
                }
                return Result.unhealthy("settings target is not responding");
            }
        });
    }
}
