package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.concurrent.Future;
import ru.baccasoft.eatster.model.PushTokenModel;
import ru.baccasoft.eatster.service.PushService;
import ru.baccasoft.eatster.service.PushTokenService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

public class MainMenuItemPush extends MainMenuItemLayout implements Button.ClickListener {
    private static final Logger LOG = Logger.getLogger(MainMenuItemPush.class);
    private static final long serialVersionUID = -8119150855564726922L;


    private final int MESSAGE_MAX_LENGTH = 100;
    private final TextField fieldMessage = new TextField();
    private final Button buttonSend = new Button("Отправить push", this);
    private final Button buttonRefresh = new Button("Обновить статус", this);
    private final Label labelInfo = new Label("Для отправки push введите сообщение и нажмите кнопку Отправить. Сообщение не может быть длиннее " + MESSAGE_MAX_LENGTH + " символов. Отправка может занять несколько часов.");
//    private final GridLayout gridStatus = new GridLayout(1, 1);
    private final String STATUS_NO_DATA = "Нет данных. ";
    private final String STATUS_IS_PREPARING = "идет подготовка... ";
    private final String STATUS_IS_DONE = "Отправка завершена. ";
    private final String STATUS_IS_ERR = "Отправка завершена с ошибкой. ";
    private final String STATUS_IS_RUNNING = "Отправка выполняется... ";
//    private final Label labelStatusInfo = new Label(STATUS_NO_DATA);
    private final Label labelStatus = new Label("Статус отправки");
    private final VerticalLayout layoutStatus = new VerticalLayout();

    private Future<Boolean> pushProccess = null;

//    PushService pushService;
//    PushTokenService pushTokenService;

    public MainMenuItemPush() {
        buildLayout();
    }

    private void buildLayout() {
        setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout(labelInfo);
        layout.setWidth("100%");
        addComponent(layout);
        //
        GridLayout grid = new GridLayout(2, 1);
        grid.setSpacing(true);
        grid.setWidth("100%");
        grid.setColumnExpandRatio(0, 2f);
        grid.addComponent(fieldMessage, 0, 0);
        grid.addComponent(buttonSend, 1, 0);
        addComponent(grid);
        fieldMessage.setWidth("100%");
        fieldMessage.setInputPrompt("не более " + MESSAGE_MAX_LENGTH + " символов");
        fieldMessage.setValue("");
        fieldMessage.setNullSettingAllowed(false);
        //
        addComponent(labelStatus);
        //
        Panel panel = new Panel();
        panel.setWidth("100%");
        addComponent(panel);
//        layoutStatus.addComponent(labelStatusInfo);
        layoutStatus.addComponent(new Label(STATUS_NO_DATA));
        panel.setContent(layoutStatus);
        addComponent(buttonRefresh);
        
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void refresh() {
        buttonSend.setEnabled(pushEnabled());
        layoutStatus.removeAllComponents();
        if (pushProccess == null) {
//            gridStatus.setRows(1);
            layoutStatus.addComponent(new Label(STATUS_NO_DATA));
//            labelStatusInfo.setValue(STATUS_NO_DATA);
            return;
        }
        if (pushProccess.isDone()) {
            String status = STATUS_IS_ERR;
            try {
                Boolean statusDone = pushProccess.get();
                if (statusDone) {
                    status = STATUS_IS_DONE;
                }
            } catch (Exception ex) {
                LOG.error("Error on get status push proccess: {0}", ex.getMessage());
            }
            layoutStatus.addComponent(new Label(status));
        } else {
            layoutStatus.addComponent(new Label(STATUS_IS_RUNNING));
        }
        PushService pushService = getUI().getPushService();
        int iTotal = pushService.getAsyncIOSKeysTotal();
        int aTotal = pushService.getAsyncAndroidKeysTotal();
        int tTotal = iTotal+aTotal;
        if (tTotal == 0) {
            layoutStatus.addComponent(new Label(STATUS_IS_PREPARING));
            return;
        }
        int iProcessed = pushService.getAsyncIOSKeysProcessed();
        int aProcessed = pushService.getAsyncAndroidKeysProcessed();
        int tProcessed = iProcessed + aProcessed;
        int iProcessedPrc = 0;
        if (iTotal > 0) {
            iProcessedPrc = iProcessed * 100 / iTotal;
        }
        int aProcessedPrc = 0;
        if (aTotal > 0) {
            aProcessedPrc = aProcessed * 100 / aTotal;
        }
        int iSent = pushService.getAsyncIOSKeysSent();
        int aSent = pushService.getAsyncAndroidKeysSent();
        int tSent = iSent + aSent;
        layoutStatus.addComponent(new Label("Всего записей: "+tTotal+". Из них iOS="+iTotal+", Android="+aTotal+"."));
        layoutStatus.addComponent(new Label("Обработано: "+tProcessed+". Из них iOS="+iProcessed+" ("+iProcessedPrc+ ")%, Android="+aProcessed+" ("+aProcessedPrc+"%)."));
        layoutStatus.addComponent(new Label("Отправлено: "+tSent+". Из них iOS="+iSent+", Android="+aSent+"."));
/*        
        String info = " Всего записей: "+tTotal+" ( iOS="+iTotal+", Android="+aTotal+"). "
                 +"Обработано: "+tProcessed+" ( iOS="+iProcessed+" "+iProcessedPrc+ "%, Android="+aProcessed+" "+aProcessedPrc+"%)"
                 +"Отправлено: "+tSent+" ( iOS="+iSent+", Android="+tSent+")"
                ;
*/
    }

    private boolean pushEnabled() {
        if (pushProccess != null) {
            if (!pushProccess.isDone()) {
                return false;
            }
        }
        PushService pushService = getUI().getPushService();
        return !pushService.isAsyncPushRunning();
    }

    private void push() {
        if (!pushEnabled()) {
            Notification.show("Не могу отправить уведомление. Дождитесь окончания предыдущей отправки", Notification.Type.WARNING_MESSAGE);
            return;
        }
        String message = fieldMessage.getValue().trim();
        if (message.equals("")) {
            Notification.show("Не могу отправить пустое уведомление.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (message.length() > MESSAGE_MAX_LENGTH) {
            Notification.show("Не могу отправить  уведомление, так как длина сообщения превышает "+MESSAGE_MAX_LENGTH+" знаков.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        PushTokenService pushTokenService = getUI().getPushTokenService();
        List<PushTokenModel> pushTokens = pushTokenService.findAll();
        //дело в том, что spring не сразу обращается в запуска асинхронного процесса и поэтому показатели
        //предыдущего выполнения пока не пустые на выходе после asyncPush
        //поэтому мы их сами почистим перед запуском 
        PushService pushService = getUI().getPushService();
        pushService.asyncPushClearIndicators();
        pushProccess = pushService.asyncPush(message, pushTokens);
        refresh();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == buttonSend) {
            push();
        }
        if (event.getButton() == buttonRefresh) {
            refresh();
        }
    }

    @Override
    public void doRefresh() {
        refresh();
    }

}
