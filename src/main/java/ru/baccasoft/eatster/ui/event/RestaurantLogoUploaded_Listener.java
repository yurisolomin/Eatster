package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantLogoUploaded_Listener extends Listener {
	@ListenerMethod
	void onRestaurantLogoUploaded(RestaurantLogoUploaded_Event event);
}
