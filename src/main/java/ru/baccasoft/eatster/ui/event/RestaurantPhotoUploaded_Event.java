package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import java.io.InputStream;
import ru.baccasoft.eatster.ui.component.RestaurantPhotoPanel;

public class RestaurantPhotoUploaded_Event implements Event {
    private final RestaurantPhotoPanel restaurantPhotoPanel;
    private final InputStream inputStream;
    private final String fileName;

    public RestaurantPhotoUploaded_Event(RestaurantPhotoPanel restaurantPhotoPanel, InputStream inputStream, String fileName) {
        this.restaurantPhotoPanel = restaurantPhotoPanel;
        this.inputStream = inputStream;
        this.fileName = fileName;
    }

    public RestaurantPhotoPanel getRestaurantPhotoPanel() {
        return restaurantPhotoPanel;
    }

    public InputStream getInputStream() {
        return inputStream;
    }


    public String getFileName() {
        return fileName;
    }
    
}
