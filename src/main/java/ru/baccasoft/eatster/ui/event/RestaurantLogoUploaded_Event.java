package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import java.io.InputStream;
import ru.baccasoft.eatster.ui.component.RestaurantLogoPanel;

public class RestaurantLogoUploaded_Event implements Event {
    private final RestaurantLogoPanel restaurantLogoPanel;
    private final InputStream inputStream;
    private final String fileName;

    public RestaurantLogoUploaded_Event(RestaurantLogoPanel restaurantLogoPanel, InputStream inputStream, String fileName) {
        this.restaurantLogoPanel = restaurantLogoPanel;
        this.inputStream = inputStream;
        this.fileName = fileName;
    }

    public RestaurantLogoPanel getRestaurantLogoPanel() {
        return restaurantLogoPanel;
    }


    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

}
