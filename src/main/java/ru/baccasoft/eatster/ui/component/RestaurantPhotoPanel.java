package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.image.ImageValidator;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoDelete_Event;
import ru.baccasoft.eatster.ui.event.RestaurantPhotoSave_Event;
import ru.baccasoft.eatster.ui.window.ImageRestrictionsWindow;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantPhotoPanel extends VerticalLayout
        implements
        Button.ClickListener,
        ValueChangeListener
        {
    private static final long serialVersionUID = -4578193189018252585L;
    private static final Logger LOG = Logger.getLogger(RestaurantPhotoPanel.class);

    private static final float WIDTH_STATUS = 4.5f;
    private static final float WIDTH_IMAGE_DEFAULT = 14f;
    private static final float HEIGHT_IMAGE_DEFAULT = 8;
        
    private class PhotoFields {

        ComboBox status = new EatsterStatusComboBox(null);//
        Image image = new Image();

        public PhotoFields() {
            status.setWidth(WIDTH_STATUS, Unit.CM);
            image.setWidth(WIDTH_IMAGE_DEFAULT, Unit.CM);
            image.setHeight(HEIGHT_IMAGE_DEFAULT, Unit.CM);
        }

    }
    private final PhotoFields fields = new PhotoFields();
    private final Button btnDelete = new Button(null, this);//"Удалить"
    private final Button btnQuestion = new Button(null, this);//"Знак вопроса"
    private final UploadField btnLoadPhoto = new ButtonUploadRestaurantPhoto(this);
    private ImageValidator imageValidator = null;
    
    private PhotoModel photoModel = null;
    private BeanFieldGroup<PhotoModel> fieldsBindings = null;
    
    public RestaurantPhotoPanel(String panelCaption) {
        buildLayout(panelCaption);
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component) {
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    public final void buildLayout(String panelCaption) {
        setSpacing(true);
        setSizeUndefined();
        btnDelete.setIcon(FontAwesome.TRASH_O);
        btnQuestion.setIcon(FontAwesome.QUESTION_CIRCLE);
        HorizontalLayout layout = new HorizontalLayout(new Label(panelCaption),fields.status,btnLoadPhoto,btnDelete,btnQuestion);
        layout.setSpacing(true);
        GridLayout grid = new GridLayout(1,2);
        grid.setSpacing(true);
        grid.setSizeUndefined();
        addToGrid(grid, 0, 0, layout);
        addToGrid(grid, 0, 1, fields.image);
//        addComponent(gridTop);
        Panel panel = new Panel();
        panel.setContent(grid);
        addComponent(panel);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        getUI().fire(new RestaurantPhotoSave_Event(this));
    }

    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnDelete) {
            getUI().fire(new RestaurantPhotoDelete_Event(this));
        }
        if (event.getButton() == btnQuestion) {
            ImageRestrictionsWindow infoWindow = new ImageRestrictionsWindow(imageValidator);
            getUI().addWindow(infoWindow);
        }
    }

    public void commit() {
        try {
            // Commit the fields from UI to DAO
            fieldsBindings.commit();
            //getUI().fire(new RestaurantPhotoSave_Event(this));
        } catch (FieldGroup.CommitException e) {
            LOG.error("Error on commit photo: " + e.getMessage());
            Notification.show("Ошибка в данных: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            // Validation exceptions could be shown here
        }
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(PhotoModel photoModel) {
        //по умолчанию никак не реагируем на смену значений
        fields.status.removeValueChangeListener(this);
        fields.status.setImmediate(false);
        //заполним варианты выбора статуса
        getUI().getPartnerScope().comboboxForPhotoStatus(fields.status);
        //
        this.photoModel = photoModel;
        fieldsBindings = BeanFieldGroup.bindFieldsBuffered(photoModel, fields);
        ExternalResource photoExternalResource = null;
        if (!photoModel.getPhotoUrlParams().isEmpty()) {
            String photoDownloadUrl = getUI().getFileDownloadUrl(photoModel.getPhotoUrlParams());
            photoExternalResource = new ExternalResource(photoDownloadUrl);
        }
        fields.image.setSource(photoExternalResource);
        if (photoModel.getId() == 0) {
            fields.image.setAlternateText("Место для нового фото");
            fields.status.setEnabled(false);
            btnDelete.setEnabled(false);
            btnLoadPhoto.setEnabled(true);
        } else {
            fields.status.setEnabled(getUI().isAdminApp());
            //реакция на смену значения
            fields.status.addValueChangeListener(this);
            fields.status.setImmediate(true);
            btnDelete.setEnabled(true);
            //возможность загружать фото только в режиме модерации
            boolean btnLoadPhotoEnabled = false;
            if (photoModel.getStatus().equals(PhotoModel.STAT_MODERATION)) {
                btnLoadPhotoEnabled = true;
            }
            if (getUI().isAdminApp()) {
                btnLoadPhotoEnabled = true;
            }
            btnLoadPhoto.setEnabled(btnLoadPhotoEnabled);
        }
        //учтем соотношение сторон по настройкам максимальных размеров фото
        if (photoModel.getObjectType().equals(PhotoModel.TYPE_REST_BACK)) {
            imageValidator = getUI().getPartnerScope().getImageValidatorRestBackground();
        } else {
            imageValidator = getUI().getPartnerScope().getImageValidatorRestPhoto();
        }
        fields.image.setWidth(HEIGHT_IMAGE_DEFAULT*imageValidator.getWidthHeightRatio(),Unit.CM);
    }

    public ImageValidator getImageValidator() {
        return imageValidator;
    }
    
    public void setAlternateText(String caption) {
        fields.image.setAlternateText(caption);
    }
}
