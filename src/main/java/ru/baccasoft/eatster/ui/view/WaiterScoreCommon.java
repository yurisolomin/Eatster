package ru.baccasoft.eatster.ui.view;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.PushTokenModel;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.UserService;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.eatster.ui.component.WTextField;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;

public abstract class WaiterScoreCommon extends VerticalLayout implements View, Button.ClickListener {

    private static final long serialVersionUID = 1L;
    public static final float WIDTH_BUTTON = 35f;
    public static final int WIDTH_FIELD = 50;
    
    public abstract void debug(String msg);
    public abstract void error(String msg);
    public abstract String getActionCaption();
    public abstract String getSumCaption();
    public abstract String getTopCaption();

    private class Fields {
        TextField userId = new WTextField();
        TextField sum = new WTextField();
        TextField comment = new WTextField();
    }
    private final Fields fields = new Fields();
    
    public final String BACK_CAPTION = "Назад";
    private final Button actionButton = new WButton(getActionCaption(), this);;
    private VerticalLayout mainLayout;
    private VerticalLayout successLayout;
    private Label successLabel;
    private final String PUSH_PREFIX = "Eatster: ";
    

    @Autowired
    WaiterService waiterService;
    @Autowired
    UserService userService;
    @Autowired
    PushService pushService;
    @Autowired
    PushTokenService pushTokenService;

    public WaiterScoreCommon() {
    }

    @PostConstruct
    void init() {
//        setSizeFull();
        buildLayout();
    }

    public void buildLayout() {
        buildFieldsLayout();
        buildSuccessLayout();
    }
    
    public void buildFieldsLayout() {
        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setMargin(new MarginInfo(false, true, false, true));
        fieldsLayout.addComponent(new WLabel(getTopCaption()));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel("iD пользователя"));
        fieldsLayout.addComponent(fields.userId);
        fields.userId.addValidator(new RegexpValidator("\\d{1,10}", "В поле iD ожидаются числовые значения до 10 знаков"));
        fieldsLayout.addComponent(new WLabel(getSumCaption()));
        fieldsLayout.addComponent(fields.sum);
        fieldsLayout.addComponent(new WLabel("Примечание"));
        fieldsLayout.addComponent(fields.comment);
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(actionButton);
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(new WButton(BACK_CAPTION, this));
        
        fields.userId.setInputPrompt("iD или ##########");
        mainLayout = new VerticalLayout(fieldsLayout);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fieldsLayout, Alignment.TOP_CENTER);
        addComponent(mainLayout);
    }
    
    public void buildSuccessLayout() {
        successLabel = new Label("");
        successLabel.setSizeUndefined();
        successLabel.setStyleName("wnotification-ok");
        successLabel.setValue("Операция прошла успешно");
        VerticalLayout labelLayout = new VerticalLayout(successLabel);
        labelLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
        labelLayout.setSizeFull();
        //
        Button backButton = new WButton(BACK_CAPTION, this);
        VerticalLayout buttonLayout = new VerticalLayout(backButton);
        successLayout = new VerticalLayout(labelLayout,buttonLayout);
        successLayout.setSizeFull();
        successLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

        successLayout.setMargin(new MarginInfo(true, true, true, true));
        addComponent(successLayout);
        successLayout.setVisible(false);
    }
