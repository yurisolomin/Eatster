package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantPhotoSave_Listener extends Listener {
	@ListenerMethod
	void onRestaurantPhotoSave(RestaurantPhotoSave_Event event);
}
