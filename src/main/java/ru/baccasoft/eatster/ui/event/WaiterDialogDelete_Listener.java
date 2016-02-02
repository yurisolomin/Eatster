package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface WaiterDialogDelete_Listener extends Listener {
	@ListenerMethod
	void onWaiterDialogDelete(WaiterDialogDelete_Event event);
}
