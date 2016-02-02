package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantPhotoDelete_Listener extends Listener {
	@ListenerMethod
	void onRestaurantPhotoDelete(RestaurantPhotoDelete_Event event);
}
