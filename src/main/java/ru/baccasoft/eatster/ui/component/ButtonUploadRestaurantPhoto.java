package ru.baccasoft.eatster.ui.component;

import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoUploaded_Event;

public class ButtonUploadRestaurantPhoto extends UploadField {

    private static final long serialVersionUID = -6779784253950627564L;

    RestaurantPhotoPanel restaurantPhotoPanel;
    //private static final int PHOTO_MAX_SIZE = 2000000;

    public ButtonUploadRestaurantPhoto(RestaurantPhotoPanel restaurantPhotoPanel) {
        super(UploadField.StorageMode.FILE);
        this.restaurantPhotoPanel = restaurantPhotoPanel;
        super.setButtonCaption("Загрузить");//
        super.setAcceptFilter("image/*");
        //super.setMaxFileSize(PHOTO_MAX_SIZE);
        super.setWidthUndefined();
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    protected void updateDisplay() {
        String filename = getLastFileName();
        //String mimeType = getLastMimeType();
        //long filesize = getLastFileSize();
        getUI().fire(new RestaurantPhotoUploaded_Event(restaurantPhotoPanel, getContentAsStream(), filename));
        
    }

}
