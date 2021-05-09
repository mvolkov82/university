package com.foxminded.university.repository.custom;

import java.util.List;

import com.foxminded.university.model.Subject;
import com.foxminded.university.model.dto.SubjectDTO;

public interface CustomizedSubjectRepository extends CustomizedRepository<SubjectDTO> {

    List<Subject> findSubjectNotInListAndNotDeleted(List<Subject> subjects);
}
