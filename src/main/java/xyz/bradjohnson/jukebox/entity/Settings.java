package xyz.bradjohnson.jukebox.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Entity object for Settings
 */
@Data
public class Settings implements Entity {
    private String id;
    @JsonProperty("requires")
    private String[] requirements;

    @Data
    public static class SuperSettings {
        private Settings[] settings;
    }
}
