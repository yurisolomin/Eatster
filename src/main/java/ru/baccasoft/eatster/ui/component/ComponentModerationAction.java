package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.component.ComponentModerationPhoto.ModerationType;
import ru.baccasoft.eatster.ui.event.ModerationAction_Event;

public class ComponentModerationAction extends VerticalLayout
        implements
        Button.ClickListener {

    private static final long serialVersionUID = 8840426199626143256L;

    private class ActionFields {

        TextField name = new TextField();
        ComboBox status = new ComboBox("Статус");//
        ComboBox actionTypeId = new EatsterIdComboBox("Тип", "Укажите тип акции");
        ComboBox actionSubTypeId = new EatsterIdComboBox("Подтип", "Укажите подтип акции");
        ComboBox startTime = new ComboBox();
        ComboBox endTime = new ComboBox();
        TextArea comment = new TextArea("Полный текст акции");//
        Image image = new Image("Фото", null);
        CheckBox onMonday = new CheckBox("ПН");
        CheckBox onTuesday = new CheckBox("ВТ");
        CheckBox onWednesday = new CheckBox("СР");
        CheckBox onThursday = new CheckBox("ЧТ");
        CheckBox onFriday = new CheckBox("ПТ");
        CheckBox onSaturday = new CheckBox("СБ");
        CheckBox onSunday = new CheckBox("ВС");
        private static final int WIDTH_TINY = 3;
        private static final int WIDTH_SMALL = 5;
        private static final int WIDTH_DEFAULT = 8;
        private static final int WIDTH_NAME = 20;
        private static final int WIDTH_IMAGE = 5;
        private static final int HEIGHT_IMAGE = 5;

        public ActionFields() {
            name.setWidth(WIDTH_NAME, Unit.CM);
            status.setWidth(WIDTH_SMALL, Unit.CM);
            actionTypeId.setWidth(WIDTH_SMALL, Unit.CM);
            actionSubTypeId.setWidth(WIDTH_SMALL, Unit.CM);
            image.setWidth(WIDTH_IMAGE, Unit.CM);
            image.setHeight(HEIGHT_IMAGE, Unit.CM);
            startTime.setWidth(WIDTH_TINY, Unit.CM);
            endTime.setWidth(WIDTH_TINY, Unit.CM);
            comment.setWordwrap(true);
            comment.setRows(7);
            comment.setWidth(WIDTH_IMAGE * 3, Unit.CM);
            comment.setHeight(HEIGHT_IMAGE, Unit.CM);
            image.setVisible(true);
        }

    }
    private final ActionFields actionFields = new ActionFields();
    private final Button btnConfirm = new Button("Утвердить", this);
    private final Button btnBan = new Button("Бан", this);
    private final TextField restaurant = new TextField();
    private static final int WIDTH_RESTAURANT = 12;

    private ActionModel actionModel = null;

    public ComponentModerationAction() {
        buildLayout();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component) {
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    public final void buildLayout() {
        Panel panel = new Panel();
        panel.setCaption("АКЦИЯ");
        panel.setSizeUndefined();
        addComponent(panel);

        GridLayout grid = new GridLayout(3, 5);
        grid.setSpacing(true);
        grid.setSizeUndefined();
        panel.setContent(grid);
        //
        addToGrid(grid, 0, 0, new Label("Название акции"));
        addToGrid(grid, 1, 0, actionFields.name);
        //
        HorizontalLayout hLayout = new HorizontalLayout(actionFields.image, actionFields.comment);
        hLayout.setSpacing(true);
        addToGrid(grid, 1, 1, hLayout);
        //
        addToGrid(grid, 1, 2, new Label(""));
        //
        hLayout = new HorizontalLayout(actionFields.onMonday, actionFields.onTuesday, actionFields.onWednesday, actionFields.onThursday, actionFields.onFriday, actionFields.onSaturday, actionFields.onSunday);
        hLayout.setSpacing(true);
        hLayout = new HorizontalLayout(new Label("Время работы с"), actionFields.startTime, new Label("по"), actionFields.endTime, hLayout);
        hLayout.setSpacing(true);
        addToGrid(grid, 1, 3, hLayout);
        //
        HorizontalLayout layout = new HorizontalLayout(restaurant, btnConfirm, btnBan);
        layout.setSpacing(true);
        addToGrid(grid, 1, 4, layout);
        grid.setComponentAlignment(layout, Alignment.TOP_LEFT);

        restaurant.setReadOnly(true);
        restaurant.setWidth(WIDTH_RESTAURANT, Unit.CM);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnBan) {
            getUI().fire(new ModerationAction_Event(actionModel, ModerationType.BAN));
        }
        if (event.getButton() == btnConfirm) {
            getUI().fire(new ModerationAction_Event(actionModel, ModerationType.CONFIRM));
        }
    }

    public ActionModel getActionModel() {
        return actionModel;
    }

    public void setActionModel(ActionModel actionModel) {
        this.actionModel = actionModel;
        BeanFieldGroup.bindFieldsBuffered(actionModel, actionFields);
        ExternalResource photoExternalResource = null;
        if (!actionModel.getPhotoUrlParams().isEmpty()) {
            String photoDownloadUrl = getUI().getFileDownloadUrl(actionModel.getPhotoUrlParams());
            photoExternalResource = new ExternalResource(photoDownloadUrl);
        }
        actionFields.image.setSource(photoExternalResource);
        actionFields.name.setReadOnly(true);
        actionFields.startTime.setReadOnly(true);
        actionFields.endTime.setReadOnly(true);
        actionFields.comment.setReadOnly(true);
        actionFields.onMonday.setReadOnly(true);
        actionFields.onTuesday.setReadOnly(true);
        actionFields.onWednesday.setReadOnly(true);
        actionFields.onThursday.setReadOnly(true);
        actionFields.onFriday.setReadOnly(true);
        actionFields.onSaturday.setReadOnly(true);
        actionFields.onSunday.setReadOnly(true);
    }

    public void setPanelCaption(String caption) {
        restaurant.setReadOnly(false);
        restaurant.setValue(caption);
        restaurant.setReadOnly(true);
    }

}
