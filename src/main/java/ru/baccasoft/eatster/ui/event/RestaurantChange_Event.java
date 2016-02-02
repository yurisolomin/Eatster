package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;

public class RestaurantChange_Event implements Event {

    private long restaurantId;

    public RestaurantChange_Event(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

}
