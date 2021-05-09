package com.foxminded.university.repository.custom;

import java.util.List;

public interface CustomizedRepository<T> {

    List<T> getAllNotDeleted();

    void markDeleted(Long id, boolean deleted);
}
