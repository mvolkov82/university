package com.foxminded.university.repository;

import com.foxminded.university.model.Auditorium;
import com.foxminded.university.repository.custom.CustomizedAuditoriumRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditoriumRepository extends CustomizedAuditoriumRepository, JpaRepository<Auditorium, Long> {
}
