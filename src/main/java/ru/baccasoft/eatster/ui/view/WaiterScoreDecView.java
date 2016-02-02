package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationTotalModel;
import ru.baccasoft.eatster.service.OperationService;
import static ru.baccasoft.eatster.service.OperationService.MASKCODE_IN_SMS;
import ru.baccasoft.eatster.service.OperationTotalService;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.eatster.ui.component.WTextField;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterScoreDecView.NAME)
public class WaiterScoreDecView extends WaiterScoreCommon {

    private static final Logger LOG = Logger.getLogger(WaiterScoreDecView.class);
    public static final String NAME = "wscoredec";
    private static final long serialVersionUID = 1L;
    //private static final String SCORES_RATE_PROPERTY = "scores.rate.on.discount";
    
    private VerticalLayout confirmLayout;
    private class Fields {
        TextField confirmUserId = new WTextField();
        TextField smsCode = new WTextField();
    }
    private final Fields fields = new Fields();
    private final Button confirmButton = new WButton("Подтвердить", this);;
    private long insertedOperationId = 0;
    
    @Autowired
    OperationService operationService;
    @Autowired
    OperationTotalService operationTotalService;
    @Autowired
    AppProp appProp;

    public WaiterScoreDecView() {
    }
    
    @Override
    public void buildLayout() {
//        setSizeFull();
//        setSizeWidth("100%");
        buildFieldsLayout();
        buildConfirmLayout();
        buildSuccessLayout();
//        buildBackLayout();
    }

