package ru.baccasoft.eatster.ui.window;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.baccasoft.eatster.model.WaiterModel;
import ru.baccasoft.eatster.service.WaiterService;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.ActionPanel;
import ru.baccasoft.eatster.ui.event.WaiterAction_Event;
import ru.baccasoft.utils.logging.Logger;

public class WaiterWindow extends Window {

    private static final Logger LOG = Logger.getLogger(ActionPanel.class);
    private static final long serialVersionUID = 1L;
//    private static final int WIDTH_BUTTON = 3;
    public enum ActionType { Create, Edit, Delete };
//    private static final String ACTION_SAVE = "Сохранить";
//    private static final String CAPTION_ADD = "Добавить";
//    private static final String CAPTION_DEL = "Удалить";
    
    private class WaiterFields {
        private static final int WIDTH_FIELD = 6;
        TextField name = new TextField();
        TextField login = new TextField();
        TextField password = new TextField();

        public WaiterFields() {
            name.setWidth(WIDTH_FIELD, Unit.CM);
            login.setWidth(WIDTH_FIELD, Unit.CM);
            password.setWidth(WIDTH_FIELD, Unit.CM);
            name.setRequired(true);
            name.setRequiredError("Заполните имя официанта");
            login.setRequired(true);
            login.setRequiredError("Заполните логин официанта");
        }
    }
    WaiterModel waiterModel;
    WaiterFields waiterFields = new WaiterFields();
    BeanFieldGroup<WaiterModel> waiterFieldBindings = null;
    GridLayout gridButtons;
    ActionType actionType;

    public WaiterWindow(WaiterModel waiterModel,ActionType actionType) {
        super();
        this.waiterModel = waiterModel;
        this.actionType = actionType;
        buildLayout();
    }
    
    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component, String caption) {
        Label lab = new Label(caption);
        grid.addComponent(lab, col - 1, row);
        grid.setComponentAlignment(lab, Alignment.TOP_LEFT);
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }
    
    private void buildLayout() {
        String captionButton = "";
        if (actionType == ActionType.Create) {
            setCaption("Добавление официанта");
            captionButton = "Добавить";
        }
        if (actionType == ActionType.Edit) {
            setCaption("Изменить данные официанта");
            captionButton = "Сохранить";
        }
        if (actionType == ActionType.Delete) {
            setCaption("Вы уверены, что хотите удалить официанта ?");
            captionButton = "Удалить";
        }
        //
        center();
        setSizeUndefined();
        //
        GridLayout gridFields = new GridLayout(2,3);
        gridFields.setSpacing(true);
        gridFields.setMargin(true);
        gridFields.setSizeUndefined();
        addToGrid(gridFields, 1, 0, waiterFields.name, "Имя официанта");
        addToGrid(gridFields, 1, 1, waiterFields.login, "Логин");
        addToGrid(gridFields, 1, 2, waiterFields.password, "Пароль");
        waiterFields.name.focus();
        waiterFieldBindings = BeanFieldGroup.bindFieldsBuffered(waiterModel, waiterFields);
        if (actionType == ActionType.Delete) {
            waiterFields.name.setReadOnly(true);
            waiterFields.login.setReadOnly(true);
            waiterFields.password.setReadOnly(true);
        }
        //
        gridButtons = new GridLayout(2,1);
        gridButtons.setSizeFull();
        VerticalLayout content = new VerticalLayout(gridFields,gridButtons);
        setContent(content);
        //
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        //
        Button actionButton = new Button(captionButton);
        actionButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if ( commit() ) {
                    getUI().fire(new WaiterAction_Event(waiterModel,actionType));
                    close(); // Close the sub-window
                }
            }
        });
        cancelButton.setSizeFull();
        actionButton.setSizeFull();
        gridButtons.addComponent(cancelButton, 0, 0);
        gridButtons.addComponent(actionButton, 1, 0);
        // Disable the close button
        setClosable(false);
    }
/*    
    public void bindFieldsBuffered(WaiterModel waiterModel) {
        this.waiterModel = waiterModel;
        waiterFieldBindings = BeanFieldGroup.bindFieldsBuffered(waiterModel, waiterFields);
    }
*/    
    public boolean commit() {
        try {
            waiterFieldBindings.commit();
            waiterFields.login.setComponentError(null);
            WaiterService waiterService = getUI().getMainView().getWaiterService();
            if (waiterService.findTheSameLogin(waiterModel.getLogin(),waiterModel.getId())) {
                String msg = "Такой логин уже существует.";
                Notification.show(msg, Notification.Type.WARNING_MESSAGE);
                waiterFields.login.setComponentError(new UserError(msg) );
                return false;
            }
            return true;
        } catch (FieldGroup.CommitException e) {
            LOG.debug("Error on commit: " + e.getMessage());
            Notification.show("Ошибка заполнения данных", Notification.Type.WARNING_MESSAGE);
            return false;
        }
    }
    
}
