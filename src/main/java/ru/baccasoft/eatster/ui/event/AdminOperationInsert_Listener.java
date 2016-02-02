package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface AdminOperationInsert_Listener extends Listener {
	@ListenerMethod
	void onAdminOperationInsert(AdminOperationInsert_Event event);
}
