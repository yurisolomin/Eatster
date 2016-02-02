package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowAction_Listener extends Listener {
	@ListenerMethod
	void onShowAction(ShowAction_Event event);
}
