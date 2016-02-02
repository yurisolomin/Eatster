package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowPhoto_Listener extends Listener {
	@ListenerMethod
	void onShowPhoto(ShowPhoto_Event event);
}
