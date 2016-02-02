package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public interface ReportModelUpdate_Listener extends Listener {
	@ListenerMethod
	void onReportModelUpdate(ReportModelUpdate_Event event);
}
