package ru.baccasoft.eatster.ui.component;

import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.ActionPhotoUploaded_Event;

public class ButtonUploadActionPhoto extends UploadField {

    private static final long serialVersionUID = -3418849071939626313L;

    ActionPanel actionPanel;

    public ButtonUploadActionPhoto(ActionPanel actionPanel) {
        super(UploadField.StorageMode.FILE);
        this.actionPanel = actionPanel;
        super.setButtonCaption("Загрузить фотографию");
        super.setAcceptFilter("image/*");
        //super.setMaxFileSize(2000000);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    protected void updateDisplay() {
        String filename = getLastFileName();
        String mimeType = getLastMimeType();
        long filesize = getLastFileSize();
        getUI().fire(new ActionPhotoUploaded_Event(actionPanel, getContentAsStream(), filename));
    }

}
