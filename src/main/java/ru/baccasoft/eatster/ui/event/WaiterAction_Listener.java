package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface WaiterAction_Listener extends Listener {
	@ListenerMethod
	void onWaiterAction(WaiterAction_Event event);
}
