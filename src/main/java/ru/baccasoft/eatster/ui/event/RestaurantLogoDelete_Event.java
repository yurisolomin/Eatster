package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.ui.component.RestaurantLogoPanel;

public class RestaurantLogoDelete_Event implements Event {

    private RestaurantLogoPanel restaurantLogoPanel = null;

    public RestaurantLogoDelete_Event(RestaurantLogoPanel restaurantLogoPanel) {
        this.restaurantLogoPanel = restaurantLogoPanel;
    }

    public RestaurantLogoPanel getRestaurantLogoPanel() {
        return restaurantLogoPanel;
    }


}
