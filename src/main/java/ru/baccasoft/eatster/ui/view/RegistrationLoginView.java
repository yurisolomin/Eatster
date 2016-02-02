package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.LoginSuccess_Event;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = RegistrationLoginView.NAME)
public class RegistrationLoginView extends VerticalLayout 
        implements 
        View, 
        Button.ClickListener
        {

    private static final Logger LOG = Logger.getLogger(RegistrationLoginView.class);
    private static final long serialVersionUID = 1L;
    public static final String NAME = "reglogin";

//    private static final int GEN_PASSWORD_LENGTH = 8;
    private static final float WIDTH_FIELD = 15;
    private static final float WIDTH_BUTTON = 5f;

    private final TextField name = new TextField("e-mail");
    private final PasswordField password = new PasswordField("Пароль");
    private final Button authButton = new Button("Войти", this);

    @Autowired
    PartnerService partnerService;
    
    public RegistrationLoginView() {
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

        Label label1 = new Label("Для входа в систему введите e-mail указанный на этапе регистрации и присланный пароль.");
        label1.setSizeUndefined();
        vLayout.addComponent(label1);
        vLayout.setComponentAlignment(label1, Alignment.TOP_CENTER);

        FormLayout form = new FormLayout(name,password,authButton);
        form.setSizeUndefined();
        //
        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(vLayout, form);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        addComponent(viewLayout);
        //
        name.setWidth(WIDTH_FIELD,Unit.CM);
        password.setWidth(WIDTH_FIELD,Unit.CM);
//        authButton.setWidth(WIDTH_BUTTON,Unit.CM);
        authButton.setWidth(WIDTH_FIELD,Unit.CM);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        LOG.debug("enter");
        if (!getUI().isPartnerApp()) {
            getUI().fire(new SwitchView_Event(PartnerLoginView.NAME));
            LOG.debug("Fail. Not partner app. Redirect to {0}", PartnerLoginView.NAME);
            return;
        }
        if (!name.getValue().trim().isEmpty()) {
            password.focus();
        }
        LOG.debug("Ok");
    }

    public void auth() {
        LOG.debug("Login partner={0},password={1}",name.getValue(),password.getValue());
        if (!name.isValid() || !password.isValid()) {
            LOG.debug("Fail. Login or password invalid");
            return;
        }
        PartnerModel partnerModel = partnerService.authPartner(name.getValue(), password.getValue());
        LOG.debug("partner={0}",partnerModel);
        if (partnerModel != null) {
            this.name.setComponentError(null);
            this.password.setComponentError(null);
            // TODO Auto-generated method stub
            Notification.show("Успешно", "Авторизация прошла успешно", Notification.Type.TRAY_NOTIFICATION);
            getUI().fire(new LoginSuccess_Event(partnerModel));
            LOG.debug("Ok.");
        } else {
            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            String wrongMessage = "Логин или пароль введены не правильно, проверьте данные и попробуйте снова.";
            this.password.focus();
            this.name.setComponentError(new UserError(wrongMessage));
            this.password.setComponentError(new UserError(wrongMessage));
            Notification.show(wrongMessage, Notification.Type.HUMANIZED_MESSAGE);
            LOG.debug("Fail. Bad login or password");
        }
    }

    //private void registration
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == authButton) {
            auth();
        }
    }

    public void setEmail(String email) {
        name.setValue(email);
    }
}
