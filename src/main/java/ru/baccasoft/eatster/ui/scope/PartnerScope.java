package ru.baccasoft.eatster.ui.scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.image.ImageValidatorActionPhoto;
import ru.baccasoft.eatster.image.ImageValidatorRestBackground;
import ru.baccasoft.eatster.image.ImageValidatorRestLogo;
import ru.baccasoft.eatster.image.ImageValidatorRestPhoto;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.ActionSubTypeModel;
import ru.baccasoft.eatster.model.ActionTypeModel;
import ru.baccasoft.eatster.model.AverageCheckModel;
import ru.baccasoft.eatster.model.CommonNamedModel;
import ru.baccasoft.eatster.model.CuisineModel;
import ru.baccasoft.eatster.model.ParkingModel;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.model.SubwayModel;
import ru.baccasoft.eatster.service.ActionSubTypeService;
import ru.baccasoft.eatster.service.ActionTypeService;
import ru.baccasoft.eatster.service.AverageCheckService;
import ru.baccasoft.eatster.service.CuisineService;
import ru.baccasoft.eatster.service.ParkingService;
import ru.baccasoft.eatster.service.SubwayService;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringComponent
public class PartnerScope {
    private static final Logger LOG = Logger.getLogger(PartnerScope.class);

    @Autowired
    CuisineService cuisineService;
    @Autowired
    SubwayService subwayService;
    @Autowired
    ParkingService parkingService;
    @Autowired
    AverageCheckService averageCheckService;
    @Autowired
    ActionTypeService actionTypeService;
    @Autowired
    ActionSubTypeService actionSubTypeService;
    @Autowired
    AppProp appProp;

    private List<SubwayModel> listSubway = new ArrayList();
    private List<CuisineModel> listCuisine = new ArrayList();
    private List<ParkingModel> listParking = new ArrayList();
    private List<AverageCheckModel> listAverageCheck = new ArrayList();
    private List<ActionTypeModel> listActionType = new ArrayList();
    private List<ActionSubTypeModel> listActionSubType = new ArrayList();
    private List<RestaurantModel> listRestaurant = new ArrayList();
    private ImageValidatorActionPhoto imageValidatorActionPhoto;
    private ImageValidatorRestBackground imageValidatorRestBackground;
    private ImageValidatorRestLogo imageValidatorRestLogo;
    private ImageValidatorRestPhoto imageValidatorRestPhoto;
    
    public PartnerScope() {
    }

    public void init(List<RestaurantModel> listRestaurant) {
        this.listRestaurant = listRestaurant;
        listCuisine = cuisineService.findAll();
        listSubway = subwayService.findAll();
        listParking = parkingService.findAll();
        listAverageCheck = averageCheckService.findAll();
        listActionType = actionTypeService.findAll();
        listActionSubType = actionSubTypeService.findAll();
        imageValidatorActionPhoto = new ImageValidatorActionPhoto(appProp);
        imageValidatorRestBackground = new ImageValidatorRestBackground(appProp);
        imageValidatorRestLogo = new ImageValidatorRestLogo(appProp);
        imageValidatorRestPhoto = new ImageValidatorRestPhoto(appProp);
    }
    
    private void comboboxAddItem(ComboBox comboBox, CommonNamedModel item) {
        String itemName = item.getName();
        long itemId = item.getId();
        comboBox.addItem(itemId);
        comboBox.setItemCaption(itemId, itemName);
    }

    
    public void comboboxForCuisine(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listCuisine) {
            comboboxAddItem(comboBox, item);
        }
    }
    
    public void comboboxForSubway(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listSubway) {
            comboboxAddItem(comboBox, item);
        }
    }
    
    public void comboboxForParking(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listParking) {
            comboboxAddItem(comboBox, item);
        }
    }

    public void comboboxForAverageCheck(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listAverageCheck) {
            comboboxAddItem(comboBox, item);
        }
    }

    public void comboboxForAvailable(ComboBox comboBox) {
        comboBox.removeAllItems();
        comboBox.addItem(true);
        comboBox.setItemCaption(true, "Есть");
        comboBox.addItem(false);
        comboBox.setItemCaption(false, "Нет");
    }

    public void comboboxForTime(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (int h = 0; h <= 23; ++h) {
            comboBox.addItem(String.format("%02d:00", h));
            comboBox.addItem(String.format("%02d:30", h));
        }
    }

    public void comboboxForActionStatus(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (ActionModel.ActionStatus item : ActionModel.getStatusList()) {
            comboBox.addItem(item.getId());
            comboBox.setItemCaption(item.getId(), item.getName());
        }
    }

    public void comboboxForActionType(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listActionType) {
            comboboxAddItem(comboBox, item);
        }
    }

    public ActionSubTypeModel comboboxForActionSubType(ComboBox comboBox, Long actionTypeId) {
        ActionSubTypeModel firstItem = null;
        comboBox.removeAllItems();
        for (ActionSubTypeModel item : listActionSubType) {
            if (item.getActionTypeId() == actionTypeId) {
                comboboxAddItem(comboBox, item);
                if (firstItem == null) {
                    firstItem = item;
                }
            }
        }
        return firstItem;
    }

    public ActionSubTypeModel comboboxFindActionSubType(long actionTypeId, String nameSubType) {
        nameSubType = nameSubType.toLowerCase();
        for (ActionSubTypeModel item : listActionSubType) {
            if (item.getActionTypeId() == actionTypeId) {
                if (item.getName().toLowerCase().equals(nameSubType)) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public void comboboxForPhotoStatus(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (PhotoModel.PhotoStatus item : PhotoModel.getStatusList()) {
            comboBox.addItem(item.getId());
            comboBox.setItemCaption(item.getId(), item.getName());
        }
    }
    
    public void comboboxForRestaurantStatus(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (RestaurantModel.RestaurantStatus item : RestaurantModel.getStatusList()) {
            comboBox.addItem(item.getId());
            comboBox.setItemCaption(item.getId(), item.getName());
        }
    }
    
/*
    public void comboboxForRestaurant(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (CommonNamedModel item : listRestaurant) {
            comboboxAddItem(comboBox, item);
        }
    }
*/
    public void comboboxForRestaurant(ComboBox comboBox) {
        comboBox.removeAllItems();
        for (RestaurantModel item : listRestaurant) {
            String itemName = item.getName()+" ("+item.getSubwayName()+")";
            long itemId = item.getId();
            comboBox.addItem(itemId);
            comboBox.setItemCaption(itemId, itemName);
        }
    }

    public ImageValidatorActionPhoto getImageValidatorActionPhoto() {
        return imageValidatorActionPhoto;
    }

    public ImageValidatorRestBackground getImageValidatorRestBackground() {
        return imageValidatorRestBackground;
    }

    public ImageValidatorRestLogo getImageValidatorRestLogo() {
        return imageValidatorRestLogo;
    }

    public ImageValidatorRestPhoto getImageValidatorRestPhoto() {
        return imageValidatorRestPhoto;
    }
    
}
