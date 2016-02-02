package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RestaurantChange_Listener extends Listener {
	@ListenerMethod
	void onRestaurantChange(RestaurantChange_Event event);
}
