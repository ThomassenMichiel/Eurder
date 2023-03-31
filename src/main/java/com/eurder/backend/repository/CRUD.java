package com.eurder.backend.repository;

import java.util.List;
import java.util.Optional;

public interface CRUD<T, S> {
    T save(T t);
    Optional<T> findById(S id);
    List<T> findAll();
}
