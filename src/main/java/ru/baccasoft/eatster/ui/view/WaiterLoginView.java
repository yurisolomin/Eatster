package ru.baccasoft.eatster.ui.view;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
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
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.eatster.ui.component.WPasswordField;
import ru.baccasoft.eatster.ui.component.WTextField;
import ru.baccasoft.eatster.ui.event.WaiterLoginSuccess_Event;
import ru.baccasoft.eatster.ui.util.LoginHelper;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterLoginView.NAME)
public class WaiterLoginView extends VerticalLayout implements View, Button.ClickListener {

    private static final Logger LOG = Logger.getLogger(WaiterLoginView.class);

    private static final long serialVersionUID = 1L;
    public static final String NAME = "wlogin";

    private TextField user;
    private PasswordField password;
    private Button loginButton;
    private LoginHelper loginHelper;

    @Autowired
    WaiterService waiterService;
    @Autowired
    AppProp appProp;

    public WaiterLoginView() {
    }

    @PostConstruct
    void init() {
        //
        loginHelper = new LoginHelper(appProp.getLoginAttemptsInMinute());
        //
        user = new WTextField();
        password = new WPasswordField();
        password.setNullRepresentation("");
        loginButton = new WButton("Войти", this);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //
        VerticalLayout fields = new VerticalLayout();
        fields.setWidth("100%");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, true));
        fields.addComponent(new WLabel("Для входа в панель официанта, введите свои данные:"));
        fields.addComponent(new WLabel(""));
        //
        String defaultLogin = "";
        String defaultPass = "";
        //начало блока, который нужно зажать в прод версии ----------------
        VerticalLayout authLayout = new VerticalLayout();
        List<WaiterModel> listWaiter = waiterService.findAll();
        if (!appProp.isShowLogins()) {
            listWaiter.clear();
        }
        int size = 3;
        for(WaiterModel r:listWaiter) {
            if (r.getLogin().isEmpty()) {
                continue;
            }
            authLayout.addComponent(new Label(r.getLogin()+"/"+r.getPassword()));
            //fields.addComponent(new Label(r.getLogin()+"/"+r.getPassword()));
            if (defaultLogin.isEmpty()) {
                defaultLogin = r.getLogin();
                defaultPass = r.getPassword();
            }
            if (--size <= 0) {
                break;
            }
        }
        //конец блока 
        user.setValue(defaultLogin);
        password.setValue(defaultPass);
        //
        fields.addComponent(new VerticalLayout(new WLabel("Имя:"),user));
        fields.addComponent(new WLabel(""));
        fields.addComponent(new VerticalLayout(new WLabel("Пароль:"),password));
        fields.addComponent(new WLabel(" "));
        fields.addComponent(new WLabel(" "));
        fields.addComponent(loginButton);
        //
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setWidth("100%");
        viewLayout.setComponentAlignment(fields, Alignment.TOP_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        addComponent(viewLayout);
        fields.addComponent(authLayout);
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
            showWarning(LoginHelper.WARN_MESSAGE);
            return;
        }
        WaiterModel waiterModel = waiterService.auth(user.getValue(), password.getValue());
        if (waiterModel != null) {
            loginHelper.clearAttempts();
            this.user.setComponentError(null);
            this.password.setComponentError(null);
            getUI().fire(new WaiterLoginSuccess_Event(waiterModel));
        } else {
            this.password.setValue(null);
            String wrongMessage = "Неверные имя или пароль официанта";
            this.password.focus();
            this.user.setComponentError(new UserError(wrongMessage));
            this.password.setComponentError(new UserError(wrongMessage));
            showWarning(wrongMessage);
        }
    }

    public void showWarning(String msg) {
        Notification notif = new Notification(null/*"Внимание!"*/,msg,Notification.Type.WARNING_MESSAGE);
        notif.setStyleName(notif.getStyleName()+ " wnotification-warn");
        notif.setPosition(Position.TOP_CENTER);
        notif.show(Page.getCurrent());
    }
}
