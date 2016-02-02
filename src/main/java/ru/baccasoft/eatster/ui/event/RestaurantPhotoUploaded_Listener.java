package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantPhotoUploaded_Listener extends Listener {
	@ListenerMethod
	void onRestaurantPhotoUploaded(RestaurantPhotoUploaded_Event event);
}
