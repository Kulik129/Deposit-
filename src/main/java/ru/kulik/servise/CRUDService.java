package ru.kulik.servise;

import java.util.List;
import java.util.UUID;


public interface CRUDService<T> {
    void save(T data);
    T findByUUID(UUID uuid);
    List<T> findAll();
    void delete(UUID uuid);
}
