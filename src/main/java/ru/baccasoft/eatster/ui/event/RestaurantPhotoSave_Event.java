package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.ui.component.RestaurantPhotoPanel;

public class RestaurantPhotoSave_Event implements Event {

    private RestaurantPhotoPanel restaurantPhotoPanel = null;

    public RestaurantPhotoSave_Event(RestaurantPhotoPanel restaurantPhotoPanel) {
        this.restaurantPhotoPanel = restaurantPhotoPanel;
    }

    public RestaurantPhotoPanel getRestaurantPhotoPanel() {
        return restaurantPhotoPanel;
    }


}
