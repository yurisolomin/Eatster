package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowWaiter_Listener extends Listener {
	@ListenerMethod
	void onShowWaiter(ShowWaiter_Event event);
}
