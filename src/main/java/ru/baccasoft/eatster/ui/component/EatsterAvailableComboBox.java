package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.ComboBox;

public class EatsterAvailableComboBox extends ComboBox {

    private static final long serialVersionUID = -8259538161514509999L;

    public EatsterAvailableComboBox(String caption, String emptyMessage) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setRequired(true);
        super.setRequiredError(emptyMessage);
        super.setValidationVisible(false);
        super.setNullSelectionAllowed(false);
        super.addItem(true);
        super.setItemCaption(true, "Есть");
        super.addItem(false);
        super.setItemCaption(false, "Нет");
    }
    
}
