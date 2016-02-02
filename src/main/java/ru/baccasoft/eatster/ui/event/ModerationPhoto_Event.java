package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.ui.component.ModerationPhotoPanel.ModerationType;

public class ModerationPhoto_Event implements Event {

   
    private final PhotoModel photoModel;
    private final ModerationType moderationType;

    public ModerationPhoto_Event(PhotoModel photoModel, ModerationType moderationType) {
        this.photoModel = photoModel;
        this.moderationType = moderationType;
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }

    public ModerationType getModerationType() {
        return moderationType;
    }

}
