package xyz.bradjohnson.jukebox.repository;

import xyz.bradjohnson.jukebox.entity.Settings;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Persistence contract implementation for Settings
 */
public class SettingsRepository implements Repository<Settings> {

    WebTarget settingsTarget;

    @Inject
    public SettingsRepository(
            @Named("settings")
                    WebTarget settingsTarget) {
        this.settingsTarget = settingsTarget;
    }

    @Override
    public Settings getById(int id) {
        // No filtered getter exists
        return null;
    }

    @Override
    public List<Settings> list() {
        return Arrays.asList(this.settingsTarget.request(MediaType.APPLICATION_JSON).get(Settings.SuperSettings.class).getSettings());
    }

    @Override
    public List<Settings> list(Predicate<Settings> predicate) {
        return Arrays.stream(this.settingsTarget.request(MediaType.APPLICATION_JSON).get(Settings.SuperSettings.class).getSettings())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public void add(Settings entity) {
        // Nothing
    }

    @Override
    public void delete(Settings entity) {
        // Nothing
    }

    @Override
    public void edit(Settings entity) {
        // Nothing
    }
}
