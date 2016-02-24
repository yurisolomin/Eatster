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

@UIScope
@SpringView(name = AdminLoginView.NAME)
public class AdminLoginView extends VerticalLayout implements View, Button.ClickListener {
    private static final long serialVersionUID = -313351789295951193L;
    public static final String NAME = "alogin";
    private static final float WIDTH_FIELD = 10f;
    private static final float WIDTH_BUTTON = 5f;

    private TextField adminUser;
    private PasswordField adminPassword;
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
        adminUser = new TextField("Администратор:");
        adminUser.setWidth(WIDTH_FIELD,Unit.CM);
        adminUser.setRequired(true);
        //user.setInputPrompt("Имя пользователя");
        adminPassword = new PasswordField("Пароль:");
        adminPassword.setWidth(WIDTH_FIELD,Unit.CM);
        adminPassword.addValidator(new PasswordValidator());
        adminPassword.setRequired(true);
        adminPassword.setNullRepresentation("");

        // Create login button
        loginButton = new Button("Войти", this);
        //loginButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        VerticalLayout fields = new VerticalLayout();
        //покажем три патнера для отладки--------------------------
        String defaultLogin = "";
        String defaultPassword = "";
/*        
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
*/
        adminUser.setValue(defaultLogin);
        adminPassword.setValue(defaultPassword);
        //
        fields.addComponent(adminUser);
        fields.addComponent(adminPassword);
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
        if (!adminUser.isValid() || !adminPassword.isValid()) {
            return;
        }
        if (!loginHelper.isLoginEnabled()) {
            Notification.show(LoginHelper.WARN_MESSAGE, Notification.Type.WARNING_MESSAGE);
            return;
        }
        PartnerModel partnerModel = partnerService.authAdmin(adminUser.getValue(), adminPassword.getValue());
        if (partnerModel != null) {
            loginHelper.clearAttempts();
            this.adminUser.setComponentError(null);
            this.adminPassword.setComponentError(null);
            // TODO Auto-generated method stub
            Notification.show("Успешно", "Авторизация прошла успешно", Notification.Type.TRAY_NOTIFICATION);
            getUI().fire(new LoginSuccess_Event(partnerModel));
        } else {
            // Wrong password clear the password field and refocuses it
            this.adminPassword.setValue(null);
            String wrongMessage = "Неверные имя администратора или пароль";
            this.adminPassword.focus();
            this.adminUser.setComponentError(new UserError(wrongMessage));
            this.adminPassword.setComponentError(new UserError(wrongMessage));
            Notification.show(wrongMessage, Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private static final class PasswordValidator extends AbstractValidator<String> {

        private static final long serialVersionUID = 7084788179429323671L;

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
