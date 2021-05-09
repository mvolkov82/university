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
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Faculty_;
import com.foxminded.university.model.dto.FacultyDTO;
import com.foxminded.university.repository.custom.CustomizedFacultyRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedFacultyRepositoryImpl implements CustomizedFacultyRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedStudentRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<FacultyDTO> criteriaQuery = criteriaBuilder.createQuery(FacultyDTO.class);
        Root<Faculty> root = criteriaQuery.from(Faculty.class);
        criteriaQuery.where(criteriaBuilder.isFalse(root.get(Faculty_.DELETED)));
        criteriaQuery.multiselect(buildMultiSelect(root));
        criteriaQuery.orderBy(getOrderList(criteriaBuilder, root));
        List<FacultyDTO> facultyDTOS = em.createQuery(criteriaQuery).getResultList();
        LOGGER.info("All not deleted faculties received. Count of records {}.", facultyDTOS.size());
        return facultyDTOS;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [facultyId={}, deleted={}])", id, deleted);
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Faculty> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Faculty.class);
        Root<Faculty> facultyRoot = criteriaUpdate.from(Faculty.class);
        criteriaUpdate.set(facultyRoot.get(Faculty_.DELETED), deleted)
                .where(criteriaBuilder.equal(facultyRoot.get(Faculty_.ID), id));
        try {
            em.createQuery(criteriaUpdate).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for faculty id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    private List<Order> getOrderList(CriteriaBuilder criteriaBuilder, Root<Faculty> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(criteriaBuilder.asc(root.get(Faculty_.NAME)));
        return orderList;
    }

    private List<Selection<?>> buildMultiSelect(Root<Faculty> root) {
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Faculty_.id));
        selectList.add(root.get(Faculty_.name));
        return selectList;
    }
}
