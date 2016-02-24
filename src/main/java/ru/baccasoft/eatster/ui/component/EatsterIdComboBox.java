package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.ComboBox;

public class EatsterIdComboBox extends ComboBox {

    private static final long serialVersionUID = 632976792107555853L;

    public EatsterIdComboBox(String caption, String emptyError) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setNullSelectionAllowed(false);
        setValidationVisible(false);
        super.setRequired(true);
        setRequiredError(emptyError);
    }

    public EatsterIdComboBox(String caption) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setNullSelectionAllowed(true);
    }
    
}
