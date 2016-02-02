package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.ActionService;
import ru.baccasoft.eatster.service.MailService;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.OperationTotalService;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantPanel extends VerticalLayout implements Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(RestaurantPanel.class);
    private static final long serialVersionUID = 1L;

    private RestaurantHeaderPanel headerPanel;
    private RestaurantInformationPanel informationPanel;
    private RestaurantPartnerPanel partnerPanel;
    private RestaurantActionsPanel actionsPanel;
    private RestaurantPhotosPanel photosPanel;
//    private RestaurantOperationsPanel operationsPanel;
    private RestaurantOperTabSheetPanel operationsPanel;
    private RestaurantWaitersPanel waitersPanel;
    private final Button saveInformationButton = new Button("Сохранить", this);
    private final Button savePartnerButton = new Button("Сохранить", this);
    private final Button createActionButton = new Button("Создать акцию", this);
//    private boolean showAllOperations = false;
    private RestaurantModel restaurantModel;
    private PartnerModel partnerModel;
    
    private final PhotoService photoService;
    private final ActionService actionService;
    private final RestaurantService restaurantService;
    private final PartnerService partnerService;
    private final OperationTotalService operationTotalService;
    private final OperationService operationService;
    private final WaiterService waiterService;

    public RestaurantPanel(PhotoService photoService, ActionService actionService, RestaurantService restaurantService, PartnerService partnerService, OperationTotalService operationTotalService, OperationService operationService, WaiterService waiterService) {
        this.photoService = photoService;
        this.actionService = actionService;
        this.restaurantService = restaurantService;
        this.partnerService = partnerService;
        this.operationTotalService = operationTotalService;
        this.operationService = operationService;
        this.waiterService = waiterService;
        super.addComponent(buildLayout());
    }
    
    private Layout buildLayout() {
        VerticalLayout restaurantLayout = new VerticalLayout();
        headerPanel = new RestaurantHeaderPanel();
        restaurantLayout.addComponent(headerPanel);
        restaurantLayout.addComponent(saveInformationButton);
        restaurantLayout.addComponent(createActionButton);
        restaurantLayout.addComponent(savePartnerButton);
        //
        informationPanel = new RestaurantInformationPanel(photoService);
        restaurantLayout.addComponent(informationPanel);
        //
        actionsPanel = new RestaurantActionsPanel(actionService);
        restaurantLayout.addComponent(actionsPanel);
        //
        partnerPanel = new RestaurantPartnerPanel();
        restaurantLayout.addComponent(partnerPanel);
        //
//        operationsPanel = new RestaurantOperationsPanel();
        operationsPanel = new RestaurantOperTabSheetPanel();
        restaurantLayout.addComponent(operationsPanel);
        //
        waitersPanel = new RestaurantWaitersPanel();
        restaurantLayout.addComponent(waitersPanel);
        //
        photosPanel = new RestaurantPhotosPanel(photoService);
        restaurantLayout.addComponent(photosPanel);
        //
        informationPanel.setVisible(true);
        actionsPanel.setVisible(false);
        photosPanel.setVisible(false);
        partnerPanel.setVisible(false);
        operationsPanel.setVisible(false);
        waitersPanel.setVisible(false);
        headerPanel.setButtonsEnabled(false,false,false,false,false,false);
        saveInformationButton.setVisible(false);
        createActionButton.setVisible(false);
        savePartnerButton.setVisible(false);
        return restaurantLayout;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == saveInformationButton) {
            saveInformation();
        }
        if (event.getButton() == createActionButton) {
            if (actionsPanel.findNewActionPanel() != null) {
                Notification.show("ВЫ НЕ СОХРАНИЛИ НОВУЮ АКЦИЮ", Notification.Type.HUMANIZED_MESSAGE);
                return;
            }
            actionsPanel.createActionPanel();
            Notification.show("НОВАЯ АКЦИЯ", Notification.Type.HUMANIZED_MESSAGE);
        }
        if (event.getButton() == savePartnerButton) {
            savePartner();
        }
    }
    
    private void saveInformation() {
        try {
            try {
                informationPanel.validate();
            } 
            catch(Validator.EmptyValueException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }
            catch(Validator.InvalidValueException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }
            LOG.debug("Commit restaurant. Before data={0}",restaurantModel);
            informationPanel.commit();
            headerPanel.commit();
/*в новом ТЗ этого нет            
            //проверим не поменялся ли статус заведения
            RestaurantModel restaurantModelOld = restaurantService.getItem(restaurantModel.getId());
            if (!restaurantModelOld.getStatus().equals(restaurantModel.getStatus())) {
                String newStatusRus = headerPanel.getStatusRus();
                String partnerEMail = partnerModel.getName();
                MailService mailService = getUI().getMailService();
                if (!mailService.send(partnerEMail,"Системное уведомление от Eatster","Статус Вашего заведения изменен на \""+newStatusRus+"\"")) {
                    LOG.debug("Fail. Mail not sent.");
                    throw new RuntimeException("Error on restaurant registration!");
                }
            }
*/
            restaurantService.updateItem(restaurantModel);
            String msg = String.format("'%s' сохранен.",restaurantModel.getName());
            Notification.show(msg, Notification.Type.HUMANIZED_MESSAGE);
            refreshRestaurantPanel();
        } catch (FieldGroup.CommitException e) {
            LOG.error("Error on commit restaurant: "+e.getMessage());
            Notification.show("Ошибка в данных", Notification.Type.ERROR_MESSAGE);
        } catch(RuntimeException e) {
            LOG.error("Error on save: " + e.getMessage());
            Notification.show("Ошибка при сохранении данных!", Notification.Type.ERROR_MESSAGE);
        }
    }
    
    
    private void savePartner() {
            try {
                if (!partnerPanel.validate()) {
                    return;
                }
            } 
            catch(Validator.EmptyValueException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }
            catch(Validator.InvalidValueException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }
        
        try {
            LOG.debug("Commit partner. Before data={0}",partnerModel);
            partnerPanel.commit();
            partnerService.updateItem(partnerModel);
            String msg = String.format("'%s' сохранен.",partnerModel.getName());
            Notification.show(msg, Notification.Type.HUMANIZED_MESSAGE);
            refreshRestaurantPanel();
        } catch (FieldGroup.CommitException e) {
            LOG.error("Error on commit partner: "+e.getMessage());
            Notification.show("Ошибка в данных: "+e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
    
public void refreshRestaurantPanel() {
        boolean readonlyInformation = !getUI().isAdminApp();
        informationPanel.setRestaurantModel(restaurantModel,readonlyInformation);
        headerPanel.bindFieldsBuffered(restaurantModel);
        //
        partnerModel = partnerService.getItem(restaurantModel.getPartnerId());
        partnerPanel.bindFieldsBuffered(partnerModel);
        operationsPanel.setRestaurantId(restaurantModel.getId());
        //
        List<WaiterModel> waiterList = waiterService.findByRestaurant(restaurantModel.getId());
        waitersPanel.bindFieldsBuffered(waiterList);
        //
        boolean isAdmin = getUI().getPartnerModel().isAdmin();
        if (informationPanel.isVisible()) {
            headerPanel.setStatusEnabled(isAdmin);
            headerPanel.setButtonsEnabled(false,true,true,true,true,true);
            saveInformationButton.setVisible(true);
            createActionButton.setVisible(false);
            savePartnerButton.setVisible(false);
//            createPhotoButton.setVisible(false);
        }
        if (actionsPanel.isVisible()) {
            headerPanel.setStatusEnabled(false);
            headerPanel.setButtonsEnabled(true,false,true,true,true,true);
            saveInformationButton.setVisible(false);
            createActionButton.setVisible(true);
            savePartnerButton.setVisible(false);
//            createPhotoButton.setVisible(false);
            actionsPanel.refresh();
        }
        if (photosPanel.isVisible()) {
            headerPanel.setStatusEnabled(false);
            headerPanel.setButtonsEnabled(true,true,true,true,true,false);
            saveInformationButton.setVisible(false);
            createActionButton.setVisible(false);
            savePartnerButton.setVisible(false);
//            createPhotoButton.setVisible(true);
            photosPanel.refresh();
        }
        if (partnerPanel.isVisible()) {
            headerPanel.setStatusEnabled(false);
            headerPanel.setButtonsEnabled(true,true,false,true,true,true);
            saveInformationButton.setVisible(false);
            createActionButton.setVisible(false);
            savePartnerButton.setVisible(true);
//            createPhotoButton.setVisible(false);
        }
        if (operationsPanel.isVisible()) {
            headerPanel.setStatusEnabled(false);
            headerPanel.setButtonsEnabled(true,true,true,false,true,true);
            saveInformationButton.setVisible(false);
            createActionButton.setVisible(false);
            savePartnerButton.setVisible(false);
//            createPhotoButton.setVisible(false);
        }
        if (waitersPanel.isVisible()) {
            headerPanel.setStatusEnabled(false);
            headerPanel.setButtonsEnabled(true,true,true,true,false,true);
            saveInformationButton.setVisible(false);
            createActionButton.setVisible(false);
            savePartnerButton.setVisible(false);
//            createPhotoButton.setVisible(false);
        }
        
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
    
    public void refreshComboBox() {
        informationPanel.refreshComboBox();
        headerPanel.refreshComboBox();
    }

    public RestaurantInformationPanel getInformationPanel() {
        return informationPanel;
    }

    public RestaurantActionsPanel getListActionsPanel() {
        return actionsPanel;
    }

    public RestaurantPhotosPanel getListPhotosPanel() {
        return photosPanel;
    }

    public RestaurantOperTabSheetPanel getOperationPanel() {
        return operationsPanel;
    }

    public RestaurantPartnerPanel getPartnerPanel() {
        return partnerPanel;
    }

    public RestaurantWaitersPanel getWaiterPanel() {
        return waitersPanel;
    }

    
    public void setActivePanel(Component activePanel) {
        informationPanel.setVisible(false);
        actionsPanel.setVisible(false);
        partnerPanel.setVisible(false);
        operationsPanel.setVisible(false);
        waitersPanel.setVisible(false);
        photosPanel.setVisible(false);
        activePanel.setVisible(true);
        refreshRestaurantPanel();
    }
    
    public RestaurantModel getRestaurantModel() {
        return restaurantModel;
    }
    
    public void setRestaurantModel(RestaurantModel restaurantModel) {
        this.restaurantModel = restaurantModel;
    }
    
    public long getSelectedRestaurantId() {
        return restaurantModel.getId();
    }

    public PartnerModel getPartnerModel() {
        return partnerModel;
    }
}
