package com.foxminded.university.service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.Degree;
import com.foxminded.university.repository.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DegreeService {

    @Autowired
    public DegreeService() {
    }

    public List<String> getDegreeAsList() {
        List<String> degree = new ArrayList<>();
        for (Degree degreeValue : Degree.values()) {
            degree.add(String.valueOf(degreeValue.getDisplayValue()));
        }
        return degree;
    }

    public Degree getDegree(String value) {
        try {
            Degree degree = Degree.valueOf(value.toUpperCase());
            return degree;
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(format("Degree %s not found", value));
        }
    }
}
