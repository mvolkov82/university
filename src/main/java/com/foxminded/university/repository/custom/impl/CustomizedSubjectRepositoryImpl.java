package com.foxminded.university.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Subject_;
import com.foxminded.university.model.dto.SubjectDTO;
import com.foxminded.university.repository.custom.CustomizedSubjectRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedSubjectRepositoryImpl implements CustomizedSubjectRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedSubjectRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [subjectId={}, deleted={}])", id, deleted);
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Subject> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Subject.class);
        Root<Subject> subjectRoot = criteriaUpdate.from(Subject.class);
        criteriaUpdate.set(subjectRoot.get(Subject_.DELETED), deleted)
                .where(criteriaBuilder.equal(subjectRoot.get(Subject_.ID), id));
        try {
            em.createQuery(criteriaUpdate).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for subject id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public List<SubjectDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SubjectDTO> query = cb.createQuery(SubjectDTO.class);
        Root<Subject> root = query.from(Subject.class);
        query.where(cb.isFalse(root.get(Subject_.DELETED)));
        query.multiselect(buildMultiSelect(root));
        query.orderBy(getOrderList(cb, root));
        List<SubjectDTO> subjectDTOS = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted subjects received. Count of records {}.", subjectDTOS.size());
        return subjectDTOS;
    }

    @Override
    public List<Subject> findSubjectNotInListAndNotDeleted(List<Subject> teacherSubjects) {
        LOGGER.debug("findSubjectNotInList()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Subject> query = cb.createQuery(Subject.class);
        Root<Subject> root = query.from(Subject.class);
        if (!teacherSubjects.isEmpty()) {
            query.select(root)
                    .where(cb.and(
                            root.in(teacherSubjects).not(),
                            cb.isFalse(root.get(Subject_.DELETED)))
                    );
        } else {
            query.select(root).where(cb.isFalse(root.get(Subject_.DELETED)));
        }
        query.orderBy(getOrderList(cb, root));

        List<Subject> subjects = em.createQuery(query).getResultList();
        return subjects;
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Subject> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Subject_.NAME)));
        return orderList;
    }

    private List<Selection<?>> buildMultiSelect(Root<Subject> root) {
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Subject_.id));
        selectList.add(root.get(Subject_.name));
        return selectList;
    }

}
