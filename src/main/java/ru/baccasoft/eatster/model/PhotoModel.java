package ru.baccasoft.eatster.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoModel extends CommonModel {
    public static final String TYPE_ACTION_PHOTO = "act.photo";
    public static final String TYPE_REST_PHOTO = "rest.photo";
    public static final String TYPE_REST_BACK = "rest.back";
    public static final String TYPE_REST_LOGO = "rest.logo";

    public static final String STAT_INACTIVE = "inactive";
    public static final String STAT_PUBLISHED = "published";
    public static final String STAT_MODERATION = "moderation";
    public static final String STAT_REJECTED = "rejected";
    public static final String STAT_ARCHIVE = "archive";

    private long objectId;
    private String objectType;//background,photo,logo
    private long fileinfoId;
    private String photoUrlParams;
    private long fileinfoMiniId;
    private String photoUrlParamsMini;
    private String status;//Published,Inactive,Moderation,Rejected,archive

    public PhotoModel() {
        super();
        objectId = 0;
        objectType = "";
        fileinfoId = 0;
        photoUrlParams = "";
        fileinfoMiniId = 0;
        photoUrlParamsMini = "";
        status = "";
    }

    public String getPhotoUrlParams() {
        return photoUrlParams;
    }

    public void setPhotoUrlParams(String photoUrlParams) {
        this.photoUrlParams = photoUrlParams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoUrlParamsMini() {
        return photoUrlParamsMini;
    }

    public void setPhotoUrlParamsMini(String photoUrlParamsMini) {
        this.photoUrlParamsMini = photoUrlParamsMini;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public long getFileinfoId() {
        return fileinfoId;
    }

    public void setFileinfoId(long fileinfoId) {
        this.fileinfoId = fileinfoId;
    }

    public long getFileinfoMiniId() {
        return fileinfoMiniId;
    }

    public void setFileinfoMiniId(long fileinfoMiniId) {
        this.fileinfoMiniId = fileinfoMiniId;
    }

    
    public static class PhotoStatus {
        String id;
        String name;
        public PhotoStatus(String id, String name) {
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
    
    public static List<PhotoStatus> getStatusList() {
        List<PhotoStatus> list = new ArrayList();
        list.add(new PhotoStatus(STAT_PUBLISHED,"опубликовано"));
        list.add(new PhotoStatus(STAT_INACTIVE,"не активно"));
        list.add(new PhotoStatus(STAT_MODERATION,"на модерации"));
        list.add(new PhotoStatus(STAT_REJECTED,"отклонена"));
        //list.add(new PhotoStatus(STAT_ARCHIVE,"в архиве"));
        return list;
    }
    
}
