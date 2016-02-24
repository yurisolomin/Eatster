package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import java.util.ArrayList;
import java.util.List;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.ui.AppUI;

public class RestaurantPhotosPanel extends VerticalLayout {

    private static final long serialVersionUID = -7766042891106128750L;
    private final VerticalLayout layoutPhotos = new VerticalLayout();
    private static final String CAPTION_PHOTO_REST = "Фото ресторана";

    private final RestaurantPhotoPanel backPhotoPanel = new RestaurantPhotoPanel("Фоновая картинка");
    private final List<RestaurantPhotoPanel> listPhotoPanles = new ArrayList();
    //количество пар фотографий
    private static final int MAX_PHOTO_PAIRS = 3; //*2 
    private long restaurantId = 0;

    public RestaurantPhotosPanel() {
        buildLayout();
    }

    private void buildLayout() {
        setSpacing(true);
        addComponent(new Label("В случае изменения фотографии она автоматически попадет на модерацию"));
        HorizontalLayout layout = new HorizontalLayout(backPhotoPanel);
        layout.setSpacing(true);
        addComponent(layout);
        layoutPhotos.setSpacing(true);
        addComponent(layoutPhotos);
        for(int i=0,index=0; i<MAX_PHOTO_PAIRS; ++i) {
            ++index;
            RestaurantPhotoPanel photoPanelL = new RestaurantPhotoPanel(CAPTION_PHOTO_REST+" "+index);
            ++index;
            RestaurantPhotoPanel photoPanelR = new RestaurantPhotoPanel(CAPTION_PHOTO_REST+" "+index);
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
        //фон
        PhotoService photoService = getUI().getPhotoService();
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
            //int index = i + 1;
            //photoPanel.setAlternateText("фото ресторана "+index);
        }
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    public void clear() {
    }
}
