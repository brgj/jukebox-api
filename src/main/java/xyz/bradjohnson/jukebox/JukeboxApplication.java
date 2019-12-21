package xyz.bradjohnson.jukebox;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import xyz.bradjohnson.jukebox.configuration.ExternalBundle;
import xyz.bradjohnson.jukebox.configuration.JukeboxConfiguration;
import xyz.bradjohnson.jukebox.repository.JukeboxRepository;
import xyz.bradjohnson.jukebox.repository.SettingsRepository;
import xyz.bradjohnson.jukebox.resource.JukeboxResource;
import xyz.bradjohnson.jukebox.service.JukeboxService;

import javax.ws.rs.client.WebTarget;

/**
 * Main class for starting the API
 */
public class JukeboxApplication extends Application<JukeboxConfiguration> {

    ExternalBundle externalBundle;

    public JukeboxApplication() {
        this.externalBundle = new ExternalBundle();
    }

    public static void main(String[] args) throws Exception {
        new JukeboxApplication().run(args);
    }

    @Override
    public String getName() {
        return "jukebox-api";
    }

    @Override
    public void initialize(Bootstrap<JukeboxConfiguration> bootstrap) {
        bootstrap.addBundle(externalBundle);
        bootstrap.addBundle(new SwaggerBundle<JukeboxConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(JukeboxConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }


    @Override
    public void run(JukeboxConfiguration configuration, Environment environment) {
        // Make dependencies available via HK2 Dependency Injection
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(JukeboxApplication.this.externalBundle.getJukeboxTarget()).named("jukebox").to(WebTarget.class);
                this.bind(JukeboxApplication.this.externalBundle.getSettingsTarget()).named("settings").to(WebTarget.class);
                this.bindAsContract(JukeboxService.class);
                this.bindAsContract(JukeboxRepository.class);
                this.bindAsContract(SettingsRepository.class);
            }
        });


        environment.jersey().register(JukeboxResource.class);
    }
}
