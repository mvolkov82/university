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
import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.Auditorium_;
import com.foxminded.university.model.Department;
import com.foxminded.university.model.Department_;
import com.foxminded.university.model.dto.AuditoriumDTO;
import com.foxminded.university.repository.custom.CustomizedAuditoriumRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedAuditoriumRepositoryImpl implements CustomizedAuditoriumRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedAuditoriumRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AuditoriumDTO> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AuditoriumDTO> query = cb.createQuery(AuditoriumDTO.class);
        Root<Auditorium> root = query.from(Auditorium.class);
        query.where(cb.isFalse(root.get(Auditorium_.DELETED)));
        query.multiselect(buildMultiSelect(root));
        query.orderBy(getOrderList(cb, root));
        List<AuditoriumDTO> auditoriumDTOs = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted auditoriums received. Count of records {}.", auditoriumDTOs.size());
        return auditoriumDTOs;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [auditoriumId={}, deleted={}])", id, deleted);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Auditorium> update = cb.createCriteriaUpdate(Auditorium.class);
        Root<Auditorium> root = update.from(Auditorium.class);
        update.set(root.get(Auditorium_.DELETED), deleted).where(cb.equal(root.get(Auditorium_.ID), id));
        try {
            em.createQuery(update).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for auditorium id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<Auditorium> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(Auditorium_.NAME)));
        return orderList;
    }

    private List<Selection<?>> buildMultiSelect(Root<Auditorium> root) {
        Join<Auditorium, Department> join = root.join(Auditorium_.department, JoinType.LEFT);
        List<Selection<?>> selectList = new ArrayList<>();
        selectList.add(root.get(Auditorium_.id));
        selectList.add(root.get(Auditorium_.name));
        selectList.add(root.get(Auditorium_.deleted));
        selectList.add(join.get(Department_.id));
        selectList.add(join.get(Department_.name));
        return selectList;
    }
}
