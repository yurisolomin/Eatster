package ru.baccasoft.eatster.ui.component;

import ru.baccasoft.eatster.ui.view.*;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import java.util.ArrayList;
import java.util.List;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantPhotosPanel extends VerticalLayout 
//        implements Button.ClickListener
        {

    private static final Logger LOG = Logger.getLogger(AdminLoginView.class);

    private static final long serialVersionUID = 1L;
    private final VerticalLayout layoutPhotos = new VerticalLayout();
    private final PhotoService photoService;
    private static final String CAPTION_PHOTO_REST = "Фото ресторана";

    private final RestaurantPhotoPanel backPhotoPanel = new RestaurantPhotoPanel("Фоновая картинка");
//    private final RestaurantPhotoPanel logoPhotoPanel = new RestaurantPhotoPanel("Логотип");
    private final List<RestaurantPhotoPanel> listPhotoPanles = new ArrayList();
    //количество пар фотографий
    private static final int MAX_PHOTO_PAIRS = 3; //*2 

    public RestaurantPhotosPanel(PhotoService photoService) {
        this.photoService = photoService;
        buildLayout();
    }

    private void buildLayout() {
        setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout(backPhotoPanel);
        layout.setSpacing(true);
        addComponent(layout);
        layoutPhotos.setSpacing(true);
        addComponent(layoutPhotos);
        for(int i=0; i<MAX_PHOTO_PAIRS; ++i) {
            RestaurantPhotoPanel photoPanelL = new RestaurantPhotoPanel(CAPTION_PHOTO_REST);
            RestaurantPhotoPanel photoPanelR = new RestaurantPhotoPanel(CAPTION_PHOTO_REST);
            layout = new HorizontalLayout(photoPanelL,photoPanelR);
            layout.setSpacing(true);
            layoutPhotos.addComponent(layout);
            listPhotoPanles.add(photoPanelL);
            listPhotoPanles.add(photoPanelR);
        }
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void refresh() {
        Notification.show("ФОТО РЕСТОРАНА", Type.HUMANIZED_MESSAGE);
//        long restaurantId = getUI().getMainView().getSelectedRestaurantId();
        long restaurantId = getUI().getMainView().getRestaurantPanel().getSelectedRestaurantId();
        //фон
        PhotoModel backPhotoModel = photoService.findRestaurantBackground(restaurantId);
        if (backPhotoModel == null) {
            backPhotoModel = new PhotoModel();
            backPhotoModel.setObjectId(restaurantId);
            backPhotoModel.setObjectType(PhotoModel.TYPE_REST_BACK);
            backPhotoModel.setStatus(PhotoModel.STAT_MODERATION);
        }
        backPhotoPanel.setPhotoModel(backPhotoModel);
        //фотки
        List<PhotoModel> listPhotos = photoService.findByRestaurant(restaurantId);
        //докинем пустые фото в массив
        for(int i=listPhotos.size(); i<listPhotoPanles.size(); ++i) {
            PhotoModel photoModel = new PhotoModel();
            photoModel.setObjectId(restaurantId);
            photoModel.setObjectType(PhotoModel.TYPE_REST_PHOTO);
            photoModel.setStatus(PhotoModel.STAT_MODERATION);
            listPhotos.add(photoModel);
        }
        for(int i=0; i<listPhotoPanles.size(); ++i) {
            RestaurantPhotoPanel photoPanel = listPhotoPanles.get(i);
            photoPanel.setPhotoModel(listPhotos.get(i));
        }
    }
}
