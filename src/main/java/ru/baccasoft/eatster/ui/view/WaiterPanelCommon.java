package ru.baccasoft.eatster.ui.view;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.PushTokenModel;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.UserBonusService;
import ru.baccasoft.eatster.service.UserService;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.eatster.ui.component.WTextField;
import ru.baccasoft.eatster.ui.event.SwitchView_Event;

public abstract class WaiterPanelCommon extends VerticalLayout implements View, Button.ClickListener {
    private static final long serialVersionUID = 4995060150203320687L;
    
    public abstract void debug(String msg);
    public abstract void error(String msg);

    protected class CommonFields {
        TextField userId = new WTextField();
        TextField checkSum = new WTextField();
        TextField comment = new WTextField();

        public CommonFields() {
            userId.addValidator(new RegexpValidator("\\d{1,10}", "В поле iD ожидаются числовые значения до 10 знаков"));
            userId.setInputPrompt("iD или ##########");
        }
        
        public void clear() {
            userId.setValue("");
            checkSum.setValue("");
            comment.setValue("");
        }
        
        public void setData(CommonFields f) {
            userId.setValue(f.userId.getValue());
            checkSum.setValue(f.checkSum.getValue());
            comment.setValue(f.comment.getValue());
        }
        
        public void setReadOnly(boolean readonly) {
            userId.setReadOnly(readonly);
            checkSum.setReadOnly(readonly);
            comment.setReadOnly(readonly);
        }
    }
    private CommonFields fields;
    
    public final String CHECKSUM_CAPTION = "Сумма чека";
    public final String USERID_CAPTION = "ID или тел. (без +7)";
    public final String COMMENT_CAPTION = "Примечание";
    private VerticalLayout editLayout;
    private final String PUSH_PREFIX = "Eatster: ";
    

    @Autowired
    WaiterService waiterService;
    @Autowired
    UserService userService;
    @Autowired
    PushService pushService;
    @Autowired
    PushTokenService pushTokenService;
    @Autowired
    AppProp appProp;
    @Autowired
    OperationService operationService;
    @Autowired
    UserBonusService userBonusService;

    public WaiterPanelCommon() {
    }

    public void setFields(CommonFields fields) {
        this.fields = fields;
    }

    public void setEditLayout(VerticalLayout editLayout) {
        this.editLayout = editLayout;
    }

//    public void setSuccessLayout(VerticalLayout successLayout) {
//        this.successLayout = successLayout;
//    }

    
    public Component addRowLayout(VerticalLayout rootLayout,Component field, String caption, int density, boolean highlight) {
        WLabel label = new WLabel(caption);
        label.setWidth("100%");
        field.setWidth("100%");
        HorizontalLayout rowLayout = new HorizontalLayout(label,field);
        rowLayout.setSpacing(true);
        rowLayout.setWidth("100%");
        MarginInfo marginInfo = new MarginInfo(false,true,false,true);
        switch(density) {
            case 1: marginInfo = new MarginInfo(true,true,false,true); break;
            case 2: marginInfo = new MarginInfo(false,true,true,true); break;
            case 3: marginInfo = new MarginInfo(true,true,true,true); break;
        }
        rowLayout.setMargin(marginInfo);
        rootLayout.addComponent(rowLayout);
        if (highlight) {
            field.setStyleName("wtexthighlight");
            label.setStyleName("wtexthighlight");
        }
        return rowLayout;
    }
    
    public Component addRowLayout(VerticalLayout rootLayout,Component field, String caption) {
        return addRowLayout( rootLayout, field, caption, 3, false);
    }
    