/*            
    public void buildBackLayout() {
        VerticalLayout bottomLayout = new VerticalLayout(backButton);
        bottomLayout.setMargin(new MarginInfo(false, true, false, true));
        addComponent(bottomLayout);
        setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);
//        addComponent(backButton);
//        setComponentAlignment(backButton, Alignment.BOTTOM_CENTER);
    }
*/    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        debug("enter");
        if (!getUI().isWaiterApp()) {
            getUI().fire(new SwitchView_Event(WaiterLoginView.NAME));
            debug("Fail. Not waiter app. Redirect to " + WaiterLoginView.NAME);
            return;
        }
        setInvisibleAll();
        mainLayout.setVisible(true);
        fields.userId.setValue("");
        fields.sum.setValue("");
        fields.comment.setValue("");
        //successLabel.setValue("");
        debug("Ok");
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public long getUserId() {
        String userIdValue = fields.userId.getValue().trim();
        if (userIdValue.isEmpty()) {
            showWarning(fields.userId,"Введите iD пользователя");
            return 0;
        }
        long userId;
        //определимся с пользователем
        //если номер телефона 11 знаков и начинается с 8, то отрежем 8 и добавим +7
        if (userIdValue.matches("[8]\\d{10}")) {
            userIdValue = "+7"+userIdValue.substring(1,11);
        }
        //если номер телефона 11 знаков, то добавим +
        if (userIdValue.matches("\\d{11}")) {
            userIdValue = "+"+userIdValue;
        }
        //если номер телефона 10 знаков, то добавим +7
        if (userIdValue.matches("\\d{10}")) {
            userIdValue = "+7"+userIdValue;
        }
        if (userIdValue.matches("\\+\\d{11}")) {
            debug("userId is phone="+userIdValue);
            UserModel userModel = userService.getItemByPhone(userIdValue);
            if (userModel == null) {
                showWarning(fields.userId,"Телефон "+userIdValue+" не обнаружен в базе Клиентов.");
                return 0;
            }
            userId = userModel.getId();
        } else {
            try {
                userId = Long.parseLong(userIdValue);
                UserModel userModel = userService.getItem(userId);
                if (userModel == null) {
                    showWarning(fields.userId,"Пользователь с iD равным "+userId+" не найден.");
                    return 0;
                }
            } catch(Exception e) {
                showWarning(fields.userId,"iD пользователя должен быть цифровым");
                return 0;
            }
        }
        fields.userId.setComponentError(null);
        return userId;
    }
    public int getSumValue() {
        //теперь с суммой
        String checkSumValue = fields.sum.getValue().trim();
        int checkSum;
        try {
            checkSum = Integer.parseInt(checkSumValue);
        } catch(Exception e) {
            //fields.checkSum.setComponentError(new UserError("Сумма чека ожидается целым числом"));
            showWarning(fields.sum,getSumCaption()+" ожидается целым числом");
            return 0;
        }
        if (checkSum <= 0) {
            //fields.checkSum.setComponentError(new UserError("Сумма чека ожидается больше нуля"));
            showWarning(fields.sum,getSumCaption()+" ожидается больше нуля");
            return 0;
        }
        fields.sum.setComponentError(null);
        return checkSum;
    }
    
    public boolean validate() {
        if (getUserId() == 0) {
            fields.userId.focus();
            return false;
        }
        if (getSumValue() == 0) {
            fields.sum.focus();
            return false;
        }
        return true;
    }
    
    public void setInvisibleAll() {
        setWidth("100%");
        successLayout.setVisible(false);
        mainLayout.setVisible(false);
    }
    
    public void fillOperationModelToDefault(OperationModel operationModel) {
        long userId = getUserId();
        //int checkSum = getCheckSum();
        String comment = fields.comment.getValue().trim();
        WaiterModel waiterModel = getUI().getWaiterModel();
        operationModel.setCheckSum(0);
        operationModel.setComment(comment);
        operationModel.setOperDate(DateAsString.getInstance().curDateAsString());
        operationModel.setOperTime(DateAsString.getInstance().curTimeAsHHMM());
        operationModel.setRestaurantId(waiterModel.getRestaurantId());
        operationModel.setScore(0);
        operationModel.setUserId(userId);
        operationModel.setWaiterId(waiterModel.getId());
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equals(BACK_CAPTION)) {
            getUI().fire(new SwitchView_Event(WaiterMainView.NAME));
        }
    }
/*
    public Button getActionButton() {
        return actionButton;
    }

    public Button getBackButton() {
        return backButton;
    }
*/
    public String getUserIdValue() {
        return fields.userId.getValue().trim();
    }

    public VerticalLayout getMainLayout() {
        return mainLayout;
    }
    
    public void showSuccessLayout() {
        setInvisibleAll();
        setSizeFull();
        successLayout.setVisible(true);
    }

    public void showWarning(TextField field,String msg) {
        Notification notif = new Notification(null,msg,Notification.Type.WARNING_MESSAGE);
        notif.setStyleName("wnotification-warn");
        notif.setPosition(Position.TOP_CENTER);
        field.setComponentError(new UserError(msg));
        notif.show(Page.getCurrent());
    }
    
    public void showError(String msg) {
        Notification notif = new Notification(null,msg,Notification.Type.WARNING_MESSAGE);
        notif.setStyleName("wnotification-err");
        notif.setPosition(Position.TOP_CENTER);
        notif.show(Page.getCurrent());
    }
    
    public void sendPush(long userId, String pushMessage) {
        debug("sendPush: userId="+userId+" pushMessage="+pushMessage);
        pushMessage = PUSH_PREFIX + pushMessage;
        UserModel userModel = userService.getItem(userId);
        List<PushTokenModel> tokens = pushTokenService.findAllByPhone(userModel.getPhone());
        debug("By phone="+userModel.getPhone()+" found tokens="+tokens.size());
        for(PushTokenModel token: tokens) {
            debug("pushToken="+token.getPushToken()+", deviceId="+token.getDeviceId());
        }
        try {
            int sended = pushService.syncPush(tokens, pushMessage);
            if (sended == 0) {
                debug("Found tokens:"+tokens.size()+". Push canceled. No settings");
            } else {
                debug("Found tokens:"+tokens.size()+". Sended pushes:"+sended);
            }
            debug("sendPush: Ok");
        } catch (Throwable e) {
            error("sendPush: Fail. Error found when sending pushes: "+e);
        }
        
    }
}
