package ru.baccasoft.eatster.ui.view;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.ui.component.WButton;
import ru.baccasoft.eatster.ui.component.WLabel;
import ru.baccasoft.utils.logging.Logger;

@UIScope
@SpringView(name = WaiterPanelAddView.NAME)
public class WaiterPanelAddView extends WaiterPanelCommon {

    private static final Logger LOG = Logger.getLogger(WaiterPanelAddView.class);
    public static final String NAME = "wscoreadd";
    private static final long serialVersionUID = -1018951571585552239L;

    private final CommonFields fields = new CommonFields();
    public final String TITLE_CAPTION = "Для начисления баллов введите iD пользователя (номер телефона или уникальный идентификатор)";
    private final Button backButton1 = new WButton("Назад", this);;
    private final Button backButton2 = new WButton("Назад", this);;
    private final Button actionButton = new WButton("Начислить", this);;
    private VerticalLayout successLayout;
    
    public WaiterPanelAddView() {
    }

    @PostConstruct
    void init() {
        setFields(fields);
        setEditLayout(buildEditLayout());
        successLayout = buildSuccessLayout();
    }
    
    @Override
    public void debug(String msg) {
        LOG.debug(msg);
    }
    
    @Override
    public void error(String msg) {
        LOG.error(msg);
    }
    
    private VerticalLayout buildEditLayout() {
        VerticalLayout fieldsLayout = new VerticalLayout();
        fieldsLayout.setMargin(new MarginInfo(false, true, false, true));
        fieldsLayout.addComponent(new WLabel(TITLE_CAPTION));
        fieldsLayout.addComponent(new WLabel(""));
        addRowLayout(fieldsLayout,fields.userId,USERID_CAPTION);
        addRowLayout(fieldsLayout,fields.checkSum,CHECKSUM_CAPTION);
        addRowLayout(fieldsLayout,fields.comment,COMMENT_CAPTION);
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(new WLabel(""));
        fieldsLayout.addComponent(actionButton);
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(new WLabel(" "));
        fieldsLayout.addComponent(backButton1);
        fieldsLayout.setWidth("100%");
        addComponent(fieldsLayout);
        return fieldsLayout;
    }
    
    public VerticalLayout buildSuccessLayout() {
        Label successLabel = new Label("");
        successLabel.setSizeUndefined();
        successLabel.setStyleName("wnotification-ok");
        successLabel.setValue("Операция прошла успешно");
        VerticalLayout labelLayout = new VerticalLayout(successLabel);
        labelLayout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
        labelLayout.setSizeFull();
        //
        VerticalLayout buttonLayout = new VerticalLayout(backButton2);
        VerticalLayout fieldsLayout = new VerticalLayout(labelLayout,buttonLayout);
        fieldsLayout.setSizeFull();
        fieldsLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        //
        fieldsLayout.setMargin(new MarginInfo(true, true, true, true));
        fieldsLayout.setVisible(false);
        addComponent(fieldsLayout);
        fieldsLayout.setVisible(false);
        return fieldsLayout;
    }

    @Override
    public void setInvisibleAll() {
        super.setInvisibleAll();
        successLayout.setVisible(false);
    }

    public void showSuccessLayout() {
        setInvisibleAll();
        setSizeFull();
        successLayout.setVisible(true);
    }
    
    private void addOperationWithStatusConfirmed() {
        debug("saveOperation");
        OperationModel operationModel = new OperationModel();
        fillOperationModel(operationModel,0,OperationModel.STATUS_CONFIRMED);
        operationService.insertItem(operationModel);
        sendPush(operationModel.getUserId(), "Вам начислено баллов "+operationModel.getAddScore());
        showSuccessLayout();
        debug("Ok.");
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == actionButton) {
            if (validate()) {
                addOperationWithStatusConfirmed();
            }
            return;
        }
        if (event.getButton() == backButton1 || event.getButton() == backButton2) {
            exitToMenuView();
        }
        super.buttonClick(event);
    }

}
