package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ActionDelete_Listener extends Listener {
	@ListenerMethod
	void onActionDelete(ActionDelete_Event event);
}
