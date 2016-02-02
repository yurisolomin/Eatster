package ru.baccasoft.eatster.model;

import java.util.ArrayList;
import java.util.List;

public class ActionModel extends CommonNamedModel {

    public static final String STAT_PUBLISHED = "published";
    public static final String STAT_INACTIVE = "inactive";
    public static final String STAT_MODERATION = "moderation";
    public static final String STAT_REJECTED = "rejected";
    public static final String STAT_ARCHIVE = "archive";
    public static final String STAT_PUBLISHED_RUS = "опубликовано";
    public static final String STAT_INACTIVE_RUS = "не активно";
    public static final String STAT_MODERATION_RUS = "на модерации";
    public static final String STAT_REJECTED_RUS = "отклонена";
    public static final String STAT_ARCHIVE_RUS = "в архиве";
    private long restaurantId;
    private String status;//Published,Inactive,Moderation,Rejected,archive
    private Long actionTypeId;
    private Long actionSubTypeId;
    private String photoUrlParams;
    private String comment;//300
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;
    private boolean onSunday;
    private String actionType; //из связанной таблицы
    private String actionSubType; //из связанной таблицы
    private String startTime;
    private String endTime;

    public static class ActionStatus {
        String id;
        String name;
        public ActionStatus(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
    
    public ActionModel() {
        super();
        restaurantId = 0;
        status = "";
        actionTypeId = null;
        actionSubTypeId = null;
//        actionTimeRangeId = 0;
        photoUrlParams = "";
        comment = "";
//        infoWorkdays = "";
        onMonday = false;
        onTuesday = false;
        onWednesday = false;
        onThursday = false;
        onFriday = false;
        onSaturday = false;
        onSunday = false;
        actionType = "";
        actionSubType = "";
        startTime = "";
        endTime = "";
//        actionTimeRange = "";
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(Long actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public Long getActionSubTypeId() {
        return actionSubTypeId;
    }

    public void setActionSubTypeId(Long actionSubTypeId) {
        this.actionSubTypeId = actionSubTypeId;
    }

    public String getPhotoUrlParams() {
        return photoUrlParams;
    }

    public void setPhotoUrlParams(String photoUrlParams) {
        this.photoUrlParams = photoUrlParams;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isOnMonday() {
        return onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean isOnFriday() {
        return onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    public boolean isOnSunday() {
        return onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }
/*
    public long getActionTimeRangeId() {
        return actionTimeRangeId;
    }

    public void setActionTimeRangeId(long actionTimeRangeId) {
        this.actionTimeRangeId = actionTimeRangeId;
    }
*/
    // Дни недели в акции. Битовая маска (понедельник и суббота = 2 + 128 = 130)
    // 2 - понедельник
    // 4 - вторник
    // 8 - среда
    // 16 - четверг
    // 32 - пятница
    // 64 - суббота
    // 128 - воскресенье
    // 256 - зарезервировано на случай 32го мая
    public int getActionDays() {
        int mask = 0;
        if (onMonday) {
            mask += 2;
        }
        if (onTuesday) {
            mask += 4;
        }
        if (onWednesday) {
            mask += 8;
        }
        if (onThursday) {
            mask += 16;
        }
        if (onFriday) {
            mask += 32;
        }
        if (onSaturday) {
            mask += 64;
        }
        if (onSunday) {
            mask += 128;
        }
        return mask;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActionSubType() {
        return actionSubType;
    }

    public void setActionSubType(String actionSubType) {
        this.actionSubType = actionSubType;
    }
/*
    public String getActionTimeRange() {
        return actionTimeRange;
    }

    public void setActionTimeRange(String actionTimeRange) {
        this.actionTimeRange = actionTimeRange;
    }
*/    
    public static List<ActionStatus> getStatusList() {
        List<ActionStatus> list = new ArrayList();
        list.add(new ActionStatus(STAT_PUBLISHED,STAT_PUBLISHED_RUS));
        list.add(new ActionStatus(STAT_INACTIVE,STAT_INACTIVE_RUS));
        list.add(new ActionStatus(STAT_MODERATION,STAT_MODERATION_RUS));
        list.add(new ActionStatus(STAT_REJECTED,STAT_REJECTED_RUS));
        list.add(new ActionStatus(STAT_ARCHIVE,STAT_ARCHIVE_RUS));
        return list;
    }
/*    
    public String getVaadinActionTypeId() {
        return cboxIdAsString(actionTypeId);
    }

    public void setVaadinActionTypeId(String actionTypeId) {
        this.actionTypeId = cboxIdAsLong(actionTypeId);
    }
*/    
/*

    public long getVaadinActionSubTypeId() {
        return actionSubTypeId;
    }

    public void setVaadinActionSubTypeId(long actionSubTypeId) {
        this.actionSubTypeId = actionSubTypeId;
    }
*/    
}
