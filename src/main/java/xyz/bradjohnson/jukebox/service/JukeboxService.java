package xyz.bradjohnson.jukebox.service;

import xyz.bradjohnson.jukebox.entity.Jukebox;
import xyz.bradjohnson.jukebox.entity.Settings;
import xyz.bradjohnson.jukebox.repository.JukeboxRepository;
import xyz.bradjohnson.jukebox.repository.SettingsRepository;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DTO class for processing data and acting as an intermediary for the persistence layer
 */
public class JukeboxService {

    private final JukeboxRepository jukeboxRepo;
    private final SettingsRepository settingsRepo;

    @Inject
    public JukeboxService(JukeboxRepository jukeboxRepo, SettingsRepository settingsRepo) {
        this.jukeboxRepo = jukeboxRepo;
        this.settingsRepo = settingsRepo;
    }

    public List<Jukebox> getJukeboxes(final String settingId,
                                      final String model,
                                      final Long offset,
                                      final Long limit) {

        Predicate<Jukebox> jukeCond = model == null ? jukebox -> true : jukebox -> jukebox.getModel().equals(model);
        List<Jukebox> jukes = this.jukeboxRepo.list(jukeCond);
        if (jukes.isEmpty()) {
            throw new NotFoundException("Model was not found");
        }

        List<Settings> settingsList = this.settingsRepo.list(settings -> settings.getId().equals(settingId));
        if (settingsList.isEmpty()) {
            throw new NotFoundException("SettingId was not found");
        }

        Set<String> requiredComponents = settingsList.stream()
                .flatMap(settings -> Arrays.stream(settings.getRequirements()))
                .collect(Collectors.toSet());

        return jukes.stream()
                .filter(jukebox -> {
                    Set<String> actualComponents = Arrays.stream(jukebox.getComponents())
                            .map(Jukebox.Component::getName)
                            .collect(Collectors.toSet());
                    return actualComponents.containsAll(requiredComponents);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
