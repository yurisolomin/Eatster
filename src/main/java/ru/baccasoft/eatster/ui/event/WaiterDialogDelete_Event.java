package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.WaiterModel;

public class WaiterDialogDelete_Event implements Event {

    private final WaiterModel waiterModel;

    public WaiterDialogDelete_Event(WaiterModel waiterModel) {
        this.waiterModel = waiterModel;
    }

    public WaiterModel getWaiterModel() {
        return waiterModel;
    }

}
