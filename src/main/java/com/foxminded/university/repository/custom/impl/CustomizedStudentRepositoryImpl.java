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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Group_;
import com.foxminded.university.model.dto.StudentDTO;
import com.foxminded.university.model.person.Student;
import com.foxminded.university.model.person.Student_;
import com.foxminded.university.repository.custom.CustomizedStudentRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedStudentRepositoryImpl implements CustomizedStudentRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomizedStudentRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StudentDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StudentDTO> query = cb.createQuery(StudentDTO.class);
        Root<Student> root = query.from(Student.class);
        query.where(cb.isFalse(root.get(Student_.DELETED)));
        query.multiselect(buildMultiSelect(root));
        query.orderBy(getOrderList(cb, root));
        List<StudentDTO> studentDTOs = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted students received. Count of records {}.", studentDTOs.size());
        return studentDTOs;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [studentId={}, deleted={}])", id, deleted);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Student> update = cb.createCriteriaUpdate(Student.class);
        Root<Student> root = update.from(Student.class);
        update.set(root.get(Student_.DELETED), deleted).where(cb.equal(root.get(Student_.id), id));
        try {
            em.createQuery(update).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for student id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    private List<Selection<?>> buildMultiSelect(Root<Student> root) {
        Join<Student, Group> join = root.join(Student_.group, JoinType.LEFT);
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Student_.id));
        selectList.add(root.get(Student_.firstName));
        selectList.add(root.get(Student_.lastName));
        selectList.add(root.get(Student_.birthDay));
        selectList.add(root.get(Student_.address));
        selectList.add(root.get(Student_.phone));
        selectList.add(root.get(Student_.email));
        selectList.add(join.get(Group_.id));
        selectList.add(join.get(Group_.name));
        selectList.add(root.get(Student_.startDate));
        selectList.add(root.get(Student_.deleted));

        return selectList;
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Student> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Student_.LAST_NAME)));
        orderList.add(cb.asc(root.get(Student_.FIRST_NAME)));
        return orderList;
    }
}
