package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ModerationAction_Listener extends Listener {
	@ListenerMethod
	void onModerationAction(ModerationAction_Event event);
}
