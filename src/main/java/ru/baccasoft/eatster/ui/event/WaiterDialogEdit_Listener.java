package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface WaiterDialogEdit_Listener extends Listener {
	@ListenerMethod
	void onWaiterDialogEdit(WaiterDialogEdit_Event event);
}