    public void buildConfirmLayout() {
/*        
        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setMargin(new MarginInfo(false, true, true, true));
        fieldsLayout.addComponent(new WLabel(getTopCaption()));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel("iD пользователя"));
        fieldsLayout.addComponent(fields.confirmUserId);
        fieldsLayout.addComponent(new WLabel("КОД из СМС"));
        fieldsLayout.addComponent(fields.smsCode);
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(confirmButton);
        fieldsLayout.addComponent(new WLabel(""));
        //fieldsLayout.setSizeUndefined();
        confirmLayout = new VerticalLayout(fieldsLayout);
        confirmLayout.setSizeFull();
        confirmLayout.setComponentAlignment(fieldsLayout, Alignment.TOP_CENTER);
*/        
        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setMargin(new MarginInfo(false, true, true, true));
        fieldsLayout.addComponent(new WLabel(getTopCaption()));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel("iD пользователя"));
        fieldsLayout.addComponent(fields.confirmUserId);
        fieldsLayout.addComponent(new WLabel("КОД из СМС"));
        fieldsLayout.addComponent(fields.smsCode);
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(confirmButton);
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WButton(BACK_CAPTION, this));
        //fieldsLayout.setSizeUndefined();
        confirmLayout = new VerticalLayout(fieldsLayout);
        //confirmLayout.setSizeFull();
        confirmLayout.setComponentAlignment(fieldsLayout, Alignment.TOP_CENTER);
        
        addComponent(confirmLayout);
        confirmLayout.setVisible(false);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        fields.confirmUserId.setReadOnly(false);
        fields.confirmUserId.setValue("");
        fields.confirmUserId.setReadOnly(true);
        fields.smsCode.setValue("");
        insertedOperationId = 0;
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
        return "Списать";
    }
    
    @Override
    public String getSumCaption(){
        return "Сумма скидки";
    }
    
    @Override
    public String getTopCaption() {
        return "Для списания баллов введите iD пользователя (номер телефона или уникальный идентификатор)";
    }
            
    @Override
    public void setInvisibleAll() {
        super.setInvisibleAll();
        confirmLayout.setVisible(false);
    }

    private long addOperationWithStatusNew() {
        LOG.debug("saveOperation");
//        int scoresRate = appProp.getPropertyIntDef(SCORES_RATE_PROPERTY,0);
//        LOG.debug("property {0}={1}",SCORES_RATE_PROPERTY,scoresRate);
//        if (scoresRate <= 0) {
//            LOG.debug("Fail. Rate <=0");
//            showError("Ошибка списания, связанная со ставкой. Обратитесь к администратору");
//            return 0;
//        }
        OperationModel operationModel = new OperationModel();
        fillOperationModelToDefault(operationModel);
        int score = getSumValue();
        operationModel.setCheckSum(0);
        operationModel.setScore(-score);
        operationModel.setStatus(OperationModel.STATUS_NEW);
        try {
            String sms = "Для подтверждения списания "+score+" баллов, сообщите официанту код: "+MASKCODE_IN_SMS;
            long operationId = operationService.insertItemWithSms(operationModel,sms);
            LOG.debug("Ok.");
            return operationId;
        } catch(Exception e) {
            LOG.debug("Fail. Exception: "+e.getMessage());
//            Notification.show("Внимание!", "Ошибка добавления операции. Попробуйте позднее.", Notification.Type.ERROR_MESSAGE);
            showError("Ошибка добавления операции. Попробуйте позднее.");
            return 0;
        }
        
/*        int updated = operationService.updateItemSMSInfo(operationId,"1122");
        if (updated != 1) {
            LOG.debug("Fail. Error on updating sms info. Record updated={0}",updated);
            Notification.show("Внимание!", "Ошибка добавления операции. Попробуйте позднее.", Notification.Type.ERROR_MESSAGE);
            return 0;
        }*/
        //showSuccess();
    }
    
    private void confirmOperation(long operationId, String smsCode) {
        LOG.debug("confirmOperation:operationId={0},smsCode={1}",operationId,smsCode);
        if (smsCode.isEmpty()) {
            LOG.debug("Fail. Sms is empty");
//            Notification.show("Внимание!", "Код подтверждения не введен", Notification.Type.ERROR_MESSAGE);
            showError("Код подтверждения не введен");
            return;
        }
        OperationModel operationModel = operationService.getItemBySmsCode(operationId,smsCode);
        LOG.debug("operationModel={0}",operationModel);
        String notifyError = "Не удалось подтвердить операцию";//. Попробуйте позднее
        if (operationModel == null) {
            LOG.debug("Fail. Operation not found");
//            Notification.show("Внимание!", notifyError, Notification.Type.ERROR_MESSAGE);
            showError(notifyError);
            return;
        }
        if (operationModel.getStatus().equals(OperationModel.STATUS_CONFIRMED)) {
            LOG.debug("Fail. Operation already confirmed");
//            Notification.show("Внимание!", notifyError, Notification.Type.ERROR_MESSAGE);
            showError(notifyError);
          return;
        }
        if (!operationModel.getStatus().equals(OperationModel.STATUS_NEW)) {
            LOG.debug("Fail. Status of operation not New");
//            Notification.show("Внимание!", notifyError, Notification.Type.ERROR_MESSAGE);
            showError(notifyError);
            return;
        }
        int updated = operationService.updateItemStatus(operationId,OperationModel.STATUS_CONFIRMED);
        if (updated != 1) {
            LOG.debug("Fail. Error on update status. Records updated={0}",updated);
            //Notification.show("Внимание!", notifyError, Notification.Type.ERROR_MESSAGE);
            showError(notifyError);
            return;
        }
        int score = -operationModel.getScore();
        sendPush(operationModel.getUserId(), "Произошло списание баллов "+score);
        showSuccessLayout();
        LOG.debug("Ok.");
    }

    @Override
    public boolean validate() {
        boolean result = super.validate();
        if (!result) {
            return false;
        }
        long userId = getUserId();
        OperationTotalModel operationTotalModel = operationTotalService.getByUser(userId);
        int checkSum = getSumValue();
        int curBalance = operationTotalModel.getScoresBalance();
        int newBalance = curBalance - checkSum;
        LOG.debug("validate:userId={0},curBalance={1},checkSum={2},newBalance={3}",userId,curBalance,checkSum,newBalance);
        if (newBalance < 0) {
//            Notification.show("Внимание!", "На счете пользователя недостаточно средств.", Notification.Type.WARNING_MESSAGE);
            LOG.debug("Fail. newBalance < 0");
            showError("На счете пользователя недостаточно средств.");
            return false;
        }
        LOG.debug("Ok");
        return true;
    }
    
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equals(getActionCaption())) {
            if (!validate()) {
                return;
            }
            insertedOperationId = addOperationWithStatusNew();
            setInvisibleAll();
            fields.confirmUserId.setReadOnly(false);
            fields.confirmUserId.setValue( getUserIdValue() );
            fields.confirmUserId.setReadOnly(true);
            fields.smsCode.setValue("");
            fields.smsCode.focus();
            confirmLayout.setVisible(true);
            return;
        }
        if (event.getButton() == confirmButton) {
            String smsCode = fields.smsCode.getValue().trim();
            if (smsCode.isEmpty()) {
                showWarning(fields.smsCode,"Введите код подтверждения");
            }
//            Notification.show("TEST", "test", Notification.Type.HUMANIZED_MESSAGE);
            confirmOperation(insertedOperationId,smsCode);
            return;
        }
        if (event.getButton().getCaption().equals(BACK_CAPTION)) {
            if (confirmLayout.isVisible()) {
                setInvisibleAll();
                getMainLayout().setVisible(true);
                return;
            }
        }
        super.buttonClick(event);
    }
}
