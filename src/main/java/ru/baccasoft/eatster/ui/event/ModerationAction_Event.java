package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.ui.component.ComponentModerationPhoto.ModerationType;

public class ModerationAction_Event implements Event {

   
    private final ActionModel photoModel;
    private final ModerationType moderationType;

    public ModerationAction_Event(ActionModel photoModel, ModerationType moderationType) {
        this.photoModel = photoModel;
        this.moderationType = moderationType;
    }

    public ActionModel getActionModel() {
        return photoModel;
    }

    public ModerationType getModerationType() {
        return moderationType;
    }

}
