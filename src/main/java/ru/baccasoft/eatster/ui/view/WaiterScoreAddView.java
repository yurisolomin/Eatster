package ru.baccasoft.eatster.ui.view;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.PushTokenModel;
import ru.baccasoft.eatster.model.UserModel;
import ru.baccasoft.eatster.service.OperationService;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.service.UserBonusService;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterScoreAddView.NAME)
public class WaiterScoreAddView extends WaiterScoreCommon {

    private static final Logger LOG = Logger.getLogger(WaiterScoreAddView.class);
    public static final String NAME = "wscoreadd";
    private static final long serialVersionUID = 1L;
    
    @Autowired
    OperationService operationService;
    @Autowired
    AppProp appProp;
    @Autowired
    UserBonusService userBonusService;

    public WaiterScoreAddView() {
    }

    @Override
    public void debug(String msg) {
        LOG.debug(msg);
    }
    
    @Override
    public void error(String msg) {
        LOG.error(msg);
    }
    
    @Override
    public String getActionCaption() {
        return "Начислить";
    }
    
    @Override
    public String getSumCaption(){
        return "Сумма чека";
    }
    
    @Override
    public String getTopCaption() {
        return "Для начисления баллов введите iD пользователя (номер телефона или уникальный идентификатор)";
    }

    //долго думал считать ли от даты модификации в БД или от полей даты и времени операции
    //решил остановиться на последнем. 
    private boolean checkIntervalOnAddScore(OperationModel operationModel) {
        LOG.debug("checkIntervalOnAddScore:operationModel={0}",operationModel);
        OperationModel operationLastModel = operationService.getLastByUser(operationModel.getUserId(),operationModel.getRestaurantId());
        if (operationLastModel == null) {
            LOG.debug("Ok. Last operation not found. Return true");
            return true;
        }
        LOG.debug("operationLastModel={0}",operationLastModel);
        Date dateTimeLast = DateAsString.getInstance().toDate(operationLastModel.getOperDate(),operationLastModel.getOperTime());
        //расчета даты и времени минус 4 часа от текущей
        //текущая дата
        Date dateCurrent = DateAsString.getInstance().curDate();
        //временнй интервал в часх
        int restrictionInterval = appProp.getWaiterRestrictionIntervalOnAddScore();
        //дата и время минус 4 час от текущей
        Date dateRestrict = DateAsString.getInstance().addHours(dateCurrent,-restrictionInterval);
        LOG.debug("dateCurrent={0},restrictionInterval(hours)={1},dateRestrict={2},dateTimeLast={3}",dateCurrent,restrictionInterval,dateRestrict,dateTimeLast);
        if (dateRestrict.compareTo(dateTimeLast) > 0) {
            LOG.debug("Ok. Date of last operation is ok. Return true");
            return true;
        }
        LOG.debug("Fail. Too often. Return false");
        return false;
    }
    private void addOperationWithStatusConfirmed() {
        debug("saveOperation");
        double scoresRate = appProp.getCashbackBaseRate();
        LOG.debug("scoresRate={0}",scoresRate);
        if (scoresRate <= 0f) {
            LOG.debug("Fail. Rate <=0");
//            Notification.show("Внимание!", "Ошибка начисления, связанная со ставкой. Обратитесь к администратору", Notification.Type.ERROR_MESSAGE);
            showError("Ошибка начисления, связанная со ставкой. Обратитесь к администратору.");
            return;
        }
        double bonusRate = 0;
        OperationModel operationModel = new OperationModel();
        fillOperationModelToDefault(operationModel);
        if (checkIntervalOnAddScore(operationModel) == false) {
            showError("Ошибка начисления. Операции начисления с данным клиентом проводятся слишком часто.");
            return;
        }
        //провериме персональный бонус
        if (userBonusService.getActiveOnTransact(operationModel.getUserId(),operationModel.getOperDate())) {
            bonusRate = appProp.getCashbackBonusRate();
        }
        LOG.debug("bonusRate={0}",bonusRate);
        //
        int checkSum = getSumValue();
        int score =  calculateScore(checkSum,scoresRate+bonusRate);
        operationModel.setCheckSum(checkSum);
        operationModel.setScore(score);
        operationModel.setStatus(OperationModel.STATUS_CONFIRMED);
        operationService.insertItem(operationModel);
        sendPush(operationModel.getUserId(), "Вам начислено баллов "+score);

/*        
        UserModel userModel = userService.getItem(operationModel.getUserId());
        List<PushTokenModel> tokens = pushTokenService.findAllByPhone(userModel.getPhone());
        LOG.debug("By phone={0} found tokens={1}",userModel.getPhone(),tokens.size());
        for(PushTokenModel token: tokens) {
            LOG.debug("pushToken={0}, deviceId={1}",token.getPushToken(),token.getDeviceId());
        }
        String pushMessage = "Eatster: начислена сумма "+operationModel.getCheckSum();
        try {
            int sended = pushService.syncPush(tokens, pushMessage);
            if (sended == 0) {
                LOG.debug("Found tokens:"+tokens.size()+". Push canceled. No settings");
            } else {
                LOG.debug("Found tokens:"+tokens.size()+". Sended pushes:"+sended);
            }
        } catch (Throwable e) {
            LOG.error("Found tokens:"+tokens.size()+". Error found when sending pushes: "+e);
        }
*/        
        showSuccessLayout();
        debug("Ok.");
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equals(getActionCaption())) {
            if (validate()) {
                addOperationWithStatusConfirmed();
            }
            return;
        }
        super.buttonClick(event);
    }

    public int calculateScore(int checkSum, double scoresRate) {
        debug("calculateScore: checkSum="+checkSum+", scoresRate="+scoresRate);
        double fScores = 1f * checkSum * scoresRate / 100;
        debug("calculated="+fScores);
        String sScores = String.format("%.0f",fScores);
        int iScores = Integer.parseInt(sScores);
        debug("Ok. Result="+iScores);
        return iScores;
    }

}
