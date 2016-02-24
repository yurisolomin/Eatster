package ru.baccasoft.eatster.ui.component;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.ComboBox;

public class EatsterTimeComboBox extends ComboBox {

    private static final long serialVersionUID = -4912803676089612779L;

    public EatsterTimeComboBox(String caption, String emptyMessage) {
        super(caption);
        super.setRequired(true);
        super.setNewItemsAllowed(false);
        super.setValidationVisible(false);
        super.setNullSelectionAllowed(false);
        super.addValidator(new StringLengthValidator(emptyMessage,5,5,false));
        for (int h = 0; h <= 23; ++h) {
            super.addItem(String.format("%02d:00", h));
            super.addItem(String.format("%02d:30", h));
        }
    }
    
}
