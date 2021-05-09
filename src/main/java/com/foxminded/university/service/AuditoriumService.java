package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.dto.AuditoriumDTO;

public interface AuditoriumService extends Service<Auditorium> {
    List<AuditoriumDTO> getAllAuditoriumsDTO();

    List<AuditoriumDTO> getAllNotDeletedAuditoriumsDTO();

    AuditoriumDTO getByIdAuditoriumDTO(Long id);

    void update(AuditoriumDTO auditoriumDTO);

    void create(AuditoriumDTO auditoriumDTO);

    void markDeleted(Long id, boolean deleted);
}
