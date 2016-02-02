package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowAllOperations_Listener extends Listener {
	@ListenerMethod
	void onShowAllOperations(ShowAllOperations_Event event);
}
