package ru.baccasoft.eatster.ui.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.model.OperationTotalModel;
import static ru.baccasoft.eatster.service.OperationService.MASKCODE_IN_SMS;
import ru.baccasoft.eatster.service.OperationTotalService;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WTextField;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterPanelDecView.NAME)
public class WaiterPanelDecView extends WaiterPanelCommon implements Button.ClickListener{

    private static final Logger LOG = Logger.getLogger(WaiterPanelDecView.class);
    public static final String NAME = "wscoredec";
    private static final long serialVersionUID = 2217282720255527136L;
    
    protected class EditFields extends CommonFields {
        TextField decScore = new WTextField();
        @Override
        public void clear() {
            super.clear();
            decScore.setValue("");
        }
        public void setData(EditFields f) {
            super.setData(f);
            decScore.setValue(f.decScore.getValue());
        }
        @Override
        public void setReadOnly(boolean readonly) {
            super.setReadOnly(readonly);
            decScore.setReadOnly(readonly);
        }
    }
    protected class ConfirmFields extends EditFields {
        TextField smsCode = new WTextField();
        long insertedOperationId = 0;
    }
    protected class ResultFields extends EditFields {
        TextField resultSum = new WTextField();
        TextField transactNumber = new WTextField();
        TextField transactDate = new WTextField();
        @Override
        public void setReadOnly(boolean readonly) {
            super.setReadOnly(readonly);
            resultSum.setReadOnly(readonly);
            transactNumber.setReadOnly(readonly);
            transactDate.setReadOnly(readonly);
        }
    }
    private final EditFields fields = new EditFields();
    private final ConfirmFields confirmFields = new ConfirmFields();
    private final ResultFields resultFields = new ResultFields();
    
    private final Button confirmButton = new WButton("Подтвердить операцию", this);;
    private final Button executeButton = new WButton("Списать", this);;
    private final Button backToMenuButton = new WButton("Назад", this);;
    private final Button backToEditButton = new WButton("Назад", this);;
    private final Button backToMenuButtonOnOk = new WButton("Назад", this);;
    public final String SCORE_CAPTION = "Списать баллов";
    public final String SMSCODE_CAPTION = "Введите код из смс";
    public final String RESULTSUM_CAPTION = "К оплате";
    //public final String TITLE_CAPTION = "Для списания баллов введите iD пользователя (номер телефона или уникальный идентификатор)";
    private VerticalLayout confirmLayout;
    private VerticalLayout successLayout;
    
    @Autowired
    OperationTotalService operationTotalService;

    public WaiterPanelDecView() {
    }

    @PostConstruct
    void init() {
        setFields(fields);
        setEditLayout(buildEditLayout());
        successLayout = buildSuccessLayout();
        confirmLayout = buildConfirmLayout();
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    private VerticalLayout buildEditLayout() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setMargin(new MarginInfo(false, true, false, true));
        rootLayout.setWidth("100%");
        //rootLayout.addComponent(new WLabel(TITLE_CAPTION));
        //
        addMargin(rootLayout,1);
        addRowLayout(rootLayout,fields.userId,USERID_CAPTION);
        addRowLayout(rootLayout,fields.checkSum,CHECKSUM_CAPTION);
        addRowLayout(rootLayout,fields.decScore,SCORE_CAPTION);
        addRowLayout(rootLayout,fields.comment,COMMENT_CAPTION);
        addMargin(rootLayout,2);
        rootLayout.addComponent(confirmButton);
        addMargin(rootLayout,3);
        rootLayout.addComponent(backToMenuButton);
        addComponent(rootLayout);
        return rootLayout;
    }

