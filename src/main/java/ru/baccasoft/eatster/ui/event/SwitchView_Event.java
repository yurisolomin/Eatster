package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;

public class SwitchView_Event implements Event{
	private String viewName = null;

	public SwitchView_Event(String s) {
		viewName = s;
	}

	public String getViewName() {
		return viewName;
	}
}
