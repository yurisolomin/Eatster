package ru.baccasoft.eatster.ui.window;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.baccasoft.eatster.model.DateAsString;
import ru.baccasoft.eatster.model.OperationModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.AdminOperationInsert_Event;

public class UserChangeScoresWindow extends Window {

    private static final long serialVersionUID = -7191529315152417163L;

    public enum ActionType {
        Add, Dec
    };

    private static final int WIDTH_FIELD = 6;
    private OptionGroup groupActions;
    private final TextField fieldScores = new TextField();
    private final TextField fieldComment = new TextField();
    private final String TITLE_ADD_SCORES = "Начислить баллы";
    private final String TITLE_DEC_SCORES = "Списать баллы";
    private final String ERROR_ACTION = "Выберите одну из опций - начислить или списать баллы";
    private final String ERROR_SCORES = "В поле Баллы допустимы только целые числа больше нуля";
    private final long userId;

    public UserChangeScoresWindow(long userId) {
        super();
        this.userId = userId;
        buildLayout();
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    private void buildLayout() {
        setCaption("Начисление или списание баллов пользователя");
        //
        center();
        setSizeUndefined();
        //
        groupActions = new OptionGroup();
        groupActions.setWidth(WIDTH_FIELD, Unit.CM);
        groupActions.setSizeFull();
        groupActions.addItems(TITLE_ADD_SCORES, TITLE_DEC_SCORES);
        groupActions.addValidator(new StringLengthValidator(ERROR_ACTION, 1, 100, false));
        groupActions.setRequired(true);
        groupActions.setRequiredError(ERROR_ACTION);
        groupActions.setValidationVisible(false);
        //
        fieldScores.setWidth(WIDTH_FIELD, Unit.CM);
        fieldScores.setInputPrompt("Количество баллов");
        fieldScores.addValidator(new RegexpValidator("\\d{1,8}", ERROR_SCORES));
        fieldScores.setRequired(true);
        fieldScores.setRequiredError(ERROR_SCORES);
        fieldScores.setValidationVisible(false);
        //
        fieldComment.setWidth(WIDTH_FIELD, Unit.CM);
        fieldComment.setInputPrompt("Примечание");
        //
        VerticalLayout fieldsLayout = new VerticalLayout(fieldScores, fieldComment);
        fieldsLayout.setSpacing(true);
        fieldsLayout.setSizeFull();
        //
        GridLayout gridFields = new GridLayout(2, 1);
        gridFields.setSpacing(true);
        gridFields.setMargin(true);
        gridFields.setSizeUndefined();
        gridFields.addComponent(groupActions, 0, 0);
        gridFields.addComponent(fieldsLayout, 1, 0);

        //
        GridLayout gridButtons = new GridLayout(2, 1);
        gridButtons.setSizeFull();
        VerticalLayout content = new VerticalLayout(gridFields, gridButtons);
        setContent(content);
        //
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        //
        Button actionButton = new Button("Подтвердить");
        actionButton.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!validate()) {
                    return;
                }
                String comment = fieldComment.getValue();
                if (comment == null) {
                    comment = "";
                }
                OperationModel operationModel = new OperationModel();
                operationModel.setCheckSum(0);
                operationModel.setComment(comment);
                operationModel.setOperDate(DateAsString.getInstance().curDateAsString());
                operationModel.setOperTime(DateAsString.getInstance().curTimeAsHHMM());
                if (getAction() == ActionType.Add) {
                    operationModel.setAddScore(getScores());
                }
                if (getAction() == ActionType.Dec) {
                    operationModel.setDecScore(getScores());
                }
                operationModel.setUserId(userId);
                operationModel.setStatus(OperationModel.STATUS_CONFIRMED);
                getUI().fire(new AdminOperationInsert_Event(operationModel));
                close(); // Close the sub-window
            }
        });
        cancelButton.setSizeFull();
        actionButton.setSizeFull();
        gridButtons.addComponent(cancelButton, 0, 0);
        gridButtons.addComponent(actionButton, 1, 0);
        // Disable the close button
        setClosable(false);
    }

    public int getScores() {
        try {
            int scores = Integer.parseInt(fieldScores.getValue());
            return scores;
        } catch (Exception e) {
            return 0;
        }
    }

    public ActionType getAction() {
        if (groupActions.getValue().equals(TITLE_ADD_SCORES)) {
            return ActionType.Add;
        }
        if (groupActions.getValue().equals(TITLE_DEC_SCORES)) {
            return ActionType.Dec;
        }
        return null;
    }

    public boolean validate() {
        try {
            fieldScores.validate();
            groupActions.validate();
            if (getScores() <= 0) {
                throw new RuntimeException("Ошибка при вводе баллов");
            }
            if (getAction() == null) {
                throw new RuntimeException("Ошибка при выборе действия начислить или списатьбаллы");
            }
            Notification.show("OK", Notification.Type.HUMANIZED_MESSAGE);
            return true;
        } catch (Exception e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            return false;
        }
    }

}
