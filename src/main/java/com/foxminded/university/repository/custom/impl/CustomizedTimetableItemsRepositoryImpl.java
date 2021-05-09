package com.foxminded.university.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import com.foxminded.university.model.Auditorium;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.TimetableItem;
import com.foxminded.university.model.TimetableItem_;
import com.foxminded.university.model.person.Teacher;
import com.foxminded.university.repository.custom.CustomizedTimetableItemsRepository;
import com.foxminded.university.repository.exception.QueryNotExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizedTimetableItemsRepositoryImpl implements CustomizedTimetableItemsRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedStudentRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TimetableItem> getAllNotDeleted() {
        LOGGER.debug("getAllNotDeleted()");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);
        query.where(cb.isFalse(root.get(TimetableItem_.DELETED)));
        query.orderBy(getOrderList(cb, root));
        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted TimeTableItems received. Count of records {}.", timetableItems.size());
        return timetableItems;
    }

    @Override
    public void markDeleted(Long id, boolean deleted) {
        LOGGER.debug("markDeleted() [TimetableItemId={}, deleted={}])", id, deleted);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<TimetableItem> update = cb.createCriteriaUpdate(TimetableItem.class);
        Root<TimetableItem> root = update.from(TimetableItem.class);
        update.set(root.get(TimetableItem_.DELETED), deleted).where(cb.equal(root.get(TimetableItem_.ID), id));
        try {
            em.createQuery(update).executeUpdate();
        } catch (PersistenceException e) {
            String message = format("Can't set flag deleted=%s for timetable id= %s", deleted, id);
            LOGGER.error(message);
            throw new QueryNotExecuteException(message, e);
        }
    }

    @Override
    public boolean isTeacherBusy(LocalDate date, Lecture lecture, Teacher teacher) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        Predicate predicateForData = cb.equal(root.get(TimetableItem_.DATE), date);
        Predicate predicateForLecture = cb.equal(root.get(TimetableItem_.LECTURE), lecture);
        Predicate predicateForTeacher = cb.equal(root.get(TimetableItem_.TEACHER), teacher);
        Predicate notDeleted = cb.equal(root.get(TimetableItem_.DELETED), false);
        Predicate finalPredicate = cb.and(predicateForData, predicateForLecture, predicateForTeacher, notDeleted);
        query.where(finalPredicate);

        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();

        return timetableItems.size() > 0;
    }


    @Override
    public boolean isAuditoriumOccupied(LocalDate date, Lecture lecture, Auditorium auditorium) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        Predicate predicateForData = cb.equal(root.get(TimetableItem_.DATE), date);
        Predicate predicateForLecture = cb.equal(root.get(TimetableItem_.LECTURE), lecture);
        Predicate predicateForAuditorium = cb.equal(root.get(TimetableItem_.AUDITORIUM), auditorium);
        Predicate notDeleted = cb.equal(root.get(TimetableItem_.DELETED), false);
        Predicate finalPredicate = cb.and(predicateForData, predicateForLecture, predicateForAuditorium, notDeleted);
        query.where(finalPredicate);

        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();

        return timetableItems.size() > 0;
    }

    @Override
    public List<TimetableItem> getTimetableItemsByDateAndLecture(LocalDate date, Lecture lecture) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        Predicate predicateForData = cb.equal(root.get(TimetableItem_.DATE), date);
        Predicate predicateForLecture = cb.equal(root.get(TimetableItem_.LECTURE), lecture);
        Predicate finalPredicate = cb.and(predicateForData, predicateForLecture);
        query.where(finalPredicate);

        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();
        return timetableItems;
    }

    @Override
    public List<TimetableItem> getAllNotDeletedForTeacher(LocalDate dateFrom, LocalDate dateTo, Teacher teacher) {
        LOGGER.debug("getAllNotDeletedForTeacher() [dateFrom={}, dateTo={}, teacher={}])", dateFrom, dateTo, teacher);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        Predicate predicateForData = cb.between(root.get(TimetableItem_.DATE), dateFrom, dateTo);
        Predicate predicateForTeacher = cb.equal(root.get(TimetableItem_.TEACHER), teacher);
        Predicate predicateForNotDeleted = cb.equal(root.get(TimetableItem_.DELETED), false);
        Predicate finalPredicate = cb.and(predicateForData, predicateForTeacher, predicateForNotDeleted);
        query.where(finalPredicate);

        query.orderBy(getOrderList(cb, root));
        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted TimeTableItems for teacher {} received. Count of records {}.", teacher.getId(), timetableItems.size());
        return timetableItems;
    }

    @Override
    public List<TimetableItem> getAllNotDeletedForGroup(LocalDate dateFrom, LocalDate dateTo, Group group) {
        LOGGER.debug("getAllNotDeletedForGroup() [dateFrom={}, dateTo={}, group={}])", dateFrom, dateTo, group);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        Predicate predicateForData = cb.between(root.get(TimetableItem_.DATE), dateFrom, dateTo);
        Predicate predicateForGroup = cb.isMember(group, root.get(TimetableItem_.GROUPS));
        Predicate predicateForNotDeleted = cb.equal(root.get(TimetableItem_.DELETED), false);
        Predicate finalPredicate = cb.and(predicateForData, predicateForGroup, predicateForNotDeleted);
        query.where(finalPredicate);

        query.orderBy(getOrderList(cb, root));
        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();
        LOGGER.info("All not deleted TimeTableItems for group {} received. Count of records {}.", group.getId(), timetableItems.size());
        return timetableItems;
    }

    @Override
    public List<TimetableItem> getAllNotDeletedByDateAndGroupAndTeacher(LocalDate dateFrom, LocalDate dateTo, Group group, Teacher teacher) {
        LOGGER.debug("getAllNotDeletedByDateAndGroupAndTeacher() [dateFrom={}, dateTo={}, group={}, teacher={}])", dateFrom, dateTo, group, teacher);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TimetableItem> query = cb.createQuery(TimetableItem.class);
        Root<TimetableItem> root = query.from(TimetableItem.class);

        List<Predicate> searchFilters = new ArrayList<>();

        if (dateFrom != null & dateTo != null) {
            Predicate filterForDates = cb.between(root.get(TimetableItem_.DATE), dateFrom, dateTo);
            searchFilters.add(filterForDates);
        } else {
            if (dateFrom != null) {
                Predicate filterForDateFrom = cb.greaterThanOrEqualTo(root.get(TimetableItem_.DATE), dateFrom);
                searchFilters.add(filterForDateFrom);
            }
            if (dateTo != null) {
                Predicate filterForDateTo = cb.lessThanOrEqualTo(root.get(TimetableItem_.DATE), dateTo);
                searchFilters.add(filterForDateTo);
            }
        }

        if (group != null) {
            Predicate filterForGroup = cb.isMember(group, root.get(TimetableItem_.GROUPS));
            searchFilters.add(filterForGroup);
        }
        if (teacher != null) {
            Predicate filterForTeacher = cb.equal(root.get(TimetableItem_.TEACHER), teacher);
            searchFilters.add(filterForTeacher);
        }

        if (searchFilters.isEmpty()) {
            Predicate todayFilter = cb.equal(root.get(TimetableItem_.DATE), LocalDate.now());
            searchFilters.add(todayFilter);
        }

        Predicate[] finalFilter = searchFilters.toArray(new Predicate[searchFilters.size()]);

        query.where(finalFilter);
        query.orderBy(getOrderList(cb, root));
        List<TimetableItem> timetableItems = em.createQuery(query).getResultList();

        LOGGER.info("All filtered timetable items received. Count of records {}. Filter: dateFrom={} dateTo={} Group={}, Teacher={}",
                timetableItems.size(),
                dateFrom,
                dateTo,
                group,
                teacher);

        return timetableItems;
    }

    private List<Order> getOrderList(CriteriaBuilder cb, Root<TimetableItem> root) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.asc(root.get(TimetableItem_.date)));
        orderList.add(cb.asc(root.get(TimetableItem_.lecture)));
        return orderList;
    }
}
