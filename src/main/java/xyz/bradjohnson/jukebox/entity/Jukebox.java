package xyz.bradjohnson.jukebox.entity;

import lombok.Data;

import javax.validation.Valid;

/**
 * Entity object for Jukebox
 */
@Data
public class Jukebox implements Entity {
    private String id;

    private String model;

    @Valid
    private Component[] components;

    @Data
    public static class Component {
        private String name;
    }
}
