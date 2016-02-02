package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface RefreshView_Listener extends Listener {
	@ListenerMethod
	void onRefreshView(RefreshView_Event event);
}
