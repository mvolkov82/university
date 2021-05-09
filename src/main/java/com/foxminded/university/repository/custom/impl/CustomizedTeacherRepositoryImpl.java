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
import com.foxminded.university.model.dto.TeacherDTO;
import com.foxminded.university.model.person.Teacher;
import com.foxminded.university.model.person.Teacher_;
import com.foxminded.university.repository.custom.CustomizedTeacherRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedTeacherRepositoryImpl implements CustomizedTeacherRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedTeacherRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TeacherDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TeacherDTO> query = cb.createQuery(TeacherDTO.class);
        Root<Teacher> root = query.from(Teacher.class);
        query.where(cb.isFalse(root.get(Teacher_.DELETED)));
        query.multiselect(buildMultiSelect(root));
        query.orderBy(getOrderList(cb, root));
        List<TeacherDTO> teacherDTOs = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted teachers received. Count of records {}.", teacherDTOs.size());
        return teacherDTOs;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [teacherId={}, deleted={}])", id, deleted);
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaUpdate<Teacher> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Teacher.class);
        Root<Teacher> root = criteriaUpdate.from(Teacher.class);
        criteriaUpdate.set(root.get(Teacher_.DELETED), deleted)
                .where(criteriaBuilder.equal(root.get(Teacher_.ID), id));
        try {
            em.createQuery(criteriaUpdate).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for teacher id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    private List<Selection<?>> buildMultiSelect(Root<Teacher> root) {
        Join<Teacher, Department> join = root.join(Teacher_.department, JoinType.LEFT);
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Teacher_.id));
        selectList.add(root.get(Teacher_.firstName));
        selectList.add(root.get(Teacher_.lastName));
        selectList.add(root.get(Teacher_.birthDay));
        selectList.add(root.get(Teacher_.address));
        selectList.add(root.get(Teacher_.phone));
        selectList.add(root.get(Teacher_.email));
        selectList.add(join.get(Department_.id));
        selectList.add(join.get(Department_.name));
        selectList.add(root.get(Teacher_.deleted));

        return selectList;
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Teacher> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Teacher_.LAST_NAME)));
        orderList.add(cb.asc(root.get(Teacher_.FIRST_NAME)));
        return orderList;
    }
}
