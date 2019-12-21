package xyz.bradjohnson.jukebox.repository;

import xyz.bradjohnson.jukebox.entity.Jukebox;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Persistence contract implementation for Jukebox
 */
public class JukeboxRepository implements Repository<Jukebox> {

    WebTarget jukeboxTarget;

    @Inject
    public JukeboxRepository(
            @Named("jukebox")
                    WebTarget jukeboxTarget) {
        this.jukeboxTarget = jukeboxTarget;
    }

    @Override
    public Jukebox getById(int id) {
        return this.jukeboxTarget.queryParam("id", id).request(MediaType.APPLICATION_JSON).get(Jukebox.class);
    }

    @Override
    public List<Jukebox> list() {
        return Arrays.asList(this.jukeboxTarget.request(MediaType.APPLICATION_JSON).get(Jukebox[].class));
    }

    @Override
    public List<Jukebox> list(Predicate<Jukebox> predicate) {
        return Arrays.stream(this.jukeboxTarget.request(MediaType.APPLICATION_JSON).get(Jukebox[].class))
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Jukebox entity) {
        // Nothing
    }

    @Override
    public void delete(Jukebox entity) {
        // Nothing
    }

    @Override
    public void edit(Jukebox entity) {
        // Nothing
    }
}