    public void addMargin(VerticalLayout fieldsLayout,int size) {
        while (size > 0) {
            fieldsLayout.addComponent(new WLabel(""));
            --size;
        }
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!getUI().isWaiterApp()) {
            getUI().fire(new SwitchView_Event(WaiterLoginView.NAME));
            debug("Fail. Not waiter app. Redirect to " + WaiterLoginView.NAME);
            return;
        }
        fields.clear();
        fields.userId.focus();
        showEditLayout();
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }
    
    public String getUserIdValue() {
        return fields.userId.getValue().trim();
    }

    public long getUserId() {
        String userIdValue = getUserIdValue();
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
    
    public String getCheckSumValue() {
        return fields.checkSum.getValue().trim();
    }
    
    public int getCheckSum() {
        String checkSumValue = getCheckSumValue();
        int checkSum;
        try {
            checkSum = Integer.parseInt(checkSumValue);
        } catch(Exception e) {
            showWarning(fields.checkSum,"\""+CHECKSUM_CAPTION+"\" ожидается целым числом");
            return 0;
        }
        if (checkSum <= 0) {
            showWarning(fields.checkSum,"\""+CHECKSUM_CAPTION+"\" ожидается больше нуля");
            return 0;
        }
        fields.checkSum.setComponentError(null);
        return checkSum;
    }
    
    public boolean validate() {
        if (getUserId() == 0) {
            fields.userId.focus();
            return false;
        }
        if (getCheckSum() == 0) {
            fields.checkSum.focus();
            return false;
        }
        if (!checkIntervalOnAddScore()) {
            return false;
        }
        return true;
    }
    
    public void fillOperationModel(OperationModel operationModel,int decScore, String status) {
        debug("fillOperationModel:decScore="+decScore+",status="+status);
        int baseRate = appProp.getCashbackBaseRate();
        int commissionRate = appProp.getCommissionRate();
        long userId = getUserId();
        int checkSum = getCheckSum();
        String comment = fields.comment.getValue().trim();
        WaiterModel waiterModel = getUI().getWaiterModel();
        debug("checkSum="+checkSum+",userId="+userId+",baseRate="+baseRate+",commissionRate="+commissionRate);
        //
        operationModel.setCheckSum(checkSum);
        operationModel.setDecScore(decScore);
        operationModel.setStatus(status);
        operationModel.setComment(comment);
        operationModel.setCashbackBaseRate(baseRate);
        operationModel.setCommissionRate(commissionRate);
        operationModel.setOperDate(DateAsString.getInstance().curDateAsString());
        operationModel.setOperTime(DateAsString.getInstance().curTimeAsHHMM());
        operationModel.setRestaurantId(waiterModel.getRestaurantId());
        operationModel.setUserId(userId);
        operationModel.setWaiterId(waiterModel.getId());
        //проверим персональный бонус
        int bonusRate = 0;
        if (userBonusService.getActiveOnTransact(userId,operationModel.getOperDate())) {
            bonusRate = appProp.getCashbackBonusRate();
        }
        operationModel.setCashbackBonusRate(bonusRate);
        debug("bonusRate="+bonusRate);
        //
        //calculate
        int clearSum = checkSum - decScore;
        debug("clearSum(checkSum-decScore)="+clearSum);
        double fAddScore = 1f * clearSum * (baseRate + bonusRate) / 100;
        debug("fAddScore="+fAddScore);
        int iAddScore = roundToCeil(fAddScore);
        debug("iAddScore="+iAddScore);
        operationModel.setAddScore(iAddScore);
        /*
        double fCommissionSum = 1f * checkSum * commissionRate / 100;
        debug("fCommissionSum="+fCommissionSum);
        int iCommissionSum = roundToCeil(fCommissionSum);
        debug("iCommissionSum="+iCommissionSum);
        operationModel.setCommissionSum(iCommissionSum);
        int incomeSum = iCommissionSum - iAddScore;
        debug("incomeSum="+incomeSum);
        operationModel.setIncomeSum(incomeSum);
        int payOffBalance = iCommissionSum - decScore;
        debug("payOffBalance="+payOffBalance);
        operationModel.setPayOffBalance(payOffBalance);
        */
        //
        debug("fillOperationModel:"+operationModel);
    }
    
    public int roundToCeil(double fValue) {
        String sValue = String.format("%.0f",fValue);
        return Integer.parseInt(sValue);
    }
/*    
    public void calculateOperationModel(OperationModel operationModel) {
        debug("calculateOperationModel: operationModel="+operationModel);
        int checkSum = operationModel.getCheckSum();
        int decScore = operationModel.getDecScore();
        int baseRate = operationModel.getCashbackBaseRate();
        int bonusRate = operationModel.getCashbackBonusRate();
        int commissionRate = operationModel.getCommissionRate();
        int clearSum = checkSum - decScore;
        debug("clearSum(checkSum-decScore)="+clearSum);
        double fAddScore = 1f * clearSum * (baseRate + bonusRate) / 100;
        debug("fAddScore="+fAddScore);
        int iAddScore = roundToCeil(fAddScore);
        debug("iAddScore="+iAddScore);
        operationModel.setAddScore(iAddScore);
        double fCommissionSum = 1f * checkSum * commissionRate / 100;
        debug("fCommissionSum="+fCommissionSum);
        int iCommissionSum = roundToCeil(fCommissionSum);
        debug("iCommissionSum="+iCommissionSum);
        operationModel.setCommissionSum(iCommissionSum);
        int incomeSum = iCommissionSum - iAddScore;
        debug("incomeSum="+incomeSum);
        operationModel.setIncomeSum(incomeSum);
    }
*/
/*    
    //долго думал считать ли от даты модификации в БД или от полей даты и времени операции
    //решил остановиться на последнем. 
    public boolean checkIntervalOnAddScore(OperationModel operationModel) {
        debug("checkIntervalOnAddScore:operationModel="+operationModel);
        OperationModel operationLastModel = operationService.getLastByUser(operationModel.getUserId(),operationModel.getRestaurantId());
        if (operationLastModel == null) {
            debug("Ok. Last operation not found. Return true");
            return true;
        }
        debug("operationLastModel="+operationLastModel);
        Date dateTimeLast = DateAsString.getInstance().toDate(operationLastModel.getOperDate(),operationLastModel.getOperTime());
        //расчета даты и времени минус 4 часа от текущей
        //текущая дата
        Date dateCurrent = DateAsString.getInstance().curDate();
        //временнй интервал в часх
        int restrictionInterval = appProp.getWaiterRestrictionIntervalOnAddScore();
        //дата и время минус 4 час от текущей
        Date dateRestrict = DateAsString.getInstance().addHours(dateCurrent,-restrictionInterval);
        debug("dateCurrent="+dateCurrent+",restrictionInterval(hours)="+restrictionInterval+",dateRestrict="+dateRestrict+",dateTimeLast="+dateTimeLast);
        if (dateRestrict.compareTo(dateTimeLast) > 0) {
            debug("Ok. Date of last operation is ok. Return true");
            return true;
        }
        debug("Fail. Too often. Return false");
        showError("Ошибка начисления. Операции начисления с данным клиентом проводятся слишком часто.");
        return false;
    }
*/
    //долго думал считать ли от даты модификации в БД или от полей даты и времени операции
    //решил остановиться на последнем. 
    public boolean checkIntervalOnAddScore() {
        long userId = getUserId();
        WaiterModel waiterModel = getUI().getWaiterModel();
        long restaurantId = waiterModel.getRestaurantId();
        debug("checkIntervalOnAddScore:userId="+userId+",restaurantId="+restaurantId);
        OperationModel operationLastModel = operationService.getLastByUser(userId,restaurantId);
        if (operationLastModel == null) {
            debug("Ok. Last operation not found. Return true");
            return true;
        }
        debug("operationLastModel="+operationLastModel);
        Date dateTimeLast = DateAsString.getInstance().toDate(operationLastModel.getOperDate(),operationLastModel.getOperTime());
        //расчета даты и времени минус 4 часа от текущей
        //текущая дата
        Date dateCurrent = DateAsString.getInstance().curDate();
        //временнй интервал в часх
        int restrictionInterval = appProp.getWaiterRestrictionIntervalOnAddScore();
        //дата и время минус 4 час от текущей
        Date dateRestrict = DateAsString.getInstance().addHours(dateCurrent,-restrictionInterval);
        debug("dateCurrent="+dateCurrent+",restrictionInterval(hours)="+restrictionInterval+",dateRestrict="+dateRestrict+",dateTimeLast="+dateTimeLast);
        if (dateRestrict.compareTo(dateTimeLast) > 0) {
            debug("Ok. Date of last operation is ok. Return true");
            return true;
        }
        debug("Fail. Too often. Return false");
        showError("Ошибка начисления. Операции начисления с данным клиентом проводятся слишком часто.");
        return false;
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
    }

    public void setInvisibleAll() {
        setSizeUndefined();
        setWidth("100%");
//        successLayout.setVisible(false);
        editLayout.setVisible(false);
    }
    
    public void showEditLayout() {
        setInvisibleAll();
        editLayout.setVisible(true);
        
    }
/*    
    public void showSuccessLayout() {
        setInvisibleAll();
        setSizeFull();
        successLayout.setVisible(true);
    }
*/
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

    public void exitToMenuView() {
        getUI().fire(new SwitchView_Event(WaiterMainView.NAME));
    }
/*    
    public int calculateScore(int checkSum, int scoresRate) {
        debug("calculateScore: checkSum="+checkSum+", scoresRate="+scoresRate);
        double fScores = 1f * checkSum * scoresRate / 100;
        debug("calculated="+fScores);
        String sScores = String.format("%.0f",fScores);
        int iScores = Integer.parseInt(sScores);
        debug("Ok. Result="+iScores);
        return iScores;
    }
*/
}
