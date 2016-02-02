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
import ru.baccasoft.eatster.ui.event.LoginSuccess_Event;
import ru.baccasoft.eatster.ui.util.LoginHelper;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = AdminLoginView.NAME)
public class AdminLoginView extends VerticalLayout implements View, Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(AdminLoginView.class);

    private static final long serialVersionUID = 1L;
    public static final String NAME = "alogin";
    private static final float WIDTH_FIELD = 10f;
    private static final float WIDTH_BUTTON = 5f;

    private TextField user;
    private PasswordField password;
    private Button loginButton;
    private LoginHelper loginHelper;

    @Autowired
    PartnerService partnerService;
    @Autowired
    AppProp appProp;

    public AdminLoginView() {
    }

    @PostConstruct
    void init() {
        loginHelper = new LoginHelper(appProp.getLoginAttemptsInMinute());
        setSizeFull();
        user = new TextField("Администратор:");
        user.setWidth(WIDTH_FIELD,Unit.CM);
        user.setRequired(true);
        //user.setInputPrompt("Имя пользователя");
        password = new PasswordField("Пароль:");
        password.setWidth(WIDTH_FIELD,Unit.CM);
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Войти", this);
        //loginButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
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
            if (!r.isAdmin()) {
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
        //
        fields.addComponent(user);
        fields.addComponent(password);
        //fields.addComponent(loginButton);
        fields.setCaption("Для входа в административную панель пройдите авторизацию:");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();
        loginButton.setWidth(WIDTH_BUTTON,Unit.CM);
        fields.addComponent(loginButton);
        fields.setComponentAlignment(loginButton, Alignment.TOP_CENTER);

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

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (!user.isValid() || !password.isValid()) {
            return;
        }
        if (!loginHelper.isLoginEnabled()) {
            Notification.show(LoginHelper.WARN_MESSAGE, Notification.Type.WARNING_MESSAGE);
            return;
        }
        PartnerModel partnerModel = partnerService.authAdmin(user.getValue(), password.getValue());
        if (partnerModel != null) {
            loginHelper.clearAttempts();
            this.user.setComponentError(null);
            this.password.setComponentError(null);
            // TODO Auto-generated method stub
            Notification.show("Успешно", "Авторизация прошла успешно", Notification.Type.TRAY_NOTIFICATION);
            getUI().fire(new LoginSuccess_Event(partnerModel));
        } else {
            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            String wrongMessage = "Неверные имя администратора или пароль";
            this.password.focus();
            this.user.setComponentError(new UserError(wrongMessage));
            this.password.setComponentError(new UserError(wrongMessage));
            Notification.show(wrongMessage, Notification.Type.HUMANIZED_MESSAGE);
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
