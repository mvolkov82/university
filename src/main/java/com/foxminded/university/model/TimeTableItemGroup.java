package com.foxminded.university.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;


@Table(name = "timetable_items_groups")
public class TimeTableItemGroup implements Serializable {
    @ManyToOne
    @MapsId("timetableItemId")
    @JoinColumn(name = "timetable_item_id")
    private TimetableItem timetableItemId;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group groupId;

    public TimeTableItemGroup() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeTableItemGroup)) {
            return false;
        }

        TimeTableItemGroup that = (TimeTableItemGroup) o;

        if (!timetableItemId.equals(that.timetableItemId)) {
            return false;
        }
        return groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        int result = timetableItemId.hashCode();
        result = 31 * result + groupId.hashCode();
        return result;
    }
}
