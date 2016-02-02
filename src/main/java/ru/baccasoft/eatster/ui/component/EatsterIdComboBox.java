package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import ru.baccasoft.utils.logging.Logger;

public class EatsterIdComboBox extends ComboBox {

    private static final Logger LOG = Logger.getLogger(EatsterIdComboBox.class);
    private static final long serialVersionUID = 1L;
//    private String emptyError;

    public EatsterIdComboBox(String caption, String emptyError) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setNullSelectionAllowed(false);
//        super.addValidator( new EmptyValidator(emptyError) );
        setValidationVisible(false);
//        this.emptyError = emptyError;
        super.setRequired(true);
        setRequiredError(emptyError);
    }

    public EatsterIdComboBox(String caption) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setNullSelectionAllowed(true);
    }
    
    private static final class EmptyValidator extends AbstractValidator<Long> {

        private static final long serialVersionUID = 1L;

        public EmptyValidator(String errorMessage) {
            super(errorMessage);
        }

        @Override
        public Class<Long> getType() {
            return Long.class;
        }

        @Override
        protected boolean isValidValue(Long value) {
            if (value == null) {
                value = 0L;
            }
            if (value == 0) {
                Notification.show(getErrorMessage(), Notification.Type.HUMANIZED_MESSAGE);
                return false;
            }
            return true;
        }
        
    }
/*    
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        setRequiredError(emptyError);
    }
*/    
/*
    @Override
        public boolean removeAllItems() {
        boolean result = super.removeAllItems();
        long itemId = 0;
        addItem(itemId);
        setNullSelectionItemId(itemId); //nu
        setItemCaption(itemId, "");
        return result;
    }
*/        
/*
    @Override
    public boolean isValid() {
        boolean result = super.isValid();
        return result;
    }
*/
}
