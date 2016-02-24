package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.ComboBox;

public class EatsterStatusComboBox extends ComboBox {

    private static final long serialVersionUID = -4912803676089612779L;

    public EatsterStatusComboBox(String caption) {
        super(caption);
        super.setNewItemsAllowed(false);
        super.setNullSelectionAllowed(false);
    }
    
}
