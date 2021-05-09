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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Department_;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Group_;
import com.foxminded.university.model.dto.GroupDTO;
import com.foxminded.university.repository.custom.CustomizedGroupRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedGroupRepositoryImpl implements CustomizedGroupRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedGroupRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<GroupDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GroupDTO> query = cb.createQuery(GroupDTO.class);
        Root<Group> root = query.from(Group.class);
        query.where(cb.isFalse(root.get(Group_.DELETED)));
        query.multiselect(buildMultiSelect(root));
        query.orderBy(getOrderList(cb, root));
        List<GroupDTO> groupDTOs = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted groups received. Count of records {}.", groupDTOs.size());
        return groupDTOs;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [groupId={}, deleted={}])", id, deleted);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Group> update = cb.createCriteriaUpdate(Group.class);
        Root<Group> root = update.from(Group.class);
        update.set(root.get(Group_.DELETED), deleted).where(cb.equal(root.get(Group_.ID), id));
        try {
            em.createQuery(update).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for group id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public List<Group> findGroupsNotInListAndNotDeleted(Set<Group> timetableItemGroups) {
        LOGGER.debug("findGroupsNotInListAndNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Group> query = cb.createQuery(Group.class);
        Root<Group> root = query.from(Group.class);

        List<Predicate> predicates = new ArrayList();
        if (!timetableItemGroups.isEmpty()) {
            predicates.add(root.in(timetableItemGroups).not());
        }
        predicates.add(cb.isFalse(root.get(Group_.DELETED)));
        Predicate[] finalFilter = predicates.toArray(new Predicate[predicates.size()]);
        query.where(finalFilter);

        query.orderBy(getOrderList(cb, root));
        List<Group> groups = em.createQuery(query).getResultList();
        return groups;
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Group> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Group_.NAME)));
        return orderList;
    }

    private List<Selection<?>> buildMultiSelect(Root<Group> root) {
        Join<Group, Department> join = root.join(Group_.department, JoinType.LEFT);
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Group_.id));
        selectList.add(root.get(Group_.name));
        selectList.add(root.get(Group_.deleted));
        selectList.add(join.get(Department_.id));
        selectList.add(join.get(Department_.name));
        return selectList;
    }
}
