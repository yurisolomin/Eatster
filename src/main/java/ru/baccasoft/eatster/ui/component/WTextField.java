package ru.baccasoft.eatster.ui.component;

import com.vaadin.ui.TextField;

public class WTextField extends TextField {

    private static final long serialVersionUID = -7347448855602610542L;

    public WTextField() {
        super();
        super.setSizeFull();
        super.addStyleName("wtextfield");
    }

    
    public WTextField(String caption) {
        super(caption);
        super.setSizeFull();
        super.addStyleName("wtextfield");
    }
    
}
