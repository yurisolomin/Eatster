package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ActionPhotoUploaded_Listener extends Listener {
	@ListenerMethod
	void onActionPhotoUploaded(ActionPhotoUploaded_Event event);
}
