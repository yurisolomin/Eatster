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

public class MainMenuItemModeration extends MainMenuItemLayout implements Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(MainMenuItemModeration.class);
    private static final long serialVersionUID = 1L;
    
    private static final int WIDTH_BUTTON = 5;

    private final Button btnActPhotos = new Button("Акции",this);
    private final Button btnRestPhotos = new Button("Фотографии",this);
    VerticalLayout layoutItems = new VerticalLayout();

    private enum ObjectType { ACTPHOTOS, RESTPHOTOS };
    private ObjectType objectType = null;
    
    
    public MainMenuItemModeration() {
        buildLayout();
    }
    
    public final void buildLayout() {
        //setMargin(true);
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
    
    public void loadActPhotos() {
        objectType = ObjectType.ACTPHOTOS;
        LOG.debug("loadActPhotos:");
        layoutItems.removeAllComponents();
        //
        ActionService actionService = getUI().getActionService();
        RestaurantService restaurantService = getUI().getRestaurantService();
        //
        List<ActionModel> listActions = actionService.findAllToModeration();
        LOG.debug("found actions={0}",listActions.size());
        for(ActionModel actionModel: listActions) {
            long restaurantId = actionModel.getRestaurantId();
            RestaurantModel restaurantModel = restaurantService.getItem(restaurantId);
            if (restaurantModel == null) {
                LOG.warn("Restaurant not found by restaurantId={0}",restaurantId);
                continue;
            }
            ComponentModerationAction moderationActionPanel = new ComponentModerationAction();
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
        //
        RestaurantService restaurantService = getUI().getRestaurantService();
        PhotoService photoService = getUI().getPhotoService();
        //
        List<PhotoModel> listPhotos = photoService.findRestPhotosToModeration();
        for(PhotoModel photoModel: listPhotos) {
            ComponentModerationPhoto moderationPhotoPanel = new ComponentModerationPhoto();
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
    
    @Override
    public void doRefresh() {
        if (objectType == ObjectType.RESTPHOTOS) {
            loadRestPhotos();
        }
        if (objectType == ObjectType.ACTPHOTOS) {
            loadActPhotos();
        }
    }
    
}
