package ru.baccasoft.eatster.ui.component;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.ModerationPhoto_Event;

public class ComponentModerationPhoto extends VerticalLayout implements Button.ClickListener {
    private static final long serialVersionUID = 8511650517619587185L;
    private static final float WIDTH_IMAGE = 14f;
    private static final int HEIGHT_IMAGE = 8;
    private static final int WIDTH_RESTAURANT = 9;
    public enum ModerationType { CONFIRM, BAN };
    
        
    private final TextField restaurant = new TextField();
    private final Image image = new Image();
    private final Button btnConfirm = new Button("Утвердить", this);
    private final Button btnBan = new Button("Бан", this);
    private final Panel panel = new Panel();
    
    private PhotoModel photoModel = null;
    
    public ComponentModerationPhoto() {
        buildLayout();
    }
    
    public final void buildLayout() {
        restaurant.setReadOnly(true);
        restaurant.setWidth(WIDTH_RESTAURANT, Unit.CM);
        image.setWidth(WIDTH_IMAGE, Unit.CM);
        image.setHeight(HEIGHT_IMAGE, Unit.CM);
        HorizontalLayout layout = new HorizontalLayout(restaurant,btnConfirm,btnBan);
        layout.setSpacing(true);
        layout.setWidth("100%");
        VerticalLayout vLayout = new VerticalLayout(layout,image);
        vLayout.setSpacing(true);
        vLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
        panel.setContent(vLayout);
        panel.setSizeUndefined();
        addComponent(panel);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnConfirm) {
            getUI().fire(new ModerationPhoto_Event(photoModel,ModerationType.CONFIRM));
        }
        if (event.getButton() == btnBan) {
            getUI().fire(new ModerationPhoto_Event(photoModel,ModerationType.BAN));
        }
    }

    public PhotoModel getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(PhotoModel photoModel) {
        this.photoModel = photoModel;
        ExternalResource photoExternalResource = null;
        if (!photoModel.getPhotoUrlParams().isEmpty()) {
            String photoDownloadUrl = getUI().getFileDownloadUrl(photoModel.getPhotoUrlParams());
            photoExternalResource = new ExternalResource(photoDownloadUrl);
        }
        image.setSource(photoExternalResource);
    }
    
    public void setPanelCaption(String caption) {
        restaurant.setReadOnly(false);
        restaurant.setValue(caption);
        restaurant.setReadOnly(true);
    }
}
