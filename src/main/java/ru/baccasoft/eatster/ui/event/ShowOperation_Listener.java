package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowOperation_Listener extends Listener {
	@ListenerMethod
	void onShowOperation(ShowOperation_Event event);
}
