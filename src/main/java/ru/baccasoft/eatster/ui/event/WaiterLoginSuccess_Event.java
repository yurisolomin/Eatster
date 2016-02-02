package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.WaiterModel;

public class WaiterLoginSuccess_Event implements Event {

    private WaiterModel waiterModel = null;

    public WaiterLoginSuccess_Event(WaiterModel restaurant) {
        this.waiterModel = restaurant;
    }

    public WaiterModel getWaiterModel() {
        return waiterModel;
    }


}
