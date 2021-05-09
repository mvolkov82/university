package com.foxminded.university.service;

import java.util.List;

public interface Service<T> {
    T create(T t);

    List<T> getAll();

    T getById(Long id);

    void update(T t);
}
