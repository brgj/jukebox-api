package xyz.bradjohnson.jukebox.resource;

import org.jvnet.hk2.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.bradjohnson.jukebox.entity.Jukebox;
import xyz.bradjohnson.jukebox.entity.Settings;
import xyz.bradjohnson.jukebox.repository.JukeboxRepository;
import xyz.bradjohnson.jukebox.repository.SettingsRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/jukebox")
@Produces(MediaType.APPLICATION_JSON)
public class JukeboxResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JukeboxResource.class);

    private final JukeboxRepository jukeboxRepo;
    private final SettingsRepository settingsRepo;

    @Inject
    public JukeboxResource(JukeboxRepository jukeboxRepo, SettingsRepository settingsRepo) {
        this.jukeboxRepo = jukeboxRepo;
        this.settingsRepo = settingsRepo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJukebox(@QueryParam("settingId")
                                     String settingId,
                             @Optional
                             @QueryParam("model")
                                     String model,
                             @Optional
                             @QueryParam("offset")
                                     Integer offset,
                             @Optional
                             @QueryParam("limit")
                                     Integer limit) {
        List<Jukebox> jukes = this.jukeboxRepo.list();
        List<Settings> settingsList = this.settingsRepo.list(settings -> settings.getId().equals(settingId));
        return null;
    }
}
