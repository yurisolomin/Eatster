package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ModerationPhoto_Listener extends Listener {
	@ListenerMethod
	void onModerationPhoto(ModerationPhoto_Event event);
}
