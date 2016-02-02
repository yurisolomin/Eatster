package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ShowPartner_Listener extends Listener {
	@ListenerMethod
	void onShowPartner(ShowPartner_Event event);
}
