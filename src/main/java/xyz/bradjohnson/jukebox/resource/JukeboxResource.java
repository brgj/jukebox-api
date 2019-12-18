package xyz.bradjohnson.jukebox.resource;

import org.jvnet.hk2.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.bradjohnson.jukebox.entity.Jukebox;
import xyz.bradjohnson.jukebox.entity.Settings;
import xyz.bradjohnson.jukebox.repository.JukeboxRepository;
import xyz.bradjohnson.jukebox.repository.SettingsRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public Response getJukebox(@QueryParam("settingId")
                                     String settingId,
                             @Optional
                             @QueryParam("model")
                                     String model,
                             @Optional
                             @QueryParam("offset")
                             @DefaultValue("0")
                                     Integer offset,
                             @Optional
                             @QueryParam("limit")
                             @DefaultValue("10")
                                     Integer limit) {
        Predicate<Jukebox> jukeCond = model == null ? jukebox -> true : jukebox -> jukebox.getModel().equals(model);
        List<Jukebox> jukes = this.jukeboxRepo.list(jukeCond);

        List<Settings> settingsList = this.settingsRepo.list(settings -> settings.getId().equals(settingId));

        Set<String> requiredComponents = settingsList.stream()
                .flatMap(settings -> Arrays.stream(settings.getRequirements()))
                .collect(Collectors.toSet());

        jukes = jukes.stream()
                .filter(jukebox -> {
                    Set<String> actualComponents = Arrays.stream(jukebox.getComponents())
                            .map(Jukebox.Component::getName)
                            .collect(Collectors.toSet());
                    return actualComponents.containsAll(requiredComponents);
                })
                .skip(offset)
                .limit(limit)
        .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(jukes.toArray()).build();
    }
}
