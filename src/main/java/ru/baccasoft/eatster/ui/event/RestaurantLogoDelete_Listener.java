package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantLogoDelete_Listener extends Listener {
	@ListenerMethod
	void onRestaurantLogoDelete(RestaurantLogoDelete_Event event);
}
