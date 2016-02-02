package ru.baccasoft.eatster.model;

public class ActionSubTypeModel extends CommonNamedModel {
    private Long actionTypeId;

    public ActionSubTypeModel() {
        super();
        actionTypeId = null;
    }
    
    public Long getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(Long actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    
}
