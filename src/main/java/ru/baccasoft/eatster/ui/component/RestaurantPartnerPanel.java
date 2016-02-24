package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import ru.baccasoft.eatster.model.PartnerModel;
import ru.baccasoft.eatster.ui.AppUI;
import ru.baccasoft.utils.logging.Logger;

public class RestaurantPartnerPanel extends TabSheet {
    private static final long serialVersionUID = 7990301684708381625L;
    private static final Logger LOG = Logger.getLogger(RestaurantPartnerPanel.class);

    private static final int WIDTH_FIELD = 15;
    private static final int PASSWORD_MIN_NUMS = 1;
    private static final int PASSWORD_MIN_CHARS = 6;
    private static final String PASSWORD_REQUIREMENTS = "минимум 6 символов и 1 цифра";
    
    private class PartnerFields extends Object {

        TextField contactName = new TextField("ФИО");
        TextField contactPost = new TextField("Должность");
        TextField contactEmail = new TextField("Доп. почта");
        TextField contactNote = new TextField("Комментарий");
//        TextField contactPhone = new TextField("Моб. телефон");
        EatsterPhoneField contactPhone = new EatsterPhoneField("Моб. телефон");
        TextField password = new TextField("Пароль");
        TextField name = new TextField("Основной e-mail");

        public PartnerFields() {
            contactName.setWidth(WIDTH_FIELD, Unit.CM);
            contactPost.setWidth(WIDTH_FIELD, Unit.CM);
            contactEmail.setWidth(WIDTH_FIELD, Unit.CM);
            contactNote.setWidth(WIDTH_FIELD, Unit.CM);
            contactPhone.setWidth(WIDTH_FIELD, Unit.CM);
            name.setWidth(WIDTH_FIELD, Unit.CM);
            password.setWidth(WIDTH_FIELD, Unit.CM);
            password.setInputPrompt(PASSWORD_REQUIREMENTS);
        }
    }

    PartnerFields partnerFields;
    BeanFieldGroup<PartnerModel> partnerFieldBindings = null;


    public RestaurantPartnerPanel() {
        buildLayout();
    }

    private void buildLayout() {
        GridLayout grid = new GridLayout(2, 15);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setSizeUndefined();

        partnerFields = new PartnerFields();

        FormLayout form;
        //
        form = new FormLayout(partnerFields.contactName, partnerFields.contactPost, partnerFields.contactEmail, partnerFields.contactNote, partnerFields.contactPhone);
        form.setSpacing(true);
        //form.setMargin(true);
        form.setSizeUndefined();
        //
        addTab(form, "Контакты");
        //
        form = new FormLayout(partnerFields.name, partnerFields.password);
        form.setSpacing(true);
        //form.setMargin(true);
        form.setSizeUndefined();
        //
        addTab(form, "Системные настройки");
        //
        partnerFields.contactName.focus();
    }

    @Override
    public AppUI getUI() {
        return (AppUI) super.getUI();
    }

    public void bindFieldsBuffered(PartnerModel partnerModel) {
        partnerFieldBindings = BeanFieldGroup.bindFieldsBuffered(partnerModel, partnerFields);
    }

    private void setComponentError(TextField field,String message) {
        field.setComponentError(new UserError(message));
        Notification.show(message, Notification.Type.WARNING_MESSAGE);
    }
    
    public boolean validate() {
        LOG.debug("validate:");
        partnerFields.name.setComponentError(null);
        partnerFields.password.setComponentError(null);
        String name = partnerFields.name.getValue().trim();
        String password = partnerFields.password.getValue().trim();
        if (!name.contains("@") || name.length() < 4 || name.replace("@","").contains("@")) {
            LOG.debug("Fail. Bad email");
            setComponentError(partnerFields.name,"Некорректный e-mail");
            return false;
        }
        if (password.isEmpty()) {
            LOG.debug("Fail. Password is empty");
            setComponentError(partnerFields.password,"Пароль не может быть пустым");
        }
        int numCount = 0;
        int chrCount = 0;
        LOG.debug("password={0}",password);
        for(int i=0; i<password.length(); ++i) {
            String ch = password.substring(i,i+1);
            LOG.debug("ch={0}",ch);
            if (ch.matches("\\d")) {
                LOG.debug("num");
                ++numCount;
            }
            if (ch.matches("([a-z]|[A-Z]|[а-я]|[А-Я])")) {
                LOG.debug("chr");
                ++chrCount;
            }
        }
        LOG.debug("numCount={0},chrCount={1}",numCount,chrCount);
        LOG.debug("numMin={0},chrMin={1}",PASSWORD_MIN_NUMS,PASSWORD_MIN_CHARS);
        if (numCount < PASSWORD_MIN_NUMS) {
            setComponentError(partnerFields.password,"Некорректный пароль.("+PASSWORD_REQUIREMENTS+")");
            LOG.debug("Fail. Bad password. Few digits");
            return false;
        }
        if (chrCount < PASSWORD_MIN_CHARS) {
            setComponentError(partnerFields.password,"Некорректный пароль.("+PASSWORD_REQUIREMENTS+")");
            LOG.debug("Fail. Bad password. Few characters");
            return false;
        }
        partnerFields.contactPhone.validate();
        LOG.debug("Ok.");
        return true;
    }
    public void commit() throws FieldGroup.CommitException {
        partnerFieldBindings.commit();
    }
    
}
