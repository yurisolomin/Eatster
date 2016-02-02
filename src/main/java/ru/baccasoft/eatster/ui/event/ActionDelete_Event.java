package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.ui.component.ActionPanel;

public class ActionDelete_Event implements Event {

    private ActionPanel actionPanel = null;

    public ActionDelete_Event(ActionPanel action) {
        this.actionPanel = action;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }


}
