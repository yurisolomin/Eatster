package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.ReportModel;

public class ReportModelUpdate_Event implements Event {

    private ReportModel reportModel = null;

    public ReportModelUpdate_Event(ReportModel action) {
        this.reportModel = action;
    }

    public ReportModel getReportModel() {
        return reportModel;
    }


}
