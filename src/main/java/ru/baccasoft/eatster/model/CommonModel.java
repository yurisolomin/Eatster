package ru.baccasoft.eatster.model;

public class CommonModel extends CommonIdModel {

    private boolean deleted;

    public CommonModel() {
        super();
        deleted = false;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
