package ru.baccasoft.eatster.ui.view;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.EatsterPhoneField;
import ru.baccasoft.eatster.ui.window.ThanksForSignWindow;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = RegistrationStartView.NAME)
public class RegistrationStartView extends VerticalLayout
        implements
        View,
        Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(RegistrationStartView.class);
    public static final String NAME = "regstart";

    private static final float WIDTH_LABEL = 4f;
    private static final float WIDTH_FIELD = 15;
    private static final float WIDTH_BUTTON = 5f;
    private static final long serialVersionUID = -3696143312458430796L;

    private class PartnerFields extends Object {

        TextField name = new TextField();
        TextField contactEmail = new TextField();
        TextField contactName = new TextField();
        TextField contactPost = new TextField();
        EatsterPhoneField contactPhone = new EatsterPhoneField();
        TextField contactNote = new TextField();

        public PartnerFields() {
            name.setWidth(WIDTH_FIELD, Unit.CM);
            contactName.setWidth(WIDTH_FIELD, Unit.CM);
            contactPost.setWidth(WIDTH_FIELD, Unit.CM);
            contactEmail.setWidth(WIDTH_FIELD, Unit.CM);
            contactNote.setWidth(WIDTH_FIELD, Unit.CM);
            contactPhone.setWidth(WIDTH_FIELD, Unit.CM);
        }
    }

    private final PartnerFields fields = new PartnerFields();
    private PartnerModel partnerModel;
    private BeanFieldGroup<PartnerModel> fieldsBindings;
    private final Button nextButton = new Button("Далее", this);
    private ThanksForSignWindow thanksForSignWindow = null;

    @Autowired
    PartnerService partnerService;
    @Autowired
    RegistrationLoginView partnerRegLoginView;

    public RegistrationStartView() {
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @PostConstruct
    void init() {
        setSpacing(true);
        setMargin(true);

        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setSpacing(true);
        fieldsLayout.setMargin(new MarginInfo(true, true, true, false));
        fieldsLayout.setSizeUndefined();

        VerticalLayout vLayout = new VerticalLayout();
        Label label = new Label("Регистрация");
        label.setStyleName(ValoTheme.LABEL_H1);
        label.setSizeUndefined();
        vLayout.addComponent(label);
        vLayout.setComponentAlignment(label, Alignment.TOP_CENTER);

        Label label1 = new Label("Введите, пожалуйста, свои контактные данные и e-mail на который будет выслан пароль для входа в систему.");
        Label label2 = new Label("Этот e-mail будет использоваться для входа в систему.");
        label1.setSizeUndefined();
        label2.setSizeUndefined();
        vLayout.addComponent(label1);
        vLayout.addComponent(label2);
        vLayout.setComponentAlignment(label1, Alignment.TOP_CENTER);
        vLayout.setComponentAlignment(label2, Alignment.TOP_CENTER);

        GridLayout grid1 = new GridLayout(2, 1);
        grid1.setSpacing(true);
        grid1.setSizeUndefined();
        grid1.setMargin(true);
        addToGrid(grid1, 0, fields.name, "Основной e-mail");
        //
        Panel panel1 = new Panel();
        panel1.setContent(grid1);
        panel1.setSizeUndefined();
        fieldsLayout.addComponent(panel1);

        GridLayout grid2 = new GridLayout(2, 5);
        grid2.setSpacing(true);
        grid2.setSizeUndefined();
        grid2.setMargin(true);
        addToGrid(grid2, 0, fields.contactName, "ФИО");
        addToGrid(grid2, 1, fields.contactPost, "Должность");
        addToGrid(grid2, 2, fields.contactPhone, "Моб. телефон");
        addToGrid(grid2, 3, fields.contactEmail, "Доп. почта");
        addToGrid(grid2, 4, fields.contactNote, "Комментарий");
        //
        Panel panel = new Panel();
        panel.setCaption("Контактные данные");
        panel.setContent(grid2);
        panel.setSizeUndefined();
        fieldsLayout.addComponent(panel);
        //
        fieldsLayout.addComponent(nextButton);
        nextButton.setWidth(WIDTH_BUTTON, Unit.CM);
        fieldsLayout.setComponentAlignment(nextButton, Alignment.TOP_CENTER);

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(vLayout, fieldsLayout);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fieldsLayout, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        addComponent(viewLayout);
        //
        fields.name.focus();
        //
        setPartnerModel(new PartnerModel());
        //
        fields.name.setRequired(true);
        fields.name.setRequiredError("Не заполнено поле Основной e-mail");
        fields.name.setValidationVisible(false);
        fields.contactName.setRequired(true);
        fields.contactName.setRequiredError("Не заполнено поле ФИО");
        fields.contactName.setValidationVisible(false);
        fields.contactPhone.setRequired(true);
        fields.contactPhone.setRequiredError("Не заполнено поле Моб. телефон");
        fields.contactPhone.setValue(EatsterPhoneField.DEFAULT_VALUE);
        fields.contactPhone.setValidationVisible(false);
    }

    public void validate() {
        fields.name.validate();
        fields.contactName.validate();
        fields.contactPhone.validate();
    }

    private void addToGrid(GridLayout grid, int row, Component component, String caption) {
        Label lab = new Label(caption);
        lab.setWidth(WIDTH_LABEL, Unit.CM);
        grid.addComponent(lab, 0, row);
        grid.setComponentAlignment(lab, Alignment.TOP_LEFT);
        grid.addComponent(component, 1, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        partnerFields.name.setValue("yurisolomin@mail.ru");
        LOG.debug("enter");
        if (!getUI().isPartnerApp()) {
            getUI().fire(new SwitchView_Event(PartnerLoginView.NAME));
            LOG.debug("Fail. Not partner app. Redirect to {0}", PartnerLoginView.NAME);
            return;
        }
        LOG.debug("Ok");
    }

    private void setPartnerModel(PartnerModel partnerModel) {
        this.partnerModel = partnerModel;
        fieldsBindings = BeanFieldGroup.bindFieldsBuffered(this.partnerModel, fields);
    }

    private void setComponentError(TextField field, String message) {
        field.setComponentError(new UserError(message));
        Notification.show(message, Notification.Type.WARNING_MESSAGE);
    }

    private void clearAllComponentError() {
        fields.name.setComponentError(null);
        fields.contactEmail.setComponentError(null);
        fields.contactName.setComponentError(null);
        fields.contactPost.setComponentError(null);
        fields.contactPhone.setComponentError(null);
        fields.contactNote.setComponentError(null);
    }

    private void registration() {
        LOG.debug("registration:");
        try {
            validate();
        } catch (Validator.EmptyValueException e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            return;
        } catch (Validator.InvalidValueException e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            return;
        }
        try {
            fieldsBindings.commit();
        } catch (FieldGroup.CommitException e) {
            LOG.debug("Error on commit: " + e.getMessage());
            Notification.show("Ошибка в данных: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }
        clearAllComponentError();
        String name = partnerModel.getName();
        if (name.isEmpty()) {
            LOG.debug("Fail. email is empty");
            setComponentError(fields.name, "Заполните e-mail");
            return;
        }
        if (!name.contains("@") || name.length() < 4 || name.replace("@", "").contains("@")) {
            LOG.debug("Fail. Bad email");
            setComponentError(fields.name, "Некорректный e-mail");
            return;
        }
        PartnerModel partnerAlreadyExists = partnerService.getPartner(name);
        if (partnerAlreadyExists != null) {
            LOG.debug("Fail. Partner '{0}' already exists", name);
            setComponentError(fields.name, "Данный e-mail уже зарегистрирован.");
            return;
        }
        try {
            long insertedPartnerId = partnerService.registrationPartner(partnerModel);
            LOG.debug("registration partnerId={0}", insertedPartnerId);
            PartnerModel partnerInserted = null;
            if (insertedPartnerId != 0) {
                partnerInserted = partnerService.getItem(insertedPartnerId);
            }
            if (partnerInserted == null) {
                LOG.debug("Partner not exists by partnerId={0}", insertedPartnerId);
                Notification.show("Ошибка при регистрации. Обратитесь к администратору.", Notification.Type.ERROR_MESSAGE);
                return;
            }
            setPartnerModel(partnerInserted);
            LOG.debug("Ok.");
            thanksForSignWindow = new ThanksForSignWindow(this);
            getUI().addWindow(thanksForSignWindow);
        } catch (Exception e) {
            LOG.error("Fail. Error on registration: " + e.getMessage());
            Notification.show("Ошибка при регистрации. Обратитесь к администратору.", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == nextButton) {
            registration();
        }
        if (thanksForSignWindow != null) {
            if (event.getButton() == thanksForSignWindow.getButtonOk()) {
                thanksForSignWindow.close();
                thanksForSignWindow = null;
                partnerRegLoginView.setEmail(fields.name.getValue());
                getUI().fire(new SwitchView_Event(RegistrationLoginView.NAME));
            }
        }
    }

}
