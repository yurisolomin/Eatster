package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.OperationModel;

public class AdminOperationInsert_Event implements Event {
    private final OperationModel operationModel;

    public AdminOperationInsert_Event(OperationModel operationModel) {
        this.operationModel = operationModel;
    }

    public OperationModel getOperationModel() {
        return operationModel;
    }

}
