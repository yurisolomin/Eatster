package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface Logout_Listener extends Listener {
	@ListenerMethod
	void onLogout(Logout_Event event);
}
