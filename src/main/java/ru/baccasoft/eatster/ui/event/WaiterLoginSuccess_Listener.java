package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface WaiterLoginSuccess_Listener extends Listener {
	@ListenerMethod
	void onWaiterLoginSuccess(WaiterLoginSuccess_Event event);
}
