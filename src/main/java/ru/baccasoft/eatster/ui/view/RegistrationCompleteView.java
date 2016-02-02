package ru.baccasoft.eatster.ui.view;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.model.RestaurantModel;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.service.PhotoService;
import ru.baccasoft.eatster.service.RestaurantService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.EatsterPhoneField;
import ru.baccasoft.eatster.ui.component.RestaurantInformationPanel;
import ru.baccasoft.eatster.ui.event.LoginSuccess_Event;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = RegistrationCompleteView.NAME)
public class RegistrationCompleteView extends VerticalLayout
        implements
        View,
        Property.ValueChangeListener,
        Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(RegistrationCompleteView.class);
    private static final long serialVersionUID = 1L;
    public static final String NAME = "regcomplete";

    private RestaurantInformationPanel informationPanel;
    private final CheckBox agreeCheckbox = new CheckBox("Я согласен с договором на оказание услуг");
    private Link contractLink;

    private final Button completeButton = new Button("Завершить регистрацию", this);
    private RestaurantModel restaurantModel = null;

    @Autowired
    PartnerService partnerService;
    @Autowired
    PhotoService photoService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    AppProp appProp;

    public RegistrationCompleteView() {
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @PostConstruct
    void init() {
        setSpacing(true);
        setMargin(true);

        VerticalLayout vLayout = new VerticalLayout();
        Label label = new Label("Регистрация");
        label.setStyleName(ValoTheme.LABEL_H1);
        label.setSizeUndefined();
        vLayout.addComponent(label);
        vLayout.setComponentAlignment(label, Alignment.TOP_CENTER);

        informationPanel = new RestaurantInformationPanel(photoService);
        informationPanel.setSizeUndefined();
        vLayout.addComponent(informationPanel);
        vLayout.setComponentAlignment(informationPanel, Alignment.TOP_CENTER);

        //
        ExternalResource contractResource = null;
        String contractUrl = appProp.getContractUrl().trim();
        if (!contractUrl.isEmpty()) {
            contractResource = new ExternalResource(contractUrl);
        }
        contractLink = new Link("Договор", contractResource);
        contractLink.setTargetName("_blank"); //new window
        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(vLayout, agreeCheckbox, contractLink, completeButton);
        viewLayout.setSpacing(true);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(agreeCheckbox, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(contractLink, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(completeButton, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        addComponent(viewLayout);
        //
        agreeCheckbox.setImmediate(true);
        agreeCheckbox.addValueChangeListener(this);
        agreeCheckbox.setRequired(true);
        //
        informationPanel.setLogoVisible(false);
        informationPanel.setLegalDataVisible(false);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        LOG.debug("enter");
        if (!getUI().isPartnerApp()) {
            getUI().fire(new SwitchView_Event(PartnerLoginView.NAME));
            LOG.debug("Fail. Not partner app. Redirect to {0}", PartnerLoginView.NAME);
            return;
        }
        agreeCheckbox.setValue(false);
        completeButton.setEnabled(false);
        informationPanel.refreshComboBox();
        restaurantModel = new RestaurantModel();
        restaurantModel.setWorkdayMonday(true);
        restaurantModel.setWorkdayTuesday(true);
        restaurantModel.setWorkdayWednesday(true);
        restaurantModel.setWorkdayThursday(true);
        restaurantModel.setWorkdayFriday(true);
        restaurantModel.setWorkdaySaturday(true);
        restaurantModel.setWorkdaySunday(true);
        restaurantModel.setHolidayStartTime(null);
        restaurantModel.setHolidayEndTime(null);
        restaurantModel.setWorkdayStartTime(null);
        restaurantModel.setWorkdayEndTime(null);
        restaurantModel.setPhone(EatsterPhoneField.DEFAULT_VALUE);
        informationPanel.setRestaurantModel(restaurantModel, false);
        informationPanel.clearSimpleMenus();
        LOG.debug("Ok");
    }

    private void saveInformation() {
        try {
            informationPanel.validate();
        } catch (Validator.EmptyValueException e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            return;
        } catch (Validator.InvalidValueException e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            return;
        }
        try {
            LOG.debug("Commit restaurant. Before data={0}", restaurantModel);
            informationPanel.commit();
            //возможно ресторан с таким именем уже есть в БД. 
            if(restaurantService.getItem(restaurantModel.getName()) != null) {
                Notification.show("Внимание! Заведение с названием '"+restaurantModel.getName()+"' уже существует.", Notification.Type.WARNING_MESSAGE);
                return;
            }
            PartnerModel partnerModel = getUI().getPartnerModel();
            restaurantModel.setPartnerId(partnerModel.getId());
            restaurantModel.setStatus(RestaurantModel.STAT_UNAUTH);
            restaurantService.registrationItem(partnerModel.getName(), restaurantModel);
            String msg = String.format("'%s' ЗАРЕГИСТРИРОВАН.", restaurantModel.getName());
            Notification.show(msg, Notification.Type.HUMANIZED_MESSAGE);
            getUI().fire(new LoginSuccess_Event(partnerModel));
        } catch (FieldGroup.CommitException e) {
            LOG.error("Error on commit restaurant: " + e.getMessage());
            Notification.show("Ошибка в данных", Notification.Type.ERROR_MESSAGE);
        } catch(Exception e) {
            LOG.error("Error on save: " + e.getMessage());
            Notification.show("Ошибка при регистрации", Notification.Type.ERROR_MESSAGE);
        }
    }

    //private void registration
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == completeButton) {
            saveInformation();
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        boolean checked = agreeCheckbox.getValue();
        completeButton.setEnabled(checked);
    }

}
