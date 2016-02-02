package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface WaiterDialogCreate_Listener extends Listener {
	@ListenerMethod
	void onWaiterDialogCreate(WaiterDialogCreate_Event event);
}
