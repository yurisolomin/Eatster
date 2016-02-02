package ru.baccasoft.eatster.ui.component;

import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.RestaurantLogoUploaded_Event;

public class ButtonUploadRestaurantLogo extends UploadField {

    private static final long serialVersionUID = 1L;

    RestaurantLogoPanel restaurantLogoPanel;
    //private static final int PHOTO_MAX_SIZE = 500000;

    public ButtonUploadRestaurantLogo(RestaurantLogoPanel restaurantLogoPanel) {
        super(UploadField.StorageMode.FILE);
        this.restaurantLogoPanel = restaurantLogoPanel;
        super.setButtonCaption("Загрузить логотип");//
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
        String mimeType = getLastMimeType();
        long filesize = getLastFileSize();
        getUI().fire(new RestaurantLogoUploaded_Event(restaurantLogoPanel, getContentAsStream(), filename));
        
    }

}
