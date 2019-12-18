package xyz.bradjohnson.jukebox.repository;

import xyz.bradjohnson.jukebox.entity.Entity;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T extends Entity> {
    T getById(int id);

    List<T> list();

    List<T> list(Predicate<T> predicate);

    void add(T entity);

    void delete(T entity);

    void edit(T entity);
}
