package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.Property;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayOutputStream;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.UploadField;
import ru.baccasoft.eatster.image.ImageValidator;
import ru.baccasoft.eatster.model.ActionModel;
import ru.baccasoft.eatster.model.ActionSubTypeModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.eatster.ui.event.ActionDelete_Event;
import ru.baccasoft.eatster.ui.event.ActionSave_Event;
import ru.baccasoft.eatster.ui.scope.PartnerScope;
import ru.baccasoft.eatster.ui.window.ImageRestrictionsWindow;
import ru.baccasoft.utils.logging.Logger;

public class ActionPanel extends VerticalLayout
        implements
        Button.ClickListener,
        Property.ValueChangeListener,
        ConfirmDialog.Listener
        {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ActionPanel.class);

    private class ActionFields {

        TextField name = new TextField();
        ComboBox status = new ComboBox("Статус");//
        ComboBox actionTypeId = new EatsterIdComboBox("Тип", "Укажите тип акции");
        ComboBox actionSubTypeId = new EatsterIdComboBox("Подтип", "Укажите подтип акции");
//        ComboBox actionTimeRangeId = new EatsterIdComboBox("Время работы", "Укажите время работы");
        ComboBox startTime = new ComboBox();
        ComboBox endTime = new ComboBox();
        TextArea comment = new TextArea("Условия проведения акции");//
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
        private static final int WIDTH_NAME = 20;
        public static final float WIDTH_IMAGE_DEFAULT = 5f;
        public static final float HEIGHT_IMAGE_DEFAULT = 5f;
        public static final float WIDTH_COMMENT = 14f;
        private static final int SUBTYPE_PAGE_LENGTH = 12;

        public ActionFields() {
            name.setWidth(WIDTH_NAME, Unit.CM);
            status.setWidth(WIDTH_SMALL, Unit.CM);
            actionTypeId.setWidth(WIDTH_SMALL, Unit.CM);
            actionSubTypeId.setWidth(WIDTH_SMALL, Unit.CM);
            actionSubTypeId.setPageLength(SUBTYPE_PAGE_LENGTH);
            image.setWidth(WIDTH_IMAGE_DEFAULT, Unit.CM);
            image.setHeight(HEIGHT_IMAGE_DEFAULT, Unit.CM);
            startTime.setWidth(WIDTH_TINY, Unit.CM);
            endTime.setWidth(WIDTH_TINY, Unit.CM);
            comment.setWordwrap(true);
            comment.setRows(7);
            comment.setWidth(WIDTH_COMMENT, Unit.CM);
            comment.setHeight(HEIGHT_IMAGE_DEFAULT, Unit.CM);
            image.setVisible(true);
        }

    }
    private final ActionFields fields = new ActionFields();
    private static final int WIDTH_DELETE = 4;
    private static final int WIDTH_SAVE = 4;
    private static final int WIDTH_LOADPHOTO = 7;
    private final Button btnDelete = new Button("Удалить", this);
    private final Button btnSave = new Button("Сохранить", this);
    private final Button btnQuestion = new Button(null, this);//"Знак вопроса"
    private final UploadField btnLoadPhoto = new ButtonUploadActionPhoto(this);
    private static final String MESSAGE_DELETE_CONFIRM = "Вы уверены, что хотите удалить акцию ?";
    
    private ActionModel actionModel = null;
    private BeanFieldGroup<ActionModel> fieldsBindings = null;
    
    ByteArrayOutputStream uploadOutputStream = null;

    public ActionPanel() {
        buildLayout();
    }

    private void addToGrid(GridLayout grid, int col, int row, Component component) {
        grid.addComponent(component, col, row);
        grid.setComponentAlignment(component, Alignment.TOP_LEFT);
    }

    public final void buildLayout() {
        btnDelete.setWidth(WIDTH_DELETE, Unit.CM);
        btnSave.setWidth(WIDTH_SAVE, Unit.CM);
        btnLoadPhoto.setWidth(WIDTH_LOADPHOTO, Unit.CM);
        btnQuestion.setIcon(FontAwesome.QUESTION_CIRCLE);

        Panel panel = new Panel();
        panel.setCaption("АКЦИЯ");
        panel.setSizeUndefined(); // Shrink to fit content layout.addComponent(panel);
        addComponent(panel);

        GridLayout grid = new GridLayout(4, 5);
        grid.setSpacing(true);
        grid.setSizeUndefined();
        panel.setContent(grid);
//        addComponent(grid);
        //
        int n = 1;
        addToGrid(grid, 0, 0, new Label("")); //первая колонка для небольшого отступа
        addToGrid(grid, 0+n, 0, new Label("Название акции"));
        addToGrid(grid, 1+n, 0, fields.name);
        addToGrid(grid, 2+n, 0, btnDelete);
        //
        VerticalLayout vLayout = new VerticalLayout(fields.status, fields.actionTypeId, fields.actionSubTypeId);
        vLayout.setSpacing(true);
        addToGrid(grid, 0+n, 1, vLayout);
        //
        HorizontalLayout hLayout = new HorizontalLayout(fields.image, btnQuestion, fields.comment);
        hLayout.setSpacing(true);
        addToGrid(grid, 1+n, 1, hLayout);
        //
        addToGrid(grid, 1+n, 2, new Label(""));
        //
        hLayout = new HorizontalLayout(fields.onMonday, fields.onTuesday, fields.onWednesday, fields.onThursday, fields.onFriday, fields.onSaturday, fields.onSunday);
        hLayout.setSpacing(true);
        hLayout = new HorizontalLayout(new Label("Время работы с"), fields.startTime, new Label("по"), fields.endTime, hLayout);
        hLayout.setSpacing(true);
        addToGrid(grid, 1+n, 3, hLayout);
        //
        addToGrid(grid, 1+n, 4, btnLoadPhoto);
        grid.setComponentAlignment(btnLoadPhoto, Alignment.TOP_RIGHT);
        addToGrid(grid, 2+n, 4, btnSave);
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == btnDelete) {
            ConfirmDialog.show(getUI(), "Пожалуйста, подтвердите:", MESSAGE_DELETE_CONFIRM, "Да", "Нет", this);        
//            getUI().fire(new ActionDelete_Event(this));
        }
        if (event.getButton() == btnSave) {
            save();
        }
        if (event.getButton() == btnQuestion) {
            ImageRestrictionsWindow infoWindow = new ImageRestrictionsWindow(getUI().getPartnerScope().getImageValidatorActionPhoto());
            getUI().addWindow(infoWindow);
        }
    }

    private void save() {
        try {
            try {
                fields.actionTypeId.validate();
                fields.actionSubTypeId.validate();
                //Кнопка сохранить: сохраняет изменения в акции и переводи акцию в статус на модерации
                //для партнера
                if (getUI().isPartnerApp()) {
                    fields.status.setValue(ActionModel.STAT_MODERATION);
                }
            } catch(EmptyValueException e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return;
            }
            // Commit the fields from UI to DAO
            fieldsBindings.commit();
            getUI().fire(new ActionSave_Event(this));
        } catch (FieldGroup.CommitException e) {
            LOG.error("Error on save action: " + e.getMessage());
            Notification.show("Ошибка в данных: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            // Validation exceptions could be shown here
        }
    }

    
    private void refreshComboBox() {
        PartnerScope partnerScope = getUI().getPartnerScope();
        partnerScope.comboboxForActionStatus(fields.status);
        partnerScope.comboboxForActionType(fields.actionTypeId);
        fields.actionSubTypeId.removeAllItems();
        partnerScope.comboboxForTime(fields.startTime);
        partnerScope.comboboxForTime(fields.endTime);
        
    }

    public ActionModel getActionModel() {
        return actionModel;
    }

    public void refreshPhoto() {
        ExternalResource photoExternalResource = null;
        if (!actionModel.getPhotoUrlParams().isEmpty()) {
            String photoDownloadUrl = getUI().getFileDownloadUrl(actionModel.getPhotoUrlParams());
            photoExternalResource = new ExternalResource(photoDownloadUrl);
        }
        fields.image.setSource(photoExternalResource);
    }
    public void setActionModel(ActionModel actionModel) {
        fields.actionTypeId.removeValueChangeListener(this);
        fields.actionTypeId.setImmediate(false);
        this.actionModel = actionModel;
        refreshComboBox();
        PartnerScope partnerScope = getUI().getPartnerScope();
        partnerScope.comboboxForActionSubType(fields.actionSubTypeId, actionModel.getActionTypeId());
        fieldsBindings = BeanFieldGroup.bindFieldsBuffered(actionModel, fields);
        refreshPhoto();
        fields.actionTypeId.addValueChangeListener(this);
        fields.actionTypeId.setImmediate(true);
        //для партнера недоступен
        fields.status.setEnabled(getUI().isAdminApp());
        //учтем соотношение сторон по настройкам максимальных размеров фото
//        AppProp appProp = getUI().getAppProp();
//        ImageValidator imageValidator= new ImageValidatorActionPhoto(appProp);
        ImageValidator imageValidator= getUI().getPartnerScope().getImageValidatorActionPhoto();
        fields.image.setWidth(fields.HEIGHT_IMAGE_DEFAULT*imageValidator.getWidthHeightRatio(),Unit.CM);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        String valueId = fields.actionTypeId.getValue().toString();
        long actionId = Long.parseLong(valueId);
        String actionName = fields.actionTypeId.getItemCaption(actionId);
        PartnerScope partnerScope = getUI().getPartnerScope();
        partnerScope.comboboxForActionSubType(fields.actionSubTypeId, actionId);
        fields.actionSubTypeId.clear();
        //После выбора типа, предлагаю автоматически подставлять подтип. (Скидка-скидка, подарок-подарок, и тп)
        ActionSubTypeModel defaultItem = partnerScope.comboboxFindActionSubType(actionId,actionName);
        if (defaultItem != null) {
            fields.actionSubTypeId.setValue(defaultItem.getId());
        }
    }

    @Override
    public void onClose(ConfirmDialog cd) {
        if (cd.isConfirmed() && cd.getMessage().equals(MESSAGE_DELETE_CONFIRM)) {
            getUI().fire(new ActionDelete_Event(this));
        }
    }

}
