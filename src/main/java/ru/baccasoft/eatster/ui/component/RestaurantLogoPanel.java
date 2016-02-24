package ru.baccasoft.eatster.ui.component;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.image.ImageValidator;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.RestaurantLogoDelete_Event;
import ru.baccasoft.eatster.ui.window.ImageRestrictionsWindow;

public class RestaurantLogoPanel extends VerticalLayout
        implements
        Button.ClickListener,
        ConfirmDialog.Listener
        {
    private static final long serialVersionUID = 6634064290434987579L;

    private static final String MESSAGE_DELETE_CONFIRM = "Вы уверены, что хотите удалить логотип ?";
    private static final float WIDTH_IMAGE_DEFAULT = 3.1f;
    private static final float HEIGHT_IMAGE_DEFAULT = 3.1f;
    
    private final Image image = new Image();
    private final Button btnDelete = new Button(null, this);
    private final Button btnQuestion = new Button(null, this);//"Знак вопроса"
    private final UploadField btnLoadPhoto = new ButtonUploadRestaurantLogo(this);
    
    private PhotoModel photoModel = null;
    
    public RestaurantLogoPanel(String panelCaption) {
        buildLayout(panelCaption);
    }

    public final void buildLayout(String panelCaption) {
        setSpacing(true);
        setSizeUndefined();
        image.setWidth(WIDTH_IMAGE_DEFAULT, Unit.CM);
        image.setHeight(HEIGHT_IMAGE_DEFAULT, Unit.CM);
        image.setAlternateText(panelCaption);
        btnDelete.setIcon(FontAwesome.TRASH_O);
        btnQuestion.setIcon(FontAwesome.QUESTION_CIRCLE);
        HorizontalLayout iconLayout = new HorizontalLayout(btnDelete,btnQuestion);
        iconLayout.setSpacing(true);
        VerticalLayout btnLayout = new VerticalLayout(btnLoadPhoto,iconLayout);
        btnLayout.setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout(image,btnLayout);
        layout.setSpacing(true);
        addComponent(layout);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnDelete) {
            ConfirmDialog.show(getUI(), "Пожалуйста, подтвердите:", MESSAGE_DELETE_CONFIRM, "Да", "Нет", this);        
        }
        if (event.getButton() == btnQuestion) {
            ImageRestrictionsWindow infoWindow = new ImageRestrictionsWindow(getUI().getPartnerScope().getImageValidatorRestLogo());
            getUI().addWindow(infoWindow);
        }
    }
    
    public PhotoModel getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(PhotoModel photoModel, boolean readonly) {
        setPhotoModel(photoModel);
        btnDelete.setEnabled(!readonly);
        btnLoadPhoto.setEnabled(!readonly);
    }
    public void setPhotoModel(PhotoModel photoModel) {
        this.photoModel = photoModel;
        ExternalResource photoExternalResource = null;
        if (!photoModel.getPhotoUrlParams().isEmpty()) {
            String photoDownloadUrl = getUI().getFileDownloadUrl(photoModel.getPhotoUrlParams());
            photoExternalResource = new ExternalResource(photoDownloadUrl);
        }
        image.setSource(photoExternalResource);
        if (photoModel.getId() == 0) {
            btnDelete.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
        }
        //учтем соотношение сторон по настройкам максимальных размеров фото
        ImageValidator imageValidator= getUI().getPartnerScope().getImageValidatorRestLogo();
        image.setWidth(HEIGHT_IMAGE_DEFAULT*imageValidator.getWidthHeightRatio(),Unit.CM);
    }

    @Override
    public void onClose(ConfirmDialog cd) {
        if (cd.isConfirmed() && cd.getMessage().equals(MESSAGE_DELETE_CONFIRM)) {
            getUI().fire(new RestaurantLogoDelete_Event(this));
        }
    }
}
