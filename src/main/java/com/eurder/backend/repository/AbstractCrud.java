package com.eurder.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract sealed class AbstractCrud<T, S> implements CRUD<T, S> permits CustomerRepository, ItemRepository, OrderRepository {
    protected ConcurrentHashMap<S, T> repository;

    protected AbstractCrud() {
        this.repository = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<T> findById(S id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<T> findAll() {
        return repository.values().stream().toList();
    }
}
