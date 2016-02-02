package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

//FIX ME статусы фоток акций находятся в таблице акций. не знаю верно ли это. хорошо бы исправить

public class ModerationPanel extends VerticalLayout implements Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(ModerationPanel.class);
    private static final long serialVersionUID = 1L;
    
    private static final int WIDTH_BUTTON = 5;

    private final Button btnActPhotos = new Button("Акции",this);
    private final Button btnRestPhotos = new Button("Фотографии",this);
    VerticalLayout layoutItems = new VerticalLayout();
    PhotoService photoService;
    RestaurantService restaurantService;
    ActionService actionService;
    private enum ObjectType { ACTPHOTOS, RESTPHOTOS };
    private ObjectType objectType = null;
    
    
    public ModerationPanel(PhotoService photoService, RestaurantService restaurantService, ActionService actionService) {
        this.photoService = photoService;
        this.restaurantService = restaurantService;
        this.actionService = actionService;
        buildLayout();
    }
    
    public final void buildLayout() {
        setMargin(true);
        setSpacing(true);
        btnActPhotos.setWidth(WIDTH_BUTTON, Unit.CM);
        btnRestPhotos.setWidth(WIDTH_BUTTON, Unit.CM);
        HorizontalLayout layout = new HorizontalLayout(btnActPhotos, btnRestPhotos);
        layout.setSpacing(true);
        addComponent(layout);
        addComponent(layoutItems);
        layoutItems.setSpacing(true);
    }

    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
/*
    public void loadActPhotos() {
        LOG.debug("loadActPhotos:");
        layoutPhotos.removeAllComponents();
        List<PhotoModel> listPhotos = photoService.findActPhotosToModeration();
        LOG.debug("loaded={0}",listPhotos.size());
        for(PhotoModel photoModel: listPhotos) {
            ModerationPhotoPanel moderationPhotoPanel = new ModerationPhotoPanel();
            layoutPhotos.addComponent(moderationPhotoPanel);
            moderationPhotoPanel.setPhotoModel(photoModel);
            long actionId = photoModel.getObjectId();
            ActionModel actionModel = actionService.getItem(actionId);
            if (actionModel == null) {
                LOG.warn("Action not found by objectId={0}",actionId);
                continue;
            }
            long restaurantId = actionModel.getRestaurantId();
            RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
            if (restaurantModel == null) {
                LOG.warn("Restaurant not found by restaurantId={0}",restaurantId);
                continue;
            }
            moderationPhotoPanel.setPanelCaption(restaurantModel.getName());
        }
        LOG.debug("Ok");
    }
*/
    public void loadActPhotos() {
        objectType = ObjectType.ACTPHOTOS;
        LOG.debug("loadActPhotos:");
        layoutItems.removeAllComponents();
        List<ActionModel> listActions = actionService.findAllToModeration();
        LOG.debug("found actions={0}",listActions.size());
        for(ActionModel actionModel: listActions) {
/*        
            long actionId = actionModel.getId();
            PhotoModel photoModel = photoService.getItem(actionId,PhotoModel.TYPE_ACT_PHOTO);
            if (photoModel == null) {
                LOG.warn("Photo not found by actionId={0}",actionId);
                continue;
            }*/
            long restaurantId = actionModel.getRestaurantId();
            RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
            if (restaurantModel == null) {
                LOG.warn("Restaurant not found by restaurantId={0}",restaurantId);
                continue;
            }
            ModerationActionPanel moderationActionPanel = new ModerationActionPanel();
            layoutItems.addComponent(moderationActionPanel);
            moderationActionPanel.setActionModel(actionModel);
            moderationActionPanel.setPanelCaption(restaurantModel.getName());
        }
        if (listActions.isEmpty()) {
            layoutItems.addComponent(new Label("Акций для модерации не обнаружено"));
        }
        LOG.debug("Ok.");
    }
    
    public void loadRestPhotos() {
        objectType = ObjectType.RESTPHOTOS;
        LOG.debug("loadRestPhotos:");
        layoutItems.removeAllComponents();
        List<PhotoModel> listPhotos = photoService.findRestPhotosToModeration();
        for(PhotoModel photoModel: listPhotos) {
            ModerationPhotoPanel moderationPhotoPanel = new ModerationPhotoPanel();
            layoutItems.addComponent(moderationPhotoPanel);
            moderationPhotoPanel.setPhotoModel(photoModel);
            long restaurantId = photoModel.getObjectId();
            RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
            if (restaurantModel == null) {
                LOG.warn("Restaurant not found by objectId={0}",restaurantId);
                continue;
            }
            moderationPhotoPanel.setPanelCaption(restaurantModel.getName());
        }
        if (listPhotos.isEmpty()) {
            layoutItems.addComponent(new Label("Фотографий для модерации не обнаружено"));
        }
        LOG.debug("Ok");
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnActPhotos) {
            loadActPhotos();
        }
        if (event.getButton() == btnRestPhotos) {
            loadRestPhotos();
        }
    }
    
    public void refresh() {
        if (objectType == ObjectType.RESTPHOTOS) {
            loadRestPhotos();
        }
        if (objectType == ObjectType.ACTPHOTOS) {
            loadActPhotos();
        }
    }
    
}
