package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.ui.window.WaiterWindow;

public class WaiterAction_Event implements Event {

    private final WaiterModel waiterModel;
    private final WaiterWindow.ActionType actionType;

    public WaiterAction_Event(WaiterModel waiterModel, WaiterWindow.ActionType actionType) {
        this.waiterModel = waiterModel;
        this.actionType = actionType;
    }

    public WaiterModel getWaiterModel() {
        return waiterModel;
    }

    public WaiterWindow.ActionType getActionType() {
        return actionType;
    }

}
