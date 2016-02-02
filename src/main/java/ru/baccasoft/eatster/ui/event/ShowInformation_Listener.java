package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowInformation_Listener extends Listener {
	@ListenerMethod
	void onShowInformation(ShowInformation_Event event);
}
