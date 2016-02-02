package ru.baccasoft.eatster.ui.view;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.service.PartnerService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.window.SupportInfoWindow;
import ru.baccasoft.eatster.ui.event.LoginSuccess_Event;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;
import ru.baccasoft.eatster.ui.util.LoginHelper;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = PartnerLoginView.NAME)
public class PartnerLoginView extends VerticalLayout implements View, Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(PartnerLoginView.class);

    private static final long serialVersionUID = 1L;
    public static final String NAME = "plogin";
    private static final float WIDTH_FIELD = 10f;
    private static final float WIDTH_BUTTON = 5f;

    private TextField user;
    private PasswordField password;
    private final Button authButton = new Button("Войти", this);
    private final Button regButton = new Button("Регистрация", this);
    private final Button restorepassButton = new Button("Восстановить пароль", this);
    private LoginHelper loginHelper;

    @Autowired
    PartnerService partnerService;
    @Autowired
    AppProp appProp;

    public PartnerLoginView() {
    }

    @PostConstruct
    void init() {
        //
        loginHelper = new LoginHelper(appProp.getLoginAttemptsInMinute());
        //
        setSizeFull();
        user = new TextField("Партнер:");
        user.setWidth(WIDTH_FIELD,Unit.CM);
        user.setRequired(true);
        user.setInputPrompt("Логин партнера");
        password = new PasswordField("Пароль:");
        password.setWidth(WIDTH_FIELD,Unit.CM);
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setNullRepresentation("");

        //loginButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        authButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //
        VerticalLayout fields = new VerticalLayout();
        //покажем три патнера для отладки--------------------------
        String defaultLogin = "";
        String defaultPassword = "";
        List<PartnerModel> listPartner = partnerService.findAll();
        if (!appProp.isShowLogins()) {
            listPartner.clear();
        }
        int size = 3;
        for(PartnerModel r:listPartner) {
            if (r.isAdmin()) {
                continue;
            }
            fields.addComponent(new Label(r.getName()+"/"+r.getPassword()));
            if (--size <= 0) {
                break;
            }
            if (defaultLogin.isEmpty()) {
                defaultLogin = r.getName();
                defaultPassword = r.getPassword();
            }
        }
        user.setValue(defaultLogin);
        password.setValue(defaultPassword);
        //---------------------------------------------------------
        fields.addComponent(user);
        fields.addComponent(password);
        
        GridLayout grid = new GridLayout(2,1);
        grid.setSizeUndefined();
        //grid.setSpacing(true);
        grid.addComponent(authButton, 0, 0);
        grid.addComponent(regButton, 1, 0);
        authButton.setWidth(WIDTH_BUTTON,Unit.CM);
        regButton.setWidth(WIDTH_BUTTON,Unit.CM);
        fields.addComponent(grid);
        fields.setComponentAlignment(grid, Alignment.TOP_CENTER);
//        fields.addComponent(loginButton);
//        fields.addComponent(registrationButton);
        restorepassButton.setWidth(WIDTH_BUTTON,Unit.CM);
        fields.addComponent(restorepassButton);
        fields.setComponentAlignment(restorepassButton, Alignment.TOP_CENTER);
        
        fields.setCaption("Для входа в панель управления рестораном, введите свой логин и пароль:");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        addComponent(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void auth() {
        LOG.debug("Login partner={0},password={1}",user.getValue(),password.getValue());
        if (!user.isValid() || !password.isValid()) {
            LOG.debug("Fail. Login or password invalid");
            return;
        }
        if (!loginHelper.isLoginEnabled()) {
            Notification.show(LoginHelper.WARN_MESSAGE, Notification.Type.WARNING_MESSAGE);
            return;
        }
        PartnerModel partnerModel = partnerService.authPartner(user.getValue(), password.getValue());
        LOG.debug("partner={0}",partnerModel);
        if (partnerModel != null) {
            loginHelper.clearAttempts();
            this.user.setComponentError(null);
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
            this.user.setComponentError(new UserError(wrongMessage));
            this.password.setComponentError(new UserError(wrongMessage));
            Notification.show(wrongMessage, Notification.Type.HUMANIZED_MESSAGE);
            LOG.debug("Fail. Bad login or password");
        }
    }
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == authButton) {
            auth();
        }
        if (event.getButton() == regButton) {
            getUI().fire(new SwitchView_Event(RegistrationStartView.NAME));
        }
        if (event.getButton() == restorepassButton) {
            String supportEmail = appProp.getSupportEmail();
            if (supportEmail.isEmpty()) {
                Notification.show("Внимание!", "Нет данных о почтовом адресе службы поддержки", Notification.Type.WARNING_MESSAGE);
            } else {
                getUI().addWindow(new SupportInfoWindow(supportEmail));
            }
        }
        
    }

    private static final class PasswordValidator extends AbstractValidator<String> {

        public PasswordValidator() {
            super("The password provided is not valid");
        }

        @Override
        protected boolean isValidValue(String value) {
            return true;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }
}
