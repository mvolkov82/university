package com.foxminded.university.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Department_;
import com.foxminded.university.model.Faculty;
import com.foxminded.university.model.Faculty_;
import com.foxminded.university.model.dto.DepartmentDTO;
import com.foxminded.university.repository.custom.CustomizedDepartmentRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedDepartmentRepositoryImpl implements CustomizedDepartmentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedDepartmentRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DepartmentDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DepartmentDTO> criteriaQuery = criteriaBuilder.createQuery(DepartmentDTO.class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        criteriaQuery.where(criteriaBuilder.isFalse(departmentRoot.get(Department_.DELETED)));
        criteriaQuery.multiselect(buildMultiSelect(departmentRoot));
        criteriaQuery.orderBy(getOrderList(criteriaBuilder, departmentRoot));
        List<DepartmentDTO> departmentDTOS = em.createQuery(criteriaQuery).getResultList();
        LOGGER.info("All not deleted departments received. Count of records {}.", departmentDTOS.size());
        return departmentDTOS;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [departmentId={}, deleted={}])", id, deleted);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Department> update = cb.createCriteriaUpdate(Department.class);
        Root<Department> root = update.from(Department.class);
        update.set(root.get(Department_.DELETED), deleted).where(cb.equal(root.get(Department_.ID), id));
        try {
            em.createQuery(update).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for department id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }

    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Department> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Department_.NAME)));
        return orderList;
    }

    private List<Selection<?>> buildMultiSelect(Root<Department> root) {
        Join<Department, Faculty> join = root.join(Department_.faculty, JoinType.LEFT);
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Department_.id));
        selectList.add(root.get(Department_.name));
        selectList.add(root.get(Department_.deleted));
        selectList.add(join.get(Faculty_.id));
        selectList.add(join.get(Faculty_.name));
        return selectList;
    }
}
