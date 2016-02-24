package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.service.MailService;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.Logout_Event;
import ru.baccasoft.utils.logging.Logger;

public class MainMenuItemRestaurant extends MainMenuItemLayout implements 
        Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(MainMenuItemRestaurant.class);
    private static final long serialVersionUID = -7232693943423592910L;

    private RestaurantHeaderPanel headerPanel;
    private RestaurantInformationPanel informationPanel;
    private RestaurantPartnerPanel partnerPanel;
    private RestaurantActionsPanel actionsPanel;
    private RestaurantPhotosPanel photosPanel;
    private RestaurantOperTabSheetPanel operationsPanel;
    private RestaurantWaitersPanel waitersPanel;
    private final Button saveInformationButton = new Button("Сохранить", this);
    private final Button savePartnerButton = new Button("Сохранить", this);
    private final Button createActionButton = new Button("Создать акцию", this);
    private RestaurantModel restaurantModel;
    private PartnerModel partnerModel;
    
    public MainMenuItemRestaurant() {
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
        informationPanel = new RestaurantInformationPanel();
        restaurantLayout.addComponent(informationPanel);
        //
        actionsPanel = new RestaurantActionsPanel();
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
        photosPanel = new RestaurantPhotosPanel();
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
            actionsPanel.addNewActionPanel();
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
            catch(Validator.InvalidValueException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
                return;
            }
            LOG.debug("Commit restaurant. Before data={0}",restaurantModel);
            informationPanel.commit();
            headerPanel.commit();
            /*в новом ТЗ этого нет но Коротков просил добавить*/
            //проверим не поменялся ли статус заведения
            //если статус поменялся на Active то отправим уведомление партнеру
            RestaurantService restaurantService = getUI().getRestaurantService();
            RestaurantModel restaurantModelOld = restaurantService.getItem(restaurantModel.getId());
            if (restaurantModel.getStatus().equals(RestaurantModel.STAT_ACTIVE)
                && !restaurantModelOld.getStatus().equals(restaurantModel.getStatus())) {
                //String newStatusRus = headerPanel.getStatusRus();
                String partnerEMail = partnerModel.getName();
                MailService mailService = getUI().getMailService();
//                if (!mailService.send(partnerEMail,"Системное уведомление от Eatster","Статус Вашего заведения изменен на \""+newStatusRus+"\"")) {
                if (!mailService.sendRestaurantActivated(partnerEMail)) {
                    LOG.debug("Fail. Mail not sent.");
                    throw new RuntimeException("Error send e-mail on restaurant status activated!");
                }
            }

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
            PartnerService partnerService = getUI().getPartnerService();
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
        PartnerService partnerService = getUI().getPartnerService();
        partnerModel = partnerService.getItem(restaurantModel.getPartnerId());
        partnerPanel.bindFieldsBuffered(partnerModel);
        //
        actionsPanel.setRestaurantId(restaurantModel.getId());
        operationsPanel.setRestaurantId(restaurantModel.getId());
        waitersPanel.setRestaurantId(restaurantModel.getId());
        photosPanel.setRestaurantId(restaurantModel.getId());
        //
        saveInformationButton.setVisible(false);
        createActionButton.setVisible(false);
        savePartnerButton.setVisible(false);
        headerPanel.setStatusEnabled(false);
        //
        boolean isAdmin = getUI().getPartnerModel().isAdmin();
        if (informationPanel.isVisible()) {
            headerPanel.setStatusEnabled(isAdmin);
            headerPanel.setButtonsEnabled(false,true,true,true,true,true);
            saveInformationButton.setVisible(true);
        }
        if (actionsPanel.isVisible()) {
            headerPanel.setButtonsEnabled(true,false,true,true,true,true);
            createActionButton.setVisible(true);
            actionsPanel.refresh();
        }
        if (photosPanel.isVisible()) {
            headerPanel.setButtonsEnabled(true,true,true,true,true,false);
            photosPanel.refresh();
        }
        if (partnerPanel.isVisible()) {
            headerPanel.setButtonsEnabled(true,true,false,true,true,true);
            savePartnerButton.setVisible(true);
        }
        if (operationsPanel.isVisible()) {
            headerPanel.setButtonsEnabled(true,true,true,false,true,true);
            operationsPanel.refresh();
        }
        if (waitersPanel.isVisible()) {
            headerPanel.setButtonsEnabled(true,true,true,true,false,true);
            waitersPanel.refresh();
        }
        // При статусе «Авторизирован по e-mail:» Заведению доступна только страница «информация»
        if (!isAdmin && restaurantModel.getStatus().equals(RestaurantModel.STAT_UNAUTH)) {
            headerPanel.setButtonsEnabled(false,false,false,false,false,false);
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
    
    @Override
    public void doRefresh() {
        //защита от неизвестного
        if (restaurantModel == null) {
            return;
        }
        long restaurantId = restaurantModel.getId();
        //для нового ресторана не обновляем
        if (restaurantId == 0) {
            return;
        }
        RestaurantService restaurantService = getUI().getRestaurantService();
        restaurantModel = restaurantService.getItem(restaurantId);
        //неожиданно, но возможно
        if (restaurantModel == null) {
           getUI().fire(new Logout_Event());
           return;
        }
        actionsPanel.clear();
        photosPanel.clear();
        operationsPanel.clear();
        waitersPanel.clear();
        refreshRestaurantPanel();
    }

}
