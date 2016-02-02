package ru.baccasoft.eatster.ui.event;

import com.github.wolfie.blackboard.Event;
import java.io.InputStream;
import ru.baccasoft.eatster.ui.component.ActionPanel;

public class ActionPhotoUploaded_Event implements Event {
    private final ActionPanel actionPanel;
    private final InputStream inputStream;
    private final String fileName;

    public ActionPhotoUploaded_Event(ActionPanel actionPanel, InputStream inputStream, String fileName) {
        this.actionPanel = actionPanel;
        this.inputStream = inputStream;
        this.fileName = fileName;
    }

    public ActionPanel getActionPanel() {
        return actionPanel;
    }

    public InputStream getInputStream() {
        return inputStream;
    }


    public String getFileName() {
        return fileName;
    }

}