    private VerticalLayout buildConfirmLayout() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setMargin(new MarginInfo(false, true, false, true));
        rootLayout.setWidth("100%");
        rootLayout.setVisible(false);
        //rootLayout.addComponent(new WLabel(TITLE_CAPTION));
        //
        addMargin(rootLayout,1);
        addRowLayout(rootLayout,confirmFields.userId,USERID_CAPTION,1,false);
        addRowLayout(rootLayout,confirmFields.checkSum,CHECKSUM_CAPTION,1,false);
        addRowLayout(rootLayout,confirmFields.decScore,SCORE_CAPTION,1,false);
        addRowLayout(rootLayout,confirmFields.comment,COMMENT_CAPTION,1,false);
        addRowLayout(rootLayout,confirmFields.smsCode,SMSCODE_CAPTION,1,false);
        addMargin(rootLayout,2);
        rootLayout.addComponent(executeButton);
        addMargin(rootLayout,3);
        rootLayout.addComponent(backToEditButton);
        addComponent(rootLayout);
        confirmFields.setReadOnly(true);
        return rootLayout;
    }
    
    private VerticalLayout buildSuccessLayout() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setMargin(new MarginInfo(true, true, false, true));
        rootLayout.setWidth("100%");
        rootLayout.setVisible(false);
        //
        Label successLabel = new Label("");
        successLabel.setSizeUndefined();
        successLabel.setStyleName("wnotification-ok");
        successLabel.setValue("Списание прошло успешно");
        VerticalLayout labelLayout = new VerticalLayout(successLabel);
        labelLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
        labelLayout.setWidth("100%");
        //
        rootLayout.addComponent(labelLayout);
        addMargin(rootLayout,1);
        addRowLayout(rootLayout,resultFields.userId,USERID_CAPTION,1,false);
        addRowLayout(rootLayout,resultFields.checkSum,CHECKSUM_CAPTION,1,false);
        addRowLayout(rootLayout,resultFields.decScore,SCORE_CAPTION,1,false);
        addRowLayout(rootLayout,resultFields.comment,COMMENT_CAPTION,1,false);
        addRowLayout(rootLayout,resultFields.resultSum,RESULTSUM_CAPTION,1,true);
        addRowLayout(rootLayout,resultFields.transactNumber,"Номер транзакции",1,false);
        addRowLayout(rootLayout,resultFields.transactDate,"Дата и время",2,false);
        addMargin(rootLayout,1);
        rootLayout.addComponent(backToMenuButtonOnOk);
        addComponent(rootLayout);
        resultFields.setReadOnly(true);
        return rootLayout;
    }

    @Override
    public void setInvisibleAll() {
        super.setInvisibleAll();
        confirmLayout.setVisible(false);
        successLayout.setVisible(false);
    }
    
    private void showConfirmLayout() {
        setInvisibleAll();
        confirmLayout.setVisible(true);
        confirmFields.smsCode.focus();
    }
    
    public void showSuccessLayout() {
        setInvisibleAll();
        successLayout.setVisible(true);
    }
    
    
    @Override
    public void debug(String msg) {
        LOG.debug(msg);
    }
    
    @Override
    public void error(String msg) {
        LOG.error(msg);
    }
    
    private long addOperationWithStatusNew() {
        LOG.debug("addOperationWithStatusNew:");
        try {
            int decScore = getDecScore();
            OperationModel operationModel = new OperationModel();
            fillOperationModel(operationModel,decScore,OperationModel.STATUS_NEW);
            String sms = "Для подтверждения списания "+decScore+" баллов, сообщите официанту код: "+MASKCODE_IN_SMS;
            long operationId = operationService.insertItemWithSms(operationModel,sms);
            LOG.debug("Ok.");
            return operationId;
        } catch(Exception e) {
            LOG.debug("Fail. Exception: "+e.getMessage());
            showError("Ошибка добавления операции. Попробуйте позднее.");
            return 0;
        }
    }
    
    public OperationModel confirmOperation(long operationId, String smsCode) {
        LOG.debug("confirmOperation:operationId={0},smsCode={1}",operationId,smsCode);
        if (smsCode.isEmpty()) {
            LOG.debug("Fail. Sms is empty");
            showError("Код подтверждения не введен");
            return null;
        }
        OperationModel operationModel = operationService.getItemBySmsCode(operationId,smsCode);
        LOG.debug("operationModel={0}",operationModel);
        String notifyError = "Не удалось подтвердить операцию";//. Попробуйте позднее
        if (operationModel == null) {
            LOG.debug("Fail. Operation not found");
            showError(notifyError);
            return null;
        }
        if (operationModel.getStatus().equals(OperationModel.STATUS_CONFIRMED)) {
            LOG.debug("Fail. Operation already confirmed");
            showError(notifyError);
            return null;
        }
        if (!operationModel.getStatus().equals(OperationModel.STATUS_NEW)) {
            LOG.debug("Fail. Status of operation not New");
            showError(notifyError);
            return null;
        }
        int updated = operationService.updateItemStatus(operationId,OperationModel.STATUS_CONFIRMED);
        if (updated != 1) {
            LOG.debug("Fail. Error on update status. Records updated={0}",updated);
            showError(notifyError);
            return null;
        }
        //шлем пуши
        int decScore = operationModel.getDecScore();
        int addScore = operationModel.getAddScore();
        String pushMessage = "Произошло списание "+decScore+" балл(ов) и начисление "+addScore+" балл(ов)";
        sendPush(operationModel.getUserId(), pushMessage);
        //
        showSuccessLayout();
        LOG.debug("Ok.");
        return operationModel;
    }

    @Override
    public boolean validate() {
        boolean result = super.validate();
        if (!result) {
            return false;
        }
        int decScore = getDecScore();
        if (decScore == 0) {
            fields.decScore.focus();
            return false;
        }
        int checkSum = getCheckSum();
        if (checkSum == 0) {
            fields.checkSum.focus();
            return false;
        }
        if (checkSum < decScore) {
            LOG.debug("Fail. checkSum < decScore");
            showWarning(fields.checkSum,"Сумма чека меньше количества списываемых баллов.");
            return false;
        }
        int cashbackMinimum = appProp.getCashbackMinimum();
        LOG.debug("cashbackMinimum={0}",cashbackMinimum);
        if (decScore < cashbackMinimum) {
            LOG.debug("Fail. decScore < cashbackMinimum");
            showWarning(fields.decScore,"Минимальная сумма списания "+cashbackMinimum+" баллов.");
            return false;
        }
        long userId = getUserId();
        OperationTotalModel operationTotalModel = operationTotalService.getByUser(userId);
        int curBalance = operationTotalModel.getCalcScoresBalance();
        int newBalance = curBalance - decScore;
        LOG.debug("validate:userId={0},curBalance={1},decScore={2},newBalance={3}",userId,curBalance,decScore,newBalance);
        if (newBalance < 0) {
            LOG.debug("Fail. newBalance < 0");
            showError("На счете пользователя недостаточно средств.");
            return false;
        }
        LOG.debug("Ok");
        return true;
    }
    
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == confirmButton) {
            if (!validate()) {
                return;
            }
            long insertedOperationId = addOperationWithStatusNew();
            if (insertedOperationId == 0) {
                return;
            }
            confirmFields.setReadOnly(false);
            confirmFields.setData(fields);
            confirmFields.setReadOnly(true);
            confirmFields.smsCode.setValue("");
            confirmFields.insertedOperationId = insertedOperationId;
            showConfirmLayout();
            return;
        }
        if (event.getButton() == executeButton) {
            String smsCode = confirmFields.smsCode.getValue().trim();
             if (smsCode.isEmpty()) {
                showWarning(confirmFields.smsCode,"Введите код подтверждения");
                return;
            }
            try {
                OperationModel operationModel = confirmOperation(confirmFields.insertedOperationId,smsCode);
                if (operationModel == null) {
                    return;
                }
                resultFields.setReadOnly(false);
                resultFields.setData(fields);
                int iResultSum = operationModel.getCheckSum() - operationModel.getDecScore();
                String resultSum = String.valueOf(iResultSum);
                resultFields.resultSum.setValue(resultSum);
                resultFields.transactNumber.setValue(""+operationModel.getId());
                Date dOperDate = DateAsString.getInstance().toDate(operationModel.getOperDate());
                String sOperDate = DateAsString.getInstance().toString(dOperDate,"dd.MM.yyyy");
                resultFields.transactDate.setValue(sOperDate+" "+operationModel.getOperTime());
                resultFields.setReadOnly(true);
                showSuccessLayout();
            } catch(Exception ex) {
                LOG.error("Error. Exception on confirmOperation: "+ex.getMessage());
                showError("Непредвиденная ошибка при записи данных.");
            }
            return;
        }
        if (event.getButton() == backToMenuButton || event.getButton() == backToMenuButtonOnOk) {
            exitToMenuView();
        }
        if (event.getButton() == backToEditButton) {
            showEditLayout();
        }
        super.buttonClick(event);
    }
    
    public String getScoreValue() {
        return fields.decScore.getValue().trim();
    }
    public int getDecScore() {
        //теперь с суммой
        String scoreValue = getScoreValue();
        int decScore;
        try {
            decScore = Integer.parseInt(scoreValue);
        } catch(Exception e) {
            showWarning(fields.decScore,"\""+SCORE_CAPTION+"\" ожидается целым числом");
            return 0;
        }
        if (decScore <= 0) {
            showWarning(fields.decScore,"\""+SCORE_CAPTION+"\" ожидается больше нуля");
            return 0;
        }
        fields.decScore.setComponentError(null);
        return decScore;
    }
    
    
}
